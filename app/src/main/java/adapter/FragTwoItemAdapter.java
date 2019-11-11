package adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import model.News;
import test.cc.com.demo30.R;

public class FragTwoItemAdapter extends RecyclerView.Adapter<FragTwoItemAdapter.ViewHolder> {

    Activity context;

    List<News> data;

    LayoutInflater inflater;

    View view;

    /**
     * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
     */
    LruCache<String, BitmapDrawable> mMemoryCache;


    public FragTwoItemAdapter(Activity activity, List<News> datas){
        this.context = activity;
        this.data = datas;
        inflater = LayoutInflater.from(activity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frag_two_frag_listview_item, parent, false);
        return new ViewHolder(view);
    }

    /** 标记是否正在滑动，如果为true，就暂停加载图片 */
    protected boolean isScrolling = false;

    /**
     * 赋值
     * @param scrolling
     */
    public void setScrolling(boolean scrolling) {
        isScrolling = scrolling;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        //改变item的背景
        ChangeItemBG(viewHolder);

        viewHolder.txt_title.setText(data.get(position).getTitle());
        viewHolder.txt_content.setText(data.get(position).getTeacher());
        viewHolder.txt_time.setText(data.get(position).getDate());

        //图片加载框架
        Glide
                .with(context)
                .load(data.get(position).getThumbnail_pic_s())
                .placeholder(R.mipmap.default_image)
                .error(R.mipmap.default_image)
                .into(viewHolder.img_icon);

    }

    /**
     * 改变背景
     * @param viewHolder
     */
    private void ChangeItemBG(ViewHolder viewHolder) {
        viewHolder.txt_title.setBackgroundResource(R.color.transparent);
        viewHolder.txt_content.setBackgroundResource(R.color.transparent);
        viewHolder.txt_time.setBackgroundResource(R.color.transparent);
        RelativeLayout.LayoutParams txt_title = (RelativeLayout.LayoutParams) viewHolder.txt_title.getLayoutParams();
        txt_title.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        txt_title.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        viewHolder.txt_title.setLayoutParams(txt_title);
        LinearLayout.LayoutParams txt_content = (LinearLayout.LayoutParams) viewHolder.txt_content.getLayoutParams();
        txt_content.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        txt_content.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        viewHolder.txt_content.setLayoutParams(txt_content);
        LinearLayout.LayoutParams txt_time = (LinearLayout.LayoutParams) viewHolder.txt_time.getLayoutParams();
        txt_time.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        txt_time.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        viewHolder.txt_time.setLayoutParams(txt_time);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * //自定义的ViewHolder，持有每个Item的的所有界面元素
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_title, txt_content, txt_time;
        public ImageView img_icon;
        public LinearLayout lin_all;

        public ViewHolder(View rootView) {
            super(rootView);
            this.lin_all = rootView.findViewById(R.id.lin_all);
            this.img_icon = rootView.findViewById(R.id.img_icon);
            this.txt_title = rootView.findViewById(R.id.txt_title);
            this.txt_content = rootView.findViewById(R.id.txt_content);
            this.txt_time = rootView.findViewById(R.id.txt_time);

            lin_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Linster.textItemOnClick(v, getPosition());
                }
            });
        }
    }

    public ItemOnClickLinster Linster;

    public void setLinster(ItemOnClickLinster linster) {
        Linster = linster;
    }

    public interface ItemOnClickLinster{
        void textItemOnClick(View view, int position);
    }


}
