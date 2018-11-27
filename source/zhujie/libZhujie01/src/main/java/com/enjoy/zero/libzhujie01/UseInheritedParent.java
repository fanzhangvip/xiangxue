package com.enjoy.zero.libzhujie01;

@InheritedTest("Parent")
public class UseInheritedParent {

    @InheritedTest("parent doSomething method")
    public void doSomething(){
        System.out.println("parent do something");
    }
}
