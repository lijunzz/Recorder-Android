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

package net.junzz.lib.mp3lame;

import android.support.annotation.Keep;

/**
 * 调用 Lame 的 JNI
 * <p>
 * 使用方式请阅读 API 文件
 */
@Keep
public class LameNative {

    static {
        System.loadLibrary("mp3lame");
    }

    public native int init(int inSampleRate, int numChannels);

    public native int buffer(short[] leftBuffers, short[] rightBuffers, int nSamples, byte[] mp3Buffers);

    public native int flush(byte[] mp3Buffers);

    public native int close();

}
