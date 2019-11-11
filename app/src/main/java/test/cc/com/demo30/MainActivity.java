package test.cc.com.demo30;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import fragment.Frag_Two_Frag_Item;
import systemstatusbar.StatusBarCompat;

/**
 * 主Activity
 * 用来显示主界面
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {

    //上下文
    Activity activity = this;

    //当前所在的fragment的值，默认为0
    public static int DEFAULTFRAGMENT = 0;

    //控件
    static ViewPager viewPager;
    TabLayout frag_two_tabLayout;
    ImageView frag_two_img_add;

    //你是多余的
    TextView txt_show_toast;

    //添加头部item布局信息
    List<String> titles = Arrays.asList("推荐", "英语", "数学", "计算机", "心理", "艺术", "政治", "物理", "化学", "生物");


    //每一个头部item所对应一个item
    static List<Frag_Two_Frag_Item> frag_two_frag_items = new ArrayList<>();

    //适配器
    static FragmentPagerAdapter fragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //加载刷新框架
        final RefreshLayout frag_two_refreshLayout = findViewById(R.id.frag_two_refreshLayout);


        //越界回弹
        frag_two_refreshLayout.setEnableOverScrollBounce(false);

        //在刷新或者加载的时候不允许操作视图
        frag_two_refreshLayout.setDisableContentWhenRefresh(true);
        frag_two_refreshLayout.setDisableContentWhenLoading(true);

        //监听列表在滚动到底部时触发加载事件（默认true）
        frag_two_refreshLayout.setEnableAutoLoadmore(false);

        //设置自定义Header
        frag_two_refreshLayout.setHeaderHeight(50);
        frag_two_refreshLayout.setRefreshHeader(new ClassicsHeader(activity));
        //设置自定义Footer
        frag_two_refreshLayout.setFooterHeight(50);
        frag_two_refreshLayout.setRefreshFooter(new ClassicsFooter(activity));

        /**
         * 正在下拉刷新数据中
         */
        frag_two_refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                Log.i("activity", "555");
                //设置刷新事件为2s
                frag_two_refreshLayout.finishRefresh(2000);
                Frag_Two_Frag_Item.adapter.notifyDataSetChanged();
            }
        });

        /**
         * 正在上拉加载数据中
         */
        frag_two_refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                Log.i("activity", "444");
                //设置加载事件为2s
                frag_two_refreshLayout.finishLoadmore(2000);
                Frag_Two_Frag_Item.adapter.notifyDataSetChanged();
            }
        });

        /**
         * sf的事件监听
         */
        frag_two_refreshLayout.setOnMultiPurposeListener(new OnMultiPurposeListener() {
            @Override
            public void onHeaderPulling(RefreshHeader header, float percent, int offset, int headerHeight, int extendHeight) {
            }

            @Override
            public void onHeaderReleasing(RefreshHeader header, float percent, int offset, int headerHeight, int extendHeight) {
            }

            @Override
            public void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int extendHeight) {
            }

            @Override
            public void onHeaderFinish(RefreshHeader header, boolean success) {
            }

            @Override
            public void onFooterPulling(RefreshFooter footer, float percent, int offset, int footerHeight, int extendHeight) {
            }

            @Override
            public void onFooterReleasing(RefreshFooter footer, float percent, int offset, int footerHeight, int extendHeight) {
            }

            @Override
            public void onFooterStartAnimator(RefreshFooter footer, int footerHeight, int extendHeight) {
            }

            @Override
            public void onFooterFinish(RefreshFooter footer, boolean success) {
            }

            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
            }

            @Override
            public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
                Log.i("activity", "" + oldState + "-------" + newState);
                if(newState == RefreshState.RefreshFinish){
                    Log.i("getActivity()", "刷新完成");
                    txt_show_toast.setVisibility(View.VISIBLE);
                }
                if(oldState == RefreshState.RefreshFinish){
                    txt_show_toast.setVisibility(View.GONE);
                }
            }
        });

        //修改状态栏的颜色
        StatusBarCompat.setStatusBarColor(activity, ContextCompat.getColor(activity, R.color.cyan));

        //初始化view
        initView();
        //初始化数据
        initData();

        //通过适配器加载
        viewPager.setAdapter(fragmentPagerAdapter);
        //设置viewpager的缓存个数，全部页面都缓存
        viewPager.setOffscreenPageLimit(9);


        //必须与viewpager绑定，否则效果就看不出来
        frag_two_tabLayout.setupWithViewPager(viewPager);
        //设置tablayout的属性
        frag_two_tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        frag_two_tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    /**
     * 初始化数据
     */
    private void initData() {

        //循环加载titles数组内容到fragment（每一个tablayout的显示界面都是一个frag）
        for(String title : titles){
            //通过frag对象加载title，并完成实例化
            Frag_Two_Frag_Item instance = Frag_Two_Frag_Item.getInstance(title);
            frag_two_frag_items.add(instance);
        }

        //用适配器完成每一个fragment的加载完成
        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                //返回指定的fragment
                return frag_two_frag_items.get(position);
            }

            @Override
            public int getCount() {
                //fragment的个数
                return frag_two_frag_items.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                //将titles的内容加载进每一个（position为指定的frag）
                return titles.get(position);
            }
        };
    }

    /**
     * 初始化view
     */
    private void initView() {
        txt_show_toast = findViewById(R.id.txt_show_toast);


        //viewpager
        viewPager = findViewById(R.id.frag_two_viewpager);
        //tablayout
        frag_two_tabLayout = findViewById(R.id.frag_two_tabLayout);

        //imageview
        frag_two_img_add = findViewById(R.id.frag_two_img_add);

        //设置点击事件
        frag_two_img_add.setOnClickListener(this);

        /**
         * 设置viewpager的滑动监听事件（左右滑动）
         */
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                A.C_Log(getActivity(), "------->position=" + position);
            }

            @Override
            public void onPageSelected(int position) {
                Log.i("getActivity()", "onPageSelected----> + position=" + position);
                DEFAULTFRAGMENT = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                A.C_Log(getActivity(), "onPageScrollStateChanged");
            }
        });
    }

    /**
     * 设置tablayout的指示器宽度
     * @param tabs
     * @param leftDip
     * @param rightDip
     */
    @Deprecated
    public static void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    /**
     * 单击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        int temdId = v.getId();
        if(temdId == R.id.frag_two_img_add){
            //跳转进title管理界面
            startActivity(new Intent(activity, AddTabItemActivity.class));
        }
    }

    /**
     * 将选中的值传进来并显示
     * @param SELECTEDFRAGMENT
     */
    public static void getSelectFrag(int SELECTEDFRAGMENT) {
        //跳转进viewpager指定的item
        viewPager.setCurrentItem(SELECTEDFRAGMENT, false);
    }

}