package com.wz.logapp.connect;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by zheng on 2017/5/17.
 * 连接服务端
 */

public class ConnectServer {
    private String INFO;
    private final String strUrl="http://10.0.49.71:8080/log";
    private String getURL;
    private URL url;
    private HttpURLConnection connection;
    private StringBuffer sb;
    public void initialConnection(String keyWord){
        try {
            //URLEncoder.encode(keyWord,"utf-8");
            this.INFO= keyWord;
            this.getURL=strUrl+this.INFO;
            url=new URL(getURL);
            Log.i("URL",url.toString());
            connection=(HttpURLConnection) url.openConnection();
            connection.setRequestMethod("get");
            connection.setUseCaches(false);
            connection.connect();
        } catch (UnsupportedEncodingException e) {
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
    }

    /**
     * 通过Post获取连接
     * @param keyWord
     */
    public void initialByPost(String keyWord,String action){
        try {
            //URLEncoder.encode(keyWord,"utf-8");
            this.INFO= keyWord;
            this.getURL=strUrl+action;
            url=new URL(getURL);
            Log.i("URL",url.toString());
            connection=(HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            // 发送POST请求必须设置如下两行
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            // 获取URLConnection对象对应的输出流
            PrintWriter printWriter = new PrintWriter(connection.getOutputStream());
            // 发送请求参数
            printWriter.write(keyWord);//post的参数 xx=xx&yy=yy
            // flush输出流的缓冲
            printWriter.flush();
            printWriter.close();
            connection.connect();

        } catch (UnsupportedEncodingException e) {
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
    }
    /**
     *  获取服务端的回应信息
     * @param keyWord 请求的参数
     * @return
     */
    public String getAnswer(String keyWord){
        // initial connection
        initialConnection(keyWord);
//    	System.out.println(question);
        String subStr="亲，出错啦，请联网使用";
        sb=new StringBuffer();
        try {
            InputStreamReader input=new InputStreamReader(connection.getInputStream(),"utf-8");
            BufferedReader reader=new BufferedReader(input);
            String line="";
            while((line=reader.readLine())!=null){
                sb.append(line);
            }
            // close bufferedReader
            reader.close();
            input.close();
            // 断开连接
            this.connection.disconnect();

            // product answer String and subString
            subStr=sb.toString();
        } catch (UnsupportedEncodingException e) {
        } catch (IOException e) {
        }
        return subStr;
    }

    /**
     * 使用action的post方式请求
     * @param keyWord
     * @param action 确定采用的action
     * @return
     */
    public String getAnswerByPost(String keyWord,String action){
        // initial connection
        initialByPost(keyWord,action);
//    	System.out.println(question);
        String subStr="亲，出错啦，请联网使用";
        sb=new StringBuffer();
        try {
            InputStreamReader input=new InputStreamReader(connection.getInputStream(),"utf-8");
            BufferedReader reader=new BufferedReader(input);
            String line="";
            while((line=reader.readLine())!=null){
                sb.append(line);
            }
            // close bufferedReader
            reader.close();
            input.close();
            // 断开连接
            this.connection.disconnect();

            // product answer String and subString
            subStr=sb.toString();
        } catch (UnsupportedEncodingException e) {
        } catch (IOException e) {
        }
        return subStr;
    }
}
