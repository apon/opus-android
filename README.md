#### 0.Opus-Android简介

Opus-Android一个适用于Android平台的[opus](https://github.com/xiph/opus)包装库！

#### 1.导入库

```
compile 'me.apon:opuscodec:1.0.0'
```
#### 2.编码

将原始音频PCM编码成Opus格式，首先要创建一个编码器：


``` 
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
// 创建编码器
OpusEncoder encoder = new OpusEncoder();
encoder.init(SAMPLE_RATE, NUM_CHANNELS, FRAME_SIZE,OpusEncoder.OPUS_APPLICATION_VOIP);
```

然后用编码器对原始数据进行编码,


```
short[] pcmBuf = ...
byte[] enBuf = new byte[100];
//编码
int encoded = encoder.encode(pcmBuf,enBuf);
```

#### 3.解码

初始化解码器

```
OpusDecoder decoder = new OpusDecoder();
decoder.init(SAMPLE_RATE, NUM_CHANNELS,FRAME_SIZE);
```

解码


```
byte[] encBuf2 = ...
short[] outBuf = new short[FRAME_SIZE * NUM_CHANNELS];
//解码 
decoder.decode(encBuf2, outBuf);
```


