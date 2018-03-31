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

#include <jni.h>
#include <string>

#include "lame.h"

lame_global_flags *lame;

extern "C"
JNIEXPORT jint
JNICALL Java_net_junzz_lib_mp3lame_LameNative_init(JNIEnv *env, jobject /* this */,
                                                   jint inSampleRate, jint numChannels) {
    lame = lame_init();

    lame_set_in_samplerate(lame, inSampleRate);
    lame_set_num_channels(lame, numChannels);

    return lame_init_params(lame);
}

extern "C"
JNIEXPORT jint
JNICALL Java_net_junzz_lib_mp3lame_LameNative_buffer(JNIEnv *env, jobject /* this */,
                                                     jshortArray leftBuffers,
                                                     jshortArray rightBuffers,
                                                     const jint nSamples, jbyteArray mp3Buffers) {
    const jshort *leftBuffer = env->GetShortArrayElements(leftBuffers, 0);
    const jshort *rightBuffer = env->GetShortArrayElements(rightBuffers, 0);

    const jsize mp3BuffersSize = env->GetArrayLength(mp3Buffers);
    jbyte *mp3Buffer = env->GetByteArrayElements(mp3Buffers, 0);

    int result = lame_encode_buffer(lame, leftBuffer, rightBuffer, nSamples,
                                    (unsigned char *) mp3Buffer, mp3BuffersSize);

    env->ReleaseShortArrayElements(leftBuffers, (jshort *) leftBuffer, 0);
    env->ReleaseShortArrayElements(rightBuffers, (jshort *) rightBuffer, 0);
    env->ReleaseByteArrayElements(mp3Buffers, mp3Buffer, 0);

    return result;
}

extern "C"
JNIEXPORT jint
JNICALL Java_net_junzz_lib_mp3lame_LameNative_flush(JNIEnv *env, jobject /* this */,
                                                    jbyteArray mp3Buffers) {
    jsize mp3BuffersSize = env->GetArrayLength(mp3Buffers);
    jbyte *mp3Buffer = env->GetByteArrayElements(mp3Buffers, 0);

    int result = lame_encode_flush(lame, (unsigned char *) mp3Buffer, mp3BuffersSize);

    env->ReleaseByteArrayElements(mp3Buffers, mp3Buffer, 0);

    return result;
}

extern "C"
JNIEXPORT jint
JNICALL Java_net_junzz_lib_mp3lame_LameNative_close(JNIEnv *env, jobject /* this */) {
    int result = lame_close(lame);
    lame = NULL;
    return result;
}
