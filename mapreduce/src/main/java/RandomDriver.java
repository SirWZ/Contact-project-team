import input.format.RandomInputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.Iterator;

/**
 * <p>
 * 类名称：RandomDriver
 * </p>
 * <p>
 * 类描述：${DESCRIPTION}
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：2018-08-28 14:35
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
public class RandomDriver {

    public static class RandomReducer extends Reducer<IntWritable, FloatWritable, Text, FloatWritable> {

        @Override
        protected void reduce(IntWritable key, Iterable<FloatWritable> value, Context context)
                throws IOException, InterruptedException {
            Iterator<FloatWritable> it = value.iterator();
            float maxValue = 0;
            float tmp = 0;
            if (it.hasNext()) {
                maxValue = it.next().get();
            } else {
                context.write(new Text("The max value is : "), new FloatWritable(maxValue));
                return;
            }

            while (it.hasNext()) {
                tmp = it.next().get();
                if (tmp > maxValue) {
                    maxValue = tmp;
                }
            }
            context.write(new Text("The max value is : "), new FloatWritable(maxValue));
        }
    }

    public static class RandomMapper extends Mapper<IntWritable, ArrayWritable, IntWritable, FloatWritable> {
        private static final IntWritable one = new IntWritable(1);

        @Override
        protected void map(IntWritable key, ArrayWritable value, Context context)
                throws IOException, InterruptedException {
            FloatWritable[] floatArray = (FloatWritable[]) value.toArray();
            float maxValue = floatArray[0].get();
            float tmp = 0;

            for (int i = 1; i < floatArray.length; i++) {
                tmp = floatArray[i].get();
                if (tmp > maxValue) {
                    maxValue = tmp;
                }
            }

            context.write(one, new FloatWritable(maxValue));
        }
    }

    public static void main(String argsp[]) throws IOException, ClassNotFoundException, InterruptedException {

        Configuration conf = new Configuration();
        conf.set("lucl.random.nums", "100");
        conf.set("mapreduce.job.maps", "2");

        Job job = new Job(conf);

        job.setJarByClass(RandomDriver.class);

        job.setInputFormatClass(RandomInputFormat.class);

        job.setMapperClass(RandomMapper.class);
        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(FloatWritable.class);

        job.setReducerClass(RandomReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FloatWritable.class);

        FileOutputFormat.setOutputPath(job, new Path("mapreduce/outputRandom"));

        job.waitForCompletion(true);


    }
}
