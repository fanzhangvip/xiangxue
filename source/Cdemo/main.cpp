#include <iostream>

#include <thread>

#include "ThreadDemo.h"
int main() {
    std::cout << "Hello, World!" << std::endl;

    std::thread t(thread_task);
    t.join();


    int n = 0;
    std::thread t1; //t1 is not a thread
    std::thread t2(f1,n+1); //pass by value
    std::thread t3(f2,std::ref(n)); // pass by reference
    std::thread t4(std::move(t3)); // t4 is now running f2(). t3 is no longer a thread

    t2.join();
    t4.join();

    std::cout << "Final value of n is " << n << "\n";

    return 0;
}