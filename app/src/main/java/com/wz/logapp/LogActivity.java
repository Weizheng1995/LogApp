package com.wz.logapp;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.wz.logapp.model.Log;
import com.wz.logapp.service.ServerService;

public class LogActivity extends AppCompatActivity {
    private TabHost tabHost;// 功能主界面tab组件
    private ListView listViewLog;// 全部日志的listView组件
    private EditText title;// 日志标题
    private EditText content;// 日志内容
    private TextView authorView;
    private ServerService serverService; // 服务层
    private  Log log;//存储log信息
    private Log logs[];//存储log数组信息
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        // 获取对象
        tabHost=(TabHost) findViewById(R.id.logTab);
        // 初始化tabhost组件
        tabHost.setup();
        // 添加内容标签
        addTab();
        //获取传过来的用户名
        authorView=(TextView) findViewById(R.id.authorView);
        Bundle bundle=getIntent().getExtras();
        String strMess=bundle.getString("author");
        authorView.setText(strMess);
    }

    /**
     * 添加标签
     */
    public void addTab(){
        LayoutInflater inflater=LayoutInflater.from(this);
        inflater.inflate(R.layout.log_add,tabHost.getTabContentView());
        inflater.inflate(R.layout.log_search,tabHost.getTabContentView());
        // 添加第一个标签
        tabHost.addTab(tabHost.newTabSpec("tab01").setIndicator("添加日志").setContent(R.id.logAdd));
        // 给ListView添加适配器和内容
        addListItem();
        // 添加第二个标签
        tabHost.addTab(tabHost.newTabSpec("tab02").setIndicator("浏览日志").setContent(R.id.logSearch));
    }
    /**
     * 为listViewLog添加数据
     */
    public void addListItem(){
        serverService=new ServerService();
        // 获取listView
        listViewLog=(ListView) findViewById(R.id.listViewLog);
        /*测试数据
        Log[] logs=new Log[2];
        Log log=new Log(1,"title1","测试内容1","2017-5-17 14:47:00","zheng");
        Log log2=new Log(1,"title2","测试内容2","2017-5-17 14:47:00","zheng2");
        logs[0]=log;
        logs[1]=log2;
        */
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                logs=serverService.queryAll();
                //创建适配器
                ArrayAdapter<Log> adapter=new ArrayAdapter<Log>(LogActivity.this,R.layout.support_simple_spinner_dropdown_item,logs);
                // 将适配器关联listview
                listViewLog.setAdapter(adapter);
            }
        };
        // 开启线程
        new Thread(runnable).start();

        listViewLog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取点击的Item的内容
                String result=parent.getItemAtPosition(position).toString();
                //实现跳转
                changeToDetailActivity(result);
                // 展示提示信息
//                Toast.makeText(LogActivity.this,result,Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 实现跳转activity,当触碰对应的listItem信息时
     */
    public void changeToDetailActivity(String result){
        String  MYACTION="com.wz.action";
        Intent implicitIntent=new Intent();
        implicitIntent.setAction(MYACTION);
        implicitIntent.putExtra("result",result);
        if(implicitIntent.resolveActivity(getPackageManager())!=null)
            startActivity(implicitIntent);
    }

    /**
     * logActivity中的按键监听事件处理
     * @param view
     */
    public void logOnclick(View view){
        switch (view.getId()){
            case R.id.btnAdd:
                // 添加日志
                addLog();
                break;
        }
    }

    /**
     * 添加日志
     */
    public void addLog(){
        serverService=new ServerService();
        title=(EditText) findViewById(R.id.txtTitle);
        content=(EditText) findViewById(R.id.txtContent);
        if(title.getText().toString().trim().isEmpty()||content.getText().toString().trim().isEmpty()){
            Toast.makeText(LogActivity.this,"请输入完整的信息",Toast.LENGTH_SHORT).show();
        }else{
            // 封装信息
            log=new Log(title.getText().toString().trim(),content.getText().toString().trim(),authorView.getText().toString().trim());
            //进行封装
            final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle data = msg.getData();
                String val = data.getString("value");
                android.util.Log.i("result","addLog请求结果:" + val);
                if("true".equals(val)){
                    //True
                    // 清空所写的内容
                    title.setText("");
                    content.setText("");
                    Toast.makeText(LogActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LogActivity.this,"添加失败",Toast.LENGTH_SHORT).show();
                }
            }
        };

        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                // TODO: http request.
                // 进行服务器请求
                boolean flag=serverService.addLog(log);
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("value",flag?"true":"false");
                msg.setData(data);
                handler.sendMessage(msg);
            }
        };
        new Thread(runnable).start();
        }
    }
}
