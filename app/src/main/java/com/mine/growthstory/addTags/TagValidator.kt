package com.mine.growthstory.addTags

import android.content.Context
import android.text.TextUtils
import com.mine.growthstory.R
import java.util.regex.Pattern

object TagValidator {
    private val TAG_NAME_PATTERN = Pattern.compile(TAG_NAME_REGEX)
    fun validateTag(context: Context, tag: String): String? {
        var errorStr: String? = null
        if (TextUtils.isEmpty(tag) || tag.length < TAG_NAME_LEN_MIN || tag.length > TAG_NAME_LEN_MAX) {
            errorStr = context.getString(R.string.invalid_tag_length)
        } else if (!TAG_NAME_PATTERN.matcher(tag).matches()) {
            errorStr = context.getString(R.string.invalid_tag_name)
        }
        return errorStr
    }
}