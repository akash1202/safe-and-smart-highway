package com.example.akash.myhighway1.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class FontUtils {

    private static String ROBOTO_MEDIUM = "Roboto-Medium.ttf";
    public static Map<String, Typeface> fonts = new HashMap<>();

    public static void setRobotoMedium(TextView textView, TextView... textViews) {
        Typeface font = fonts.get(ROBOTO_MEDIUM);
        if (font == null) {
            font = Typeface.createFromAsset(textView.getContext().getAssets(), ROBOTO_MEDIUM);
            fonts.put(ROBOTO_MEDIUM, font);
        }
        textView.setTypeface(font);
        for (TextView textView1 : textViews) {
            if (textView1 != null) {
                textView1.setTypeface(font);
            }
        }
    }

    public static void setFont(TextView textView, String fontName) {
        Typeface font = fonts.get(fontName);
        if (font == null) {
            font = Typeface.createFromAsset(textView.getContext().getAssets(), fontName);
            fonts.put(fontName, font);
        }
        textView.setTypeface(font);
    }

    public static void setDefaultFont(Context context, String staticTypeFaceFieldName, String fontAssetName) {
        Typeface regular = fonts.get(fontAssetName);
        if (regular == null) {
            regular = Typeface.createFromAsset(context.getAssets(), fontAssetName);
            fonts.put(fontAssetName, regular);
        }
        replaceFont(staticTypeFaceFieldName, regular);
    }

    protected static void replaceFont(String staticTypeFaceFieldName, final Typeface newTypeFace) {
        try {
            final Field staticField = Typeface.class.getDeclaredField(staticTypeFaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeFace);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }


}
