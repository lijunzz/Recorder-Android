package net.junzz.lib.recorder;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.util.Log;

import net.junzz.lib.mp3lame.LameNative;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 录音机录制 MP3 文件。
 */
public class Recorder {

    private String TAG = Recorder.class.getSimpleName();
    private LameNative mLame = new LameNative();
    private AudioRecord mRecorder;

    /**
     * 初始化录音机。
     */
    public void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mRecorder = new AudioRecord.Builder()
                    .setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION)
                    .setAudioFormat(new AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(32000)
                            .setChannelMask(AudioFormat.CHANNEL_IN_MONO)
                            .build())
                    // .setBufferSizeInBytes(2 * minBuffSize)
                    .build();
        } else {
            mRecorder = new AudioRecord(MediaRecorder.AudioSource.VOICE_COMMUNICATION,
                    32000, AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, 2 * 2560);
        }

        // int minBuffSize = AudioRecord.getMinBufferSize(mRecorder.getSampleRate(),
        // mRecorder.getChannelConfiguration(), mRecorder.getAudioFormat());

        // 初始化 Lame
        int initResult = mLame.init(mRecorder.getSampleRate(), mRecorder.getChannelCount());
        Log.i(TAG, "Lame init result: " + initResult);
    }

    /**
     * 释放录音机资源。
     */
    public void release() {
        if (mRecorder.getRecordingState() != AudioRecord.RECORDSTATE_STOPPED) {
            mRecorder.stop();
        }
        mRecorder.release();
        mRecorder = null;

        int initResult = mLame.close();
        Log.i(TAG, "Lame close result: " + initResult);
    }

    /**
     * 开始录音；
     * <p>如果文件已存在将会覆盖原文件内容。</p>
     *
     * @param file 录音保存的文件
     * @throws IOException 文件未找到或文件不可用
     */
    public void start(File file) throws IOException {
        Log.d("TAG", "file.canWrite()" + file.canWrite());

        FileOutputStream fos = new FileOutputStream(file);
        int capacity = mRecorder.getSampleRate() * mRecorder.getChannelCount();
        short[] recordBuffer = new short[capacity];
        byte[] lameBuffer = new byte[capacity];

        mRecorder.startRecording();
        while (mRecorder.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
            int recorderSample = mRecorder.read(recordBuffer, 0, capacity);
            Log.d(TAG, "Recorder sample size: " + recorderSample);

            if (recorderSample > 0) {
                int lameSample = mLame.buffer(recordBuffer, recordBuffer, recorderSample, lameBuffer);
                Log.d(TAG, "Lame sample size: " + lameSample);

                if (lameSample > 0) {
                    fos.write(lameBuffer, 0, lameSample);
                }
            }
        }

        int flushSample = mLame.flush(lameBuffer);
        Log.d(TAG, "Lame flush size: " + flushSample);
        if (flushSample > 0) {
            fos.write(lameBuffer, 0, flushSample);
        }

        fos.flush();
        fos.close();
    }

    /**
     * 停止录音。
     */
    public void stop() {
        mRecorder.stop();
    }

}
