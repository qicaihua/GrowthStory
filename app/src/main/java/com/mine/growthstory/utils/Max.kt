package com.mine.growthstory.utils

import java.lang.RuntimeException

/**
 * N个数 支持任意类型数字比较大小 封装max函数
 */
fun main() {
    val a = 3.5
    val b = 3.8
    val c = 4.1
    val largest = max(a, b, c)
    println(largest)
}

fun <T : Comparable<T>> max(vararg nums: T): T {
    if (nums.isEmpty()) throw RuntimeException("Params can not be empty.")
    var maxNum = nums[0]
    for (num in nums) {
        if (num > maxNum) {
            maxNum = num
        }
    }
    return maxNum
}