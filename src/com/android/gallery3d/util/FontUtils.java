/*
 * Helper to override default typefaces at runtime.
 */
package com.android.gallery3d.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.lang.reflect.Field;

public class FontUtils {
    private static final String TAG = "FontUtils";

    public static void overrideAllDefaultFonts(Context context, String regularAssetPath, String boldAssetPath) {
        try {
            Typeface regular = Typeface.createFromAsset(context.getAssets(), regularAssetPath);
            Typeface bold;
            try {
                bold = Typeface.createFromAsset(context.getAssets(), boldAssetPath);
            } catch (Exception e) {
                bold = Typeface.create(regular, Typeface.BOLD);
            }

            replaceFont("DEFAULT", regular);
            replaceFont("DEFAULT_BOLD", bold);
            replaceFont("SANS_SERIF", regular);
            replaceFont("SERIF", regular);
            replaceFont("MONOSPACE", regular);
        } catch (Exception e) {
            Log.w(TAG, "Could not load Outfit fonts from assets: " + e);
        }
    }

    private static void replaceFont(String staticFieldName, final Typeface newTypeface) {
        try {
            Field staticField = Typeface.class.getDeclaredField(staticFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (Exception e) {
            Log.w(TAG, "Failed to set font " + staticFieldName + " : " + e);
        }
    }
}
