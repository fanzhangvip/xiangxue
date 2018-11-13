package com.enjoy02.lib;

public class MainTest {

    public static void main(String... args) {
        Owner owner = new Owner("Lily");

        Mouse mouse1 = new Mouse("老鼠1号");
        Mouse mouse2 = new Mouse("老鼠2号");
        Mouse mouse3 = new Mouse("老鼠3号");
        Mouse mouse4 = new Mouse("老鼠4号");
        Mouse mouse5 = new Mouse("老鼠5号");
        Mouse mouse6 = new Mouse("老鼠6号");
        Mouse mouse7 = new Mouse("老鼠7号");

        Cat cat = new Cat("Tom");
        cat.addObserver(mouse1);
        cat.addObserver(mouse2);
        cat.addObserver(mouse3);
        cat.addObserver(mouse4);
        cat.addObserver(mouse5);
        cat.addObserver(mouse6);
        cat.addObserver(mouse7);
        cat.addObserver(owner);

        cat.Meow();

    }
}
