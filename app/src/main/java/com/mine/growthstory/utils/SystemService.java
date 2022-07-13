package com.mine.growthstory.utils;

import android.content.Context;
import android.view.LayoutInflater;

public class SystemService {

    @SuppressWarnings("unchecked")
    private static <T> T getService(Context context, String serviceName) {
        return (T) context.getSystemService(serviceName);
    }

    public static LayoutInflater layoutInflater(Context context) {
        return getService(context, Context.LAYOUT_INFLATER_SERVICE);
    }

}
