Dart中所有东西都是对象，包括数字、函数等
它们都继承自Object，并且默认值都是null（包括数字）因此数字、字符串都可以调用各种方法

### Dart中支持以下数据类型：

* Numbers
* Strings
* Booleans
* List（也就是数组）
* Maps
容器后面再讲，这里先说说常用的字符串和数值类型
还是先建工程吧！Dart代码如下

```
void main()
{
  //Dart 语言本质上是动态类型语言，类型是可选的
  //可以使用 var 声明变量，也可以使用类型来声明变量
  //一个变量也可以被赋予不同类型的对象
  //但大多数情况，我们不会去改变一个变量的类型

  //字符串赋值的时候，可以使用单引号，也可以使用双引号
  var str1 = "Ok?";

  //如果使用的是双引号，可以内嵌单引号
  //当然，如果使用的是单引号，可以内嵌双引号，否则需要“\”转义
  //String str2 = ‘It\’s ok!’;
  String str2 = "It's ok!";

  //使用三个单引号或者双引号可以多行字符串赋值
  var str3 = """Dart Lang
  Hello,World!""";

  //在Dart中，相邻的字符串在编译的时候会自动连接
  //这里发现一个问题，如果多个字符串相邻，中间的字符串不能为空，否则报错
  //但是如果单引号和双引号相邻，即使是空值也不会报错，但相信没有人这么做
  //var name = 'Wang''''Jianfei'; 报错
  var name = 'Wang'' ''Jianfei';

  //assert 是语言内置的断言函数，仅在检查模式下有效
  //如果断言失败则程序立刻终止
  assert(name == "Wang Jianfei");

  //Dart中字符串不支持“+”操作符，如str1 + str2
  //如果要链接字符串，除了上面诉说，相邻字符串自动连接外
  //还可以使用“$”插入变量的值
  print("Name：$name");

  //声明原始字符串，直接在字符串前加字符“r”
  //可以避免“\”的转义作用，在正则表达式里特别有用
  print(r"换行符：\n");

  //Dart中数值是num，它有两个子类型：int 和 double
  //int是任意长度的整数，double是双精度浮点数
  var hex = 0xDEADBEEF;

  //翻了半天的文档，才找打一个重要的函数：转换进制，英文太不过关了
  //上面提到的字符串插值，还可以插入表达式：${}
  print("整型转换为16进制：$hex —> 0x${hex.toRadixString(16).toUpperCase()}");

}
```
注：新版本SDK已支持“+”操作符连接字符串

运行结果如下：


String和num有丰富的函数，这里就不一一介绍了，大家可以慢慢尝试

再聊点`const`和`final`，用法和其他语言类似
在声明变量的时候，除了`var`，还可以使用`const`和`final`
同时，在使用`const`和`final`的时候，可以省略`var`或者其他类型
```
var i = 10；
const i = 10；
final i = 10；
```
```
int i = 10；
const int i = 10；
final int i = 10；
```
`const`和`final`定义的都是常量，值不能改变
并且在声明的时候就必须初始化
但是也有细微差别，简单来说

`const`定义的是编译时常量，只能用编译时常量来初始化
`final`定义的常量可以用变量来初始化
```
final time = new DateTime.now(); //Ok
const time = new DateTime.now(); //Error，new DateTime.now()不是const常量
```
`var`、`final`等在左边定义变量的时候，并不关心右边是不是常量
但是如果右边用了`const`，那么不管左边如何，右边都必须是常量
```
const list = const[1,2,3];//Ok
const list = [1,2,3];//Error

final list = [1,2,3];//Ok
final list = const[1,2,3];//Ok
final list = const[new DateTime.now(),2,3];//Error,const右边必须是常量
```