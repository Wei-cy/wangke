package fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import HTTP.HTTPURLS;
import HTTP.HttpRequest;
import adapter.FragTwoItemAdapter;
import model.News;
import model.TwoFragData;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import systemstatusbar.StatusBarCompat;
import test.cc.com.demo30.NewDetailActivity;
import test.cc.com.demo30.R;

/**
 * 第一个界面里面所嵌套的公用frag
 */
public class Frag_Two_Frag_Item extends BaseFragment {

    String item_title="默认";

    public static String BUNDLE_TITLE = "title";
    public static final String APPKEY = "0bea107901b817f31bafbda687d2753d";

    public static FragTwoItemAdapter adapter;

    List<News> datas = new ArrayList<>();

    RecyclerView frag_item_listView;

    ImageView img_itemtwofrag_load;

    //获取最后一个可见view的坐标
    int lastItemPosition;
    //获取第一个可见view的坐标
    int firstItemPosition;

    /**
     * handler
     * 通过线程获取数据并加载adapter
     */
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.arg1 == 10012){
                Toast.makeText(getActivity(), R.string.getDataError, Toast.LENGTH_SHORT).show();
                return;
            }

            img_itemtwofrag_load.clearAnimation();
            img_itemtwofrag_load.setVisibility(View.GONE);
            //设置RecyclerView的数据加载
            frag_item_listView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new FragTwoItemAdapter(getActivity(), datas);
            frag_item_listView.setAdapter(adapter);

            adapter.setLinster(new FragTwoItemAdapter.ItemOnClickLinster(){
                @Override
                public void textItemOnClick(View view, int position) {
                    Log.i("getActivity()", "点击事件");
                    Intent intent = new Intent(getActivity(), NewDetailActivity.class);
                    intent.putExtra("url", datas.get(position).getDateilurl());
                    intent.putExtra("img", datas.get(position).getThumbnail_pic_s());
                    intent.putExtra("title", datas.get(position).getTitle());
                    startActivity(intent);
                }
            });
        }
    };

    Map<String, String> params;


    @Override
    protected int setContentView() {
        return R.layout.frag_two_frag_item;
    }

    @Override
    protected void init() {
        Bundle bundle = getArguments();
        if(bundle != null){
            item_title = bundle.getString(BUNDLE_TITLE);
        }
        frag_item_listView = rootView.findViewById(R.id.frag_item_listView);
        img_itemtwofrag_load = rootView.findViewById(R.id.img_itemtwofrag_load);
    }

    @Override
    protected void lazyLoad() {
        img_itemtwofrag_load.setVisibility(View.VISIBLE);
        openA(getActivity(), img_itemtwofrag_load);
        handler.post(getDatas);


        frag_item_listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE ){
                    Log.i("getActivity()", "这里应该是一动不动的");
                    //这里滑动停止，开始加载可见项
                    System.out.println(firstItemPosition + "   " + lastItemPosition);
                    adapter.setScrolling(false);
                    adapter.notifyDataSetChanged();
                }else if(newState == RecyclerView.SCROLL_STATE_DRAGGING){
                    Log.i("getActivity()", "这里应该是开始滑动");
                    //这里做处理（停止加载一切事情）
                    adapter.setScrolling(true);
                }else if(newState == RecyclerView.SCROLL_STATE_SETTLING){
                    Log.i("getActivity()", "这里应该是手指离开屏幕的事件");
                    //

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取最后一个可见view的位置
                    lastItemPosition = linearManager.findLastVisibleItemPosition();
                    //获取第一个可见view的位置
                    firstItemPosition = linearManager.findFirstVisibleItemPosition();
                }
            }
        });

        //如果确定每个item的内容不会改变RecyclerView的大小，设置这个选项可以提高性能
        frag_item_listView.setHasFixedSize(true);
        frag_item_listView.setItemAnimator(new DefaultItemAnimator());

    }


    /**
     * 获取数据
     */
    Runnable getDatas = new Runnable() {
        @Override
        public void run() {
            switch (item_title){
                case "推荐":
                    params = new HashMap<>();
                    params.put("key", APPKEY);
                    HttpRequest.post(HTTPURLS.tuijian, (HashMap<String, String>) params, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String reponse = response.body().string();
                            Log.i("getActivity()", "===" + reponse);
                            JsonDta(reponse);
                        }
                    });
                    break;
                case "英语":
                    params = new HashMap<>();
                    params.put("key", APPKEY);
                    HttpRequest.post(HTTPURLS.english, (HashMap<String, String>) params, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String reponse = response.body().string();
                            Log.i("getActivity()", "===" + reponse);
                            JsonDta(reponse);
                        }
                    });
                    break;
                case "数学":
                    params = new HashMap<>();
                    params.put("key", APPKEY);
                    HttpRequest.post(HTTPURLS.math, (HashMap<String, String>) params, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String reponse = response.body().string();
                            Log.i("getActivity()", "===" + reponse);
                            JsonDta(reponse);
                        }
                    });
                    break;
                case "计算机":
                    params = new HashMap<>();
                    params.put("key", APPKEY);
                    HttpRequest.post(HTTPURLS.computer, (HashMap<String, String>) params, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String reponse = response.body().string();
                            Log.i("getActivity()", "===" + reponse);
                            JsonDta(reponse);
                        }
                    });
                    break;
                case "心理":
                    params = new HashMap<>();
                    params.put("key", APPKEY);
                    HttpRequest.post(HTTPURLS.psychology, (HashMap<String, String>) params, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String reponse = response.body().string();
                            Log.i("getActivity()", "===" + reponse);
                            JsonDta(reponse);
                        }
                    });
                    break;
                case "艺术":
                    params = new HashMap<>();
                    params.put("key", APPKEY);
                    HttpRequest.post(HTTPURLS.art, (HashMap<String, String>) params, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String reponse = response.body().string();
                            Log.i("getActivity()", "===" + reponse);
                            JsonDta(reponse);
                        }
                    });
                    break;
                case "政治":
                    params = new HashMap<>();
                    params.put("key", APPKEY);
                    HttpRequest.post(HTTPURLS.politic, (HashMap<String, String>) params, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String reponse = response.body().string();
                            Log.i("getActivity()", "===" + reponse);
                            JsonDta(reponse);
                        }
                    });
                    break;
                case "物理":
                    params = new HashMap<>();
                    params.put("key", APPKEY);
                    HttpRequest.post(HTTPURLS.physic, (HashMap<String, String>) params, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String reponse = response.body().string();
                            Log.i("getActivity()", "===" + reponse);
                            JsonDta(reponse);
                        }
                    });
                    break;
                case "化学":
                    params = new HashMap<>();
                    params.put("key", APPKEY);
                    HttpRequest.post(HTTPURLS.chemistry, (HashMap<String, String>) params, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String reponse = response.body().string();
                            Log.i("getActivity()", "===" + reponse);
                            JsonDta(reponse);
                        }
                    });
                    break;
                case "生物":
                    params = new HashMap<>();
                    params.put("key", APPKEY);
                    HttpRequest.post(HTTPURLS.biology, (HashMap<String, String>) params, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String reponse = response.body().string();
                            Log.i("getActivity()", "===" + reponse);
                            JsonDta(reponse);
                        }
                    });
                    break;
                    default :
                        break;
            }
        }
    };

    /**
     * 解析json数据
     * @param reponse
     */
    private void JsonDta(String reponse) {
        //创建对象接收返回的数据
        TwoFragData data = null;
        data = new Gson().fromJson(reponse, TwoFragData.class);
        if(data.getError_code() != 0){
            //模仿网络请求返回的参数
            Message message = handler.obtainMessage();
            message.arg1 = data.getError_code();
            handler.sendMessage(message);
        }else {
            for (int i = 0; i < data.getResult().getData().size(); i++){
                News newInfo = new News();
                data.getResult().getData().get(i).setUniquekey(newInfo.getUniquekey());
                newInfo.setTitle(data.getResult().getData().get(i).getTitle());
                newInfo.setDate(data.getResult().getData().get(i).getDate());
                newInfo.setCategory(data.getResult().getData().get(i).getCategory());
                newInfo.setTeacher(data.getResult().getData().get(i).getAuthor_name());
                //newInfo.setteacher(data.getResult().getData().get(i).getteaher());
                newInfo.setDateilurl(data.getResult().getData().get(i).getUrl());
                newInfo.setThumbnail_pic_s(data.getResult().getData().get(i).getThumbnail_pic_s());
//                newInfo.setThumbnail_pic_s02(data.getResult().getData().get(i).getThumbnail_pic_s02());
//                newInfo.setThumbnail_pic_s03(data.getResult().getData().get(i).getThumbnail_pic_s03());
                datas.add(newInfo);
            }
            //模仿网络请求返回的参数
            Message message = handler.obtainMessage();
            handler.sendMessage(message);
        }
    }

    /**
     * 传值
     * @param item_title
     * @return
     */
    public static Frag_Two_Frag_Item getInstance(String item_title){
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, item_title);
        Frag_Two_Frag_Item frag_two_frag_item = new Frag_Two_Frag_Item();
        frag_two_frag_item.setArguments(bundle);
        return frag_two_frag_item;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            StatusBarCompat.setStatusBarColor(getActivity(), ContextCompat.getColor(getContext(), R.color.cyan));
        }
    }

    /**
     * 开启一个动画
     * @param img
     */
    public static void openA(Activity activity, ImageView img){
        //加载loading动画
        rotateAnimation = AnimationUtils.loadAnimation(activity, R.anim.loading);
        LinearInterpolator interpolator = new LinearInterpolator();
        rotateAnimation.setInterpolator(interpolator);
        img.startAnimation(rotateAnimation);
    }

    private static Animation rotateAnimation;

}
