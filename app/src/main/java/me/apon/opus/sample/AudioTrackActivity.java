package me.apon.opus.sample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Arrays;

import me.apon.audiowave.LineWave;
import me.apon.opuscodec.OpusDecoder;
import me.apon.opuscodec.OpusEncoder;

public class AudioTrackActivity extends AppCompatActivity {

    private static final String TAG = AudioTrackActivity.class.getSimpleName();

    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 100;

    private AudioThread mAudioThread;

    private boolean isStarted = false;

    private Button mButton;

    private LineWave mLineWave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = findViewById(R.id.button);
        mLineWave = findViewById(R.id.linewave);
        if (isStarted){
            mButton.setText("stop");
        }else {
            mButton.setText("start");
        }

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStarted){
                    stop();
                    mButton.setText("start");
                }else {
                    start();
                    mButton.setText("stop");
                }
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    start();
                }
            }
        }
    }

    private void start() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.RECORD_AUDIO },
                    PERMISSIONS_REQUEST_RECORD_AUDIO);
            return;
        }

        isStarted = true;
        mButton.setText("stop");
        mAudioThread = new AudioThread();
        mAudioThread.start();
    }

    private void stop(){
        mAudioThread.interrupt();
        try {
            mAudioThread.join();
        } catch (InterruptedException e) {

        }

        isStarted = false;
    }


    private class AudioThread extends Thread {

        //Opus支持的采样率为(Hz)：8000, 12000, 16000, 24000, 48000
        static final int SAMPLE_RATE = 8000;

        /**
         * FRAME_SIZE的合法值（与采样率有关）
         *
         * 8000/1000 = 8
         *
         * 8 * 2.5 = 20
         * 8 * 5 = 40
         * 8 * 10 = 80
         * 8 * 20 = 160
         * 8 * 40 = 320
         * 8 * 60 = 640
         */
        static final int FRAME_SIZE = 160;

        //声道数
        static final int NUM_CHANNELS = 1;

        @Override
        public void run() {

            int minBufSize = AudioRecord.getMinBufferSize(SAMPLE_RATE,
                    NUM_CHANNELS == 1 ? AudioFormat.CHANNEL_IN_MONO : AudioFormat.CHANNEL_IN_STEREO,
                    AudioFormat.ENCODING_PCM_16BIT);

            AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    SAMPLE_RATE,
                    NUM_CHANNELS == 1 ? AudioFormat.CHANNEL_IN_MONO : AudioFormat.CHANNEL_IN_STEREO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    minBufSize);



            AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC,
                    SAMPLE_RATE,
                    NUM_CHANNELS == 1 ? AudioFormat.CHANNEL_OUT_MONO : AudioFormat.CHANNEL_OUT_STEREO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    minBufSize,
                    AudioTrack.MODE_STREAM);
            mLineWave.setAudioSessionId(track.getAudioSessionId());

            // 创建编码器
            OpusEncoder encoder = new OpusEncoder();
            encoder.init(SAMPLE_RATE, NUM_CHANNELS, FRAME_SIZE,OpusEncoder.OPUS_APPLICATION_VOIP);

            // 解码器
            OpusDecoder decoder = new OpusDecoder();
            decoder.init(SAMPLE_RATE, NUM_CHANNELS,FRAME_SIZE);

            recorder.startRecording();
            track.play();

            short[] pcmBuf = new short[FRAME_SIZE * NUM_CHANNELS];
            byte[] enBuf = new byte[100];
            short[] outBuf = new short[FRAME_SIZE * NUM_CHANNELS];

            try {
                while (!Thread.interrupted()) {

                    //将数据读到pcmBuf
                    recorder.read(pcmBuf,0,pcmBuf.length);

                    //编码
                    int encoded = encoder.encode(pcmBuf,enBuf);


                    byte[] encBuf2 = Arrays.copyOf(enBuf, encoded);

                    //解码
                    decoder.decode(encBuf2, outBuf);

                    //播放
                    track.write(outBuf, 0, outBuf.length);
                }
            } finally {
                recorder.stop();
                recorder.release();
                track.stop();
                track.release();
            }
        }
    }
}
