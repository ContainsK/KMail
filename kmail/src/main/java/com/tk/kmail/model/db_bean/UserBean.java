package com.tk.kmail.model.db_bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by TangKai on 2018/12/18.
 */
@Entity
public class UserBean extends BaseObservable {
    @Id(autoincrement = true)
    private Long id;
    private String username, password, descrip;

    @Bindable
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }
    @Bindable
    public String getDescrip() {
        return this.descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
        notifyPropertyChanged(BR.descrip);
    }

    @Generated(hash = 1663808361)
    public UserBean(Long id, String username, String password, String descrip) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.descrip = descrip;
    }

    @Generated(hash = 1203313951)
    public UserBean() {
    }
}
