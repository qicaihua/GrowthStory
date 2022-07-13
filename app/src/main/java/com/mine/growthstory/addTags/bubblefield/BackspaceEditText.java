/**
 * Copyright 2014 Evernote Corporation. All rights reserved.
 */

package com.mine.growthstory.addTags.bubblefield;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;

/**
 * Works around Android bug 42904 (no KEYCODE_DEL events generated in 4.2+) by providing a
 * custom InputConnection that detects the API call made when backspace is pressed and simulates
 * the appropriate KeyEvents.
 *
 * @see <a href="https://code.google.com/p/android/issues/detail?id=42904">Bug</a>
 * <p>
 */
@SuppressLint("AppCompatCustomView")
public class BackspaceEditText extends AutoCompleteTextView {

  private boolean mEditable = true;

  public BackspaceEditText(Context context) {
    this(context, null);
  }

  public BackspaceEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public boolean isEditable() {
    return mEditable;
  }

  public void setEditable(boolean editable) {
    mEditable = editable;
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    // don't consume touch event
    return mEditable && super.onTouchEvent(event);
  }

  @Override
  public InputConnection onCreateInputConnection(@NonNull EditorInfo outAttrs) {
    InputConnection ic = super.onCreateInputConnection(outAttrs);
    if (ic == null) {
      return null;
    }

    return new InputConnectionFix(ic, true);
  }

  private static final class InputConnectionFix extends InputConnectionWrapper {

    public InputConnectionFix(InputConnection target, boolean mutable) {
      super(target, mutable);
    }

    @Override
    public boolean deleteSurroundingText(int beforeLength, int afterLength) {
      if (beforeLength == 1 && afterLength == 0) {
        sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
        sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
        return true;
      }
      return super.deleteSurroundingText(beforeLength, afterLength);
    }
  }
}
