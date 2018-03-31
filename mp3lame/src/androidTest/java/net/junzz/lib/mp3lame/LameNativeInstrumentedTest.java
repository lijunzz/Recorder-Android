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

import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LameNativeInstrumentedTest {

    /**
     * 音频采样率。
     */
    private static final int AUDIO_SAMPLE_RATE = 32000;

    private LameNative mLame;
    private byte[] mp3Buffers;

    @Before
    public void initLameJNI() {
        mLame = new LameNative();
        mp3Buffers = new byte[100];
        for (int i = 0; i < 100; i++) {
            mp3Buffers[i] = Byte.valueOf(String.valueOf(i));
        }
    }

    @After
    public void releaseLameJNI() {
        mp3Buffers = null;
        mLame = null;
    }

    /**
     * 测试 Lame 初始化。
     */
    @Test
    public void initLameLib() {
        Assert.assertEquals(mLame.init(AUDIO_SAMPLE_RATE, 1), 0);
    }

    /**
     * 测试 Lame 关闭。
     */
    @Test
    public void closeLameLib() {
        Assert.assertEquals(mLame.close(), 0);
    }

}
