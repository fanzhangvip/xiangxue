String sayHello(String name){
  return 'Hello $name!';
}



//or
sayHello1(name){
  return 'Hello $name!';
}

//or
//如果函数只是简单的返回一个表达式的值，可以使用箭头语法 =>expr;
//它等价于{return expr;}
sayHello2(name) =>  'Hello $name!';

//or
// (name)=>’Hello $name!’;
var sayHello3 = (name) =>  'Hello $name!';

///函数别名
typedef int Add(int a,int b);

int Substract(int a,int b) => a - b;

//函数闭包
//初始化变量的时候，参数对应的是函数的参数num n
//调用函数类型变量的时候，参数对应的是返回值中的参数num i
Function makeSubstract(num n)
{//n = 5, i= 2 所以 n - i = 3
  return (num i) => n - i;//返回的是一个函数闭包
}

//选参数
//Dart中支持两种可选参数：命名可选参数和位置可选参数
//但两种可选不能同时使用
//
//命名可选参数使用大括号{}，默认值用冒号:
//位置可选参数使用方括号[]，默认值用等号=

//在命名可选参数的函数中，大括号外的a为必填参数
//大括号内的参数可以指定0个或多个
//并且与顺序无关，在调用函数的时候需要指明参数名
//没有赋值的参数值为null
FunX(a, {b, c:3, d:4, e})
{
  print('$a $b $c $d $e');
}

//在位置可选参数的函数中，大括号内的参数可以指定0个或多个
//在调用的时候参数值会依次按顺序赋值
FunY(a, [b, c=3, d=4, e])
{
  print('$a $b $c $d $e');
}


main(){
  //assert()添加了第二个参数message，用于在抛出异常的时候，输出具体信息
  assert(sayHello is Function,"sayHello is Function");
  print(Substract is Function);
  print(Substract is Add);

  var x = makeSubstract(5);// x = {_Closure}Closure: (num) => num
  print("x=$x， x(2)= ${x(2)}");//x(2) = (2) => 5 -2

  var callbacks = [];
  for(var i = 0; i < 3; i++){
    // 在列表 callbacks 中添加一个函数对象，这个函数会记住 for 循环中当前 i 的值。
    callbacks.add(() => print("Save $i"));
  }
  callbacks.forEach((e) => e());

  FunX(1, b:3, d:5);
  FunY(1, 3, 5);
}