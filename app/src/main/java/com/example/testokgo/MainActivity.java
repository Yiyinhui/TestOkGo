package com.example.testokgo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testokgo.R;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.adapter.AdapterParam;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.CookieStore;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okrx2.adapter.ObservableResponse;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.OkUpload;
import com.lzy.okserver.download.DownloadTask;
import com.lzy.okserver.upload.UploadTask;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;


import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.CertificatePinner;
import okhttp3.CipherSuite;
import okhttp3.Connection;
import okhttp3.ConnectionSpec;
import okhttp3.Cookie;
import okhttp3.Credentials;
import okhttp3.EventListener;
import okhttp3.Handshake;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.Route;
import okhttp3.TlsVersion;
import okhttp3.internal.http.RetryAndFollowUpInterceptor;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class MainActivity extends AppCompatActivity {
    private OkHttpClient mHttpClient;
    Request request;
    private TextView mClick;
    private ImageView mImageView;

    private String[] datas = {"张三", "李四", "王五", "麻子", "小强"};
    private String[] theme = {"张三", "李四", "王五", "麻子", "小强"};
    private String[] content = {"我是张三，你好", "我是李四，你好", "我是王五，你好", "我是李四，你好", "我是王五，你好"};
    private int[] imageViews = {R.drawable.loading_animation_3, R.drawable.loading_animation_2, R.drawable.loading_animation_1, R.drawable.loading_animation_2, R.drawable.loading_animation_1};

    private ArrayAdapter<String> adapter;
    private ListView listView;
    private List dataList = new ArrayList();

    private RecyclerView recyclerView;
    String s_te1[] = {"内容一", "内容一", "内容一"};
    String s_te2[] = {"内容二", "内容二", "内容二"};


    private static final String TAG = "MainActivity";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public static void verifyStoragePermissions(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (activity.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    activity.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = findViewById(R.id.image);
        //某些权限属于Protected Permission
        //例如：读写手机存储权限，仅仅在AndroidManifest.xml中申明是无法真正获取到权限的
        //代码动态的获取此权限
//        if (Build.VERSION.SDK_INT >= 23) {
//            int REQUEST_CODE_CONTACT = 101;
//            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
//            //验证是否许可权限
//            for (String str : permissions) {
//                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
//                    //申请权限
//                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
//                }
//            }
//        }

        verifyStoragePermissions(this);

        testView();
        testRecyclerView();
        initView();
    }

    private void testView() {

        listView = (ListView) findViewById(R.id.ll1);
        /*
        数组适配器
         */
        // 初始化适配器
        //adapter = new ArrayAdapter<>(this,android.R.layout.simple_expandable_list_item_1,datas);
//        listView.setAdapter(adapter);
//        dataList.add("1111");
//        dataList.add("222");
//        dataList.add("33");
//        dataList.add("4");
        //adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_single_choice,dataList);

        //        准备数据源
        final List<Map<String, Object>> lists = new ArrayList<>();
        for (int i = 0; i < theme.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("image", imageViews[i]);
            map.put("theme", theme[i]);
            map.put("content", content[i]);
            lists.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(MainActivity.this, lists, R.layout.listview_style
                , new String[]{"image", "theme", "content"}
                , new int[]{R.id.image1, R.id.text1, R.id.text2});

        listView.setAdapter(new BaseAdapter() {

            /**
             * 返回item的个数
             * @return
             */
            @Override
            public int getCount() {
                return lists.size();
            }

            /**
             * 返回每一个item对象
             * @param i
             * @return
             */
            @Override
            public Object getItem(int i) {
                return lists.get(i);
            }

            /**
             * 返回每一个item的id
             * @param i
             * @return
             */
            @Override
            public long getItemId(int i) {
                return i;
            }

            /**
             * 暂时不做优化处理，后面会专门整理BaseAdapter的优化
             * @param i
             * @param convertView
             * @param viewGroup
             * @return
             */
            @Override
            public View getView(int i, View convertView, ViewGroup viewGroup) {
                View view;
                if (convertView == null) {
                    view = LayoutInflater.from(MainActivity.this).inflate(R.layout.listview_style, viewGroup, false);
                    ImageView imageView = (ImageView) view.findViewById(R.id.image1);
                    TextView textView1 = (TextView) view.findViewById(R.id.text1);
                    TextView textView2 = (TextView) view.findViewById(R.id.text2);
                    imageView.setImageResource((int) lists.get(i).get("image"));
                    textView1.setText((String) lists.get(i).get("theme"));
                    textView2.setText((String) lists.get(i).get("content"));
                } else {
                    view = convertView;
                }

//        此处需要返回view 不能是view中某一个
                return view;
            }
        });


//        simple_list_item1:单独的一行文本框
//        simple_list_item2:有两个文本框组成
//        simple_list_item_checked每项都是由一个已选中的列表项
//        simple_list_item_multiple_choice:都带有一个复选框
//        simple_list_item_single_choice：都带有一个单选框
    }

    private void testRecyclerView() {

//        把自定义的RecycleView变量和activity_main.xml中的id绑定
        recyclerView = (RecyclerView) findViewById(R.id.myRecycle);

//        设置RecycleView的布局方式，这里是线性布局，默认垂直
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        实例化自定义适配器
        MyAdapter myAdapter = new MyAdapter();

//        把适配器添加到RecycleView中
        recyclerView.setAdapter(myAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));


    }

    class MyAdapter extends RecyclerView.Adapter {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Myholder myholder = new Myholder(LayoutInflater.from(getApplicationContext()).inflate(R.layout.recyclerview_style, null));//引入自定义列表项的资源文件

            return myholder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Myholder mm = (Myholder) holder;

//            将数据映射到控件中
            mm.te1.setText(s_te1[position]);
            mm.te2.setText(s_te2[position]);
        }

        @Override
        public int getItemCount() {
            return s_te1.length;
        }

        //        在适配器当中自定义内部类，其中的子对象用于呈现数据
        class Myholder extends RecyclerView.ViewHolder {
            TextView te1, te2;
            TextView t;

            public Myholder(View view) {
                super(view);

//                实例化自定义对象

//                实例化子对象，把对象和列表项布局文件中的id绑定
                te1 = itemView.findViewById(R.id.te1);
                te2 = itemView.findViewById(R.id.te2);
            }


        }
    }

    private void initView() {

        mClick = findViewById(R.id.click);
        mImageView = findViewById(R.id.image);
        mClick.setOnClickListener(new View.OnClickListener() {


            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View view) {

//                try {
//                    initOkGo();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                //initOkDownLoad();

                initOkUpload();


            }
        });


    }

    /**
     * 取消请求
     */
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelAll();         //取消全局默认的OkHttpClient中的所有请求
//        OkGo.getInstance().cancelTag(this);   //取消全局默认的OkHttpClient中的标识为tag的请求
//        OkGo.cancelAll(OkHttpClient);         //取消给定OkHttpClient中的所有请求
//        OkGo.cancelTag(OkHttpClient,tag);     //取消给定OkHttpClient中标识为tag的请求
    }

    //callback回调默认只需要复写onSuccess,
    // callback的失败回调onError并没有声明为抽象的,如果有需要,请自行复写,
    // onSuccess没有执行,那么一定是出错了回调了onError
    private void initOkGo() throws IOException {
        //这个String不能漏，否则无法new 特殊callback
        String urlImage = "https://www.baidu.com/img/bd_logo1.png";
        String urlTxt = "\"https://publicobject.com/helloworld.txt\"";
        OkGo.<File>get(urlImage)
                // 请求方式和请求url

                .params("key1", "value1", true)
                //添加参数的时候,最后一个isReplace为可选参数,默认为true，即代表相同key的时候，后添加的会覆盖先前添加的。

                .tag("aaa")
                // 请求的 tag, 主要用于取消对应的请求

                //.isMultipart(true)
                //post可用，get不可用。
                // 该方法表示是否强制使用multipart/form-data表单上传，
                // 因为该框架在有文件的时候，无论你是否设置这个参数，默认都是multipart/form-data格式上传，
                // 但是如果参数中不包含文件，默认使用application/x-www-form-urlencoded格式上传，
                // 如果你的服务器要求无论是否有文件，都要使用表单上传，那么可以用这个参数设置为true。

                //.isSpliceUrl(true);
                //post可用，get不可用。
                // 该方法表示是否强制将params的参数拼接到url后面，默认false不拼接。
                // 一般来说，post、put等有请求体的方法应该把参数都放在请求体中，不应该放在url上，
                // 但是有的服务端可能不太规范，url和请求体都需要传递参数，那么这时候就使用该参数，他会将你所有使用.params()方法传递的参数，自动拼接在url后面。


                .cacheKey("cacheKey")
                // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                //每次框架是根据这个cacheKey去到数据库中寻找缓存的，所以一般来说，每个不同功能的请求都要设置不一样的cacheKey，
                // 如果相同，会导致数据库中的缓存数据发生覆盖或错乱。
                // 如果不指定cacheKey，默认是用url带参数的全路径名为cacheKey。

                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
//                 /** 按照HTTP协议的默认缓存规则，例如有304响应头时(服务端已经执行了GET，但文件未变化。)缓存 */
//                DEFAULT,  cacheTime 参数无效
//                  网络请求成功,服务端返回非304 onStart -> convertResponse -> onSuccess -> onFinish
//                  网络请求成功服务端返回304 onStart -> onCacheSuccess -> onFinish
//                  网络请求失败 onStart -> onError -> onFinish
//                /** 不使用缓存 */
//                NO_CACHE,不使用缓存，该模式下cacheKey、cacheTime 参数均无效
//                  网络请求成功 onStart -> convertResponse -> onSuccess -> onFinish
//                  网络请求失败 onStart -> onError -> onFinish
//                /** 请求网络失败后，读取缓存 */
//                REQUEST_FAILED_READ_CACHE,先请求网络，如果请求网络失败，则读取缓存，如果读取缓存失败，本次请求失败。
//                  网络请求成功,不读取缓存 onStart -> convertResponse -> onSuccess -> onFinish
//                  网络请求失败,读取缓存成功 onStart -> onCacheSuccess -> onFinish
//                  网络请求失败,读取缓存失败 onStart -> onError -> onFinish
//                /** 如果缓存不存在才请求网络，否则使用缓存 */
//                IF_NONE_CACHE_REQUEST,
//                  已经有缓存,不请求网络 onStart -> onCacheSuccess -> onFinish
//                  没有缓存请求网络成功 onStart -> convertResponse -> onSuccess -> onFinish
//                  没有缓存请求网络失败 onStart -> onError -> onFinish
//                /** 先使用缓存，不管是否存在，仍然请求网络 */
//                FIRST_CACHE_THEN_REQUEST,
//                  无缓存时,网络请求成功 onStart -> convertResponse -> onSuccess -> onFinish
//                  无缓存时,网络请求失败 onStart -> onError -> onFinish
//                  有缓存时,网络请求成功 onStart -> onCacheSuccess -> convertResponse -> onSuccess -> onFinish
//                  有缓存时,网络请求失败 onStart -> onCacheSuccess -> onError -> onFinish
                .cacheTime(-1)
//        当前缓存的有效时间是多长，单位毫秒.这个根据自己需要设置，
//        如果不设置，默认是CacheEntity.CACHE_NEVER_EXPIRE=-1，也就是永不过期。
//        该参数对DEFAULT模式是无效的，因为该模式是完全遵循标准的http协议的，缓存时间是依靠服务端响应头来控制，所以客户端的cacheTime参数无效。
                //cachePolicy
//        这个是自定义的缓存策略，内置的五大缓存模式其实就是这个缓存策略CachePolicy接口的五种不同实现，
//        如果这五种模式不能满足你，你完全可以自行实现这个接口，写出属于你自己的缓存规则。
                //onCacheSuccess
//        当缓存读取成功后，回调的是这个方法，如果你只复写了onSuccess方法，是无法获取缓存的，这里要注意。

                //.retryCount(5);
                //该方法是配置超时重连次数，也可以在全局初始化的时候设置，默认使用全局的配置，即为3次，
                // 你也可以在这里为你的这个请求特殊配置一个，并不会影响全局其他请求的超时重连次数。

                //.headers("key","value");//
                // 该方法是传递服务端需要的请求头，如果你不知道什么是请求头

                //.addUrlParams("123", )
                //.addFileParams("123",)
                //.addFileWrapperParams()
                // 这里是支持一个key传递多个文本参数，也支持一个key传递多个文件参数，详细也看上面的http协议连接


//        okgo完全遵循了http协议，
//        如果服务端的session是按照set-cookie头返回给客户端，并且希望在下次请求的时候自动带上这个cookie值，
//        那么只需要在okgo初始化的时候(MyApplication内)添加这么一行代码：
//                OkHttpClient.Builder builder = new OkHttpClient.Builder();
//                builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));    SPCookieStore：使用SharedPreferences保持cookie，如果cookie不过期，则一直有效
//                builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));    DBCookieStore：使用数据库保持cookie，如果cookie不过期，则一直有效
//                builder.cookieJar(new CookieJarImpl(new MemoryCookieStore(this)));    MemoryCookieStore：使用内存保持cookie，app退出后，cookie消失
//                OkGo.getInstance().setOkHttpClient(builder.build())
//        自定义cookie管理策略，有两种方法：
//        按照okhttp原生提供的方法，实现CookieJar接口，传入OkHttpClient.Builder中即可
//        按照okgo内置的方式，实现CookieStore接口，同样传入OkHttpClient.Builder中即可


//                    .execute(new StringCallback() {//大段报错，第一行泛型可能没有定义
//                        @Override
//                        public void onSuccess(Response<String> response) {
//                            System.out.println("Response:   "+response.body());
//                        }
//                    });
//                .execute(new BitmapCallback() {//请求bitmap
//                    @Override
//                    public void onSuccess(Response<Bitmap> response) {
//                        System.out.println("Response:   " + response.body());
//                        //Glide.with(getBaseContext()).load(response).into(mImageView);
//                    }
//                });
                .execute(new FileCallback("asfdsaf") {//文件下载
                    //                    FileCallback()：空参构造
//                    FileCallback(String destFileName)：可以额外指定文件下载完成后的文件名
//                    FileCallback(String destFileDir, String destFileName)：可以额外指定文件的下载目录和下载完成后的文件名
                    @Override
                    public void onSuccess(Response<File> response) {

                    }
                });

        /*
        Response介绍
         */
//        body：当前返回的数据。如果请求成功，回调onSuccess()，该字段为convertResponse()解析数据后返回的数据。如果发生异常，回调onError()，该字段值为null。
//              使用方法body()获取该值。
//        throwable：如果发生异常，回调onError()，该字段保存了当前的异常信息。如果请求成功，回调onSuccess()，该字段为null。
//                  使用方法getException()获取该值。
//        isFromCache：表示当前的数据是来自哪里，true：来自缓存，false：来自网络。
//                  使用方法isFromCache()获取该值。
//        rawCall：表示当前请求的真正okhttp3.Call对象。
//                  使用方法getRawCall()获取该值。
//        rawResponse：表示当前请求服务端真正返回的okhttp3.Response对象，注意：如果数据来自缓存，该对象为null，如果来自网络，该对象才有值。
//                  使用方法getRawResponse()获取该值。
//        另外，该对象还有以下几个方法：(与网络相关)
//        code()：http协议的响应状态码，如果数据来自网络，无论成功失败，该值都为真实的响应码，如果数据来自缓存，该值一直为-1。
//        message()：http协议对响应状态码的描述信息，如果数据来自网络，无论成功失败，该值都为真实的描述信息，如果数据来自缓存，该值一直为null。
//        headers()：服务端返回的响应头信息，如果数据来自网络，无论成功失败，该值都为真实的头信息，如果数据来自缓存，该值一直为null。
//        isSuccessful()：本次请求是否成功，判断依据是是否发生了异常。


        /**
         * Cookie 交互
         */
//查看okgo管理的cookie中，某个url所对应的全部cookie
//        CookieStore cookieStore = OkGo.getInstance().getCookieJar().getCookieStore();
//        HttpUrl httpUrl = HttpUrl.parse("http://server.jeasonlzy.com/OkHttpUtils/method/");
//        List<Cookie> cookies = cookieStore.getCookie(httpUrl);
//        Toast.makeText(getApplicationContext(),httpUrl.host() + "对应的cookie如下：" + cookies.toString(), Toast.LENGTH_LONG).show();

        //查看okgo管理的所有cookie
//        CookieStore cookieStore = OkGo.getInstance().getCookieJar().getCookieStore();
//        List<Cookie> allCookie = cookieStore.getAllCookie();
//        Log.d("All cookie", "所有cookie如下：" + allCookie.toString());

        //手动向okgo管理的cookie中，添加一些自己的cookie，那么以后满足条件时，okgo就会带上这些cookie
//        HttpUrl httpUrl = HttpUrl.parse("http://server.jeasonlzy.com/OkHttpUtils/method/");
//        Cookie.Builder builder = new Cookie.Builder();
//        Cookie cookie = builder.name("myCookieKey1").value("myCookieValue1").domain(httpUrl.host()).build();
//        CookieStore cookieStore = OkGo.getInstance().getCookieJar().getCookieStore();
//        cookieStore.saveCookie(httpUrl, cookie);

        //手动把okgo管理的cookie移除
//        HttpUrl httpUrl = HttpUrl.parse("http://server.jeasonlzy.com/OkHttpUtils/method/");
//        CookieStore cookieStore = OkGo.getInstance().getCookieJar().getCookieStore();
//        cookieStore.removeCookie(httpUrl);


        /**
         * excute 的回调
         */
//                AbsCallback 对Callback的默认包装
//                StringCallBack 对convertResponse()按文本解析，解析的编码依据服务端响应头中的Content-Type中的编码格式，自动进行编码转换，确保不出现乱码，
//                BitmapCallback 如果请求的数据是图片，则可以使用该回调，回调的图片进行了压缩处理，确保不发生OOM
//                FileCallBack 如果要做文件下载，可以使用该回调，内部封装了关于文件下载和进度回调的方法
//                .execute(new Callback<Object>() {
//                    @Override/**拿到响应后，将数据转换成需要的格式，子线程中执行，可以是耗时操作*/
//                    public Object convertResponse(okhttp3.Response response) throws Throwable {
//                        return null;
//                    }
//
//                    @Override/** 请求网络开始前，UI线程 */
//                    public void onStart(com.lzy.okgo.request.base.Request<Object, ? extends com.lzy.okgo.request.base.Request> request) {
//
//                    }
//
//                    @Override/** 对返回数据进行操作的回调， UI线程 */
//                    public void onSuccess(Response<Object> response) {
//                            System.out.println("Response:   "+response.isSuccessful());
//                    }
//
//                    @Override
//                    public void onCacheSuccess(Response<Object> response) {
//
//                    }
//
//                    @Override/** 请求失败，响应错误，数据解析错误等，都会回调该方法， UI线程 */
//                    public void onError(Response<Object> response) {
//                        System.out.println("222222222222222222222222222");
//                    }
//
//                    @Override/** 请求网络结束后，UI线程 */
//                    public void onFinish() {
//
//                    }
//
//                    @Override /** 上传过程中的进度回调，get请求不回调，UI线程 */
//                    public void uploadProgress(Progress progress) {
//
//                    }
//
//                    @Override /** 下载过程中的进度回调，UI线程 */
//                    public void downloadProgress(Progress progress) {
//
//                    }
//                });


        /**
         * 保存数据到服务器
         * */
    }

        /*
        上传单个文件
         */
