package com.example.boot_demo.query;

public class QueryUserLogin {
    private String account;
    private String pwd;

    public QueryUserLogin(){

    }

    public QueryUserLogin(String account, String pwd) {
        this.account = account;
        this.pwd = pwd;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
