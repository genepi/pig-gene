package wordcount;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class WordCount {
	/**
	 * The map class of WordCount.
	 */
	 public static class WordcountMap extends Mapper<LongWritable, Text, Text, IntWritable> {
		    private final static IntWritable one = new IntWritable(1);
		    private Text word = new Text();
		        
		    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		        String line = value.toString();
		        StringTokenizer tokenizer = new StringTokenizer(line);
		        while (tokenizer.hasMoreTokens()) {
		            word.set(tokenizer.nextToken());
		            context.write(word, one);
		        }
		    }
		 } 

	/**
	 * The reducer class of WordCount
	 * 
	 * 
	 */
	
	 public static class WordcountReduce extends Reducer<Text, IntWritable, Text, IntWritable> {

		    public void reduce(Text key, Iterable<IntWritable> values, Context context) 
		      throws IOException, InterruptedException {
		        int sum = 0;
		        for (IntWritable val : values) {
		            sum += val.get();
		        }
		        context.write(key, new IntWritable(sum));
		    }
		 }

	/**
	 * The main entry point.
	 */
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = new Job(conf, "Example Hadoop 0.20.1 WordCount");
		job.setJarByClass(WordCount.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setMapperClass(WordcountMap.class);
		job.setReducerClass(WordcountReduce.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}