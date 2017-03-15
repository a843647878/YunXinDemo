package com.cu.yunxindemo.entity.im;

public class AppBean {

    private int id;
    private int icon;
    private String funcName;

    public int getIcon() {
        return icon;
    }

    public String getFuncName() {
        return funcName;
    }

    public int getId() {
        return id;
    }

    public AppBean(int icon, String funcName,int id){
        this.icon = icon;
        this.funcName = funcName;
        this.id = id;
    }
}
