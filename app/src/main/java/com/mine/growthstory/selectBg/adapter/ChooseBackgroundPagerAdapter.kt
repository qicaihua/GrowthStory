package com.mine.growthstory.selectBg.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 *  author : qch
 *  date   : 2022/07/05 13:52
 *  desc   : 选择背景素材页tab适配器
 */
class ChooseBackgroundPagerAdapter(fm: FragmentManager, var list: List<Fragment>) :
    FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }
}