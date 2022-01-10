package org.zznode.beans;

import java.io.Serializable;

public class User implements Serializable {

    private Integer id;
    private String userName;
    private String userSex;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public User(int id, String userName, String userSex) {
        this.id = id;
        this.userName = userName;
        this.userSex = userSex;
    }


}
