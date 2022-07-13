package com.mine.growthstory.addTags

const val TAG_NAME_REGEX = "^[^,\\p{Cc}\\p{Z}]([^,\\p{Cc}\\p{Zl}\\p{Zp}]{0,98}[^,\\p{Cc}\\p{Z}])?$"
const val TAG_NAME_LEN_MIN = 1
const val TAG_NAME_LEN_MAX = 100
const val NOTE_TAGS_MAX = 100
