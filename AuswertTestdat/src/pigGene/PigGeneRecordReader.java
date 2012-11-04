package pigGene;

import java.io.IOException;
import java.util.ArrayList;

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
	private final ArrayList<Text> lines = new ArrayList<Text>();
	private CompressionCodecFactory compressionCodecs = null;
	private LongWritable key = null;
	private Text value = null;
	private LineReader in;

	private long start;
	private long pos;
	private long end;
	private int maxLineLength;

	@Override
	public void initialize(InputSplit genericSplit, TaskAttemptContext context) throws IOException, InterruptedException {
		FileSplit split = (FileSplit) genericSplit;
		Configuration job = context.getConfiguration();

		// TODO: change maxlength String in the Configuration ...
		maxLineLength = job.getInt("mapred.linerecordreader.maxlength", Integer.MAX_VALUE);
		start = split.getStart();
		end = start + split.getLength();
		final Path file = split.getPath();
		compressionCodecs = new CompressionCodecFactory(job);
		final CompressionCodec codec = compressionCodecs.getCodec(file);

		// open the file
		FileSystem fs = file.getFileSystem(job);
		FSDataInputStream fileIn = fs.open(split.getPath());

		if (codec != null) {
			in = new LineReader(codec.createInputStream(fileIn), job);
			end = Long.MAX_VALUE; // wird das gebraucht ???
		} else {
			in = new LineReader(fileIn, job);
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
			if (newSize == 0) {
				break;
			}
			pos += newSize;
			if (newSize <= maxLineLength) {
				break;
			}
			LOG.info("Skipped line of size " + newSize + " at pos " + (pos - newSize)); // line
		}

		if (newSize == 0) {
			key = null;
			value = null;
			return false;
		}

		// TODO: hier muss ich jetzt mit split die einzelnen Tokens aus dem
		// value Objekt rausholen. Da muss ich auch noch zusätzlich auf die
		// Groesse des lines-Arrays Rücksicht nehmen, d.h.: wenn ich nichts mehr
		// lesen kann aber im Array noch was drin steht, dann muss ich weiterhin
		// true zurueckliefern und die current Value setzen.

		return true;
	}

	@Override
	public LongWritable getCurrentKey() throws IOException, InterruptedException {
		return key;
	}

	@Override
	public Text getCurrentValue() throws IOException, InterruptedException {
		return value;
	}

	@Override
	public float getProgress() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void close() throws IOException {
		if (in != null) {
			in.close();
		}
	}

}