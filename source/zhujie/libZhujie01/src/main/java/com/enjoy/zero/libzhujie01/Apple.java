package com.enjoy.zero.libzhujie01;

/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询伊娜老师QQ 2133576719
 * 类说明:注解使用
 */
public class Apple {

    @FruitName("Apple")
    private String name;

    @FruitColor(fruitColor = FruitColor.Color.RED)
    private String color;

    @FruitProducerFactory(id = 1, name = "小苹果", address = "长沙市")
    private String producer;

    public Apple() {
    }

    public Apple(String name, String color, String producer) {
        this.name = name;
        this.color = color;
        this.producer = producer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    @Override
    public String toString() {
        return "Apple{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", producer='" + producer + '\'' +
                '}';
    }
}