//    private void saveToInternet() {
//        String url = "https://www.baidu.com/img/bd_logo1.png";
//        File file = new File("https://www.baidu.com/img/bd_logo1.png");
//        OkGo.<String>post(url)
//                .tag(this)
//                .params("icon", file)
//                .isMultipart(true)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//
//                    }
//
//                    @Override
//                    public void onError(Response<String> response) {
//                    }
//                });
//
//    }

        /*
        上传多个文件
         */
//    private void saveToInternetMulty() {
//        List<File> files = new ArrayList<>();
//        files.add(new File("xxx"));
//        HttpParams param = new HttpParams();
//        param.put("number", "file");
//        String url =  "https://www.baidu.com/img/bd_logo1.png";
//        OkGo.<String>post(url)
//                .tag(this)
//                .isMultipart(true)
//                .params(param)
//                .addFileParams("files", files)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//
//                    }
//                });
//
//    }


    //    OkRx主要功能
//              可以很完美结合RxJava做**网络请求**
//              在使用上比Retrofit更简单方便，门槛更低，灵活性更高
//              网络请求和RxJava调用可以做成一条链试，写法优雅
//              使用Converter接口，支持任意类型的数据自动解析
//              OkRx是扩展的OkGo，所以OkGo包含的所有功能和写法，OkRx全部支持
    private void initOkRx() {
        OkGo.<String>post("https://www.baidu.com/img/bd_logo1.png")//

                .headers("aaa", "111")//

                .params("bbb", "222")//

                .converter(new StringConvert())
                //该方法是网络请求的转换器，功能是将网络请求的结果转换成我们需要的数据类型。

                .adapt(/*new AdapterParam(),*/new ObservableResponse<String>())
                //该方法是方法返回值的适配器，功能是将网络请求的Call<T>对象，适配成我们需要的Observable<T>对象。
                //new AdapterParam()这个参数就是控制，我把当前的Call<T>对象适配成Observable<T>时，是使用同步还是异步的方法。
                //他们有什么区别的，其实只有一个最大的区别，就是缓存的使用。
                // 默认情况下，我们使用的是异步请求来适配的，使用方式和okgo使用缓存一样，指定cacheKey和cacheMode(.FIRST_CACHE_THEN_REQUES)
                //如果使用同步方法来适配的话，缓存模式CacheMode.FIRST_CACHE_THEN_REQUEST是不生效的，异步才生效，原因是同步请求没法返回两次数据。


                //至此生成Observable<Response<String>>,被观察者。
                // Observable 发出一系列事件，他是事件的产生者；
                //Subscriber 负责处理事件，他是事件的消费者；
                //Operator 是对 Observable 发出的事件进行修改和变换；


                .subscribeOn(Schedulers.io())       //指定 subscribe() 发生在 IO 线程

                //Scheduler线程调度器
//        1.Schedulers.immediate():  直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler。
//        2.Schedulers.newThread():  总是启用新线程，并在新线程执行操作。
//        3.Schedulers.io(): I/O  操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。
//                         行为模式和 newThread() 差不多，区别在于 io() 的内部实现是是用一个 *无数量上限* 的线程池，可以重用空闲的线程，
//                         因此多数情况下 io() 比 newThread() 更有效率。
//                         不要把计算工作放在 io() 中，可以避免创建不必要的线程。
//        4.Schedulers.computation():  计算所使用的 Scheduler。
//                         这个计算指的是 CPU 密集型计算，即不会被 I/O 等操作限制性能的操作，例如图形的计算。
//                         这个 Scheduler 使用的固定的线程池，大小为  CPU 核数。
//                         不要把 I/O 操作放在 computation() 中，否则 I/O 操作的等待时间会浪费 CPU。
//        另外， Android 还有一个专用的 AndroidSchedulers.mainThread()，它指定的操作将在**Android**主线程运行。
//
//
//        Sceeduler默认给我们提供了subscribeOn() 和 observeOn() 两个方法来对线程进行控制

                .doOnSubscribe(new Consumer<Disposable>() {
                    //Modifies the source {@code ObservableSource} so that it invokes the given action when it is subscribed from its subscribers.
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        //showLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()) //指定 Subscriber 的回调发生在主线程

//        subscribeOn(Scheduler.io()) 和 observeOn(AndroidSchedulers.mainThread()) 的使用方式非常常见，
//        它适用于多数的 『后台线程取数据，主线程显示』的程序策略。

                .subscribe(new Observer<Response<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("123");
                    }

                    @Override
                    public void onNext(Response<String> stringResponse) {
                        //handleResponse(stringResponse);
                        System.out.println("123");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("123");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("123");
                    }
                });

//        StringConverter：按文本解析，解析的编码依据服务端响应头中的Content-Type中的编码格式，自动进行编码转换，确保不出现乱码。
//        BitmapConverter：如果请求的数据是图片，则可以使用该转换器，该方法对图片进行了压缩处理，确保不发生OOM。
//        FileConverter：如果要做文件下载，可以使用该转换器，内部封装了关于文件下载和进度回调的方法。
//        如果这些不能满足你的需求，参考上面的自定义Converter链接。


    }


    //    OkDownload主要功能
//    1.结合OkGo的request进行网络请求，支持与OkGo保持相同的配置方法和传参方式
//    2.支持断点下载，支持突然断网，强杀进程后，继续断点下载
//    3.每个下载任务具有无状态、下载、暂停、等待、出错、完成共六种状态
//    4.所有下载任务按照tag区分，切记不同的任务必须使用不一样的tag，否者断点会发生错乱
//    5.相同的下载url地址，如果使用不一样的tag，也会认为是两个下载任务
//    6.不同的下载url地址，如果使用相同的tag，会认为是同一个任务，导致断点错乱
//    7.默认同时下载数量为3个，默认下载路径/storage/emulated/0/download，下载路径和下载数量都可以在代码中配置
//    8.下载文件名可以自己定义，也可以不传，让框架自动获取文件名
    private void initOkDownLoad() {


        //第一步，构建下载请求
        GetRequest<File> request = OkGo.<File>get("https://www.baidu.com/img/bd_logo1.png");
        String tag = "OkDownload";

        //第二步，构建下载任务，使用OkDownload中的request方法，传入一个tag和我们上一步创建的request对象，创建出下载任务
        final DownloadTask task = OkDownload.request(tag, request) //创建下载任务，tag为一个任务的唯一标示
                // 第一个参数是tag，表示当前任务的唯一标识，
                // 所有下载任务按照tag区分，不同的任务必须使用不一样的tag，否者断点会发生错乱，
                // 如果相同的下载url地址，如果使用不一样的tag，也会认为是两个下载任务，
                // 不同的下载url地址，如果使用相同的tag，也会认为是同一个任务，导致断点错乱。切记，切记！！

                //.priority(apk.priority)
                // 任务优先级，int值，值越大优先级越高
                // 也可以不设置，默认优先级为0，当所有任务优先级都一样的时候，就会按添加顺序下载

                //.folder("/path/okDownload")
                //下载的文件夹 bugcom.lzy.okgo.exception.StorageException: SDCard isn't available, please check SD card and permission: WRITE_EXTERNAL_STORAGE, and you must pay attention to Android6.0 RunTime Permissions!
                // 单独指定当前下载任务的文件夹目录，如果你是6.0以上的系统，记得下载的时候先自己获取sd卡的运行时权限，否则文件夹创建不成功，无法下载。**重要**
                // 当然也可以不指定，默认下载路径/storage/emulated/0/download。

                .fileName("okxxxx")
                //手动指定下载的文件名
                //手动指定下载的文件名，一般来说是不需要手动指定的，也建议不要自己指定，除非你明确知道你要下载的是什么，或者你想改成你自己的文件名。
                // 如果不指定，文件名将按照以下规则自动获取

                //.extra1(apk)                              //下载任务的额外数据
                //.extra2("当前任务需要保存的额外数据")     //下载任务的额外数据
                //.extra3("任何数据类型都可以传")           //下载任务的额外数据

                .register(new com.lzy.okserver.download.DownloadListener(tag) {
                    //当前任务的回调监听注册多少个都没问题同时生效，
                    // 当状态发生改变的时候，每个监听都会收到通知。
                    // 当然如果你只是想下载一个文件，不关心他的回调，那么你不用注册任何回调。
                    public void onStart(Progress progress) {
                        Log.d("OkDownLoad", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Download" + File.separator + "**************************************************************");

                    }

                    @Override
                    public void onProgress(Progress progress) {
                        Log.d("OkDownLoad", "Progress");
                    }

                    @Override
                    public void onError(Progress progress) {
                        Log.d("OkDownLoad", "ErrorXXXXXXXXXXXXXXXXXXXXX" + progress.exception);
                    }

                    @Override
                    public void onFinish(File file, Progress progress) {
                        Log.d("OkDownLoad", "Finish");

                    }

                    @Override
                    public void onRemove(Progress progress) {
                        Log.d("OkDownLoad", "Remove");
                    }
                })

                .save();
        //第一次创建任务需要调用save（）


        //第三步，开始一个新任务，或者继续下载暂停的任务都是这个方法
        task.start(); //开始或继续下载
        //task.restart(); //重新下载一个任务。重新下载会先删除以前的任务，同时也会删除文件，然后从头开始重新下载该文件。
        //task.pause(); //暂停下载
        //task.remove(); //删除下载，只删除记录，不删除文件
        //task.remove(true); //删除下载，同时删除记录和文件

//        控制台log要做以下几点说明：
//        1. onStart()方法是在下载请求之前执行的，所以可以做一些请求之前相关的事，比如修改请求参数，加密，显示对话框等等。
//        2. 服务端返回的响应码是206，注意是206，这个很重要，只有206才能实现断点下载，表示本次返回的是 *部分* 响应体，并不是全部的数据。
//        3. 服务端一定要返回Content-Length，注意，是一定要返回Content-Length这个响应头，如果没有，该值默认是-1，这个值表示当前要下载的文件有多大，
//           如果服务端不给的话，客户端在下载过程中是不可能知道我要下载的文件有多大的，所以常见的问题就是进度是负数。
//        4. 下载文件的时候，响应体会打印一句话：maybe [binary boby], emitted!，这句话表示当前的数据是二进制文件，控制台没法打印也没必要打印出来，所以不用打印了。所以不要认为是bug，这是正常的。
//        5. 下载完后，最后会调用onFinish()，不过我设计成在调用onFinish()之前，还会额外调用一次onProgress()方法，这样的好处可以在onProgress方法中捕获到所有的状态变化，方便管理。

        //文件下载状态
//        public static final int NONE = 0;         //无状态
//        public static final int WAITING = 1;      //等待
//        public static final int LOADING = 2;      //下载中
//        public static final int PAUSE = 3;        //暂停
//        public static final int ERROR = 4;        //错误
//        public static final int FINISH = 5;       //完成


    }

    //    OkUpload主要功能
//    1.结合OkGo的request进行网络请求，支持与OkGo保持相同的配置方法和传参方式
//    2.上传只能使用有请求体的方法
//    3.该上传管理为简单管理，不支持断点续传或分片上传，只是简单的将所有上传任务使用线程池进行了统一管理
//    4.上传任务存入数据库，按tag区分，意义与OkUpload的tag一致
//    5.默认同时上传数量为1个,该数列可以在代码中配置修改
    private void initOkUpload() {

        OkUpload okUpload = OkUpload.getInstance();
        okUpload.getThreadPool().setCorePoolSize(1);

        PostRequest<String> postRequest = OkGo.<String>post("https://www.baidu.com")//
                .headers("aaa", "111")//
                .params("bbb", "222")//
                .params("fileKey", new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Download" + File.separator))//
                .converter(new StringConvert());//最后一定需要传递一个Converter对象，表示上传成功后，如何解释上传的结果，不传的话，会报错。

        UploadTask<String> task = OkUpload.request("path", postRequest)//
                .priority(1)//
                .extra1("aaaa")//
                .save();

        task.start();

    }
}
