package com.mine.growthstory.addTags

import android.os.Bundle
import android.text.TextUtils
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mine.growthstory.R
import com.mine.growthstory.addTags.bubblefield.BubbleField

/**
 * 1、在文本框中输入标签文字，可新增标签
 * 2、点键盘下一步操作后，标签文字变成标签，支持删除
 * 3、点击标签右上角叉，执行删除操作
 */
open class TagEditActivity : AppCompatActivity(){
    private var mCurrentNoteTags: ArrayList<String>? = null
    private var mBubbleField:BubbleField<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        setContentView(R.layout.tag_edit_layout)

        mCurrentNoteTags = ArrayList<String>()
        mCurrentNoteTags!!.add(0,"tag1")
        mCurrentNoteTags!!.add(1,"tag2")
        mCurrentNoteTags!!.add(2,"tag3")
        mCurrentNoteTags!!.add(3,"tag4")
        mCurrentNoteTags!!.add(4,"tag5")

        mBubbleField = findViewById<BubbleField<String>>(R.id.bubble_field)
        mBubbleField?.setItems(mCurrentNoteTags)
        setListeners()
        super.onCreate(savedInstanceState)
    }

    private fun setListeners() {
        mBubbleField?.setActionListener(mOnCloseBubbleListener)
        mBubbleField?.setOnEditorActionListener(mOnEditorActionListener)
    }

    /**
     * 输入标签文字 点键盘"下一步" 文字标签变成实际标签
     * createBubble
     */
    private val mOnEditorActionListener =
        OnEditorActionListener { textView, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_NEXT -> {
                    return@OnEditorActionListener this@TagEditActivity.createBubble(textView)
                }
            }
            false
        }

    private val mOnCloseBubbleListener: BubbleField.BubbleActionListener<String?> =
        BubbleField.BubbleActionListener<String?> { str, _ ->
            this@TagEditActivity.removeTag(str)
            this@TagEditActivity.update()
        }

    private fun removeTag(removeValue: String) {
        val it = mCurrentNoteTags!!.iterator()
        while (it.hasNext()) {
            if (removeValue.equals(it.next(), ignoreCase = true)) {
                it.remove()
                return
            }
        }
    }

    private fun update() {
        mBubbleField?.setItems(mCurrentNoteTags as List<Nothing>?)
        mBubbleField?.update()
    }

    private fun createBubble(textView: TextView): Boolean {
        val str = textView.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(str)) {
            return true
        }
        if (canAddTag(str)) {
            addTag(str)
            update()
            textView.text = ""
        }
        return true
    }

    private fun addTag(tag: String) {
        if (!hasTag(tag)) {
            mCurrentNoteTags!!.add(tag)
        }
    }

    private fun hasTag(tag: String): Boolean {
        if (TextUtils.isEmpty(tag)) {
            return false
        }
        for (aTag in mCurrentNoteTags!!) {
            if (tag.equals(aTag, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    private fun canAddMoreTags(): Boolean {
        return mCurrentNoteTags!!.size < NOTE_TAGS_MAX
    }

    private fun canAddTag(tag: String?): Boolean {
        var errorStr = TagValidator.validateTag(this@TagEditActivity, tag!!)
        if (errorStr == null) {
            if (!canAddMoreTags()) {
                errorStr = getString(R.string.too_many_tags_on_note)
            }
        }
        if (errorStr != null) {
            Toast.makeText(applicationContext,errorStr,Toast.LENGTH_SHORT).show()
        }
        return errorStr == null
    }
}