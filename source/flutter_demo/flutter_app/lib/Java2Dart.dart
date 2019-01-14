import 'dart:math';
///Dart学习笔记http://www.cndartlang.com/user/2
class Bicycle {
  int cadence;
  //未初始化的变量（即使是数字类型的变量）的值都为 null
  //所有名字以下划线开头的变量，Dart 的编译器都会将其强制标记为私有的
  //默认情况下，Dart 会为所有公开的变量提供存取方法，除非你需要提供仅仅可读、可写，或者在某些情况下需要在 getter 方法中进行计算或是在 setter 方法中进行某些值得更新，否则都不需要再重新定义存取方法。
  //在之前 Java 例子中，cadence 和 gear 都有自己的存取方法，而在此例子中，这些实例变量可以直接通过 bike.gear 或者 bike.cadence 访问到。
  //你先直接通过 bike.cadence 来进行实例变量的访问，然后再去通过定义存取方法来进行重构。提供重构之后的 API 和开始直接访问的版本并不会有很大的差别
  int _speed = 0; //之前的 Java 示例中使用了 private 作为声明成员变量私有性的标记，而在 Dart 中并不是这样，增加一个[_speed]只读的变量"中学习更多有关可访问特性的知识
  int get speed => _speed;
  int gear;

  ///添加一个构造函数
  ///这个构造函数没有方法体，这个在 Dart 中是合法的。
  ///如果你在没有方法体的构造函数后忘记书写分号(;) ，DartPad 将会显示一个错误，告诉你"A function body must be provided."
  ///在构造函数的参数中使用 this 可以直接对实例变量进行赋值，不用再编写多余的代码。
  ///这里的代码同下列的代码是相同功效的
//  Bicycle(this.cadence, this._speed, this.gear);

  Bicycle(this.cadence,this.gear);

//  Bicycle(int cadence,int speed,int gear){
//  this.cadence = cadence;
//  this._speed = speed;
//  this.gear =gear;
//  }

  void applayBrake(int decrement){
    _speed -= decrement;
  }

  void speedUp(int increment){
    _speed += increment;
  }

  //修饰符 @override 会告诉分析器你当前是在复写某个成员方法，如果该复写不成功，分析器就会报错
  //Dart 可以使用单引号或者双引号进行字符串的声明。
  //针对只有一行的方法可以使用 => 来简化方法的书写
  @override
  String toString() => 'Bicycle: $speed, candence= $cadence, gear= $gear';//可以在字符串内使用 ${expression} 的方式来实现字符串模板的效果，如果该表达式仅仅是一个标识符，还可以去掉花括号 $variableName
}

class Rectangle{
  int width;
  int height;
  Point origin;

  ///使用可选参数（而不是使用重载）
  //可以直接添加下列的一个没有方法体的构造方法来替代 Java 代码中的四个构造方法
  //this.origin, this.width 和 this.height 使用了 Dart 提供的简便方法来直接对类中的实例变量进行赋值。
  ///this.origin, this.width 和 this.height 嵌套在闭合的花括号中 ({}) ，用来表示它们是可选的命名参数
  //this.origin = const Point(0, 0) 这样的代码表明给实例变量 origin 提供了默认的值 Point(0,0)，
  /// 默认值必须是在编译期就可以确定的常量。上述代码中的构造方法为三个实例变量都提供了默认参数
  Rectangle({this.origin = const Point(0, 0), this.width = 0, this.height = 0});

  @override
  String toString() =>
      'Origin: (${origin.x}, ${origin.y}), width: $width, height: $height';
}

//选项 1：创建一个顶层的方法
//选项2 ：创建一个工厂模式的构造方法
//Dart 支持抽象类
abstract class Shape {

  ///使用 Dart 的 factory 关键字来创建一个工厂模式的构造方法
  factory Shape(String type){
    if(type == 'circle')return Circle(4);
    if(type == 'square')return Square(4);
    throw 'Can\'t create $type';
  }

  num get area;
}

class Circle implements Shape{
  final num radius;

  Circle(this.radius);

  // Dart 1.x 的版本中，核心库中的常量是大写的（比如 PI）；在 Dart 2 的版本中，都是小写的(pi)
  @override
  num get area => pi * pow(radius,2);
}

class Square implements Shape{
  final num side;

  Square(this.side);

  @override
  num get area => pow(side, 2);
}

///接口的实现
///Dart 语言并没有提供 interface 关键字，但是每一个类都隐式地定义了一个接口。
///你将会看到一个"Missing concrete implementations" 的错误，添加两个实例变量 area 和 radius 即可修复这个问题
///虽然 CircleMock 并没有定义任何行为，但是在 Dart 中这是完全合法的，不会有任何报错。
class CircleMock implements Circle{
  num area;
  num radius;
}

///1. 在最外层作用域中（在所有类的作用域之外）实现一个工厂方法。
Shape shapeFactory(String type){
  if(type == 'circle')return Circle(3);
  if(type == 'square')return Square(3);
  throw 'Can\'t create $type';
}

String scream(int length) => "A${'a'* length}h!";


///void main(){} 如果不需要传参数也可以的
///main() 方法是 Dart 的主方法，如果你需要访问命令行传递过来的参数，可以使用 main(List<String> args 方法
///main() 方法存在于最外层的作用域，在 Dart 中你可以在类之外编写代码，变量、方法、存取方法都可以独立于类之外维持生命周期
///无论是 main() 还是 Bicycle 类都声明为 public 的，默认情况下都是 public 的，
///在 Dart 中没有诸如 public、private、protected 这样的关键词
void main(List<String> args) {
  //实例化 并打印bicycle
  var bike = new Bicycle(2, 1); //new关键字是可选的
  final bike1 = Bicycle(2, 1); //如果你确信某个变量的值不会再发生改变，你可以使用 final 来代替 var
  print(bike);

  print(Rectangle(origin: const Point(10, 20), width: 100, height: 200));
  print(Rectangle(origin: const Point(10, 10)));
  print(Rectangle(width: 200));
  print(Rectangle());

  final circle = Circle(2);
  final square = Square(2);
  print("circle.area= ${circle.area}, square.area= ${square.area}");
  final circle1 = shapeFactory('circle');
  ///使用 Dart 的 factory 关键字来创建一个工厂模式的构造方法
  final square1 = Shape('square');
  print("circle1.area= ${circle1.area}, square1.area= ${square1.area}");

  //常规方式
  final values = [1, 2, 3, 5, 10, 50];
  for (var length in values) {
    print(scream(length));
  }
  //将函数当做参数进行传递
  //将函数直接赋值给变量
  //对函数进行解构，只传递给函数一部分参数来调用它，让它返回一个函数去处理剩下的参数（也被称为柯里化）
  //创建一个可以被党作为常量的匿名函数（也被称为 lambda 表达式，在 Java 的 JDK 8 release 中支持了 lambda 表达式）
  //转为函数式
  //会把values里面的每个元素当参数传递给scream(int length)方法调用
  values.map(scream).forEach(print);
  print("=====================");
  values.map(scream).skip(1).take(3).forEach(print);



}