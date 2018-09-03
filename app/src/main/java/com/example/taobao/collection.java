package com.example.taobao;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.myapplication.R;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 袁依吉 on 2018/7/5.
 */

public class collection extends Activity {
    private static final String TAG ="collection" ;
    ListView listView4;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection);
        listView4 = (ListView) findViewById(R.id.listView4);
        Message message = new Message();
        message.what = 123;
        message.obj = 2;
        handler.sendMessage(message);
        con();
        Log.d("collection", "Handle");

    }
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 123) {
                con();
            }
        }
    };



        public void con() {
                final ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();//新建ArrayList
                List<MyData> allDatas = DataSupport.findAll(MyData.class);

                for (int i = 0; i < allDatas.size(); i++) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    MyData myData1 = allDatas.get(i);
                    Log.d(TAG,myData1.toString()+"===============");
                    String url=myData1.getImage();
                    Bitmap httpBitmap = getHttpBitmap(url);
                    map.put("ItemImage", httpBitmap);// 添加图像资源的ID
                    map.put("ItemTitle", myData1.getTitle());// 按序号做ItemText
                    map.put("ItemPrice", "价格" +myData1.getPrice());
                    map.put("ItemSold", "销量" + myData1.getSold());
                    map.put("ItemFrom", "产地" + myData1.getLocation());
                    map.put("ItemId", myData1.getItem_id());
                    lstImageItem.add(map);
                    Log.d("collection", lstImageItem.toString());
                }//for循环

                // 生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
                final SimpleAdapter saImageItems = new SimpleAdapter(this,
                        lstImageItem,// 数据来源
                        R.layout.list_item,// item的XML实现
                        // 动态数组与ImageItem对应的子项
                        new String[]{ "ItemImage","ItemTitle", "ItemPrice", "ItemSold"},
                        // Item的XML文件里面的一个ImageView,两个TextView ID
                        new int[]{ R.id.imageView2, R.id.textView3, R.id.textView4, R.id.textView5});
                listView4.setAdapter(saImageItems);

            //适配器图片显示
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
    }


    //地址转Bitmap
    public static Bitmap getHttpBitmap(String url){

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

