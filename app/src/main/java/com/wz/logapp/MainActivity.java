package com.wz.logapp;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.wz.logapp.model.User;
import com.wz.logapp.service.ServerService;


public class MainActivity extends AppCompatActivity {
    private EditText name;
    private EditText pwd;
    private ServerService serverService;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 按键监听事件
     * @param view
     */
    public void onclick(View view){
        switch (view.getId()){
            case R.id.btnLogin:
                login();
                break;
            case R.id.btnReset:
                resetInfo();
                break;
        }
    }
    public void login(){
        name= (EditText) findViewById(R.id.txtName);
        pwd=(EditText) findViewById(R.id.txtPassword);
        user=new User();
        user.setName(name.getText().toString().trim());
        user.setPassword(pwd.getText().toString().trim());
        //登录验证
        serverService=new ServerService();
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle data = msg.getData();
                String val = data.getString("value");
                Log.i("result","请求结果:" + val);
                if("true".equals(val)){
                    //True
                    changeActivity();
                }else{
                    Toast.makeText(MainActivity.this,"请确认您的输入信息正确",Toast.LENGTH_SHORT).show();
                }
            }
        };

        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                // TODO: http request.
                boolean flag =serverService.loginService(user);
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("value",flag?"true":"false");
                msg.setData(data);
                handler.sendMessage(msg);
            }
        };
        new Thread(runnable).start();
    }
    /**
     * 跳转activity
     */
    public void changeActivity(){
        /**
        Intent intent=new Intent(this,LogActivity.class);
        startActivity(intent);
         **/
        String MYACTION="com.wz.log";
        Intent implicitIntent=new Intent();
        implicitIntent.setAction(MYACTION);
        implicitIntent.putExtra("author",name.getText().toString().trim());
        if(implicitIntent.resolveActivity(getPackageManager())!=null)
            startActivity(implicitIntent);
    }
    /**
     * 重置登录信息
     */
    public void resetInfo(){
        name= (EditText) findViewById(R.id.txtName);
        pwd=(EditText) findViewById(R.id.txtPassword);
        name.setText("");
        pwd.setText("");
    }
}
