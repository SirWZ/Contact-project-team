package input.recordreader;

import input.split.RandomInputSplit;
import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;

/**
 * <p>
 * 类名称：RandomRecordReader
 * </p>
 * <p>
 * 类描述：${DESCRIPTION}
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：2018-08-28 14:32
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
public class RandomRecordReader extends RecordReader<IntWritable, ArrayWritable> {

    private int start;
    private int end;
    private int index;

    private IntWritable key = null;
    private ArrayWritable value = null;
    private RandomInputSplit rsplit = null;

    @Override
    public void initialize(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        this.rsplit = (RandomInputSplit) inputSplit;
        this.start = this.rsplit.getStart();
        this.end = this.rsplit.getEnd();
        this.index = this.start;
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        if (null == key) {
            key = new IntWritable();
        }
        if (null == value) {
            value = new ArrayWritable(FloatWritable.class);
        }
        if (this.index <= this.end) {
            key.set(this.index);
            value = rsplit.getFloatArray();
            index = end + 1;
            return true;
        }

        return false;
    }

    @Override
    public IntWritable getCurrentKey() throws IOException, InterruptedException {
        return key;
    }

    @Override
    public ArrayWritable getCurrentValue() throws IOException, InterruptedException {
        return value;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        if (this.index == this.end) {
            return 0F;
        }
        return Math.min(1.0F, (this.index - this.start) / (float) (this.end - this.start));
    }

    @Override
    public void close() throws IOException {

    }
}
