/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zzw.openglescamerademo.cameraview;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.ViewGroup;


class TextureViewPreview {

//    private final TextureView mTextureView;

    private SurfaceTexture surfaceTexture;

    TextureViewPreview(Context context, ViewGroup parent) {
        surfaceTexture = new SurfaceTexture(0);
    }

    public void onDestoroy() {
        if (surfaceTexture != null) {
            surfaceTexture.release();
        }
    }


    Surface getSurface() {
        return new Surface(surfaceTexture);
    }

    Class getOutputClass() {
        return SurfaceTexture.class;
    }


    boolean isReady() {
        return surfaceTexture != null;
    }

    public SurfaceTexture getSurfaceTexture() {
        return surfaceTexture;
    }


    interface Callback {
        void onSurfaceChanged();
    }

    private Callback mCallback;

    private int mWidth = 1080;

    private int mHeight = 1920;

    void setCallback(Callback callback) {
        mCallback = callback;
    }

    private void dispatchSurfaceChanged() {
        mCallback.onSurfaceChanged();
    }

    SurfaceHolder getSurfaceHolder() {
        return null;
    }

    private void setSize(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    int getWidth() {
        return mWidth;
    }

    int getHeight() {
        return mHeight;
    }

}
