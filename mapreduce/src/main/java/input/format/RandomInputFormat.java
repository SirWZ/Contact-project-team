package input.format;

import input.recordreader.RandomRecordReader;
import input.split.RandomInputSplit;
import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <p>
 * 类名称：RandomInputFormat
 * </p>
 * <p>
 * 类描述：${DESCRIPTION}
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：2018-08-28 14:07
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
public class RandomInputFormat extends InputFormat<IntWritable, ArrayWritable> {
    public static float[] floatValues = null;

    @Override
    public List<InputSplit> getSplits(JobContext jobContext) throws IOException, InterruptedException {
        int NumOfValues = jobContext.getConfiguration().getInt("lucl.random.nums", 100);
        floatValues = new float[NumOfValues];
        Random random = new Random();
        for (int i = 0; i < NumOfValues; i++) {
            floatValues[i] = random.nextFloat();
        }
        int NumSplits = jobContext.getConfiguration().getInt("mapreduce.job.maps", 2);
        int begin = 0;
        int length = (int) Math.floor(NumOfValues / NumSplits);
        int end = length - 1;

        List<InputSplit> splits = new ArrayList<InputSplit>();

        for (int i = 0; i < NumSplits - 1; i++) {    // 2个splits分片，分别为0和1
            RandomInputSplit split = new RandomInputSplit(begin, end);
            splits.add(split);
            begin = end + 1;
            end = begin + (length - 1);
        }

        return splits;
    }

    @Override
    public RecordReader<IntWritable, ArrayWritable> createRecordReader(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        return new RandomRecordReader();
    }
}
