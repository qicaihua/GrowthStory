package com.mine.growthstory.navMenu

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.button.MaterialButton
import com.mine.growthstory.R
import com.mine.growthstory.addTags.TagEditActivity
import kotlinx.android.synthetic.main.activity_nav.*

/**
 * 带底部导航按钮的主页
 */
class NavMainActivity : AppCompatActivity(){
    private var mTabHomeFragment: OtherFragment? = null
    private var mTabFruitFragment: OtherFragment? = null
    private var mTabMineFragment: OtherFragment? = null
    //当前fragment
    private var mCurrentFragment: Fragment? = null

    companion object{
        const val TAB_HOME = 0
        const val TAB_FRUIT = 1
        const val TAB_MINE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav)

        toggle_group.addOnButtonCheckedListener { group, checkedId, isChecked ->
            val childCount = group.childCount
            //选中的按钮
            var selectIndex = 0
            for (index in 0 until childCount) {
                val button = group.getChildAt(index) as MaterialButton
                if (button.id == checkedId) {
                    //选中的按钮
                    selectIndex = index
                    button.setTextColor(Color.RED)
                    button.iconTint = ColorStateList.valueOf(Color.RED)
                } else {
                    button.setTextColor(Color.BLACK)
                    button.iconTint = ColorStateList.valueOf(Color.BLACK)
                }
            }
            switchFragment(selectIndex)
        }
        toggle_group.check(R.id.tab1)
    }

    private fun switchFragment(selectIndex: Int) {
        when(selectIndex){
            TAB_HOME->{
                if(mTabHomeFragment == null){
                    mTabHomeFragment = OtherFragment()
                    val bundle = Bundle()
                    bundle.putString("tab","home")
                    mTabHomeFragment!!.arguments = bundle
                }
                showFragment(mTabHomeFragment!!)
            }
            TAB_FRUIT->{
                startActivity(Intent(this, TagEditActivity::class.java))


//                if( mTabFruitFragment== null){
//                    mTabFruitFragment = OtherFragment()
//                    val bundle = Bundle()
//                    bundle.putString("tab","fruit")
//                    mTabHomeFragment!!.arguments = bundle
//                }
//                showFragment(mTabFruitFragment!!)
            }
            TAB_MINE->{
                if(mTabMineFragment == null){
                    mTabMineFragment = OtherFragment()
                    val bundle = Bundle()
                    bundle.putString("tab","mine")
                    mTabHomeFragment!!.arguments = bundle
                }
                showFragment(mTabMineFragment!!)
            }
        }
    }

    /**
     * 显示fragment
     */
    private fun showFragment(targetFragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        if (!targetFragment.isAdded) {
            transaction.add(R.id.container, targetFragment)
        }
        for (frag in supportFragmentManager.fragments) {
            transaction.hide(frag)
        }
        transaction.show(targetFragment)
        transaction.commitAllowingStateLoss()
        mCurrentFragment = targetFragment
    }
}
