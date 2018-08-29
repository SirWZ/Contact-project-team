package input.split;

import input.format.RandomInputFormat;
import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputSplit;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Random;

/**
 * <p>
 * 类名称：RandomInputSplit
 * </p>
 * <p>
 * 类描述：${DESCRIPTION}
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：2018-08-28 14:12
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
public class RandomInputSplit extends InputSplit implements Writable {

    private int start;
    private int end;
    private ArrayWritable floatArray = new ArrayWritable(FloatWritable.class);

    public RandomInputSplit() {
    }

    public RandomInputSplit(int start, int end) {
        this.start = start;
        this.end = end;

        int len = this.end - this.start + 1;
        FloatWritable[] result = new FloatWritable[len];
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            FloatWritable fw = new FloatWritable(random.nextFloat());
            result[i] = fw;
        }
        floatArray.set(result);
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.getStart());
        dataOutput.writeInt(this.getEnd());
        this.floatArray.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.start = dataInput.readInt();
        this.end = dataInput.readInt();
        this.floatArray.readFields(dataInput);
    }

    @Override
    public long getLength() throws IOException, InterruptedException {
        return this.end - this.start;
    }

    @Override
    public String[] getLocations() throws IOException, InterruptedException {
        return new String[]{"local"};
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public ArrayWritable getFloatArray() {
        return floatArray;
    }
}
