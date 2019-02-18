class Point {
  num x;
  num y;
  num z;

  ///构造函数
  Point(this.x, this.y, z) {
    /////第一个值传递给this.x，第二个值传递给this.y
    this.z = z;
  }

  ///命名构造函数，格式为Class.name(var param)
  Point.fromeList(var list)
      : x = list[0],
        y = list[1],
        z = list[2] {
    ///使用冒号初始化变量
  }

  ///当然，上面句你也可以简写为：
  Point.fromeList1(var list) : this(list[0], list[1], list[2]);

  String toString() => 'x:$x  y:$y  z:$z';
}

class ImmutablePoint {
  final num x;
  final num y;

  const ImmutablePoint(this.x, this.y); //常量构造函数
  static final ImmutablePoint origin =
      const ImmutablePoint(0, 0); // 创建一个常量对象不能用new，要用const
}

class Rectangle {
  ///你可以使用get和set关键字扩展功能
  ///如果字段为final或者const的话，那么它只有一个getter方法
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

///抽象类
///在Dart中类和接口是统一的，类就是接口
///如果你想重写部分功能，那么你可以继承一个类
///如果你想实现某些功能，那么你也可以实现一个类
///使用abstract关键字来定义抽象类，并且抽象类不能被实例化
///抽象方法不需要关键字，直接以分号 ; 结束即可
abstract class Shape {
  // 定义了一个 Shape 类/接口
  num perimeter(); // 这是一个抽象方法，不需要abstract关键字，是隐式接口的一部分。
}

class Rectangle1 implements Shape {
  // Rectangle 实现了 Shape 接口
  final num height, width;

  Rectangle1(this.height, this.width); // 紧凑的构造函数语法
  num perimeter() => 2 * height + 2 * width; // 实现了 Shape 接口要求的 perimeter 方法
}

class Square extends Rectangle1 {
  // Square 继承 Rectangle
  Square(num size) : super(size, size); // 调用超类的构造函数
}

///工厂构造函数
///Factory单独拿出来讲，因为这不仅仅是构造函数，更是一种模式
///有时候为了返回一个之前已经创建的缓存对象，原始的构造方法已经不能满足要求
///那么可以使用工厂模式来定义构造函数
///并且用关键字new来获取之前已经创建的缓存对象
class Logger{
  final String name;
  bool mute = false;

  // 变量前加下划线表示私有属性
  static final Map<String,Logger> _cache = <String,Logger>{};

  factory Logger(String name){
    if(_cache.containsKey(name)){
      return _cache[name];
    }else{
      final logger = new Logger._internal(name);
      _cache[name] = logger;
      return logger;
    }
  }

  Logger._internal(this.name);

  void log(String msg){
    if(!mute){
      print(msg);
    }
  }
}

///实现的关键字为implements 当使用 implements的时候，子类Student无法访问父类Person的参数 用implements的时候，父类就感觉是C++中的纯虚函数
///继承的关键字为extends Dart中是单继承，子类可以继承父类的非私有变量
abstract class Person { //此时abstract关键字可加可不加，如果加上的话Person不能被实例化
  String greet(who); //函数可以没有实现语句，名曰隐式接口，前面不用加 abstract 关键字
}

abstract class Action{//不加abstract action()需要有方法体
   action();
}

class Anim{
  String type = "Anim";
}

class Student with MixinClass implements Person,Action,Anim {
  String name;
  Student(this.name);

  @override
  String greet(who) {
    return 'Student: I am $name!,and ${action()},type=$type';
  }

  @override
  action() {
    print("I am do Study");
  }

  @override
  String type = "Stu";

}

class MixinClass{
  String greet(who) {
    return ' with class ${who}';
  }
}

class Teacher extends Anim implements Person{
  String name;
  Teacher(this.name);

  String type = "Teach";

  String greet(who) => 'Teacher: I am $name and ${action()},type=$type';

  @override
  action() {
    print("I am do teach");
  }
}

void main() {
//  var point = new Point(1, 2, 3);
//  print(point.hashCode); //未定义父类的时候，默认继承自Object
//
//  var p2 = Point.fromeList([1, 2, 3]);
//  print(p2);
//
//  var rect = Rectangle(3, 4, 20, 15);
//  assert(rect.left == 3);
//  rect.right = 12;
//  assert(rect.left == -8);
//
//  var logger = Logger("UI");
//  logger.log("Button clicked");
//
//  Person p3 = new Student('Wang');
//  Person p4 = Teacher('Lee');

  Student s1 = Student("Fan");
//  var t1 = Teacher("Zheng");
//  print(p3.greet("Chen"));
//  print(p4.greet('Chen'));
  print(s1.greet('Chen'));
//  print(t1.greet('Chen'));

  print( "s1 is Person ${Student is Person}, s1 is MixinClass ${Student is MixinClass}");
}
