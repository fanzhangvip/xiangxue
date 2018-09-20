package com.zero.xiangxue.handlerdemo;


/**
 * Created by honjane on 2017/3/12.
 */

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
