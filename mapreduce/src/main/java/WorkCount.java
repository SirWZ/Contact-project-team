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

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * <p>
 * 类名称：WorkCount
 * </p>
 * <p>
 * 类描述：${DESCRIPTION}
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：2018-08-28 9:29
 * </p>
 * <p>
 * 修改人：
 * </p>
 * <p>
 * 修改时间：
 * </p>
 * <p>
 * 修改备注：
 * </p>
 * <p>
 * Copyright (c) 版权所有
 * </p>
 *
 * @version 1.0.0
 */
public class WorkCount {

    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
        IntWritable intWritable = new IntWritable(1);
        Text text = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String valueByStr = value.toString();
            StringTokenizer tokenizer = new StringTokenizer(valueByStr);
            while (tokenizer.hasMoreTokens()) {
                text.set(tokenizer.nextToken());
                context.write(text, intWritable);
            }
        }

        @Override
        public void run(Context context) throws IOException, InterruptedException {

            super.run(context);
        }
    }


    public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            Iterator<IntWritable> iterator = values.iterator();
            while (iterator.hasNext()) {
                sum += iterator.next().get();
            }
            context.write(key, new IntWritable(sum));
        }
    }


    public static void main(String args[]) throws IOException, ClassNotFoundException, InterruptedException {
        File file = new File("mapreduce/output");
        deleteDir(file);

        Configuration conf = new Configuration();
        Job job = new Job(conf);
        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);
        job.setCombinerClass(Reduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        job.setJarByClass(WorkCount.class);
        FileInputFormat.addInputPath(job, new Path("mapreduce/input"));
        FileOutputFormat.setOutputPath(job, new Path("mapreduce/output"));

        boolean b = job.waitForCompletion(true);
        System.out.println(b);


    }

    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
