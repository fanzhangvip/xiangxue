在Dart中，你可以导入一个库来使用它所提供的功能
库的使用可以使代码的重用性得到提高，并且可以更好的组合代码
当然，你也可以自己定义一个库

Dart中任何文件都是一个库，即使你没有用关键字library声明

### 1、import
```import```语句用来导入一个库
后面跟一个字符串形式的Uri来指定表示要引用的库
```
//dart:前缀表示Dart的标准库，如dart:io、dart:html
import 'dart:math';

//当然，你也可以用相对路径或绝对路径的dart文件来引用
import 'lib/student/student.dart';

//Pub包管理系统中有很多功能强大、实用的库，可以使用前缀 package:
import 'package:args/args.dart';
```
当各个库有命名冲突的时候，可以使用```as```关键字来使用命名空间
```
import 'lib/student/student.dart' as Stu;

Stu.Student s = new Stu.Student();
```
show关键字可以显示某个成员（屏蔽其他）
hide关键字可以隐藏某个成员（显示其他）
```
import 'lib/student/student.dart' show Student, Person;

import 'lib/student/student.dart' hide Person;
```
### 2、library
library定义这个库的名字
但库的名字并不影响导入，因为import语句用的是字符串Uri
```
library person;
```
### 3、part和part of
为了维护一个库，我们可以把各个功能放到各个dart文件中
但part of所在文件不能包括import、library等关键字
可以包含在part关键字所在文件中

建议避免使用part和part of语句，因为那样会使代码很难阅读、修改，可以多用library
part加字符串类型的Uri类似include，表示包含某个文件
part of加库名表示该文件属于那个库

// math.dart文件开头
library math;
part 'point.dart';
part 'random.dart';

// point.dart文件开头
part of math;

// random.dart文件开头
part of math;
4、export
你可以使用export关键字导出一个更大的库

library math;

export 'random.dart';
export 'point.dart';
也可以导出部分组合成一个新库

library math;

export 'random.dart' show Random;
export 'point.dart' hide Sin;
5、利用Pub管理自己的库
在第一小节中介绍了，可以用以下方法引用自己的库

import 'lib/student/student.dart';
那么如果我想要用Pub来管理自己的库
以便引用简洁，并且可以在yaml文件中很好的控制版本信息该怎么部署？
下面简单总结一下

（1）新建库student
其中根目录的命名为”库名-版本号“
根目录中包含一个yaml文件和一个lib文件夹


pubspec.yaml


lib文件夹中有一个dart文件，命名为”库名.dart”，以及一个src文件夹


student.dart

library student;

export 'src/person.dart';
export 'src/student.dart';
在src文件夹中，包含两个dart文件


person.dart

library student.src.person;

class Person {
    String name;

    Person(this.name);

    String toString() => name;
}
student.dart

library student.src.student;

import 'person.dart';

class Student extends Person {
    Student(String name):super(name);

    String id = '';

    String toString() => 'Name:$name  ID:$id';
}
（2）在其他项目中配置方法
main.dart

import 'dart:math';
import 'package:args/args.dart';
import 'package:student/student.dart';

void main(List<String> args) {
    Point point = new Point(0, 0);
    print(point);

    var parser = new ArgParser();
    var results = parser.parse(args);
    print(results.arguments);

    Student student = new Student('Wang');
    student.id = '060806110006';
    print(student);
}
pubspec.yaml中添加student库，并连接库的路径


当配置好yaml后，目录树中就可以看到新添加的包以及版本号了


看一下运行结果



这里只是理了一下库的引用方法，没有放到Pub\Cache\hosted\pub.dartlang.org文件夹中
如果放到缓存文件夹中，yaml设置hosted，可以更好的控制版本