package com.mine.growthstory.navMenu

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

/**
 * 其他Fragment,暂时显示基本文字
 */
class OtherFragment: Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("OtherFragment","onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("OtherFragment","onCreate")
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val textView  = TextView(context)
        textView.text = "OtherFragment"
        textView.gravity = Gravity.CENTER
        textView.setTextColor(Color.BLACK)
        Log.d("OtherFragment","onCreateView--" + textView.text)
        return textView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textview:TextView = view as TextView
        textview.text = "${arguments?.getString("tab")}"
        Log.d("OtherFragment","onViewCreated--" + textview.text)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("OtherFragment","onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.d("OtherFragment","onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("OtherFragment","onResume")
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.d("OtherFragment","onHiddenChanged:"+hidden)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("OtherFragment","onDestroy")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("OtherFragment","onDestroyView")
    }
}