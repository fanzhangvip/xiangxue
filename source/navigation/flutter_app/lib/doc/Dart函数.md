### 1、函数定义
首先，函数也是对象，当没有指定返回值的时候，函数返回null
函数定义方法如下：
```
String sayHello(String name)
{
  return 'Hello $name!';
}
```
//is  is!操作符判断对象是否为指定类型，如num、String等
```assert(sayHello is Function);```
> 断言函数assert()，Debug模式下，当表达式的值为false时抛出异常。
> 在SDK 1.22.0中，assert()添加了第二个参数message，用于在抛出异常的时候，输出具体信息。
> 当然，因为Dart中的类型是可选的，也可以这样写
```
sayHello(name)
{
  return 'Hello $name!';
}
```
不过建议明确函数的输入类型和返回类型
方便修改，也方便阅读
如果函数只是简单的返回一个表达式的值，可以使用箭头语法 =>expr;
它等价于{return expr;}
所以上面的函数也可以这样写

```sayHello(name) => 'Hello $name!';```
Dart中匿名函数的写法 (name)=>’Hello $name!’;
于是可以如下定义匿名函数

```var  sayHello = (name)=>'Hello $name!';```
### 2、函数别名
当看到用typedef定义函数别名的时候，不自觉的想到了函数指针
```
typedef int Add(int a, int b);
int Subtract(int a, int b) => a - b;

void main()
{
  print(Substract is Function);
  print(Substract is Add);
}
```
上面代码的命名感觉有点误导人的感觉
如果两个函数的参数和返回值都一样
那么is操作符就会返回true

### 3、函数闭包
下面的代码有一点需要注意
初始化变量的时候，参数对应的是函数的参数num n
调用函数类型变量的时候，参数对应的是返回值中的参数num i
```
Function makeSubstract(num n)
{
  return (num i) => n - i;
}

void main()
{
  var x = makeSubstract(5);
  print(x(2));
}
```
下面这段很有意思的代码
需要反应过来一件事：Dart中函数也是对象
下面的代码应该看得明白，之后会讲控制语句以及容器
```
var callbacks = [];
for (var i = 0; i < 3; i++) {
  // 在列表 callbacks 中添加一个函数对象，这个函数会记住 for 循环中当前 i 的值。
  callbacks.add(() => print('Save $i'));
}
callbacks.forEach((c) => c()); // 分别输出 0 1 2
```
### 4、可选参数
Dart中支持两种可选参数：命名可选参数和位置可选参数
但两种可选不能同时使用

命名可选参数使用大括号{}，默认值用冒号:
位置可选参数使用方括号[]，默认值用等号=
 在SDK 1.21.0中，允许使用操作符 = 或 : 设置命名可选参数的默认值
在命名可选参数的函数中，大括号外的a为必填参数
大括号内的参数可以指定0个或多个
并且与顺序无关，在调用函数的时候需要指明参数名
没有赋值的参数值为null
```
FunX(a, {b, c:3, d:4, e})
{
  print('$a $b $c $d $e');
}
```
在位置可选参数的函数中，大括号内的参数可以指定0个或多个
在调用的时候参数值会依次按顺序赋值
```
FunY(a, [b, c=3, d=4, e])
{
  print('$a $b $c $d $e');
}


void main()
{
  FunX(1, b:3, d:5);
  FunY(1, 3, 5);
}
```