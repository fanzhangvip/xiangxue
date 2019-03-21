package com.zero.rxjava1;

import rx.Completable;
import rx.CompletableSubscriber;
import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func0;

public class Main {


    public static void main(String ... args){
        singleTest1();
        singleTest2();
        rangeTest();

        Observable<Long> deferObservable = getDefer();
        Observable<Long> justObservable = getJust();


        for (int i = 0; i < 3; i++) {
            justObservable.subscribe(new Action1<Long>() {
                @Override
                public void call(Long aLong) {
                    System.out.println("justObservable: " + aLong);
                }
            });
        }
        for (int i = 0; i <3 ; i++) {
            deferObservable.subscribe(new Action1<Long>() {
                @Override
                public void call(Long aLong) {
                    System.out.println("deferObservable: " + aLong);
                }
            });
        }

    }

    public static void singleTest1(){
        Single.create(new Single.OnSubscribe<Integer>() {
            @Override
            public void call(SingleSubscriber<? super Integer> singleSubscriber) {
                if(!singleSubscriber.isUnsubscribed()){
                    singleSubscriber.onSuccess(1);
                }
            }
        }).subscribe(new SingleSubscriber<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                System.out.println("value: " + integer);
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }

    public static void singleTest2(){
        Single.create(new Single.OnSubscribe<Integer>() {
            @Override
            public void call(SingleSubscriber<? super Integer> singleSubscriber) {
                if(!singleSubscriber.isUnsubscribed()){
                    singleSubscriber.onError(new Throwable(("Single error")));
                }
            }
        }).subscribe(new SingleSubscriber<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                System.out.println("onSuccess: " + integer);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onError: " + throwable.getMessage());
            }
        });
    }

    public static void completableTest1(){
        Completable.error(new Throwable("Completable error"))
                .subscribe(new CompletableSubscriber() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onSubscribe(Subscription subscription) {

                    }
                });
    }

    public static void completableTest2(){
        Completable.complete().subscribe(new CompletableSubscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onSubscribe(Subscription subscription) {

            }
        });
    }

    public static void rangeTest(){
        Observable.range(10,5).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("rangeTest: " + integer);
            }
        });
    }

    public static Observable<Long> getJust(){
        return Observable.just(System.currentTimeMillis());
    }

    public static Observable<Long> getDefer(){
        return Observable.defer(new Func0<Observable<Long>>() {
            @Override
            public Observable<Long> call() {
                return Observable.just(System.currentTimeMillis());
            }
        });
    }

}


