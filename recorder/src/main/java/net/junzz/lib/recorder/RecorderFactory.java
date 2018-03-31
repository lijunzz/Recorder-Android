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

import android.support.annotation.Keep;
import android.support.annotation.NonNull;

import static net.junzz.lib.recorder.RecorderModel.RECORDER_MODEL_AAC;
import static net.junzz.lib.recorder.RecorderModel.RECORDER_MODEL_MP3;

/**
 * Recorder 工厂。
 */
@Keep
public class RecorderFactory {

    @NonNull
    public Recorder newRecorder(@RecorderModel String model) {
        switch (model) {
            case RECORDER_MODEL_MP3:
                return new RecorderMp3();
            case RECORDER_MODEL_AAC:
                return new RecorderAac();
        }

        return new RecorderAac();
    }

}
