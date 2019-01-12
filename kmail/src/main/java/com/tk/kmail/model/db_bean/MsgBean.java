package com.tk.kmail.model.db_bean;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

/**
 * 将Message 转换为MsgBean 缓存到本地数据库，此时Message是加密状态。
 * <p>
 * 处理每次请求下来的数据并且缓存到数据库
 * Created by TangKai on 2019/1/9.
 */
@Entity
public class MsgBean {
    @Expose
    @Id(autoincrement = true)
    public Long id;
    public long uid = System.currentTimeMillis();
    public String content;
    public String title, dec, className;
    public Date sendTime;
    public int flag;

    public int getFlag() {
        return this.flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Date getSendTime() {
        return this.sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDec() {
        return this.dec;
    }

    public void setDec(String dec) {
        this.dec = dec;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUid() {
        return this.uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    @Generated(hash = 276846687)
    public MsgBean(Long id, long uid, String content, String title, String dec,
            String className, Date sendTime, int flag) {
        this.id = id;
        this.uid = uid;
        this.content = content;
        this.title = title;
        this.dec = dec;
        this.className = className;
        this.sendTime = sendTime;
        this.flag = flag;
    }

    @Generated(hash = 237905234)
    public MsgBean() {
    }


}
