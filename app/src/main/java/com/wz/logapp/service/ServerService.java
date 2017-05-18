package com.wz.logapp.service;

import com.wz.logapp.connect.ConnectServer;
import com.wz.logapp.model.Log;
import com.wz.logapp.model.User;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;


/**
 * Created by zheng on 2017/5/17.
 * 连接服务端的服务层
 */

public class ServerService {
    private boolean flag=false;
    // 连接服务端对象
    private ConnectServer server;
    public ServerService(){
        server=new ConnectServer();
    }

    /**
     * 进行远程登陆验证
     * @param user 封装了登录验证的信息
     * @return
     */
    public boolean loginService(User user){
        String action="/login?";
        String name=user.getName().trim();
        String password=user.getPassword().trim();
        // 生成传输keyWord
        final String keyWord=action+"name="+name+"&password="+password;
        String backMessage=server.getAnswer(keyWord);
        if("true".equals(backMessage)){
            // 验证成功
            flag=true;
        }else{
            // 验证失败
            flag=false;
        }
        return flag;
    }

    /**
     * 进行日志添加功能
     * @param log 封装了日志信息
     * @return
     */
    public boolean addLog(Log log){
        String action="/add";
        //使用json封装了日志信息
         String info=objectToJson(log);
        // 生成传输keyWord
        final String keyWord=info;
        String backMessage=server.getAnswerByPost(keyWord,action);
        android.util.Log.i("addLog-backMessage:",backMessage);
        if("true".equals(backMessage)){
            // 验证成功
            flag=true;
        }else{
            // 验证失败
            flag=false;
        }
        return flag;
    }
    /**
     * 封住log对象成json，以便传输
     * @param log
     * @return
     */
    public String objectToJson(Log log){
        JSONObject jsonObject=new JSONObject();
        try{
            jsonObject.put("title",log.getTitle());
            jsonObject.put("content",log.getContent());
            jsonObject.put("createTime",log.getCreateTime());
            jsonObject.put("author",log.getAuthor());
        }catch (JSONException e){
            android.util.Log.i("解析JSON出错",e.getCause().toString());
        }
        return jsonObject.toString();
    }
    public Log[] queryAll(){
        Log logs[];
        String action="/queryAll";
        // 获取服务器传回来的数据
        String backMessage=server.getAnswerByPost("",action);
        // 进行数据的JSONArray解析
        JSONArray jsonArray=JSONArray.fromObject(backMessage);
        // 遍历
        JSONObject jsonObject;
        int length=jsonArray.size();
        // 为数组申请空间
        logs=new Log[length];
        Log log;
        for(int i=0;i<length;i++){
            jsonObject=jsonArray.getJSONObject(i);
            // 获取数据
            int id=jsonObject.getInt("id");
            String title=jsonObject.getString("title");
            String content=jsonObject.getString("content");
            String createTime=jsonObject.getString("createTime");
            String author=jsonObject.getString("author");
            log=new Log(id,title,content,createTime,author);
            logs[i]=log;
        }
        return logs;
    }
}
