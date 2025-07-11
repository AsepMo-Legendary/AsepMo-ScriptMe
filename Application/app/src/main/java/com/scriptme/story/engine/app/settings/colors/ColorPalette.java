package com.scriptme.story.engine.app.settings.colors;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;

import com.scriptme.story.R;

public class ColorPalette {

    public static int[] getAccentColors(Context context) {
        return new int[]{
			ContextCompat.getColor(context, R.color.engine_color_red_500),
			ContextCompat.getColor(context, R.color.engine_color_purple_500),
			ContextCompat.getColor(context, R.color.engine_color_deep_purple_500),
			ContextCompat.getColor(context, R.color.engine_color_blue_500),
			ContextCompat.getColor(context, R.color.engine_color_light_blue_500),
			ContextCompat.getColor(context, R.color.engine_color_cyan_500),
			ContextCompat.getColor(context, R.color.engine_color_teal_500),
			ContextCompat.getColor(context, R.color.engine_color_green_500),
			ContextCompat.getColor(context, R.color.engine_color_yellow_500),
			ContextCompat.getColor(context, R.color.engine_color_orange_500),
			ContextCompat.getColor(context, R.color.engine_color_deep_orange_500),
			ContextCompat.getColor(context, R.color.engine_color_brown_500),
			ContextCompat.getColor(context, R.color.engine_color_blue_grey_500),
        };
    }

    public static int getObscuredColor(int c) {
        float[] hsv = new float[3];
        int color = c;
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.85f; // value component
        color = Color.HSVToColor(hsv);
        return color;
    }

	public static String getHexColor(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }

