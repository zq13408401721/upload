package com.example.okhttp.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.okhttp.myapplication.bean.DataBean;
import com.example.okhttp.myapplication.bean.JokeBean;
import com.example.okhttp.myapplication.bean.NewBean;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements XRecyclerView.LoadingListener {

    /**
     * 验证码 get 无参数
     * http://yun918.cn/study/public/index.php/verify
     * <p>
     * 注册接口 post  参数(String username,String password,String phone,String verify)
     * http://yun918.cn/study/public/register
     */

    /**
     * 获取分页数据接口 get header("Authorization","APPCODE db33b75c89524a56ac94d6519e106a59")
     * http://ali-weixin-hot.showapi.com/articleDetalList?page=1
     *
    **/
    //private String net_url = "http://ali-weixin-hot.showapi.com/articleDetalList?page=";
    private String net_url = "https://ali-joke.showapi.com/textJoke?maxResult=20&page=";

    @BindView(R.id.txt_info)
    TextView txtInfo;
    @BindView(R.id.btn_get)
    Button btnGet;
    @BindView(R.id.btn_post)
    Button btnPost;
    @BindView(R.id.xrecyclerView)
    XRecyclerView xrecyclerView;
    MyAdapter myAdapter;

    List<JokeBean.ShowapiResBodyBean.ContentlistBean> list;
    int page = 1;
    private boolean isloading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
    }

    private void initView(){
        list = new ArrayList<>();
        myAdapter = new MyAdapter(list);
        xrecyclerView.setAdapter(myAdapter);
        xrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xrecyclerView.setLoadingListener(this);
        onRefresh();
    }

    @OnClick({R.id.btn_get, R.id.btn_post})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get:
                initOkHttpGet();
                initOkhttpGetExe();
                break;
            case R.id.btn_post:
                initOkHttpPost();
                initOkHttpPostExe();
                break;
        }

    }

    //get异步请求
    private void initOkHttpGet() {
        String url = "http://yun918.cn/study/public/index.php/imagelist?uid=1";
        //第一步创建OKHttpClient
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        //第二步创建Rquest
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        //第三步创建call回调对象
        Call call = client.newCall(request);
        //第四步
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("onFailure", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.i("result", result);
            }
        });
    }

    //get同步
    private void initOkhttpGetExe() {
        String url = "http://yun918.cn/study/public/index.php/imagelist?uid=1";
        //第一步创建OKHttpClient
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        //第二步创建Rquest
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        //第三步创建call回调对象
        final Call call = client.newCall(request);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = call.execute();
                    String result = response.body().string();
                    Log.i("response", result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    //post异步
    private void initOkHttpPost() {
        //接口参数 String username,String password
        String url = "http://yun918.cn/study/public/login";
        //第一步创建OKHttpClient
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        //第二步创建RequestBody
        RequestBody body = new FormBody.Builder()
                .add("username", "zq")
                .add("password", "123456")
                .build();
        //第三步创建Rquest
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        //第四步创建call回调对象
        final Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("onFailure", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.i("result", result);
            }
        });


    }

    //post同步
    private void initOkHttpPostExe() {
        //接口参数 String username,String password
        String url = "http://yun918.cn/study/public/login";
        //第一步创建OKHttpClient
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        //第二步创建RequestBody
        RequestBody body = new FormBody.Builder()
                .add("username", "zq")
                .add("password", "123456")
                .build();
        //第三步创建Rquest
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        //第四步创建call回调对象
        final Call call = client.newCall(request);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = call.execute();
                    String result = response.body().string();
                    Log.i("response", result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    //刷新最新数据
    @Override
    public void onRefresh() {
        if(!isloading){
            isloading = true;
            page = 1;
            list.clear();
            getData(true);
        }
    }

    //获取更多数据
    @Override
    public void onLoadMore() {
        if(!isloading){
            isloading = true;
            page++;
            getData(false);
        }
    }

    //bool 是否是刷新
    private void getData(final boolean bool){
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        Request request = new Request.Builder()
                .url(net_url+page)
                .addHeader("Authorization","APPCODE db33b75c89524a56ac94d6519e106a59")
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("onFailure",e.getMessage());
                isloading = false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                isloading = false;
                //查看当前方法所在线程  main(主线)
                String threadName = Thread.currentThread().getName();
                String result = response.body().string();
                //DataBean bean = new Gson().fromJson(result,DataBean.class);
                JokeBean bean = new Gson().fromJson(result,JokeBean.class);
                if(bean.getShowapi_res_code() == 0){
                    list.addAll(bean.getShowapi_res_body().getContentlist());
                    //copyData(bean.getShowapi_res_body().getPagebean().getContentlist());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(bool){
                                //关闭刷新动画
                                xrecyclerView.refreshComplete();
                            }else{
                                //关闭加载更多动画
                                xrecyclerView.loadMoreComplete();
                            }
                            //刷新界面 adapter
                            myAdapter.notifyDataSetChanged();
                        }
                    });
                }else{
                    Log.i("onResponse error",bean.getShowapi_res_error());
                }
            }
        });

    }

    private void copyData(List<DataBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean> data){
        for (DataBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean item : data) {
            //this.list.add(item);
        }
    }

    //
    private void addClassName(){

    }
}
