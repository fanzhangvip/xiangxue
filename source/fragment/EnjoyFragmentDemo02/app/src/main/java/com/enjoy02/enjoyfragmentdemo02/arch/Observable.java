package com.enjoy02.enjoyfragmentdemo02.arch;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象主题，被观察者
 */
public abstract class Observable {


    private List<Observer> observerList = new ArrayList<>();

    /**
     * 添加观察者
     *
     * @param observer 观察者
     */
    public void addObserver(Observer observer) {
        observerList.add(observer);
    }

    /**
     * 删除观察者
     *
     * @param observer 观察者
     * @return 删除成功返回true, 否则返回false
     */
    public boolean removeObserver(Observer observer) {
        return observerList.remove(observer);
    }

    public void notifyObservers() {
        for (Observer observer : observerList) {
            observer.update(this);
        }
    }
}
