package com.example.taobao;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.example.myapplication.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by 袁依吉 on 2018/7/3.
 */

public class list extends Activity {
    private static final String TAG = "list";
    String intentUrl;
    String htmlContent;
    GridView gridView;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        Log.d("list", "启动");
        gridView = (GridView) findViewById(R.id.GridView2);//列表网格显示
        //gridView = (GridView) findViewById(R.id.GridView2);//列表网格显示
        //获取网址
        //        Intent intent=getIntent();
        //        intentUrl=intent.getStringExtra("edit");
        //        Log.d("list", "获取网址"+intentUrl);
        //


        //        try {
        //            //获取网址
        //            Intent intent=getIntent();
        //            intentUrl=intent.getStringExtra("edit");
        //            Log.d("list", "获取网址"+intentUrl);
        //
        //
        //            htmlContent = HtmlService.getHtml(intentUrl);
        //            Log.d("list", "源代码"+htmlContent);
        //
        //
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }
        con();//获取源代码
    }


    public void con() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取网址
                Intent intent = getIntent();
                intentUrl = intent.getStringExtra("edit");
                //Log.d("list", "获取网址" + intentUrl);
                //用HttpClient发送请求，分为五步
                //第一步：创建HttpClient对象
                HttpClient httpCient = new DefaultHttpClient();
                //第二步：创建代表请求的对象,参数是访问的服务器地址
                HttpGet httpGet = new HttpGet(intentUrl.toString());

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
                        message.what = 123;
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
            if (msg.what == 123) {
                htmlContent = (String) msg.obj;
                tran(htmlContent);
            }
        }
    };



        //获取网页源代码转换成JSON

    public void tran(String htmlContent) {

        try {

            Log.d("list", "转换前" + htmlContent);
            JSONObject obj = new JSONObject(htmlContent);

            JSONArray itemData = obj.getJSONArray("listItem");

            final ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();//新建ArrayList

            for (int i = 0; i < itemData.length(); i++) {

                JSONObject item = itemData.getJSONObject(i);

                String imgurl = item.getString("pic_path").replace("60x60", "100x100");
                String title = item.getString("title");
                String price = item.getString("price");
                String sold = item.getString("sold");
                String item_id = item.getString("item_id");
                String area = item.getString("area");
                //Log.d(TAG, String.valueOf(imgurl));
                //                Drawable drawable = loadImageFromNetwork(imgurl);
                //                imageView2.setImageDrawable(drawable) ;
                Bitmap bitImg = getHttpBitmap(imgurl);

                HashMap<String, Object> map = new HashMap<String, Object>();

                //Log.d(TAG, String.valueOf(bitImg));
                map.put("ItemImage", bitImg);// 添加图像资源的ID
                map.put("ItemImgurl", imgurl);
                map.put("ItemTitle", title);// 按序号做ItemText
                map.put("ItemPrice", "价格" + price);
                map.put("ItemSold", "销量" + sold);
                map.put("ItemFrom", "产地" + area);
                map.put("ItemId", item_id);
                lstImageItem.add(map);

            }//for循环

            // 生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
            final SimpleAdapter saImageItems = new SimpleAdapter(this,
                    lstImageItem,// 数据来源
                    R.layout.list_item,// item的XML实现
                    // 动态数组与ImageItem对应的子项
                    new String[]{ "ItemImage","ItemTitle", "ItemPrice", "ItemSold"},
                    // Item的XML文件里面的一个ImageView,两个TextView ID
                    new int[]{ R.id.imageView2, R.id.textView3, R.id.textView4, R.id.textView5});


            saImageItems.setViewBinder(new SimpleAdapter.ViewBinder() {

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
            gridView.setAdapter(saImageItems);


            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    HashMap<String, Object> map = (HashMap<String, Object>)lstImageItem.get(i);
                    String title = (String)map.get("ItemTitle");
                    String price = (String)map.get("ItemPrice");
                    String sold = (String)map.get("ItemSold");
                    String from = (String)map.get("ItemFrom");
                    String id = (String)map.get("ItemId");
                    String imgUrl = (String)map.get("ItemImgurl");



                    Intent intent = new Intent(list.this, details.class);
                    intent.putExtra("title",title);
                    intent.putExtra("price",price);
                    intent.putExtra("sold",sold);
                    intent.putExtra("from",from);
                    intent.putExtra("id",id);
                    intent.putExtra("ItemImgurl",imgUrl);
                    startActivity(intent);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }





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



    //    public Bitmap getHttpBitmap(String url) {
    //        URL httpUrl = null;
    //        Bitmap bitmap = null;
    //        try {
    //            httpUrl = new URL(url);
    //            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
    //            conn.setConnectTimeout(6000);
    //            conn.setDoInput(true);
    //            conn.setUseCaches(false);
    //            InputStream in = conn.getInputStream();//这行报错
    //                    bitmap = BitmapFactory.decodeStream(in);
    //            in.close();
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //            Toast.makeText(this,"系统繁忙，请稍后再试",Toast.LENGTH_SHORT).show();
    //        }
    //        return bitmap;
    //
    //    }


    //    public Bitmap getURLimage(String url) {
    //        Bitmap bmp = null;
    //        try {
    //            URL myurl = new URL(url);
    //            // 获得连接
    //            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
    //            conn.setConnectTimeout(6000);//设置超时
    //            conn.setDoInput(true);
    //            conn.setUseCaches(false);//不缓存
    //            conn.connect();
    //            InputStream is = conn.getInputStream();//获得图片的数据流
    //            bmp = BitmapFactory.decodeStream(is);
    //            is.close();
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //        return bmp;
    //    }




}


