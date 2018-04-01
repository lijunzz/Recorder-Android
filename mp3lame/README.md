The LAME Project
================

[LAME](http://lame.sourceforge.net/) 是根据 LGPL 许可的高品质 MPEG Audio Layer III (MP3) 编码器。

mp3lame
=======
MP3 作为常用音频格式，在Android和iOS平台都支持解码，但不支持编码；

mp3lame 使用 Android Studio + NDK + Cmake （可以从 SDK Manager 中下载）将 Lame 编译成 Android AAR 文件，以支持在 Android 生成 MP3 音频文件。

使用方式
-------

查看 [API 文档](API-Lame)。

入门
----

将 `mp3lame-release.aar` 导入到 `libs` 目录下，在 `build.gradle` 添加如下代码：

```groovy
implementation fileTree(include: ['*.aar'], dir: 'libs')
```

然后 `LameNative` 类使用。

鸣谢
====

[小枫_S](https://blog.csdn.net/q919233914)