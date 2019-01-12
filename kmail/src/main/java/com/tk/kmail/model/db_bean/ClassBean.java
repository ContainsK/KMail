package com.tk.kmail.model.db_bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import javax.mail.Folder;

/**
 * Created by TangKai on 2019/1/11.
 */
@Entity
public class ClassBean {
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private int flag;
    @Transient
    public Folder folder;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getFlag() {
        return this.flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Generated(hash = 1877505620)
    public ClassBean(Long id, String name, int flag) {
        this.id = id;
        this.name = name;
        this.flag = flag;
    }

    @Generated(hash = 1395092832)
    public ClassBean() {
    }
}
