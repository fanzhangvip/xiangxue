package com.enjoy.zero.zhujiedemo;

import android.widget.Button;

/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询安生老师QQ 669100976
 * 类说明:
 */
public class Main2Activity_Utils {


    public  void bind(Main2Activity activity){
        activity.mButton = (Button)activity.findViewById(R.id.button);
        activity.mButton1 = (Button)activity.findViewById(R.id.button1);
        activity.mButton2 = (Button)activity.findViewById(R.id.button2);
        activity.mButton3 = (Button)activity.findViewById(R.id.button3);
        activity.mButton4 = (Button)activity.findViewById(R.id.button4);
    }



    public  void unBind(Main2Activity activity){
        activity.mButton = null;
        activity.mButton1 = null;
        activity.mButton2 = null;
        activity.mButton3 = null;
        activity.mButton4 = null;
    }


}
