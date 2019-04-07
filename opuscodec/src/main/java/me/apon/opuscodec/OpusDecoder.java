package me.apon.opuscodec;

/**
 * Created by yaopeng(aponone@gmail.com) on 2019/3/6.
 */
public class OpusDecoder {

    static
    {
        System.loadLibrary("aopus");
    }
    public native boolean init(int sampleRate, int channels, int frameSize);

    public native int decode(byte[] encodedBuffer, short[] buffer);

    public native boolean close();


}
