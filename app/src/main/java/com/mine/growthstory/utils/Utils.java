package com.mine.growthstory.utils;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class Utils {
  private static float DENSITY = 0;

  public static int convertToPx(float dp) {

    return Math.round(dp * getDisplayDensity());
  }

  public static float getDisplayDensity() {
    if (DENSITY == 0) {
      DENSITY = getDisplayMetrics().density;
    }
    return DENSITY;
  }

  public static DisplayMetrics getDisplayMetrics() {
    return Resources.getSystem().getDisplayMetrics();
  }
}

