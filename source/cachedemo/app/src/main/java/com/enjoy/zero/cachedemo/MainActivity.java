package com.enjoy.zero.cachedemo;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //创建一个被观察者（开关）
        Observable switcher = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                emitter.onNext("On");
                emitter.onNext("Off");
                emitter.onNext("On");
                emitter.onNext("On");
            }
        });
        //创建一个观察者（台灯）
        Observer light = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                //处理传过来的onNext事件
                Log.d("DDDDD", "handle this---" + s);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {
                //被观察者的onCompleted()事件会走到这里;
                Log.d("DDDDDD", "结束观察...\n");
            }
        };
        //订阅
        switcher.subscribe(light);
    }
}
