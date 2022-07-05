package com.mine.growthstory.selectBg

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.mine.growthstory.R
import com.mine.growthstory.selectBg.adapter.ChooseBackgroundPagerAdapter
import com.mine.growthstory.selectBg.bean.Material
import com.mine.growthstory.selectBg.bean.ProfileBackground
import kotlinx.android.synthetic.main.activity_choose_background.*
import org.json.JSONException
import org.json.JSONObject

/**
 *  author : qch
 *  e-mail : qicaihua@yinxiang.com
 *  date   : 2021/4/26 10:03
 *  desc   : 选择个人主页背景图界面
 */
class ChooseBackgroundActivity : AppCompatActivity() {

    companion object {
        const val KEY_BG_URL = "key_bg_url"
        const val TAG = "ChooseBackgroundActivity"
        /**
         * 当前页入口方法
         */
        fun start(context: Context, bgUrl: String?) {
            val intent = getIntent(context, bgUrl)
            context.startActivity(intent)
        }

        private fun getIntent(context: Context, bgUrl: String?) = Intent(context, ChooseBackgroundActivity::class.java).apply {
            putExtra(KEY_BG_URL, bgUrl)
        }
    }

    private var mBgUrl: String = ""
    private var mMaterialInfo: Material<ProfileBackground>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_background)
        initView()
        initData()
        initListener()
    }

    private fun initData() {
        // todo glide设置默认图片
        Glide.with(this).load(mBgUrl).into(iv_background)
    }

    @SuppressLint("InflateParams")
    private fun initView() {
        //获取传过来的背景url
        mBgUrl = intent?.getStringExtra(KEY_BG_URL) ?: ""
        //推荐和上传两部分
        val tabTextList = listOf(getString(R.string.user_change_background_free), getString(R.string.user_change_background_vip))
        //创建推荐和上传的fragment，并且设置获取的背景图URL
        val fragmentLists = listOf(MaterialFragment.getInstance(MaterialFragment.EXTRA_RECOMMEND, mBgUrl), MaterialFragment.getInstance(MaterialFragment.EXTRA_UPLOAD, mBgUrl))
        //选择背景页，tab适配器，给viewpager设置adapter
        vp_tab.adapter = ChooseBackgroundPagerAdapter(supportFragmentManager, fragmentLists)
        with(tabLayout) {
            //给tabLayout设置viewpager
            setupWithViewPager(vp_tab)
            for (i in tabTextList.indices) {
                val tab = getTabAt(i) as TabLayout.Tab
                //tab文字布局样式
                val view = LayoutInflater.from(context).inflate(R.layout.choose_material_tab_view, null, false)
                val titleTv = view.findViewById(R.id.tv_tab_title) as TextView?
                if (i == 0) {//第一个tab文字下面显示indicator
                    val indicator = view.findViewById(R.id.indicator) as View?
                    indicator?.visibility = View.VISIBLE
                }
                titleTv?.text = tabTextList[i]
                tab.customView = view
            }
        }
    }

    private fun initListener() {
        iv_background.setOnClickListener(mClickListener)
        tv_cancel.setOnClickListener(mClickListener)
        tv_confirm.setOnClickListener(mClickListener)

        tabLayout.addOnTabSelectedListener(object : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {

            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val customView = tab?.customView
                val view = customView?.findViewById(R.id.indicator) as View?
                view?.visibility = View.GONE
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val customView = tab?.customView
                val view = customView?.findViewById(R.id.indicator) as View?
                view?.visibility = View.VISIBLE
                vp_tab.currentItem = tab?.position ?: 0
            }
        })
    }

    private val mClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.iv_background, R.id.tv_cancel -> finish()
            R.id.tv_confirm -> updateBackgroundMaterial()
        }
    }

    private fun updateBackgroundMaterial() {
        if (mMaterialInfo != null && !TextUtils.isEmpty(mMaterialInfo?.entity?.background) && !TextUtils.equals(mMaterialInfo?.entity?.background, mBgUrl)) {
            val extra: MutableMap<String, String> = HashMap()
            extra["theme_id"] = mMaterialInfo?.id?.toString() ?: ""
            updateBgUrl(mMaterialInfo?.entity?.background ?: "")
        }
    }

    private fun updateBgUrl(newBgUrl: String) {
        val param = JSONObject()
        try {
            param.put("isNicknameSet", false)
            param.put("isIntroductionSet", false)
            param.put("isAvatarUrlSet", false)
            param.put("backgroundImageUrl", newBgUrl)
            param.put("isBackgroundImageUrlSet", true)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        //请求更新成功，保存url
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}