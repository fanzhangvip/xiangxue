package com.enjoy.enjoyfragmentdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.enjoy.enjoyfragmentdemo.fragment.OneFragment;

import java.util.ArrayDeque;
import java.util.Stack;
/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询伊娜老师QQ 2133576719
 * 类说明:
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        //TODO: 1.获取fragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        //TODO: 2.开启一个fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //TODO: 3.向FrameLayout容器添加MainFragment,现在并未真正执行
        transaction.add(R.id.frame_main, MainFragment.newIntance(), MainFragment.class.getName());
        //TODO: 4.提交事务，真正去执行添加动作
        transaction.commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            return backOrUp();
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public boolean backOrUp() {
        // 获取当前回退栈中的Fragment个数
        FragmentManager childFragmentManager = getSupportFragmentManager().findFragmentByTag(MainFragment.class.getName()).getChildFragmentManager().findFragmentByTag(OneFragment.class.getName()).getChildFragmentManager();
        int backStackEntryCount = childFragmentManager.getBackStackEntryCount();
        Log.i("Zero", "count: " + backStackEntryCount);
        // 判断当前回退栈中的fragment个数,
        if (backStackEntryCount > 1) {
            // 立即回退一步
            childFragmentManager.popBackStackImmediate();
            // 获取当前退到了哪一个Fragment上,重新获取当前的Fragment回退栈中的个数
            FragmentManager.BackStackEntry backStack = childFragmentManager
                    .getBackStackEntryAt(childFragmentManager
                            .getBackStackEntryCount() - 1);
            // 获取当前栈顶的Fragment的标记值
            String tag = backStack.getName();
            Log.i("Zero", "tag: " + tag);
            // 判断当前是哪一个标记
            return true;
        } else {
            //回退栈中只剩一个时,退出应用
//            finish();
            return false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Log.i("Zero", "keyCode: " + keyCode + " back: " + KeyEvent.KEYCODE_BACK);
        // 判断当前按键是返回键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return backOrUp();
        }
        return true;
    }

    //测试 Stack Vs ArrayDeque
    public static void Test() {
        new Thread() {
            public void run() {
                Log.i("Zero", "thread start");
                stackTest();
                Log.i("Zero", "thread ...");
                arrayDequeTest();
                Log.i("Zero", "thread end");
            }
        }.start();
    }

    /**
     * 性能测试
     */
    public static void stackTest() {
        Stack<Integer> stack = new Stack<>();
        final int N = 10000000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < N; i++) {
            stack.push(i);
        }
        for (int i = 0; i < N; i++) {
            stack.pop();
        }
        Log.i("Zero", "stack timediff: " + (System.currentTimeMillis() - start));
    }

    /**
     * 使用ArrayDeque实现栈的功能
     */
    public static void arrayDequeTest() {
        final int N = 10000000;
        ArrayDeque<Integer> arrayDeque = new ArrayDeque<>(N);
        long start = System.currentTimeMillis();
        for (int i = 0; i < N; i++) {
            arrayDeque.add(i);
        }
        for (int i = 0; i < N; i++) {
            arrayDeque.pollLast();
        }
        Log.i("Zero", "arrayDeque timediff: " + (System.currentTimeMillis() - start));
    }
}
