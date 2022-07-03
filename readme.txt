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
7、卡片列表
问题：recyclerview向上滑动以后TooBar不见了，被列表挡住了，如何解决？
需要借助AppBarLayout
8、AppBarLayout
垂直方向的LinearLayout，内部做了很多滚动事件的封装
怎么解决遮挡问题呢？只需要两步：1.ToolBar嵌套到AppBarlayout中，2.给recyclerview制定一个布局行为
9、下拉刷新
10、可折叠式标题栏
CollapsingToolbarLayout
1）不能独立存在，只能为AppBarLayout的直接子布局
2）而AppBarLayout又必须是CoordinatorLayout的子布局
11、充分利用系统状态栏空间
让背景图和系统状态栏融合，借助android:fitSystemWindows实现+状态栏透明主题
表示该控件会出现在系统状态栏里
