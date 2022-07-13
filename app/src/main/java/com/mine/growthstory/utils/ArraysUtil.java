package com.mine.growthstory.utils;

import android.content.res.TypedArray;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public final class ArraysUtil {


  private ArraysUtil() {
  }

  public static boolean indexWithin(int index, List list) {
    if (list == null) {
      return false;
    }
    return index >= 0 && index < list.size();
  }

  /**
   * Given an unsorted array, returns whether or not the array contains given
   * object o.
   *
   * @param o   Object to look for. Cannot be null.
   * @param arr Unsorted array
   * @return
   */
  public static boolean contains(Object o, Object[] arr) {
    if (arr == null || o == null) {
      return false;
    }
    for (Object anArr : arr) {
      if (anArr != null && anArr.equals(o)) {
        return true;
      }
    }
    return false;
  }

  // similar to above except takes a List instead of []
  public static <T> boolean contains(T object, List<T> list) {
    if (list == null || object == null) {
      return false;
    }
    for (T listObj : list) {
      if (listObj.equals(object)) {
        return true;
      }
    }
    return false;
  }

  public static <T> List<T> clone(List<T> src) {
    // if src is null, return an empty list
    if (src == null) {
      return new ArrayList<>();
    }

    return new ArrayList<>(src);
  }

  public static <T> boolean isEmpty(Collection<T> list) {
    return (list == null || list.size() == 0);
  }

  public static <K, V> boolean isEmpty(Map<K, V> map) {
    return (map == null || map.size() == 0);
  }

  public static boolean isEmpty(Object[] arr) {
    return (arr == null || arr.length == 0);
  }

  /**
   * This method filters invalid resource IDs in the array and returns an int array containing only valid
   * resource IDs.
   *
   * @param typedArray The array containing the resources.
   * @return An array containing valid resource IDs. Returns {@code null} if the input array was {@code} null.
   */
  public static int[] stripOutInvalidResourceIds(@Nullable TypedArray typedArray) {
    if (typedArray == null) {
      return null;
    }

    int[] tempBuffer = new int[typedArray.length()];
    int length = 0;

    for (int i = 0; i < tempBuffer.length; i++) {
      int resourceId = typedArray.getResourceId(i, -1);
      if (resourceId != -1) {
        tempBuffer[length] = resourceId;
        length++;
      }
    }

    if (length == tempBuffer.length) {
      // no empty resource id, usual case
      return tempBuffer;
    } else {
      return Arrays.copyOf(tempBuffer, length);
    }
  }

  /**
   * This method filters {@code null} values from the input array. If the input array doesn't contain
   * any {@code null} value, then the same array is returned.
   *
   * @param array             The array to filter {@code null} values or empty Strings.
   * @param checkNullAndEmpty If {@code true} this method will filter empty Strings, too.
   * @return An array without a {@code null} value. Returns {@code null} if the input array is {@code null}.
   */
  public static String[] stripOutNull(@Nullable String[] array, boolean checkNullAndEmpty) {
    if (array == null) {
      return null;
    }

    boolean hasNull = false;
    //noinspection ForLoopReplaceableByForEach, don't allocate space for iterator
    for (int i = 0; i < array.length; i++) {
      if (stripString(array[i], checkNullAndEmpty)) {
        hasNull = true;
        break;
      }
    }

    if (!hasNull) {
      // avoids allocation later
      return array;
    }

    List<String> stringList = Arrays.asList(array); // creates ArrayList wrapper around array
    Iterator<String> iterator = stringList.iterator();
    while (iterator.hasNext()) {
      if (stripString(iterator.next(), checkNullAndEmpty)) {
        iterator.remove();
      }
    }

    return stringList.toArray(new String[stringList.size()]);
  }

  private static boolean stripString(String value, boolean checkNullAndEmpty) {
    return checkNullAndEmpty ? TextUtils.isEmpty(value) : value == null;
  }

  public static <T> void stripToSize(LinkedList<T> list, int toSize) {
    if (list == null) {
      return;
    }

    if (toSize < 0) {
      return;
    }

    int elementsToRemove = list.size() - toSize;

    if (elementsToRemove <= 0) {
      return;
    }

    ListIterator<T> listIterator = list.listIterator();
    for (int i = 0; i < elementsToRemove && listIterator.hasNext(); i++) {
      listIterator.next();
      listIterator.remove();
    }
  }

  @Nullable
  public static <T> T get(@Nullable T[] array, int index) {
    if (array == null || index < 0 || index >= array.length) {
      return null;
    } else {
      return array[index];
    }
  }
}
