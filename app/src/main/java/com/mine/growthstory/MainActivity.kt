package com.mine.growthstory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.materialtest.FruitAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    val fruits = mutableListOf(Fruit("Apple",R.mipmap.apple),
        Fruit("Banana",R.mipmap.banana),
        Fruit("Orange",R.mipmap.orange),
        Fruit("Watermelon",R.mipmap.watermelon),
        Fruit("Pear",R.mipmap.pear),
        Fruit("Grape",R.mipmap.grape),
        Fruit("PineApple",R.mipmap.pineapple),
        Fruit("Strawberry",R.mipmap.strawberry),
        Fruit("Cherry",R.mipmap.cherry),
        Fruit("Mango",R.mipmap.mango)
    )

    val fruitList = ArrayList<Fruit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //设置toolbar
        setSupportActionBar(toolbar)

        //设置导航菜单
        supportActionBar?.let {
            //显示导航按钮
            it.setDisplayHomeAsUpEnabled(true)
            //设置导航菜单图标  默认是返回箭头，这里修改了样式和功能
            it.setHomeAsUpIndicator(R.mipmap.ic_menu)
        }

        //导航滑动菜单默认选中call
        navView.setCheckedItem(R.id.navCall)
        //导航滑动菜单设置点击事件
        navView.setNavigationItemSelectedListener {
            drawerLayout?.closeDrawers()
            true
        }

        fab.setOnClickListener {
            Snackbar.make(it, "Data deleted", Snackbar.LENGTH_SHORT)
                .setAction("Undo") {
                    Toast.makeText(this, "Data restored", Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        //展示水果卡片列表
        initFruits()
        val layoutManager = GridLayoutManager(this,2)
        recycleView.layoutManager = layoutManager
        val adapter = FruitAdapter(this,fruitList)
        recycleView.adapter = adapter

        //下拉刷新
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            refreshFruits(adapter)
        }
    }

    private fun refreshFruits(adapter: FruitAdapter) {
        thread {
            Thread.sleep(2000)
            runOnUiThread {
                initFruits()
                adapter.notifyDataSetChanged()
                swipeRefresh.isRefreshing = false
            }
        }
    }

    /**
     * menu
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toorbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.backup -> Toast.makeText(this, "You clicked Backup", Toast.LENGTH_SHORT).show()
            R.id.delete -> Toast.makeText(this, "You clicked Delete", Toast.LENGTH_SHORT).show()
            R.id.settings -> Toast.makeText(this, "You clicked Settings", Toast.LENGTH_SHORT).show()
            //点击home菜单，打开抽屉菜单，从左边滑动到右边；行为和xml保持一致，设置GravityCompat.START
            android.R.id.home -> drawerLayout.openDrawer(GravityCompat.START)
        }
        return true
    }

    private fun initFruits(){
        fruitList.clear()
        repeat(20){
            val index = (0 until fruits.size).random()
            fruitList.add(fruits[index])
        }
    }
}