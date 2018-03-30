# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
             mp3lame

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/src/mp3lame.cpp
             src/main/cpp/src/vector/xmm_quantize_sub.c
             src/main/cpp/src/bitstream.c
             src/main/cpp/src/encoder.c
             src/main/cpp/src/fft.c
             src/main/cpp/src/gain_analysis.c
             src/main/cpp/src/id3tag.c
             src/main/cpp/src/lame.c
             src/main/cpp/src/mpglib_interface.c
             src/main/cpp/src/newmdct.c
             src/main/cpp/src/presets.c
             src/main/cpp/src/psymodel.c
             src/main/cpp/src/quantize.c
             src/main/cpp/src/quantize_pvt.c
             src/main/cpp/src/reservoir.c
             src/main/cpp/src/set_get.c
             src/main/cpp/src/tables.c
             src/main/cpp/src/takehiro.c
             src/main/cpp/src/util.c
             src/main/cpp/src/vbrquantize.c
             src/main/cpp/src/VbrTag.c
             src/main/cpp/src/version.c )

# Specifies a path to native header files.
include_directories(src/main/cpp/include/)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       mp3lame

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )