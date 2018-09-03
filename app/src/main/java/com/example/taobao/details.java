package com.example.taobao;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 袁依吉 on 2018/7/4.
 */

public class details extends Activity {
    private static final String TAG = "details";
    String data;
    String imgData;
    String url2 = "https://acs.m.taobao.com/h5/mtop.taobao.detail.getdetail/6.0/?data=%7B%22itemNumId%22%3A%22{0}%22%7D&qq-pf-to=pcqq.group";
    String images = "images.*?]";
    String tmpUrl1;
    String tmpUrl2;
    String imgUrl;
    TextView titleView;
    TextView priceView;
    TextView soldView;
    TextView fromView;
    ListView listView;
    Button collect;
    Button buy;
    Button collection;
    Bitmap image;
    String title;
    String sold;
    String price;
    String from;
    String id;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        collect = (Button)findViewById(R.id.collect);
        buy = (Button)findViewById(R.id.buy);
        collection = (Button)findViewById(R.id.collection);

        listView = (ListView) findViewById(R.id.listView);

        titleView = (TextView) findViewById(R.id.title);
        priceView = (TextView) findViewById(R.id.price);
        soldView = (TextView) findViewById(R.id.sold);
        fromView = (TextView) findViewById(R.id.from);
        final Intent intent = getIntent();
        titleView.setText(intent.getStringExtra("title"));
        priceView.setText(intent.getStringExtra("price"));
        soldView.setText(intent.getStringExtra("sold"));
        fromView.setText(intent.getStringExtra("from"));
        imgUrl = intent.getStringExtra("ItemImgurl");
        price = intent.getStringExtra("price");
        sold = intent.getStringExtra("sold");
        from = intent.getStringExtra("from");
        title = intent.getStringExtra("title");
        id = intent.getStringExtra("id");
        Log.d("details","图片"+imgUrl);



        final MyData goods = new MyData(imgUrl, price, sold,title,from,id);
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isExist = isExist();
                if(isExist){
                    Toast.makeText(details.this," 收藏失败",Toast.LENGTH_SHORT).show();
                }else {
                    goods.save();
//                    Log.d(TAG, goods.getImage());
                    Toast.makeText(details.this, "收藏成功", Toast.LENGTH_SHORT).show();
                }
            }
            public boolean isExist(){
                List<MyData> goodss = DataSupport.findAll(MyData.class);
                for(MyData myData : goodss){
                    if(goods.getItem_id().equals(myData.getItem_id())){
                        return true;
                    }
                }
                return false;
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentbuy = new Intent();
                intentbuy.setData(Uri.parse(tmpUrl2));
                intent.setAction(Intent.ACTION_VIEW);
                startActivity(intentbuy);
            }
        });




        collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(details.this, collection.class);
                startActivity(intent2);
            }
        });

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        con2();
    }



    public void con2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取网址
                Intent intent = getIntent();
                tmpUrl1 = intent.getStringExtra("id");
                tmpUrl2 = url2.replace("{0}", tmpUrl1);
                Log.d("list", "获取网址" + tmpUrl2);
                //用HttpClient发送请求，分为五步
                //第一步：创建HttpClient对象
                HttpClient httpCient = new DefaultHttpClient();
                //第二步：创建代表请求的对象,参数是访问的服务器地址
                HttpGet httpGet = new HttpGet(tmpUrl2.toString());

                try {
                    //第三步：执行请求，获取服务器发还的相应对象
                    HttpResponse httpResponse = httpCient.execute(httpGet);
                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        //第五步：从相应对象当中取出数据，放到entity当中
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity,"utf-8");//将entity当中的数据转换为字符串

                        //在子线程中将Message对象发出去
                        Message message = new Message();
                        message.what = 3;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();//这个start()方法不要忘记了
    }



    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 3) {
//                data = (String) msg.obj;
//                tran2(data);
                tran2(msg.obj.toString());
            }
        }
    };




    public void tran2(String data) {
        final ArrayList<HashMap<String, Object>> lstImage = new ArrayList<HashMap<String, Object>>();//新建ArrayList

        imgData = matchesToList(images, data).toString().replaceAll("images\":", "")
        .replaceAll("]","").replaceAll("\"","").replaceAll("\\[","");
       String[] s = imgData.split(",");
        for (int i = 0; i < s.length; i++) {
            HashMap<String, Object> x = new HashMap<String, Object>();
            String ss = "http:" + s[i];
            Log.d(TAG, ss);
            Bitmap bitImg2 = getHttpBitmap(ss);
            x.put("ItemImage", bitImg2);// 添加图像资源的ID

            lstImage.add(x);
        }
            // 生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
            final SimpleAdapter saImage = new SimpleAdapter(this,
                    lstImage,// 数据来源
                    R.layout.details_item,// item的XML实现
                    // 动态数组与ImageItem对应的子项
                    new String[]{"ItemImage"},
                    // Item的XML文件里面的一个ImageView,两个TextView ID
                    new int[]{R.id.imageView3});


            saImage.setViewBinder(new SimpleAdapter.ViewBinder() {

                @Override
                public boolean setViewValue(View view, Object data, String textRepresentation) {
                    // TODO Auto-generated method stub
                    if (view instanceof ImageView && data instanceof Bitmap) {

                        ImageView iv = (ImageView) view;
                        iv.setImageBitmap((Bitmap) data);
                        return true;
                    } else
                        return false;
                }
            });


        // 添加并且显示
        //gridView = (GridView) findViewById(R.id.GridView2);//列表网格显示
        listView.setAdapter(saImage);


    }










    public static List<String> matchesToList(String pattern,String text){
        List<String> set=new ArrayList<String>();
        Pattern pa= Pattern.compile(pattern);
        Matcher ms=pa.matcher(text);
        while(ms.find()){
            set.add(ms.group());
        }
        return set;
    }



    public static Bitmap getHttpBitmap(final String url){

        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            //            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;


    }


}








