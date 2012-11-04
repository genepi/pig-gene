package pigGene;

import java.io.IOException;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.LineRecordReader;
import org.apache.hadoop.util.LineReader;

public class PigGeneRecordReader extends RecordReader<LongWritable, Text> {
	private static final Log LOG = LogFactory.getLog(LineRecordReader.class);
	private final LinkedList<Text> readLines = new LinkedList<Text>();
	private CompressionCodecFactory compressionCodecs = null;
	private final byte[] recordDelimiterBytes;
	private LongWritable key = null;
	private Text value = null;
	private LineReader in;

	private long start;
	private long pos;
	private long end;
	private int maxLineLength;

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

		if (codec != null) {
			if (null == recordDelimiterBytes) {
				in = new LineReader(codec.createInputStream(fileIn), job);
			} else {
				in = new LineReader(codec.createInputStream(fileIn), job, recordDelimiterBytes);
			}
			end = Long.MAX_VALUE;
		} else {
			if (start != 0) {
				--start;
				fileIn.seek(start);
			}
			if (null == recordDelimiterBytes) {
				in = new LineReader(fileIn, job);
			} else {
				in = new LineReader(fileIn, job, recordDelimiterBytes);
			}
		}
		pos = start;
	}

	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		if (key == null) {
			key = new LongWritable();
		}

		key.set(pos);
		if (value == null) {
			value = new Text();
		}

		int newSize = 0;
		while (pos < end) {
			newSize = in.readLine(value, maxLineLength, Math.max((int) Math.min(Integer.MAX_VALUE, end - pos), maxLineLength));
			if (newSize == 0) { // every line was read
				break;
			}
			pos += newSize;
			if (newSize < maxLineLength) {
				break;
			}

			// only logs if the line was too long to read...
			LOG.info("Skipped line of size " + newSize + " at pos " + (pos - newSize));
		}

		// only return FALSE if linesQueue is empty and nothing more to read...
		// something new to read: split the new line and put it into queue
		// if nothing new to read - get existing values from the queue
		if (newSize == 0 && readLines.isEmpty()) {
			key = null;
			value = null;
			return false;
		} else if (newSize != 0) {
			splitLine(key, value);
		}

		return true;
	}

	// hier delimiter evtl auch noch dynamisch anhand des pig-skripts setzen...
	// TODO: unbedingt die laenge des jeweiligen bytes beruecksichtigen und
	// start und laenge immer neu anpassen
	private void splitLine(LongWritable key, Text value) {
		int leadingInfoFields = 9;
		String delimiter = "\t";
		int idCounter = 0;
		Text leadingInfo = new Text();
		String inputLine = value.toString();

		String[] tmpSplits = inputLine.split(delimiter);
		int noOfTestPersons = tmpSplits.length - leadingInfoFields;
		int start = 0;
		int len = 0; // redefine... !!!!!!!!!!!!!!

		for (int i = 0; i < leadingInfoFields; i++) {
			// zusammenbauen von leadinginfo
			leadingInfo.append(tmpSplits[i].getBytes(), start, len);
		}

		for (int i = 0; i < noOfTestPersons; i++) {
			Text data = new Text();
			data.append(leadingInfo.getBytes(), 0, leadingInfo.getLength());

			// append von der jeweiligen Personenspalte
			data.append(tmpSplits[leadingInfoFields + idCounter].getBytes(), start, len);

			// append der jeweiligen ID (ID startet bei 0)
			String id = Integer.toString(idCounter);
			data.append(id.getBytes(), start, len);
			readLines.add(data);
			idCounter++;
		}

	}

	// TODO: nextKey methode... - kann der Key beibehalten werden? der ist
	// ohnehin
	// egal und dann wird als key immer die urspruengliche Zeile bleiben in der
	// alle Werte (von allen Testpersonen) drin stehen...

	@Override
	public LongWritable getCurrentKey() throws IOException, InterruptedException {
		return key;
	}

	@Override
	public Text getCurrentValue() throws IOException, InterruptedException {
		return readLines.poll();
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