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

import android.media.MediaRecorder;
import android.os.Build;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;

/**
 * 录制 AAC 音频。
 */
class RecorderAac implements Recorder {

    @NonNull
    private final MediaRecorder mAacRecorder = new MediaRecorder();

    @Override
    public void prepare(@NonNull File file) throws IOException {
        mAacRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mAacRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mAacRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mAacRecorder.setOutputFile(file);
        } else {
            mAacRecorder.setOutputFile(file.getPath());
        }
        mAacRecorder.prepare();
    }

    @Override
    public void start() {
        mAacRecorder.start();
    }

    @Override
    public void stop() {
        mAacRecorder.stop();
    }

    @Override
    public void release() {
        mAacRecorder.release();
    }

}
