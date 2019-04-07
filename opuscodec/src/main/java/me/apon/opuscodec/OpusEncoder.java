package me.apon.opuscodec;

/**
 * Created by yaopeng(aponone@gmail.com) on 2019/3/6.
 */
public class OpusEncoder {

    static {
        System.loadLibrary("aopus");
    }

    public static final int OPUS_APPLICATION_VOIP                = 2048;
    public static final int OPUS_APPLICATION_AUDIO               = 2049;
    public static final int OPUS_APPLICATION_RESTRICTED_LOWDELAY = 2051;

    public native boolean init(int sampleRate, int channels, int frameSize,int appType);

    public native int encode(short[] buffer, byte[] out);

    public native boolean close();



}
