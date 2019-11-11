package test.cc.com.demo30;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import model.News;
import systemstatusbar.StatusBarCompat;

public class NewDetailActivity extends Activity implements View.OnClickListener {


    Activity activity = NewDetailActivity.this;

    WebView newdetail_webview;

    ImageView xwd_img_back;

    TextView xwd_txt_share;

    //URL
    public static String URL = "";
    //title
    public static String TITLE = "";
    //IMG
    public static String IMG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_detail);

        StatusBarCompat.setStatusBarColor(activity, ContextCompat.getColor(activity, R.color.cyan));

        initView();
        initData();
    }

    public void initData() {
        Intent intent = getIntent();
        News news = new News();
        if(intent != null){
            URL = intent.getStringExtra("url");
            IMG = intent.getStringExtra("img");
            TITLE = intent.getStringExtra("title");

            news.setThumbnail_pic_s(IMG);
            news.setDateilurl(URL);
            news.setTitle(TITLE);
        }

        newdetail_webview.loadUrl(news.getDateilurl());
        //设置可自由缩放网页
        newdetail_webview.getSettings().setSupportZoom(true);
        newdetail_webview.getSettings().setBuiltInZoomControls(true);
        //支持JavaScript
        newdetail_webview.getSettings().setJavaScriptEnabled(true);
        newdetail_webview.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if( url.startsWith("http:") || url.startsWith("https:") ) {
                    return false;
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:function setTop(){document.querySelector('.ad-footer').style.display=\"none\";}setTop();");
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                //这里进行无网络或错误处理，具体可以根据errorCode的值进行判断，做跟详细的处理。
            }
        });


        newdetail_webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                view.loadUrl("javascript:function setTop(){document.querySelector('.ad-footer').style.display=\"none\";}setTop();");
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    public void initView() {
        newdetail_webview = findViewById(R.id.newdetail_webview);

        xwd_img_back = findViewById(R.id.xwd_img_back);
        xwd_txt_share = findViewById(R.id.xwd_txt_share);

        xwd_img_back.setOnClickListener(this);
        xwd_txt_share.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int temdId = v.getId();
        if(temdId == R.id.xwd_img_back){
            finish();
        }else if(temdId == R.id.xwd_txt_share){
            /**
             * 从底部弹出一个分享的界面
             */
            Toast.makeText(activity, R.string.notshare, Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(activity, ShareListActivity.class);
//            intent.putExtra("title", TITLE);
//            intent.putExtra("img", IMG);
//            intent.putExtra("url", URL);
//            startActivity(intent);
//            overridePendingTransition(0, 0);
        }
    }

}
