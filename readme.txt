1、将actionBar隐藏，替换成toolbar
1)更换主题：<style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
2)定义toolbar控件 指定anroid:theme and app:popuptheme
  指定toolbar上面的各种元素自动使用深色系  android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar"
  指定弹出菜单浅色系 android:popupTheme="@style/ThemeOverlay.Appcompat.Light"
2、省略findViewById步骤
引入kotlin-android-extensions 就是省去我们频繁找id的插件了
3、滑动菜单
androidx.drawerlayout.widget.DrawerLayout
布局中允许放入两个子控件
菜单中的子控件必须设置layout_gravity="start|end"表示从哪边滑出

优化菜单布局：NavigationView
需要准备两个东西:menu(菜单项)和headerLayout(头布局)
菜单项注意点：menu中嵌套group标签，group属性 checkableBehavior指定为single，表示组中所有菜单项只能单选

4、悬浮按钮FloatingActionButton
5、snackbar
并不是toast的替代品，在提示中加入一个可交互按钮，譬如删除操作加undo，提供用户一种弥补措施
问题：snackbar把悬浮按钮遮住了怎么办？借助CoordinatorLayout（见下面）
6、CoordinatorLayout
加强版的FrameLayout+一些material效果
将fragment替换成CoordinatorLayout以后，snackbar弹出时不再覆盖FloatingActionButton了，因为
CoordinatorLayout可以监听所有子控件的各种事件,snackbar不是它的子控件，但是点击事件时会传入父亲view，就是FloatingActionButton

