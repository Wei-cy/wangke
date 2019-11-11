package test.cc.com.demo30;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapter.AddAllTabItemGVAdapter;
import adapter.AddMyTabItemGVAdapter;
import model.Add_AllTabItemInfo;
import model.Add_MyTabItemInfo;
import myview.MyGridView;
import systemstatusbar.StatusBarCompat;

/**
 * 标题的管理activity
 */
public class AddTabItemActivity extends Activity implements
        View.OnClickListener,
        AdapterView.OnItemLongClickListener,
        AdapterView.OnItemClickListener {

    //当前选中的gv的item，，默认为0
    public static int SELECTEDFRAGMENT = 0;

    //上下文
    Activity activity = AddTabItemActivity.this;

    //处理消息的handler
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };


    //当前显示的title集合对象
    List<Add_MyTabItemInfo> datas = new ArrayList<>();

    //没有显示的title集合对象
    List<Add_AllTabItemInfo> add_datas = new ArrayList<>();

    //image
    ImageView add_tabitem_img_back;

    //内容
    TextView add_tabitem_txt_mytab, add_tabitem_txt_select, add_tabitem_txt_edit, add_tabitem_txt_alltab, add_tabitem_txt_add;

    //自定义gridview
    MyGridView add_mytabitem_gridview, add_alltabitem_gridview;


    //当前显示的内容加载器
    AddMyTabItemGVAdapter addMyTabItemGVAdapter;

    //没有显示的内容加载器
    AddAllTabItemGVAdapter addAllTabItemGVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_tab_item);

        //设置状态栏
        StatusBarCompat.setStatusBarColor(activity, ContextCompat.getColor(activity, R.color.cyan));
        Log.i("activity", "==" + MainActivity.DEFAULTFRAGMENT);

        //初始化view
        initView();
        //初始化数据
        initData();
    }

    /**
     * 初始化view
     */
    public void initView() {
        //获取控件并设置点击事件
        add_tabitem_img_back = findViewById(R.id.add_tabitem_img_back);
        add_tabitem_txt_mytab = findViewById(R.id.add_tabitem_txt_mytab);
        add_tabitem_txt_select = findViewById(R.id.add_tabitem_txt_select);
        add_tabitem_txt_edit = findViewById(R.id.add_tabitem_txt_edit);
        add_mytabitem_gridview = findViewById(R.id.add_mytabitem_gridview);
        add_alltabitem_gridview = findViewById(R.id.add_alltabitem_gridview);

        add_tabitem_txt_alltab = findViewById(R.id.add_tabitem_txt_alltab);
        add_tabitem_txt_add = findViewById(R.id.add_tabitem_txt_add);

        add_tabitem_img_back.setOnClickListener(this);
        add_tabitem_txt_edit.setOnClickListener(this);
        add_tabitem_txt_select.setOnClickListener(this);

        add_mytabitem_gridview.setOnItemLongClickListener(this);
        add_mytabitem_gridview.setOnItemClickListener(this);
    }

    //初始化数据
    public void initData() {
        //初始化当前显示的title数据
        initMyData();
        //初始化所有的title数据
        initAllData();
    }

    /**
     * 给alldatas填充数据
     */
    private void initAllData() {
        add_datas.add(new Add_AllTabItemInfo(1, "体育", 1));
        add_datas.add(new Add_AllTabItemInfo(2, "农业", 1));
        add_datas.add(new Add_AllTabItemInfo(3, "医学", 1));
        add_datas.add(new Add_AllTabItemInfo(4, "机械", 1));
        add_datas.add(new Add_AllTabItemInfo(5, "材料", 1));
        add_datas.add(new Add_AllTabItemInfo(6, "航空航天", 1));
        add_datas.add(new Add_AllTabItemInfo(7, "兵器", 1));

       //使用适配器去完成填充数据到gridview
       addAllTabItemGVAdapter = new AddAllTabItemGVAdapter(activity, add_datas);
      //viewpager加载
       add_alltabitem_gridview.setAdapter(addAllTabItemGVAdapter);
   }

    /**
     * 给Mydatas填充数据
     */
    private void initMyData() {
        datas.add(new Add_MyTabItemInfo(1, "推荐", 0));
        datas.add(new Add_MyTabItemInfo(2, "英语", 1));
        datas.add(new Add_MyTabItemInfo(3, "数学", 1));
        datas.add(new Add_MyTabItemInfo(4, "计算机", 1));
        datas.add(new Add_MyTabItemInfo(5, "心理", 1));
        datas.add(new Add_MyTabItemInfo(6, "艺术", 1));
        datas.add(new Add_MyTabItemInfo(7, "政治", 1));
        datas.add(new Add_MyTabItemInfo(8, "物理", 1));
        datas.add(new Add_MyTabItemInfo(9, "化学", 1));
        datas.add(new Add_MyTabItemInfo(10,"生物", 1));

        //使用适配器去完成填充数据到gridview
        addMyTabItemGVAdapter = new AddMyTabItemGVAdapter(activity, datas);
        //viewpager加载
        add_mytabitem_gridview.setAdapter(addMyTabItemGVAdapter);
    }


    /**
     * 单击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        int temdId = v.getId();
        if(temdId == R.id.add_tabitem_txt_select){
        }else if(temdId == R.id.add_tabitem_img_back){
            finish();
        }else if(temdId == R.id.add_tabitem_txt_edit){
            //GridView的item添加边框
            if(!AddMyTabItemGVAdapter.isShowDelete){
                add_tabitem_txt_edit.setText(R.string.complete);
                addMyTabItemGVAdapter.setIsShowDelete(true);
            }else{
                add_tabitem_txt_edit.setText(R.string.mytabedit);
                addMyTabItemGVAdapter.setIsShowDelete(false);
            }
        }
    }

    /**
     * GV的长按点击事件
     * @param parent
     * @param view
     * @param position
     * @param id
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if(AddMyTabItemGVAdapter.isShowDelete){
            //不显示删除按钮
            addMyTabItemGVAdapter.setIsShowDelete(false);
        }else{
            //显示删除按钮，并改变textview为“完成”字样
            addMyTabItemGVAdapter.setIsShowDelete(true);
            add_tabitem_txt_edit.setText(R.string.complete);
        }
        return true;
    }

    /**
     * GV的点击事件
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("activity", "position=" + position);
        finish();
        //点击position，赋给SELECTEDFRAGMENT，使用于TwoFragment中
        SELECTEDFRAGMENT = position;
        //根据Item的值去往twofrag界面对应的frag里面
        MainActivity.getSelectFrag(SELECTEDFRAGMENT);
    }

}
