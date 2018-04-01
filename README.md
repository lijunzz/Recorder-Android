Recorder-Android
================

**Recorder** 通过 `AudioRecord` 或 `MediaRecorder` 录制 MP3 或 AAC 音频文件。

因 Android 默认不支持MP3编码，所以库通过 Lame 编码生成 MP3 音频文件。

系统支持
-------

* Andorid API >= 19

开始使用
-------

录制 AAC 音频 导入 release 文件夹下 recorder-release.aar 即可；

录制 MP3 音频还需导入 mp3lame-release.aar 文件。

使用方式
-------

请求 App 录音机权限
```java
private final String[] permissions = {Manifest.permission.RECORD_AUDIO};

ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
```

----

获取保存音频文件的路径。
```java
File dir = getExternalFilesDir("Sounds");
if (dir == null) {
    dir = getFilesDir();
}

mFile = new File(dir, "test.mp3");

```

----

准备录音机开始捕捉和编码数据。
```java
mRecorder.prepare(mFile);
```

----

开始录制。
```java
new Thread(mRecorder::start).start();
```

----

停止录制。
```java
mRecorder.stop();
```

----

释放与此 Recorder 对象关联的资源。
```java
mRecorder.release();
```

License
=======

    Copyright 2018 LiJun

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
