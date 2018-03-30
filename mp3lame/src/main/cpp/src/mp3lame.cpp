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
