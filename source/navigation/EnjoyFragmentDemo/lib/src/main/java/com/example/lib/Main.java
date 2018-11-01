package com.example.lib;

import java.util.ArrayDeque;
import java.util.Stack;

public class Main {

    public static void main(String ... args){
        System.out.println("Hello World!!!");
        stackTest();
        arrayDequeTest();
    }


    public static void stackTest(){
        Stack<Integer> stack = new Stack<>();
        final int N = 10000000;
        long start = System.currentTimeMillis();
        for(int i = 0; i < N; i++ ){
            stack.push(i);
        }
        System.out.println("stack timediff: " + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        for(int i = 0; i < N; i++){
            stack.pop();
        }
        System.out.println("stack timediff: " + (System.currentTimeMillis() - start));
    }

    public static void arrayDequeTest(){
        final int N = 10000000;
        ArrayDeque<Integer> arrayDeque = new ArrayDeque<>(N);
        long start = System.currentTimeMillis();
        for(int i = 0; i < N; i++ ){
            arrayDeque.addFirst(i);
        }
        System.out.println("arrayDeque timediff: " + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        for(int i = 0; i < N; i++){
            arrayDeque.pollFirst();
        }
        System.out.println("arrayDeque timediff: " + (System.currentTimeMillis() - start));
    }
}
