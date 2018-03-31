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
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 支持的音频格式。
 */
@Keep
@Retention(RetentionPolicy.SOURCE)
@StringDef({RecorderModel.RECORDER_MODEL_AAC, RecorderModel.RECORDER_MODEL_MP3})
public @interface RecorderModel {

    String RECORDER_MODEL_AAC = "AAC";
    String RECORDER_MODEL_MP3 = "MP3";

}
