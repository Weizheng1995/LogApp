package com.wz.logapp.model;

import com.wz.logapp.util.Util;

/**
 * Created by zheng on 2017/5/17.
 * 日志实体类
 */

public class Log {
    private int id;
    private String title;
    private String content;
    private String createTime;
    private String author;

    public Log() {
    }

    public Log(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.createTime= Util.getCurrentDate();
    }

    public Log(int id, String title, String content, String createTime, String author) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        StringBuffer sb=new StringBuffer();
        sb.append(id+"|");
        sb.append(title+"|");
        sb.append(content.substring(0,8<content.length()?8:content.length())+"|");
        sb.append(createTime);
        return sb.toString();
    }
}
