package com.mine.growthstory.addTags.bubblefield;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.mine.growthstory.R;
import com.mine.growthstory.utils.ArraysUtil;
import com.mine.growthstory.utils.KeyBoardUtils;
import com.mine.growthstory.utils.SystemService;

import java.util.ArrayList;
import java.util.List;

public class BubbleField<T> extends LinearLayout {
  private StretchScrollView mScrollView;
  private WrapLayout mWrapLayout;
  protected BackspaceEditText mEditText;

  private String mEditTextHint = getContext().getString(R.string.redesign_edit_tags_subtitle);
  private int mBubbleLayoutRes = R.layout.bubble_item;
  private int mEndLayoutRes   = -1;
  private boolean mShowEndBubble;
  private List<Integer> mActionViewIds = new ArrayList<>();

  int mMaxWidth = -1;
  int mMaxHeight = -1;

  private List<T> mItems;

  private boolean mEditable;

  public interface BubbleActionListener<U> {
    void onAction(U item, View view);
  }

  protected BubbleActionListener<T> mActionListener;

  public interface BubbleItem {
    void onPrepareBubble(View view);
  }

  private OnClickListener mOnClickListener = new OnClickListener() {
    @SuppressWarnings("unchecked")
    @Override
    public void onClick(View v) {
      if (mActionListener != null) {
        Object obj = v.getTag();
        mActionListener.onAction((T) obj, v);
      }
    }
  };

  public BubbleField(Context context) {
    this(context, null);
  }

  public BubbleField(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public BubbleField(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs);
    LayoutInflater layoutInflater = SystemService.layoutInflater(context);
    layoutInflater.inflate(R.layout.bubble_field, this);

    mScrollView = findViewById(R.id.bubble_field_scroll_view);
    mWrapLayout = findViewById(R.id.wrap_layout);
    //mTitleText = (TextView) findViewById(R.id.title);
    mEditText = findViewById(R.id.bubble_field_edit_text);

    mActionViewIds.add(R.id.close_btn);

    mEditable = true;

    if (attrs != null) {
      TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BubbleField, defStyle, 0);

      mMaxWidth = (int) a.getDimension(R.styleable.BubbleField_maxWidth, -1);
      mMaxHeight = (int) a.getDimension(R.styleable.BubbleField_maxHeight, -1);
      mEditable = a.getBoolean(R.styleable.BubbleField_editable, true);

      a.recycle();
    }

    // ignore touch events if not editable
    mEditText.setEditable(mEditable);

