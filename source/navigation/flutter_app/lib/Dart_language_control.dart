class Person {
  String name;
  String country;

  void setCountry(String country) {
    this.country = country;
  }

  @override
  String toString() => 'Name:$name\nCountry:$country';
}

int finallyMethodTest(int i) {
  if (i == 0) {
    return i;
  } else {
    throw 'This a Exception! finally test';
  }
}

///try-catch-finally-return执行顺序
String finallyTest(int i) {
  try {
    var itest = finallyMethodTest(i);
    if(itest== 0){//finally里面的return会覆盖这里的结果？？？
      return "finallyTest i = $i";
    }
  }
  catch (e) {
    //可以试试删除catch语句，只用try...finally是什么效果
    ///结构 没得catch 不会报错 直接执行finally的语句
    print('Catch Exception finally test: $e');
    return "catch finally test";
  }
  finally {
    print('Close');
    return " finally test";
  }
}

main() {
  /// 取整 ~/
  int a = 3;
  int b = 2;
  print(a ~/ b);

  ///级联
  Person p = Person();
  p
    ..name = "Fan"
    ..setCountry("China");
  print(p);

  ///if语句
  var i = 0;
  if (i < 0) {
    print('i < 0');
  } else if (i == 0) {
    print('i = 0');
  } else {
    print('i > 0');
  }

  ///循环 for forEach  for-in
  for (int i = 0; i < 3; i++) {
    print(i);
  }

  var collection = [0, 1, 2, 3];
  collection.forEach((x) => print(x)); //forEach的参数为Function

  for (var x in collection) {
    print(x);
  }

  ///while do-while
  bool boolean = true;
  var count = 0;
  while (boolean) {
    //do something
    count++;
    if (count == 5) break;
  }

  do {
    //do something
    count--;
    if (count == 0) break;
  } while (boolean);

  /// Switch And Case
  /// swith的参数可以是num，或者String
  var command = 'OPEN';
  switch (command) {
    case 'CLOSED':
      break;
    case 'OPEN':
      break;
    default:
      print("Default");
  }

  ///如果分句的内容为空，想要fall-through（落空），可以省略break
  ///如果分句的内容不为空，那么必须加break，否则抛出异常
  switch (command) {
    case 'CLOSED':
      print('CLOSED');
      break;
    case 'OPEN': //产生落空效果，执行下一分句
    case 'NOW_OPEN':
      print('OPEN');
      break;
    default:
      print('Default');
  }

  ///如果你想要fall-through，case语句内容又不为空
  ///而又不是按顺序落空，那么可以使用continue和标签
  switch (command) {
    case 'CLOESED':
      print('CLOSED');
      continue nowClosed; // Continues executing at the nowClosed label.
    case 'OPEN':
      print('OPEN');
      break;
    nowClosed: // Runs for both CLOSED and NOW_CLOSED.
    case 'NOW_CLOSED':
      print('NOW_CLOSED');
      break;
  }

  ///异常 在Dart中可以抛出非空对象（不仅仅是Exception或Error）作为异常
  ///你可以抛出多个类型的Exception，但是由第一个catch到的分句来处理
  ///如果catch分句没有指定类型，那么它可以处理任何类型的异常
//  throw new Exception('值必须大于0');
//  throw "值必须大于0呀";

  try {
    throw 'This a Exception!';
  } on Exception catch (e) {
    print('Unknown exception: $e');
  } catch (e) {
    print('Unknown type: $e');
  }

  ///无论是否发生异常，为了使某些代码得以运行，可以使用finally语句
//  try {
//    throw 'This a Exception! finally test';
//    return "test";
//  } catch (e) {
//    //可以试试删除catch语句，只用try...finally是什么效果
//    print('Catch Exception finally test: $e');
//    return "catch finally test";
//  } finally {
//    print('Close');
//    return " finally test";
//  }
print("==========================异常捕获测试======================");
  var test = finallyTest(0);
  print("test = $test");
print("------------------------------------------------------------");
  var test1 = finallyTest(1);
  print("test1 = $test1");

}
