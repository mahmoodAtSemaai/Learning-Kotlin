// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.webkul.mobikul.odoo.custom;

import android.graphics.Bitmap;
import android.media.Image;

import com.google.firebase.ml.common.FirebaseMLException;

import java.nio.ByteBuffer;

/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */
public interface VisionImageProcessor {

    /**
     * Processes the images with the underlying machine learning models.
     */
    void process(ByteBuffer data, FrameMetadata frameMetadata, GraphicOverlay graphicOverlay)
            throws FirebaseMLException;

    /**
     * Processes the bitmap images.
     */
    void process(Bitmap bitmap, GraphicOverlay graphicOverlay);

    /**
     * Processes the images.
     */
    void process(Image bitmap, int rotation, GraphicOverlay graphicOverlay);

    /**
     * Stops the underlying machine learning model and release resources.
     */
    void stop();
}