    if (mEditable) {
      mWrapLayout.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          mEditText.requestFocus();
          //弹出软键盘
          KeyBoardUtils.showKeyboard(BubbleField.this.getContext(), mEditText);
        }
      });
    } else {
      mEditText.setInputType(InputType.TYPE_NULL);
      mWrapLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
          BubbleField.this.checkTouchEventForScrollView(); // dimension could have changed
        }
      });
    }
  }

  public void setOnItemClickListener(OnItemClickListener l) {
    mEditText.setOnItemClickListener(l);
  }

  public void removeBubble(T item) {
    View view = mWrapLayout.findViewWithTag(item);
    if (view != null) {
      mWrapLayout.removeView(view);
    }
  }

  public void setItems(List<T> tags) {
    mItems = tags;
  }

  public void update() {
    update(true);
  }

  public void update(boolean requestFocus) {
    setBubbles(mItems, requestFocus);
  }

  private void setBubbles(List<T> items, boolean requestFocus) {
    mWrapLayout.removeAllViews();

    if (items == null || items.isEmpty()) {
      mWrapLayout.addView(mEditText);
      mEditText.setHint(mEditTextHint);
      if (getVisibility() == View.VISIBLE) {
        if (requestFocus) {
          mEditText.requestFocus();
        }
        mScrollView.scrollBy(0, mWrapLayout.getHeight());
      }
      checkTouchEventForScrollView();
      return;
    }

    // add recipients first, bubbles will appear in the order they are added (i.e. the first bubble
    // added will be the first in the list)
    for (T item : items) {
      addBubble(item);
    }

    // then add the end bubble
    addEndBubble();

    mWrapLayout.addView(mEditText);
    mEditText.setHint(null);
    if (getVisibility() == View.VISIBLE) {
      if (requestFocus) {
        mEditText.requestFocus();
      }

      if (scrollWhenRefresh()) {
        mScrollView.scrollBy(0, mWrapLayout.getHeight());
      }
    }

    checkTouchEventForScrollView();
  }

  @SuppressWarnings("SimplifiableIfStatement")
  private void checkTouchEventForScrollView() {
    // don't consume touch event if scrollview can't even scroll
    boolean allowScroll;
    if (ArraysUtil.isEmpty(mItems)) {
      allowScroll = false;
    } else {
      allowScroll = mEditable || mScrollView.canScrollVertically(-1) || mScrollView.canScrollVertically(1);
    }
    mScrollView.setAllowTouchEvent(allowScroll);
  }

  private void addBubble(T item) {
    View child = View.inflate(getContext(), getItemLayoutResId(), null);
    child.setTag(item);

    if (item instanceof BubbleItem) {
      ((BubbleItem) item).onPrepareBubble(child);
    }

    TextView textView = child.findViewById(R.id.text);
    textView.setText(item.toString());
    addActionListeners(child, item);

    mWrapLayout.addView(child);
  }

  private void addActionListeners(View bubble, T item) {
    for (int id : mActionViewIds) {
      View actionView = bubble.findViewById(id);
      if (actionView != null) {
        actionView.setOnClickListener(mOnClickListener);
        if (item != null) {
          actionView.setTag(item);
        }
      }
    }

    if (!mEditable && itemClickable()) {
      View textView = bubble.findViewById(R.id.text);
      if (textView != null) {
//         consume touch events on bubble tap
        textView.setClickable(true);
      }
    }
  }

  private void addEndBubble() {
    if (mShowEndBubble && mEndLayoutRes != -1) {
      View child = View.inflate(getContext(), mEndLayoutRes, null);
      child.setId(R.id.bubble_field_end);
      addActionListeners(child, null);
      mWrapLayout.addView(child);
    }
  }

  protected boolean itemClickable() {
    return true;
  }

  protected int getItemLayoutResId() {
    return mBubbleLayoutRes;
  }

  protected boolean scrollWhenRefresh() {
    return true;
  }

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
      setMeasuredDimension(width, height);
    }
  }

  public void setActionListener(BubbleActionListener<T> listener) {
    mActionListener = listener;
  }

  public <U extends ListAdapter & Filterable> void setAdapter(U adapter) {
    mEditText.setAdapter(adapter);
  }

  public void setOnEditorActionListener(OnEditorActionListener l) {
    mEditText.setOnEditorActionListener(l);
  }

  @Override
  public void setOnKeyListener(OnKeyListener l) {
    mEditText.setOnKeyListener(l);
  }

  public void setImeOptions(int imeOptions) {
    mEditText.setImeOptions(imeOptions);
  }

  public void addTextChangedListener(TextWatcher watcher) {
    mEditText.addTextChangedListener(watcher);
  }

  public Editable getText() {
    return mEditText.getText();
  }

  public void setText(CharSequence text) {
    mEditText.setText(text);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    update(false);
  }

  public AutoCompleteTextView getTextView() {
    return mEditText;
  }

  public StretchScrollView getScrollView() {
    return mScrollView;
  }

  public void setInputType() {
    mEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
  }

  public void setTextHint(String hint) {
    mEditTextHint = hint;
    if (mItems == null || mItems.isEmpty()) {
      mEditText.setHint(null);
    } else {
      mEditText.setHint(hint);
    }
  }

  public CharSequence getTextHint() {
    return mEditText.getHint();
  }

  public void setBubbleLayoutResource(int resId) {
    mBubbleLayoutRes = resId;
  }

  public void addActionViewId(int viewId) {
    mActionViewIds.add(viewId);
  }

  public void setEndBubble(int layout) {
    mEndLayoutRes = layout;
  }

  public void setShowEndBubble(boolean show) {
    mShowEndBubble = show;
    update();
  }

  public boolean isShowingEndBubble() {
    return mShowEndBubble;
  }
}
