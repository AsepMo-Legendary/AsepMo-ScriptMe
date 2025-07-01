package com.scriptme.story.engine.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.TypedValue;
import android.util.Log;
import android.view.View;

import java.net.URISyntaxException;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Random;

import com.scriptme.story.R;

public class WidgetUtils {
    public static final String TAG = WidgetUtils.class.getSimpleName();
    
    private static Map<String, Typeface> cachedFontMap = new HashMap<String, Typeface>();

    public static boolean isLight(int color) {
        return Math.sqrt(
            Color.red(color) * Color.red(color) * .241 +
            Color.green(color) * Color.green(color) * .691 +
            Color.blue(color) * Color.blue(color) * .068) > 130;
    }
    public static int pxToSp(final Context context, final float px) {
        return Math.round(px / context.getResources().getDisplayMetrics().scaledDensity);
    }

    public static int spToPx(final Context context, final float sp) {
        return Math.round(sp * context.getResources().getDisplayMetrics().scaledDensity);
    }

    public static int dp2px(Context context, float dp) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return Math.round(px);
    }
    /**
     * Converts dp to px
     *
     * @param res Resources
     * @param dp  the value in dp
     * @return int
     */
    public static int toPixels(Resources res, float dp) {
        return (int) (dp * res.getDisplayMetrics().density);
    }

    public static float evaluate(float fraction, float startValue, float endValue) {
        return startValue + fraction * (endValue - startValue);
    }
    
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isRtl(Resources res) {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) &&
                (res.getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL);
    }
    
    public static Typeface findFont(Context context, String fontPath, String defaultFontPath){

        if (fontPath == null){
            return Typeface.DEFAULT;
        }

        String fontName = new File(fontPath).getName();
        String defaultFontName = "";
        if (!TextUtils.isEmpty(defaultFontPath)){
            defaultFontName = new File(defaultFontPath).getName();
        }

        if (cachedFontMap.containsKey(fontName)){
            return cachedFontMap.get(fontName);
        }else{
            try{
                AssetManager assets = context.getResources().getAssets();

                if (Arrays.asList(assets.list("")).contains(fontPath)){
                    Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontName);
                    cachedFontMap.put(fontName, typeface);
                    return typeface;
                }else if (Arrays.asList(assets.list("fonts")).contains(fontName)){
                    Typeface typeface = Typeface.createFromAsset(context.getAssets(), String.format("fonts/%s",fontName));
                    cachedFontMap.put(fontName, typeface);
                    return typeface;
                }else if (Arrays.asList(assets.list("iconfonts")).contains(fontName)){
                    Typeface typeface = Typeface.createFromAsset(context.getAssets(), String.format("iconfonts/%s",fontName));
                    cachedFontMap.put(fontName, typeface);
                    return typeface;
                }else if (!TextUtils.isEmpty(defaultFontPath) && Arrays.asList(assets.list("")).contains(defaultFontPath)){
                    Typeface typeface = Typeface.createFromAsset(context.getAssets(), defaultFontPath);
                    cachedFontMap.put(defaultFontName, typeface);
                    return typeface;
                } else {
                    throw new Exception("Font not Found");
                }

            }catch (Exception e){
                Log.e(TAG, String.format("Unable to find %s font. Using Typeface.DEFAULT instead.", fontName));
                cachedFontMap.put(fontName, Typeface.DEFAULT);
                return Typeface.DEFAULT;
            }
        }
    }

    public static String getExtension(String name) {
        String ext;

        if (name.lastIndexOf(".") == -1) {
            ext = "";

        } else {
            int index = name.lastIndexOf(".");
            ext = name.substring(index + 1, name.length());
        }
        return ext;
    }

    public static String getFileExtension(File file) {
        return getFileExtension(file.getName());
    }

    /**
     * Gets extension of the file name excluding the . character
     */
    public static String getFileExtension(String fileName) {
        if (fileName.contains("."))
            return fileName.substring(fileName.lastIndexOf('.') + 1);
        else 
            return "";
    }

    public static boolean isSupportedVideo(File file) {
        String ext = getExtension(file.getName());
        return ext.equalsIgnoreCase("video");
    }
    
    
    public static class ColorGenerator {

        public static ColorGenerator DEFAULT;

        public static ColorGenerator MATERIAL;

        static {
            DEFAULT = create(Arrays.asList(
                                 0xfff16364,
                                 0xfff58559,
                                 0xfff9a43e,
                                 0xffe4c62e,
                                 0xff67bf74,
                                 0xff59a2be,
                                 0xff2093cd,
                                 0xffad62a7,
                                 0xff805781
                             ));
            MATERIAL = create(Arrays.asList(
                                  0xffe57373,
                                  0xfff06292,
                                  0xffba68c8,
                                  0xff9575cd,
                                  0xff7986cb,
                                  0xff64b5f6,
                                  0xff4fc3f7,
                                  0xff4dd0e1,
                                  0xff4db6ac,
                                  0xff81c784,
                                  0xffaed581,
                                  0xffff8a65,
                                  0xffd4e157,
                                  0xffffd54f,
                                  0xffffb74d,
                                  0xffa1887f,
                                  0xff90a4ae
                              ));
        }

        private final List<Integer> mColors;
        private final Random mRandom;

        public static ColorGenerator create(List<Integer> colorList) {
            return new ColorGenerator(colorList);
        }

        private ColorGenerator(List<Integer> colorList) {
            mColors = colorList;
            mRandom = new Random(System.currentTimeMillis());
        }

        public int getRandomColor() {
            return mColors.get(mRandom.nextInt(mColors.size()));
        }

        public int getColor(Object key) {
            return mColors.get(Math.abs(key.hashCode()) % mColors.size());
        }
    }
    
    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }
}

