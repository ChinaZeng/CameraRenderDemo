
cmake_minimum_required(VERSION 3.4.1)

set(OpenCV_DIR F:/opencv-3.4.3-android-sdk/OpenCV-android-sdk/sdk/native/jni)

find_package(OpenCV REQUIRED)

if (OpenCV_FOUND)
    include_directories(${OpenCV_INCLUDE_DIRS})
    message(STATUS "OpenCV library status:")
    message(STATUS " version: ${OpenCV_VERSION}")
    message(STATUS " libraries: ${OpenCV_LIBS}")
    message(STATUS " include path: ${OpenCV_INCLUDE_DIRS}")
else (OpenCV_FOUND)
    message(FATAL_ERROR "OpenCV library not found")
endif (OpenCV_FOUND)

include_directories(src/main/cpp/libyuv/include)

add_subdirectory(src/main/cpp/libyuv)

add_library( # Sets the name of the library.
        native-lib
        SHARED
        src/main/cpp/native-lib.cpp)


target_link_libraries(
        native-lib
        ${OpenCV_LIBS}
        jnigraphics
        log
        yuv)