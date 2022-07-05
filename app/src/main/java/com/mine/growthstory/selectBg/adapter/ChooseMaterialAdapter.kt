package com.mine.growthstory.selectBg.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.mine.growthstory.R
import com.mine.growthstory.selectBg.bean.Material
import com.mine.growthstory.selectBg.bean.ProfileBackground
import com.mine.growthstory.utils.GlideUtil
import kotlinx.android.synthetic.main.choose_material_item.view.*


/**
 *  author : qch
 *  date   : 2022/07/05 13:55
 *  desc   : 背景素材条目适配器
 */
class ChooseMaterialAdapter(var authToken: String, private var defaultUrl: String) : RecyclerView.Adapter<ChooseMaterialAdapter.ChooseMaterialHolder>() {

    private var dataList: ArrayList<Material<ProfileBackground>>? = null
    private var mSelectedIndex = -1
    private var mListener: ProxyClickListener? = null
    private var mFirstLoad = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseMaterialHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.choose_material_item, parent, false)
        return ChooseMaterialHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ChooseMaterialHolder, @SuppressLint("RecyclerView") position: Int) {
        val profileBackground = dataList?.get(position)?.entity
        var isSelected = mSelectedIndex == position
        GlideUtil.showImage(holder.itemView.iv_material, mapOf("auth" to authToken), profileBackground?.thumbnail, 0)
        holder.itemView.material_foreground.isVisible = isSelected
        holder.itemView.dot.isSelected = isSelected
        holder.itemView.setOnClickListener { view ->
            if (mSelectedIndex != position) {
                mFirstLoad = false
                mSelectedIndex = position
                notifyDataSetChanged()
                mListener?.onClickView(view, position, dataList?.get(position))
            }
        }
    }

    inner class ChooseMaterialHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    fun setData(data: ArrayList<Material<ProfileBackground>>?) {
        dataList?.clear()
        dataList = data
        notifyDataSetChanged()
    }

    interface ProxyClickListener {
        fun onClickView(view: View, position: Int, info: Material<ProfileBackground>?)
    }

    fun setProxyClickListener(listener: ProxyClickListener) {
        mListener = listener
    }

    fun resetSelectedIndex() {
        mSelectedIndex = -1
        notifyDataSetChanged()
    }
}