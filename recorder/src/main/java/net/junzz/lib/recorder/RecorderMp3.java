/*
 * Copyright 2018 LiJun
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.junzz.lib.recorder;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import net.junzz.lib.mp3lame.LameNative;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 录制 MP3 音频。
 */
class RecorderMp3 implements Recorder {

    private static final String TAG = "RecorderMp3";
    // 采样率
    private static final int AUDIO_SAMPLE_RATE = 32000;

    @NonNull
    private final LameNative mLame = new LameNative();
    private AudioRecord mMp3Recorder;
    private FileOutputStream mFileOutputStream;
    private RandomAccessFile mRandomAccessFile;

    @Override
    public void prepare(@NonNull File file) throws IOException {
        // 防止传入的文件未创建。
        mRandomAccessFile = new RandomAccessFile(file, "rws");
        mFileOutputStream = new FileOutputStream(mRandomAccessFile.getFD());

        // 初始化 AudioRecord
        int minBuffSize = AudioRecord.getMinBufferSize(AUDIO_SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mMp3Recorder = new AudioRecord.Builder()
                    .setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION)
                    .setAudioFormat(new AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(AUDIO_SAMPLE_RATE)
                            .setChannelMask(AudioFormat.CHANNEL_IN_MONO)
                            .build())
                    .setBufferSizeInBytes(2 * minBuffSize)
                    .build();
        } else {
            mMp3Recorder = new AudioRecord(MediaRecorder.AudioSource.VOICE_COMMUNICATION,
                    AUDIO_SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,
                    2 * minBuffSize);
        }

        // 初始化 Lame
        int initResult = mLame.init(mMp3Recorder.getSampleRate(), mMp3Recorder.getChannelCount());
        if (initResult != 0) {
            // Log.e(TAG, "Lame init result: " + initResult);
            throw new ExceptionInInitializerError("Lame init error.");
        } else {
            Log.d(TAG, "Lame init success.");
        }
    }

    @Override
    public void start() {
        int capacity = mMp3Recorder.getSampleRate() * mMp3Recorder.getChannelCount();
        short[] recordBuffer = new short[capacity];
        byte[] lameBuffer = new byte[capacity];

        mMp3Recorder.startRecording();
        while (mMp3Recorder.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
            int recorderSample = mMp3Recorder.read(recordBuffer, 0, capacity);
            // Log.d(TAG, "RecorderMp3 sample size: " + recorderSample);

            if (recorderSample > 0) {
                int lameSample = mLame.buffer(recordBuffer, recordBuffer, recorderSample, lameBuffer);
                // Log.d(TAG, "Lame sample size: " + lameSample);

                if (lameSample > 0) {
                    try {
                        mFileOutputStream.write(lameBuffer, 0, lameSample);
                    } catch (IOException e) {
                        Log.e(TAG, "Lame audio stream write failure.", e);
                        mMp3Recorder.stop();
                    }
                }
            }
        }

        int flushSample = mLame.flush(lameBuffer);
        // Log.d(TAG, "Lame flush size: " + flushSample);
        if (flushSample > 0) {
            try {
                mFileOutputStream.write(lameBuffer, 0, flushSample);
            } catch (IOException e) {
                Log.e(TAG, "Lame audio stream write failure.", e);
            }
        }

        try {
            mFileOutputStream.flush();
        } catch (IOException e) {
            Log.e(TAG, "Lame audio stream flush failure.", e);
        }
        try {
            mFileOutputStream.close();
            mFileOutputStream = null;
        } catch (IOException e) {
            Log.e(TAG, "Lame audio stream close failure.", e);
        }
        try {
            mRandomAccessFile.close();
            mRandomAccessFile = null;
        } catch (IOException e) {
            Log.e(TAG, "Audio 'RandomAccessFile' close failure.", e);
        }
    }

    @Override
    public void stop() {
        if (mMp3Recorder.getRecordingState() != AudioRecord.RECORDSTATE_STOPPED) {
            mMp3Recorder.stop();
        }
    }

    @Override
    public void release() {
        mMp3Recorder.release();
        mMp3Recorder = null;

        int initResult = mLame.close();
        if (initResult != 0) {
            Log.e(TAG, "Lame close result: " + initResult);
        } else {
            Log.d(TAG, "Lame close result: " + initResult);
        }
    }

}
