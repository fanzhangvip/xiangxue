package com.zero.xiangxue.handlerdemo;


public class MyMessage {

    public int what;

    public int arg1;


    public int arg2;

    public Object obj;

    public MyHandler target;

    @Override
    public String toString() {
        return obj.toString();
    }
}
