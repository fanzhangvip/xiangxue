Dart是一门使用类和单继承的面向对象语言
所有的对象都是类的实例，并且所有的类都是Object的子类

### 1、定义
类的定义用```class```关键字
如果未显式定义构造函数，会默认一个空的构造函数
使用```new```关键字和构造函数来创建对象
```
class Point {
  num x;
  num y;
  num z;
}

void main() {
  var point = new Point();
  print(point.hasCode);//未定义父类的时候，默认继承自Object
}
```
### 2、构造函数
如果只是简单的参数传递，可以在构造函数的参数前加this关键字定义
或者参数后加 : 再赋值
```
class Point {
    num x;
    num y;
    num z;

    Point(this.x, this.y, z) { //第一个值传递给this.x，第二个值传递给this.y
            this.z = z;
    }

    Point.fromeList(var list): //命名构造函数，格式为Class.name(var param)
            x = list[0], y = list[1], z = list[2]{//使用冒号初始化变量
    }

    //当然，上面句你也可以简写为：
    //Point.fromeList(var list):this(list[0], list[1], list[2]);

     String toString() => 'x:$x  y:$y  z:$z';
}

void main() {
    var p1 = new Point(1, 2, 3);
    var p2 = new Point.fromeList([1, 2, 3]);
    print(p1);//默认调用toString()函数
}
```
如果你要创建一个不可变的对象，你可以定义编译时常量对象
需要在构造函数前加const
```
class ImmutablePoint {
    final num x;
    final num y;
    const ImmutablePoint(this.x, this.y); // 常量构造函数
    static final ImmutablePoint origin = const ImmutablePoint(0, 0); // 创建一个常量对象不能用new，要用const
}
```
### 3、Getters And Setters
Getter和Setter是用来读写一个对象属性的方法
每个字段都对应一个隐式的Getter和Setter
但是调用的时候是obj.x，而不是obj.x()

你可以使用```get和set```关键字扩展功能
如果字段为```final或者const```的话，那么它只有一个getter方法
```
class Rectangle {
    num left;
    num top;
    num width;
    num height;

    Rectangle(this.left, this.top, this.width, this.height);

    // right 和 bottom 两个属性的计算方法
    num get right => left + width;
    set right(num value) => left = value - width;
    num get bottom => top + height;
    set bottom(num value) => top = value - height;
}

main() {
    var rect = new Rectangle(3, 4, 20, 15);
    assert(rect.left == 3);
    rect.right = 12;
    assert(rect.left == -8);
}
```
### 4、抽象类
在Dart中类和接口是统一的，类就是接口
如果你想重写部分功能，那么你可以继承一个类
如果你想实现某些功能，那么你也可以实现一个类

使用```abstract```关键字来定义抽象类，并且抽象类不能被实例化
抽象方法不需要关键字，直接以分号 ; 结束即可
```
abstract class Shape { // 定义了一个 Shape 类/接口
    num perimeter(); // 这是一个抽象方法，不需要abstract关键字，是隐式接口的一部分。
}

class Rectangle implements Shape { // Rectangle 实现了 Shape 接口
    final num height, width;
    Rectangle(num this.height, num this.width);  // 紧凑的构造函数语法
    num perimeter() => 2*height + 2*width;       // 实现了 Shape 接口要求的 perimeter 方法
}

class Square extends Rectangle { // Square 继承 Rectangle
    Square(num size) : super(size, size); // 调用超类的构造函数
}
```
### 5、工厂构造函数
```Factory```单独拿出来讲，因为这不仅仅是构造函数，更是一种模式
有时候为了返回一个之前已经创建的缓存对象，原始的构造方法已经不能满足要求
那么可以使用工厂模式来定义构造函数
并且用关键字new来获取之前已经创建的缓存对象
```
class Logger {
    final String name;
    bool mute = false;

    // 变量前加下划线表示私有属性
    static final Map<String, Logger> _cache = <String, Logger>{};

    factory Logger(String name) {
        if (_cache.containsKey(name)) {
            return _cache[name];
        } else {
            final logger = new Logger._internal(name);
            _cache[name] = logger;
            return logger;
        }
    }

    Logger._internal(this.name);

    void log(String msg) {
        if (!mute) {
            print(msg);
        }
    }
}

var logger = new Logger('UI');
logger.log('Button clicked');
 ```


### 补充一下实现和继承

1. **实现的关键字为implements**
2. **继承的关键字为extends**
```
abstract class Person {  //此时abstract关键字可加可不加，如果加上的话Person不能被实例化
    String greet(who); //函数可以没有实现语句，名曰隐式接口，前面不用加 abstract 关键字
}
```
当使用 implements的时候，子类Student无法访问父类Person的参数
所以变量name放到了Student
如果生成了一个Student的实例，调用了某函数，那么Student中必须有那个函数的实现语句，无论Person是否实现
```
class Student implements Person {
    String name;
    Student(this.name);

    String greet(who) => 'Student: I am $name!';
}

class Teacher implements Person {
    String name;
    Teacher(this.name);

    String greet(who) => 'Teacher: I am $name!';
}

void main() {
    Person student = new Student('Wang');
    Person teacher = new Teacher('Lee');

    print( student.greet('Chen'));
    print(teacher.greet('Chen'));
}
```
我感觉用implements的时候，父类就感觉是C++中的纯虚函数
可以试一下把Person前的abstract关键字去掉
然后添加构造函数和方法，对Student和Teacher没有影响
但那样的话Student和Teacher的name变量仍然不能省略，那样感觉就冗余了
根据您自己的情况修改吧！
总之，implements可以是实现多个类的多个函数

相比之下继承就好理解得多
Dart中是单继承，子类可以继承父类的非私有变量
当重写某个函数的时候
不用考虑abstract或者接口或者函数实现
直接重写，比如greet()函数
而且仍然保持多态性
```
class Person {
  String name;

  Person(this.name);
  String greet(who) => 'I am $name!';
}

class Student extends Person {
  Student(String name):super(name);
  String greet(who) => 'Student: I am $name!';
}

class Teacher extends Person {
  Teacher(String name):super(name);
     String greet(who) => 'Teacher: I am $name!';
}

void main() {
   Person p1 = new Student('Wang');
   Person p2 = new Teacher('Lee');

   print(p1.greet('Chen'));
   print(p2.greet('Chen'));
}
```