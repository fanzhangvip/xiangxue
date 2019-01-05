### 1、取整
取整~/操作符之前可能很少看到，代码如下：
```
int a = 3；
int b = 2；
print(a~/b);//输出1
```
### 2、级联
当你要对一个单一的对象进行一系列的操作的时候
可以使用级联操作符 ..
```
class Person {
    String name;
    String country;
    void setCountry(String country){
      this.country = country;
    }
    String toString() => 'Name:$name\nCountry:$country';
}
void main() {
  Person p = new Person();
  p ..name = 'Wang'
    ..setCountry('China');
  print(p);
}
```
### 3、If语句
If语句的判断条件为bool值，用法和大多语言一样
```
if(i<0){
  print('i < 0');
}else if(i ==0){
  print('i = 0');
} else {
  print('i > 0');
}
```
在checked模式下，如果是非bool值会抛出异常
而在production模式下，非bool值会被翻译成false
```
if (1) {
  print('JavaScript is ok.');
} else {
  print('Dart in production mode is ok.');
}
```
在以前出版的书中有段代码
```
if (?device) { // Returns true if the caller specified the parameter.
//...The user set the value. Do something with it...
}
```

 大概意思是说判断是否传入参数
但试了各种情况，这样写的话都会报错，不知道是不是已经过时的原因
建议判断是否传入参数的时候用 ```device == null ```来替换
### 4、循环
```
for(int i = 0; i<3; i++) {
  print(i);
}
```
如果迭代的对象是容器，那么可以使用forEach或者for-in
```
var collection = [0, 1, 2];

collection.forEach((x) => print(x));//forEach的参数为Function
for(var x in collection) {
  print(x);
}
```
另外，while和do-while没有什么变化
```
while(boolean){
  //do something
}

do{
  //do something
} while(boolean)
```
### 5、Switch And Case
swith的参数可以是num，或者String
```
var command = 'OPEN';
switch (command) {
  case 'CLOSED':
    break;
  case 'OPEN':
    break;
  default:
    print('Default');
}
```
这里有一点要注意，如果分句的内容为空，想要fall-through（落空），可以省略break
如果分句的内容不为空，那么必须加break，否则抛出异常
```
var command = 'OPEN';
switch (command) {
  case 'CLOSED':
    print('CLOSED');
    break;
  case 'OPEN'://产生落空效果，执行下一分句
  case 'NOW_OPEN':
    print('OPEN');
    break;
  default:
    print('Default');
}
```
如果你想要fall-through，case语句内容又不为空
而又不是按顺序落空，那么可以使用```continue```和```标签```
```
var command = 'CLOSED';
  switch (command) {
    case 'CLOSED':
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
  ```
### 6、异常处理
在Dart中可以抛出非空对象（不仅仅是Exception或Error）作为异常

```throw new ExpectException('值必须大于0！');```

```throw '值必须大于0!';```
* 你可以抛出多个类型的Exception，但是由第一个catch到的分句来处理
如果catch分句没有指定类型，那么它可以处理任何类型的异常
```
try {
      throw 'This a Exception!';
  } on Exception catch(e) {
    print('Unknown exception: $e');
  } catch(e) {
    print('Unknown type: $e');
  }
  ```
无论是否发生异常，为了使某些代码得以运行，可以使用finally语句
```
try {
      throw 'This a Exception!';
  } catch(e) {//可以试试删除catch语句，只用try...finally是什么效果
    print('Catch Exception: $e');
  } finally {
    print('Close');
  }
```