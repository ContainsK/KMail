package com.tk.kmail.model.db_bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import javax.mail.Message;

/**
 * 将Message 转换为MsgBean 缓存到本地数据库，此时Message是加密状态。
 * <p>
 * 处理每次请求下来的数据并且缓存到数据库
 * Created by TangKai on 2019/1/9.
 */
@Entity
public class MsgBean {
    @Id(autoincrement = true)
    private Long id;
    private String uid;
    private String content;
    private String title, sendTime, dec;
    @Transient
    public Message msg;

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDec() {
        return this.dec;
    }

    public void setDec(String dec) {
        this.dec = dec;
    }

    public String getSendTime() {
        return this.sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Generated(hash = 2105877229)
    public MsgBean(Long id, String uid, String content, String title,
            String sendTime, String dec) {
        this.id = id;
        this.uid = uid;
        this.content = content;
        this.title = title;
        this.sendTime = sendTime;
        this.dec = dec;
    }

    @Generated(hash = 237905234)
    public MsgBean() {
    }

}
