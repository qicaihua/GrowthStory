package com.mine.growthstory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
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
            android.R.id.home -> drawer_layout.openDrawer(GravityCompat.START)
        }
        return true
    }
}