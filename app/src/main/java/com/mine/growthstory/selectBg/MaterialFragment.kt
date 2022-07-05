package com.mine.growthstory.selectBg

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mine.growthstory.R
import com.mine.growthstory.selectBg.adapter.ChooseMaterialAdapter
import com.mine.growthstory.selectBg.bean.Material
import com.mine.growthstory.selectBg.bean.ProfileBackground
import kotlinx.android.synthetic.main.material_fragment.*


class MaterialFragment : Fragment() {

    private var mAdapter: ChooseMaterialAdapter? = null
    private var mTag = ""

    companion object {
        const val TAG = "MaterialFragment_"
        const val EXTRA_RECOMMEND = "extra_recommend"
        const val EXTRA_UPLOAD = "extra_upload"
        private const val KEY_EXTRA_TAG = "key_extra_tag"
        private const val KEY_EXTRA_DEFAULT_URL = "key_extra_default_url"

        fun getInstance(tag: String, defaultUrl: String?): MaterialFragment {
            val instance = MaterialFragment()
            val bundle = Bundle()
            bundle.putString(KEY_EXTRA_TAG, tag)
            bundle.putString(KEY_EXTRA_DEFAULT_URL, defaultUrl)
            instance.arguments = bundle
            return instance
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.material_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        initData()
    }

    private fun initView() {
        mTag = arguments?.getString(KEY_EXTRA_TAG) ?: EXTRA_RECOMMEND
        val defaultUrl = arguments?.getString(KEY_EXTRA_DEFAULT_URL) ?: ""
        this.mAdapter = ChooseMaterialAdapter("", defaultUrl)
        with(rcv_list) {
            layoutManager = LinearLayoutManager(activity)
            adapter = mAdapter
        }
    }

    private fun initListener() {
        mAdapter?.setProxyClickListener(object : ChooseMaterialAdapter.ProxyClickListener {
            override fun onClickView(view: View, position: Int, info: Material<ProfileBackground>?) {
                // 当前页列表点击条目后，另外一页数据不应该存在有条目被选中
                val extra: MutableMap<String, String> = HashMap()
                extra["theme_id"] = info?.id?.toString() ?: ""
            }
        })
    }

    private fun initData() {
        fetchData()
    }

    private fun fetchData() {
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

