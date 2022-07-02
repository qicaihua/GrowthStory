1、将actionBar隐藏，替换成toolbar
1)更换主题：<style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
2)定义toolbar控件 指定anroid:theme and app:popuptheme
  指定toolbar上面的各种元素自动使用深色系  android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar"
  指定弹出菜单浅色系 android:popupTheme="@style/ThemeOverlay.Appcompat.Light"

kotlin-android-extensions 就是省去我们频繁找id的插件了


滑动菜单
androidx.drawerlayout.widget.DrawerLayout
布局中允许放入两个子控件
菜单中的子控件必须设置layout_gravity="start|end"表示从哪边滑出

优化菜单布局
NavigationView

