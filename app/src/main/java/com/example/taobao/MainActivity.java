package com.example.taobao;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.example.myapplication.R;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.xml.sax.DTDHandler;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    String keyEdit="";
    String tmpUrl="";
    String url="http://s.m.taobao.com/search?event_submit_do_new_search_auction=1&_input_charset=utf-8&searchfrom=1&action=home%3Aredirect_app_action&from=1&q={0}&sst=1&n=20&buying=buyitnow&m=api4h5&wlsort=10&page=1";
    EditText edit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridView=(GridView)findViewById(R.id.GridView);//主页网格显示
        Button btn=(Button)findViewById(R.id.search);//跳转按钮
        edit = (EditText)findViewById(R.id.editText);



        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < 12; i++) {
            int j=i+1;
            MyData goods = DataSupport.find(MyData.class , j);
            HashMap<String, Object> map = new HashMap<String, Object>();
            //map.put("ItemImage", goods.getImage());// 添加图像资源的ID
            //map.put("ItemText", "NO." + goods.getTitle());// 按序号做ItemText
            //map.put("ItemText2", "subNO." + goods.getPrice());
            lstImageItem.add(map);
        }

        // 生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
        SimpleAdapter saImageItems = new SimpleAdapter(this,
                lstImageItem,// 数据来源
                R.layout.item,// item的XML实现
                // 动态数组与ImageItem对应的子项
                new String[] { "ItemImage", "ItemText" ,"ItemText2"},
                // Item的XML文件里面的一个ImageView,两个TextView ID
                new int[] { R.id.imageView, R.id.textView,R.id.textView2 });
        // 添加并且显示
        gridView.setAdapter(saImageItems);





        // 添加消息处理
        // 注册监听事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
//                Toast.makeText(MainActivity.this,
//                        arg2+"号", Toast.LENGTH_SHORT).show();
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                List<MyData> myDatas = DataSupport.findAll(MyData.class);
//                DataSupport.deleteAll(MyData.class);
//                Log.d(TAG, myDatas.toString());
                try {
                    Log.d("tmpUrl","line92");
                    keyEdit = URLEncoder.encode(edit.getText().toString(), "utf-8");
                    if (!keyEdit.equals("")) {
                        tmpUrl=url.replace("{0}", keyEdit);
                        Intent intent=new Intent(MainActivity.this, list.class);
                        intent.putExtra("edit",tmpUrl);
                        startActivity(intent);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
//                Intent intent=new Intent(MainActivity.this, list.class);
//                intent.putExtra("edit",tmpUrl);
//                startActivity(intent);
            }
        });











    }
}
