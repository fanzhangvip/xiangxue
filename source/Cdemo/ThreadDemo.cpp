//
// Created by Zero on 2019/2/28.
//
#include <stdio.h>
#include <stdlib.h>
#include <iostream>
#include <thread>
#include <utility>
#include <chrono>
#include <functional>
#include <atomic>
#include <boost/thread.hpp>

#include "ThreadDemo.h"


void thread_task() {
    std::cout << "hello thread" << std::endl;
}

void f1(int n){
    for(int i = 0; i < 5; ++i)
    {
        std::cout << "Thread " << n << " executing\n";
        std::this_thread::sleep_for(std::chrono::milliseconds(10));
    }
}

void f2(int& n){
    for (int i = 0; i < 5; ++i) {
        std::cout << "Thread 2 executing\n";
        ++n;
        std::this_thread::sleep_for(std::chrono::milliseconds(10));

    }
}




