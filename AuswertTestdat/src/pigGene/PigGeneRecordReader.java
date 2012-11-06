package pigGene;

import java.io.IOException;
import java.util.LinkedList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.util.LineReader;

public class PigGeneRecordReader extends RecordReader<LongWritable, Text> {
	private static final int leadingInfoFields = 9;
	private static final String delimiter = "\t";
	private final LinkedList<String> readLines = new LinkedList<String>();
	private CompressionCodecFactory compressionCodecs = null;
	private final byte[] recordDelimiterBytes;
	private LongWritable key = null;
	private Text value = null;
	private LineReader in;

	private long keyCounter = 0;
	private long start;
	private long pos;
	private long end;
	private int maxLineLength;

	private final Text textLine = new Text();

	public PigGeneRecordReader(byte[] recordDelimiter) {
		recordDelimiterBytes = recordDelimiter;
	}

	public PigGeneRecordReader() {
		this(null);
	}

	@Override
	public void initialize(InputSplit genericSplit, TaskAttemptContext context) throws IOException, InterruptedException {
		FileSplit split = (FileSplit) genericSplit;
		Configuration job = context.getConfiguration();

		// using the maximum length specified in
		// the configuration of the LineRecordReader...
		maxLineLength = job.getInt("mapred.linerecordreader.maxlength", Integer.MAX_VALUE);

		start = split.getStart();
		end = start + split.getLength();
		final Path file = split.getPath();
		compressionCodecs = new CompressionCodecFactory(job);
		final CompressionCodec codec = compressionCodecs.getCodec(file);

		// open the file and seek the start of the split
		FileSystem fs = file.getFileSystem(job);
		FSDataInputStream fileIn = fs.open(split.getPath());
		boolean skipFirstLine = false;

		if (codec != null) {
			if (null == recordDelimiterBytes) {
				in = new LineReader(codec.createInputStream(fileIn), job);
			} else {
				in = new LineReader(codec.createInputStream(fileIn), job, recordDelimiterBytes);
			}
			end = Long.MAX_VALUE;
		} else {
			if (start != 0) {
				skipFirstLine = true;
				--start;
				fileIn.seek(start);
			}
			if (null == recordDelimiterBytes) {
				in = new LineReader(fileIn, job);
			} else {
				in = new LineReader(fileIn, job, recordDelimiterBytes);
			}
		}
		if (skipFirstLine) { // skip first line and re-establish "start".
			start += in.readLine(new Text(), 0, (int) Math.min(Integer.MAX_VALUE, end - start));
		}
		pos = start;
	}

	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		// if LinkedList containes elements - consume them
		if (!readLines.isEmpty()) {
			return true;
		}

		if (value == null) {
			value = new Text();
		}

		int newSize = 0;
		boolean headerLine = false;
		while (pos < end || headerLine) {
			newSize = in.readLine(value, maxLineLength, Math.max((int) Math.min(Integer.MAX_VALUE, end - pos), maxLineLength));
			if (newSize == 0) { // every line was read
				break;
			}
			pos += newSize;
			if (newSize < maxLineLength) { // line length okay?
				if (value.charAt(0) == '#') { // ignore! -> header line
					headerLine = true;
				} else { // normal line - continue
					break;
				}
			}
		}

		// only return FALSE if LinkedList is empty and nothing more to read...
		// something new to read: split the new line and put it into queue
		if (newSize == 0) {
			key = null;
			value = null;
			return false;
		}

		if (key == null) {
			key = new LongWritable();
		}
		key.set(keyCounter++);
		splitLine(key, value);
		return true;
	}

	private void splitLine(LongWritable key, Text value) {
		StringBuilder leadingInfoBuffer = new StringBuilder();
		String inputLine = value.toString();
		String[] tmpSplits = inputLine.split(delimiter);
		int noOfTestPersons = tmpSplits.length - leadingInfoFields;

		// create leading info
		for (int i = 0; i < leadingInfoFields; i++) {
			leadingInfoBuffer.append(tmpSplits[i]).append(delimiter);
		}

		// append person and id column
		int idCounter = 0;
		String leadingInfo = leadingInfoBuffer.toString();
		for (int i = 0; i < noOfTestPersons; i++) {
			StringBuilder text = new StringBuilder();

			text.append(leadingInfo); // first leadingInfoField-number fields
			text.append(tmpSplits[leadingInfoFields + idCounter]).append(delimiter); // testPerson
			text.append(Integer.toString(idCounter)); // testPersonId
			idCounter++;

			readLines.add(text.toString());
		}
	}

	@Override
	public LongWritable getCurrentKey() throws IOException, InterruptedException {
		return key;
	}

	@Override
	public Text getCurrentValue() throws IOException, InterruptedException {
		textLine.set(readLines.poll());
		return textLine;
	}

	@Override
	public float getProgress() throws IOException, InterruptedException {
		if (start == end) {
			return 0.0f;
		} else {
			return Math.min(1.0f, (pos - start) / (float) (end - start));
		}
	}

	@Override
	public void close() throws IOException {
		if (in != null) {
			in.close();
		}
	}

}