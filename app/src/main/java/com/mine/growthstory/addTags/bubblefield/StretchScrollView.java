package com.mine.growthstory.addTags.bubblefield;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.widget.NestedScrollView;
import com.mine.growthstory.R;

public class StretchScrollView extends NestedScrollView {
  private static final boolean DEBUG = false;

  private ScrollChangedListener mOnScrollChangedListener;

  private boolean mAllowTouchEvent = true;
  private boolean mScrollable = true;

  public StretchScrollView(Context context) {
    this(context, null);
  }

  public StretchScrollView(Context context, AttributeSet attrs) {
    super(context, attrs);

    if (attrs != null) {
      TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StretchView);

      mMaxWidth = (int) a.getDimension(R.styleable.StretchView_maxWidth, -1);
      mMaxHeight = (int) a.getDimension(R.styleable.StretchView_maxHeight, -1);

      a.recycle();
    }
  }

  public StretchScrollView(Context context, AttributeSet attrs, int defStyle) {
    this(context, attrs);
  }

  public void setOnScrollChangedListener(ScrollChangedListener listener) {
    mOnScrollChangedListener = listener;
  }

  public void setScrollable(boolean scrollable) {
    mScrollable = scrollable;
  }

  public boolean isScrollable() {
    return mScrollable;
  }

  public boolean isAllowTouchEvent() {
    return mAllowTouchEvent;
  }

  public void setAllowTouchEvent(boolean allowTouchEvent) {
    mAllowTouchEvent = allowTouchEvent;
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    if (!mScrollable) {
      return false;
    } else {
      return super.onInterceptTouchEvent(ev);
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    boolean result = super.onTouchEvent(event);
    if (mAllowTouchEvent) {
      return result;
    } else {
      return false;
    }
  }

  int mMaxWidth = -1;
  int mMaxHeight = -1;

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    int measuredWidth = getMeasuredWidth();
    int measuredHeight = getMeasuredHeight();

    int width;
    int height;
    if (mMaxWidth > 0 && measuredWidth > mMaxWidth) {
      width = mMaxWidth;
    } else {
      width = measuredWidth;
    }

    if (mMaxHeight > 0 && measuredHeight > mMaxHeight) {
      height = mMaxHeight;
    } else {
      height = measuredHeight;
    }

    if (width != measuredWidth || height != measuredHeight) {
      final int desiredHSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
      final int desiredWSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);

      setMeasuredDimension(width, height);
      measureChildren(desiredWSpec, desiredHSpec);
    }

  }

  public int getMaxWidth() {
    return mMaxWidth;
  }

  public int getMaxHeight() {
    return mMaxHeight;
  }

  public void setMaxWidth(int maxWidth) {
    mMaxWidth = maxWidth;
  }

  public void setMaxHeight(int maxHeight) {
    mMaxHeight = maxHeight;
  }

  @Override
  protected void onScrollChanged(int l, int t, int oldl, int oldt) {
    super.onScrollChanged(l, t, oldl, oldt);
    if (mOnScrollChangedListener != null) {
      mOnScrollChangedListener.onScrollChanged(l, t, oldl, oldt);
    }
  }

  public interface ScrollChangedListener {
    void onScrollChanged(int horizontal, int vertical,
                         int oldHorizontal, int oldVertical);
  }

  @Override
  public boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
    if (mScrollable) {
      return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    } else {
      return false;
    }
  }

  private final Rect mTempRect = new Rect();

  public int scrollToChild(View child, boolean isSmoothScroll) {
    child.getDrawingRect(mTempRect);

    /* Offset from child's local coordinates to ScrollView coordinates */
    offsetDescendantRectToMyCoords(child, mTempRect);

    int scrollDelta = EComputeScrollDeltaToGetChildRectOnScreen(mTempRect);
//    Log.d("XXX",scrollToChild()::scrollDelta="+scrollDelta+target="+(getScrollY()+scrollDelta));

    if (scrollDelta != 0) {
      if (isSmoothScroll) {
        smoothScrollBy(0, scrollDelta);
      } else {
        scrollBy(0, scrollDelta);
      }
      return getScrollY() + scrollDelta;
    }
    return 0;
  }

  protected int EComputeScrollDeltaToGetChildRectOnScreen(Rect rect) {
    if (getChildCount() == 0) return 0;

    int height = getHeight();
    int screenTop = getScrollY();
    int screenBottom = screenTop + height;

    int fadingEdge = getVerticalFadingEdgeLength();

    // leave room for top fading edge as long as rect isn't at very top
    if (rect.top > 0) {
      screenTop += fadingEdge;
    }

    // leave room for bottom fading edge as long as rect isn't at very bottom
    if (rect.bottom < getChildAt(0).getHeight()) {
      screenBottom -= fadingEdge;
    }

    int scrollYDelta = 0;

    if (DEBUG)
      Log.d("XXX","EComputeScrollDeltaToGetChildRectOnScreen()::rect.height()=" + rect.height() + "::rect.top=" + rect.top
        + "::height=" + height + "::screenTop=" + screenTop);

    if (rect.top > screenTop) {
      // need to move down to get it in view: move down just enough so
      // that the entire rectangle is in view (or at least the first
      // screen size chunk).

//      if (rect.height() > height) {
//        // just enough to get screen size chunk on
//        scrollYDelta += (rect.top - screenTop);
//      } else {
//        // get entire rect at bottom of screen
//        scrollYDelta += (rect.bottom - screenBottom);
//      }
      scrollYDelta += (rect.top - screenTop);

      // make sure we aren't scrolling beyond the end of our content
//      int bottom = getChildAt(0).getBottom();
//      int distanceToBottom = bottom - screenBottom;
//      scrollYDelta = Math.min(scrollYDelta, distanceToBottom);

    } else if (rect.top < screenTop && rect.bottom < screenBottom) {
      // need to move up to get it in view: move up just enough so that
      // entire rectangle is in view (or at least the first screen
      // size chunk of it).

      if (rect.height() > height) {
        // screen size chunk
        scrollYDelta -= (screenBottom - rect.bottom);
      } else {
        // entire rect at top
        scrollYDelta -= (screenTop - rect.top);
      }

      // make sure we aren't scrolling any further than the top our content
      scrollYDelta = Math.max(scrollYDelta, -getScrollY());
    }
    return scrollYDelta;
  }
}
