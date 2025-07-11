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

package com.scriptme.story.engine.app.editor.util;

import android.content.Context;

import com.scriptme.story.engine.app.editor.settings.PreferenceHelper;

public class ProCheckUtils {
    public static boolean isPro(Context context, boolean includeDonations) {

        String packageName = context.getPackageName();

        if (Build.FOR_AMAZON)
            return true;
        else if (packageName.equals("com.maskyn.fileeditorpro"))
            return true;
        else if (includeDonations && PreferenceHelper.hasDonated(context))
            return true;
        else
            return false;
    }

    public static boolean isPro(Context context) {
        return isPro(context, true);
    }
}