    public static int getDarkerColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.72f; // value component
        return Color.HSVToColor(hsv);
    }
	
    public static int getTransparentColor(int color, int alpha) {
        return  ColorUtils.setAlphaComponent(color, alpha);
    }

    public static int[] getTransparencyShadows(int color) {
        int[] shadows = new int[10];
        for (int i=0; i < 10;i++)
            shadows[i] = (ColorPalette.getTransparentColor(color, ((100 - (i * 10)) * 255) / 100));
        return shadows;
    }

    public static int[] getBaseColors(Context context) {
        return new int[]{
			ContextCompat.getColor(context, R.color.engine_color_red_500),
			ContextCompat.getColor(context, R.color.engine_color_pink_500),
			ContextCompat.getColor(context, R.color.engine_color_purple_500),
			ContextCompat.getColor(context, R.color.engine_color_deep_purple_500),
			ContextCompat.getColor(context, R.color.engine_color_indigo_500),
			ContextCompat.getColor(context, R.color.engine_color_blue_500),
			ContextCompat.getColor(context, R.color.engine_color_light_blue_500),
			ContextCompat.getColor(context, R.color.engine_color_cyan_500),
			ContextCompat.getColor(context, R.color.engine_color_teal_500),
			ContextCompat.getColor(context, R.color.engine_color_green_500),
			ContextCompat.getColor(context, R.color.engine_color_light_green_500),
			ContextCompat.getColor(context, R.color.engine_color_lime_500),
			ContextCompat.getColor(context, R.color.engine_color_yellow_500),
			ContextCompat.getColor(context, R.color.engine_color_amber_500),
			ContextCompat.getColor(context, R.color.engine_color_orange_500),
			ContextCompat.getColor(context, R.color.engine_color_deep_orange_500),
			ContextCompat.getColor(context, R.color.engine_color_brown_500),
			ContextCompat.getColor(context, R.color.engine_color_blue_grey_500),
			ContextCompat.getColor(context, R.color.engine_color_grey_500)
        };
    }

    public static int[] getColors(Context context, int c) {
        if (c == ContextCompat.getColor(context, R.color.engine_color_red_500)) {
            return new int[]{
				ContextCompat.getColor(context, R.color.engine_color_red_200),
				ContextCompat.getColor(context, R.color.engine_color_red_300),
				ContextCompat.getColor(context, R.color.engine_color_red_400),
				ContextCompat.getColor(context, R.color.engine_color_red_500),
				ContextCompat.getColor(context, R.color.engine_color_red_600),
				ContextCompat.getColor(context, R.color.engine_color_red_700),
				ContextCompat.getColor(context, R.color.engine_color_red_800),
				ContextCompat.getColor(context, R.color.engine_color_red_900)
            };
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_pink_500)) {
            return new int[]{
				ContextCompat.getColor(context, R.color.engine_color_pink_200),
				ContextCompat.getColor(context, R.color.engine_color_pink_300),
				ContextCompat.getColor(context, R.color.engine_color_pink_400),
				ContextCompat.getColor(context, R.color.engine_color_pink_500),
				ContextCompat.getColor(context, R.color.engine_color_pink_600),
				ContextCompat.getColor(context, R.color.engine_color_pink_700),
				ContextCompat.getColor(context, R.color.engine_color_pink_800),
				ContextCompat.getColor(context, R.color.engine_color_pink_900)
            };
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_purple_500)) {
            return new int[]{
				ContextCompat.getColor(context, R.color.engine_color_purple_200),
				ContextCompat.getColor(context, R.color.engine_color_purple_300),
				ContextCompat.getColor(context, R.color.engine_color_purple_400),
				ContextCompat.getColor(context, R.color.engine_color_purple_500),
				ContextCompat.getColor(context, R.color.engine_color_purple_600),
				ContextCompat.getColor(context, R.color.engine_color_purple_700),
				ContextCompat.getColor(context, R.color.engine_color_purple_800),
				ContextCompat.getColor(context, R.color.engine_color_purple_900)
            };
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_deep_purple_500)) {
            return new int[]{
				ContextCompat.getColor(context, R.color.engine_color_deep_purple_200),
				ContextCompat.getColor(context, R.color.engine_color_deep_purple_300),
				ContextCompat.getColor(context, R.color.engine_color_deep_purple_400),
				ContextCompat.getColor(context, R.color.engine_color_deep_purple_500),
				ContextCompat.getColor(context, R.color.engine_color_deep_purple_600),
				ContextCompat.getColor(context, R.color.engine_color_deep_purple_700),
				ContextCompat.getColor(context, R.color.engine_color_deep_purple_800),
				ContextCompat.getColor(context, R.color.engine_color_deep_purple_900)
            };
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_indigo_500)) {
            return new int[]{
				ContextCompat.getColor(context, R.color.engine_color_indigo_200),
				ContextCompat.getColor(context, R.color.engine_color_indigo_300),
				ContextCompat.getColor(context, R.color.engine_color_indigo_400),
				ContextCompat.getColor(context, R.color.engine_color_indigo_500),
				ContextCompat.getColor(context, R.color.engine_color_indigo_600),
				ContextCompat.getColor(context, R.color.engine_color_indigo_700),
				ContextCompat.getColor(context, R.color.engine_color_indigo_800),
				ContextCompat.getColor(context, R.color.engine_color_indigo_900)
            };
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_blue_500)) {
            return new int[]{
				ContextCompat.getColor(context, R.color.engine_color_blue_200),
				ContextCompat.getColor(context, R.color.engine_color_blue_300),
				ContextCompat.getColor(context, R.color.engine_color_blue_400),
				ContextCompat.getColor(context, R.color.engine_color_blue_500),
				ContextCompat.getColor(context, R.color.engine_color_blue_600),
				ContextCompat.getColor(context, R.color.engine_color_blue_700),
				ContextCompat.getColor(context, R.color.engine_color_blue_800),
				ContextCompat.getColor(context, R.color.engine_color_blue_900)
            };
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_light_blue_500)) {
            return new int[]{
				ContextCompat.getColor(context, R.color.engine_color_light_blue_200),
				ContextCompat.getColor(context, R.color.engine_color_light_blue_300),
				ContextCompat.getColor(context, R.color.engine_color_light_blue_400),
				ContextCompat.getColor(context, R.color.engine_color_light_blue_500),
				ContextCompat.getColor(context, R.color.engine_color_light_blue_600),
				ContextCompat.getColor(context, R.color.engine_color_light_blue_700),
				ContextCompat.getColor(context, R.color.engine_color_light_blue_800),
				ContextCompat.getColor(context, R.color.engine_color_light_blue_900)
            };
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_cyan_500)) {
            return new int[]{
				ContextCompat.getColor(context, R.color.engine_color_cyan_200),
				ContextCompat.getColor(context, R.color.engine_color_cyan_300),
				ContextCompat.getColor(context, R.color.engine_color_cyan_400),
				ContextCompat.getColor(context, R.color.engine_color_cyan_500),
				ContextCompat.getColor(context, R.color.engine_color_cyan_600),
				ContextCompat.getColor(context, R.color.engine_color_cyan_700),
				ContextCompat.getColor(context, R.color.engine_color_cyan_800),
				ContextCompat.getColor(context, R.color.engine_color_cyan_900)
            };
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_teal_500)) {
            return new int[]{
				ContextCompat.getColor(context, R.color.engine_color_teal_200),
				ContextCompat.getColor(context, R.color.engine_color_teal_300),
				ContextCompat.getColor(context, R.color.engine_color_teal_400),
				ContextCompat.getColor(context, R.color.engine_color_teal_500),
				ContextCompat.getColor(context, R.color.engine_color_teal_600),
				ContextCompat.getColor(context, R.color.engine_color_teal_700),
				ContextCompat.getColor(context, R.color.engine_color_teal_800),
				ContextCompat.getColor(context, R.color.engine_color_teal_900)
            };
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_green_500)) {
            return new int[]{
				ContextCompat.getColor(context, R.color.engine_color_green_200),
				ContextCompat.getColor(context, R.color.engine_color_green_300),
				ContextCompat.getColor(context, R.color.engine_color_green_400),
				ContextCompat.getColor(context, R.color.engine_color_green_500),
				ContextCompat.getColor(context, R.color.engine_color_green_600),
				ContextCompat.getColor(context, R.color.engine_color_green_700),
				ContextCompat.getColor(context, R.color.engine_color_green_800),
				ContextCompat.getColor(context, R.color.engine_color_green_900)
            };
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_light_green_500)) {
            return new int[]{
				ContextCompat.getColor(context, R.color.engine_color_light_green_200),
				ContextCompat.getColor(context, R.color.engine_color_light_green_300),
				ContextCompat.getColor(context, R.color.engine_color_light_green_400),
				ContextCompat.getColor(context, R.color.engine_color_light_green_500),
				ContextCompat.getColor(context, R.color.engine_color_light_green_600),
				ContextCompat.getColor(context, R.color.engine_color_light_green_700),
				ContextCompat.getColor(context, R.color.engine_color_light_green_800),
				ContextCompat.getColor(context, R.color.engine_color_light_green_900)
            };
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_lime_500)) {
            return new int[]{
				ContextCompat.getColor(context, R.color.engine_color_lime_200),
				ContextCompat.getColor(context, R.color.engine_color_lime_300),
				ContextCompat.getColor(context, R.color.engine_color_lime_400),
				ContextCompat.getColor(context, R.color.engine_color_lime_500),
				ContextCompat.getColor(context, R.color.engine_color_lime_600),
				ContextCompat.getColor(context, R.color.engine_color_lime_700),
				ContextCompat.getColor(context, R.color.engine_color_lime_800),
				ContextCompat.getColor(context, R.color.engine_color_lime_900)
            };
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_yellow_500)) {
            return new int[]{
				ContextCompat.getColor(context, R.color.engine_color_yellow_400),
				ContextCompat.getColor(context, R.color.engine_color_yellow_500),
				ContextCompat.getColor(context, R.color.engine_color_yellow_600),
				ContextCompat.getColor(context, R.color.engine_color_yellow_700),
				ContextCompat.getColor(context, R.color.engine_color_yellow_800),
				ContextCompat.getColor(context, R.color.engine_color_yellow_900)
            };
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_amber_500)) {
            return new int[]{
				ContextCompat.getColor(context, R.color.engine_color_amber_200),
				ContextCompat.getColor(context, R.color.engine_color_amber_300),
				ContextCompat.getColor(context, R.color.engine_color_amber_400),
				ContextCompat.getColor(context, R.color.engine_color_amber_500),
				ContextCompat.getColor(context, R.color.engine_color_amber_600),
				ContextCompat.getColor(context, R.color.engine_color_amber_700),
				ContextCompat.getColor(context, R.color.engine_color_amber_800),
				ContextCompat.getColor(context, R.color.engine_color_amber_900)
            };
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_orange_500)) {
            return new int[]{
				ContextCompat.getColor(context, R.color.engine_color_orange_200),
				ContextCompat.getColor(context, R.color.engine_color_orange_300),
				ContextCompat.getColor(context, R.color.engine_color_orange_400),
				ContextCompat.getColor(context, R.color.engine_color_orange_500),
				ContextCompat.getColor(context, R.color.engine_color_orange_600),
				ContextCompat.getColor(context, R.color.engine_color_orange_700),
				ContextCompat.getColor(context, R.color.engine_color_orange_800),
				ContextCompat.getColor(context, R.color.engine_color_orange_900)
            };
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_deep_orange_500)) {
            return new int[]{
				ContextCompat.getColor(context, R.color.engine_color_deep_orange_200),
				ContextCompat.getColor(context, R.color.engine_color_deep_orange_300),
				ContextCompat.getColor(context, R.color.engine_color_deep_orange_400),
				ContextCompat.getColor(context, R.color.engine_color_deep_orange_500),
				ContextCompat.getColor(context, R.color.engine_color_deep_orange_600),
				ContextCompat.getColor(context, R.color.engine_color_deep_orange_700),
				ContextCompat.getColor(context, R.color.engine_color_deep_orange_800),
				ContextCompat.getColor(context, R.color.engine_color_deep_orange_900)
            };
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_brown_500)) {
            return new int[]{
				ContextCompat.getColor(context, R.color.engine_color_brown_200),
				ContextCompat.getColor(context, R.color.engine_color_brown_300),
				ContextCompat.getColor(context, R.color.engine_color_brown_400),
				ContextCompat.getColor(context, R.color.engine_color_brown_500),
				ContextCompat.getColor(context, R.color.engine_color_brown_600),
				ContextCompat.getColor(context, R.color.engine_color_brown_700),
				ContextCompat.getColor(context, R.color.engine_color_brown_800),
				ContextCompat.getColor(context, R.color.engine_color_brown_900)
            };
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_grey_500)) {
            return new int[]{
				ContextCompat.getColor(context, R.color.engine_color_grey_400),
				ContextCompat.getColor(context, R.color.engine_color_grey_500),
				ContextCompat.getColor(context, R.color.engine_color_grey_600),
				ContextCompat.getColor(context, R.color.engine_color_grey_700),
				ContextCompat.getColor(context, R.color.engine_color_grey_800),
				ContextCompat.getColor(context, R.color.engine_color_grey_900),
				Color.parseColor("#000000")
            };
        } else {
            return new int[]{
				ContextCompat.getColor(context, R.color.engine_color_blue_grey_300),
				ContextCompat.getColor(context, R.color.engine_color_blue_grey_400),
				ContextCompat.getColor(context, R.color.engine_color_blue_grey_500),
				ContextCompat.getColor(context, R.color.engine_color_blue_grey_600),
				ContextCompat.getColor(context, R.color.engine_color_blue_grey_700),
				ContextCompat.getColor(context, R.color.engine_color_blue_grey_800),
				ContextCompat.getColor(context, R.color.engine_color_blue_grey_900)
            };
        }
    }

    public static int getAccentColor(Context context, int c) {
        if (c == ContextCompat.getColor(context, R.color.engine_color_red_500)) {
            return ContextCompat.getColor(context, R.color.engine_color_cyan_500);
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_pink_500)) {
            return ContextCompat.getColor(context, R.color.engine_color_teal_500);
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_purple_500)) {
            return ContextCompat.getColor(context, R.color.engine_color_green_500);
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_deep_purple_500)) {
            return ContextCompat.getColor(context, R.color.engine_color_green_500);
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_indigo_500)) {
            return ContextCompat.getColor(context, R.color.engine_color_yellow_500);
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_blue_500)) {
            return ContextCompat.getColor(context, R.color.engine_color_orange_500);
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_light_blue_500)) {
            return ContextCompat.getColor(context, R.color.engine_color_deep_orange_500);
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_cyan_500)) {
            return ContextCompat.getColor(context, R.color.engine_color_red_500);
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_teal_500)) {
            return ContextCompat.getColor(context, R.color.engine_color_red_500);
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_green_500)) {
            return ContextCompat.getColor(context, R.color.engine_color_purple_500);
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_light_green_500)) {
            return ContextCompat.getColor(context, R.color.engine_color_deep_purple_500);
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_lime_500)) {
            return ContextCompat.getColor(context, R.color.engine_color_indigo_500);
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_yellow_500)) {
            return ContextCompat.getColor(context, R.color.engine_color_blue_500);
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_amber_500)) {
            return ContextCompat.getColor(context, R.color.engine_color_indigo_500);
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_orange_500)) {
            return ContextCompat.getColor(context, R.color.engine_color_indigo_500);
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_deep_orange_500)) {
            return ContextCompat.getColor(context, R.color.engine_color_cyan_500);
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_brown_500)) {
            return ContextCompat.getColor(context, R.color.engine_color_blue_grey_500);
        } else if (c == ContextCompat.getColor(context, R.color.engine_color_grey_500)) {
            return ContextCompat.getColor(context, R.color.engine_color_brown_500);
        } else {
            return ContextCompat.getColor(context, R.color.engine_color_red_500);
        }
    }
}
