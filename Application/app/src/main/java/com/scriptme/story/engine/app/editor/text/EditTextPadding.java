/*
 * Copyright (C) 2014 Vlad Mihalachi
 *
 * This file is part of Turbo Editor.
 *
 * Turbo Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Turbo Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.scriptme.story.engine.app.editor.text;

import android.content.Context;

import com.scriptme.story.engine.app.editor.util.PixelDipConverter;

public class EditTextPadding {

    public static int getPaddingWithoutLineNumbers(Context context) {
        return (int) PixelDipConverter.convertDpToPixel(5, context);
    }

    public static int getPaddingWithLineNumbers(Context context, float fontSize) {
        return (int) PixelDipConverter.convertDpToPixel(fontSize * 2f, context);
    }

    public static int getPaddingTop(Context context) {
        return getPaddingWithoutLineNumbers(context);
    }
}
