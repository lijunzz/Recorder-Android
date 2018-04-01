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

package net.junzz.app.recorder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import net.junzz.lib.recorder.Recorder;
import net.junzz.lib.recorder.RecorderFactory;
import net.junzz.lib.recorder.RecorderModel;

import java.io.File;
import java.io.IOException;

/**
 * 演示 Recorder。
 */
public class RecorderActivity extends AppCompatActivity {

    private static final String TAG = "RecorderActivity";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private final String[] permissions = {Manifest.permission.RECORD_AUDIO};

    @NonNull
    private final RecorderFactory mFactory = new RecorderFactory();
    @NonNull
    private final Recorder mRecorder = mFactory.newRecorder(RecorderModel.RECORDER_MODEL_MP3);
    @Nullable
    private File mFile;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                // permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                for (int result : grantResults) {
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        permissionToRecordAccepted = true;
                    } else {
                        permissionToRecordAccepted = false;
                        break;
                    }
                }
                break;
        }
        if (!permissionToRecordAccepted) finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recorder_activity);

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        // 录制的文件可以在文件管理中播放。
        createAudioFile();

        findViewById(R.id.prepare).setOnClickListener(v -> {
            try {
                if (mFile != null) {
                    mRecorder.prepare(mFile);
                }
            } catch (IOException e) {
                Log.e(TAG, "Recorder prepare failure.", e);
            }
        });
        findViewById(R.id.start).setOnClickListener(v -> new Thread(mRecorder::start).start());
        findViewById(R.id.stop).setOnClickListener(v -> mRecorder.stop());
        findViewById(R.id.release).setOnClickListener(v -> mRecorder.release());
    }

    private void createAudioFile() {
        File dir = getExternalFilesDir("Sounds");
        if (dir == null) {
            dir = getFilesDir();
        }

        mFile = new File(dir, "test.mp3");
    }

}
