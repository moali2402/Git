/*
 * Copyright (C) 2011 Patrik Akerfeldt
 * Copyright (C) 2011 Jake Wharton
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
package dev.vision.rave.views;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.R;

import dev.vision.rave.DataHolder;
import android.content.Context;
import android.util.AttributeSet;

/**
 * Draws circles (one for each view). The current view position is filled and
 * others are only stroked.
 */
public class CPageIndicator extends CirclePageIndicator {
 

    public CPageIndicator(Context context) {
        this(context, null);
    }

    public CPageIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.vpiCirclePageIndicatorStyle);
    }

    public CPageIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setPageColor(DataHolder.Text_Colour);
        setStrokeColor(DataHolder.Text_Colour);
        setFillColor(DataHolder.Text_Colour);

    }
}
