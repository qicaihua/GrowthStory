package com.mine.growthstory.addTags.bubblefield;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.mine.growthstory.R;
import com.mine.growthstory.utils.Utils;

public class WrapLayout extends ViewGroup {
  private int mHorizontalSpacing;
  private int mVerticalSpacing;
  private boolean mAlignRowBottom;

  public static class LayoutParams extends ViewGroup.LayoutParams {
    public float mHorizontalSpacing;
    public float mVerticalSpacing;

    /**
     * @param horizontalSpacing Pixels between items, horizontally
     * @param verticalSpacing   Pixels between items, vertically
     */
    public LayoutParams(float horizontalSpacing, float verticalSpacing) {
      super(0, 0);
      mHorizontalSpacing = horizontalSpacing;
      mVerticalSpacing = verticalSpacing;
    }
  }

  public WrapLayout(Context context) {
    this(context, null);
  }

  public WrapLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public WrapLayout(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    Resources res = getResources();
    int px = Utils.convertToPx(1);

    if (attrs == null) {
      mHorizontalSpacing = px;
      mVerticalSpacing = px;
      mAlignRowBottom = false;
    } else {
      TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WrapLayout, defStyle, 0);

      mHorizontalSpacing = a.getDimensionPixelSize(R.styleable.WrapLayout_horizontalSpacing, px);
      mVerticalSpacing = a.getDimensionPixelSize(R.styleable.WrapLayout_verticalSpacing, px);
      mAlignRowBottom = a.getBoolean(R.styleable.WrapLayout_alignRowBottom, false);

      a.recycle();
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED) {
      setMeasuredDimension(0, 0);
      return;
    }

    final int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
    int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
    final int count = getChildCount();
    int lineHeight = 0;

    int xpos = getPaddingLeft();
    int ypos = getPaddingTop();

    for (int i = 0; i < count; i++) {
      final View child = getChildAt(i);
      if (child.getVisibility() != GONE) {
        final LayoutParams lp = (LayoutParams) child.getLayoutParams();

        int childHeightMeasureSpec;
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
          childHeightMeasureSpec =
            MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
        } else {
          childHeightMeasureSpec =
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST),
          childHeightMeasureSpec);

        final int childw = child.getMeasuredWidth();
        lineHeight = (int) Math.max(lineHeight, child.getMeasuredHeight() + lp.mVerticalSpacing);

        if (xpos + childw + getPaddingRight() > width) {
          xpos = getPaddingLeft();
          ypos += lineHeight;
          lineHeight = child.getMeasuredHeight() + (int) lp.mVerticalSpacing;
        }

        xpos += childw + lp.mHorizontalSpacing;
      }
    }

    if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
      height = ypos + lineHeight;

    } else if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
      if (ypos + lineHeight < height) {
        height = ypos + lineHeight;
      }
    }
    setMeasuredDimension(width, height);
  }

  @Override
  protected ViewGroup.LayoutParams generateDefaultLayoutParams() {

    return new LayoutParams(mHorizontalSpacing, mVerticalSpacing);
  }

  @Override
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
    LayoutParams params = new LayoutParams(mHorizontalSpacing, mVerticalSpacing);
    params.width = p.width;
    params.layoutAnimationParameters = p.layoutAnimationParameters;
    params.height = p.height;

    return params;
  }

  @Override
  protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
    return p instanceof LayoutParams;
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    final int count = getChildCount();
    final int width = r - l;
    int xpos = getPaddingLeft();
    int ypos = getPaddingTop();

    int lineHeight = 0;
    int start = 0; // the first item always starts at a new line
    for (int i = 0; i < count; i++) {
      final View child = getChildAt(i);
      if (child.getVisibility() != GONE) {
        final int childw = child.getMeasuredWidth();
        final int childh = child.getMeasuredHeight();
        final LayoutParams lp = (LayoutParams) child.getLayoutParams();
        if (xpos + childw + getPaddingRight() > width) {
          // lets readjust the previous row if necessary
          alignsChildrenToBottom(lineHeight, start, i);
          start = i;
          // now, lets start a new line proper
          xpos = getPaddingLeft();
          ypos += lineHeight;
          lineHeight = childh + (int) lp.mVerticalSpacing;
        }
        child.layout(xpos, ypos, xpos + childw, ypos + childh);
        xpos += childw + lp.mHorizontalSpacing;
        // get the maximum line height for the current line
        lineHeight = (int) Math.max(lineHeight, childh + lp.mVerticalSpacing);
      }
    }
    alignsChildrenToBottom(lineHeight, start, count);
  }

  /**
   * Aligns all children from {@code start} index to {@code end} exclusive to the bottom
   *
   * @param lineHeight
   * @param start
   * @param end
   * @return
   */
  private int alignsChildrenToBottom(int lineHeight, int start, int end) {
    if (mAlignRowBottom) {
      for (int i = start; i < end; ++i) {
        final View child = getChildAt(i);
        if (child.getVisibility() == GONE) {
          continue;
        }
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        int height = child.getHeight();
        int heightDiff = lineHeight - (height + (int) lp.mVerticalSpacing);
        if (heightDiff > 0) {
          child.layout(child.getLeft(), child.getTop() + heightDiff, child.getRight(), child.getBottom() + heightDiff);
        }
      }
    }
    return start;
  }
}
