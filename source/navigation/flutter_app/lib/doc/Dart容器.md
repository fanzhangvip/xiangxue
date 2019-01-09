因为各种原因，Dart配置起来各种不顺
Pub的初衷应该是好的
但是鉴于网络环境，运行Web应用的时候各种受阻
身心疲惫
后面会提到配置方面的问题

这里说一下容器，重要性就不谈了

### 1、StringBuffer
先声明一下，StringBuffer并不是容器
但还是放到了这一章
按照官方的说法，StringBuffer可以特别高效的构建多个字符串
另外，以前的书中用的add方法已经改为write
```
StringBuffer sb = new StringBuffer();

sb.write("Use a StringBuffer ");
sb.writeAll(['for ', 'efficient ', 'string ', 'creation ']);
sb..write('if you are ')..write('building lots of string.');

print(sb.toString());

sb.clear();
```
### 2、List
列表，也就是常说的数组
常见的添加、索引、删除等方法如下
```
// 使用List的构造函数，也可以添加int参数，表示List固定长度
var vegetables = new List();

// 或者简单的用List来赋值
var fruits = ['apples', 'oranges'];

// 添加元素
fruits.add('kiwis');

// 添加多个元素
fruits.addAll(['grapes', 'bananas']);

// 获取List的长度
assert(fruits.length == 5);

// 利用索引获取元素
assert(fruits[0] == 'apples');

// 查找某个元素的索引号
assert(fruits.indexOf('apples') == 0);

// 利用索引号删除某个元素
var appleIndex = fruits.indexOf('apples');
fruits.removeAt(appleIndex);
assert(fruits.length == 4);

// 删除所有的元素
fruits.clear();
assert(fruits.length == 0);
```
你可以使用sort()对List的元素进行排序
并且必须制定比较两个对象的函数，函数的返回值中
**return <0 表示小于，0表示相同，>0表示大于**
```
var fruits = ['bananas', 'apples', 'oranges'];

fruits.sort((a, b) => a.compareTo(b));
assert(fruits[0] == 'apples');
```
List以及其他的容器可以指定参数类型
```
// 下面的List只能包含String
var fruits = new List<String>();

fruits.add('apples');
var fruit = fruits[0];
assert(fruit is String);

fruits.add(5); // 错误: 在checked mode中会抛出异常
```
### 3、Set
集合在Dart中无序，并且每个元素具有唯一性
因为它是无序的，因此你不能像List那样用索引来访问元素
```
var ingredients = new Set();

ingredients.addAll(['gold', 'titanium', 'xenon']);
assert(ingredients.length == 3);

// 添加已存在的元素无效
ingredients.add('gold');
assert(ingredients.length == 3);

// 删除元素
ingredients.remove('gold');
assert(ingredients.length == 2);

// 检查在Set中是否包含某个元素
assert(ingredients.contains('titanium'));

// 检查在Set中是否包含多个元素
assert(ingredients.containsAll(['titanium', 'xenon']));
ingredients.addAll(['gold', 'titanium', 'xenon']);

// 获取两个集合的交集
var nobleGases = new Set.from(['xenon', 'argon']);
var intersection = ingredients.intersection(nobleGases);
assert(intersection.length == 1);
assert(intersection.contains('xenon'));

// 检查一个Set是否是另一个Set的子集
var allElements = ['hydrogen', 'helium', 'lithium', 'beryllium',
'gold', 'titanium', 'xenon'];
assert(ingredients.isSubsetOf(allElements));
```
### 4、Map
映射，也有人称之为字典
Map是一个无序的键值对容器
```
// Map的声明
var hawaiianBeaches = {
    'oahu' : ['waikiki', 'kailua', 'waimanalo'],
    'big island' : ['wailea bay', 'pololu beach'],
    'kauai' : ['hanalei', 'poipu']
};
var searchTerms = new Map();

// 指定键值对的参数类型
var nobleGases = new Map<int, String>();

// Map的赋值，中括号中是Key，这里可不是数组
nobleGase[54] = 'dart';

//Map中的键值对是唯一的
//同Set不同，第二次输入的Key如果存在，Value会覆盖之前的数据
nobleGases[54] = 'xenon';
assert(nobleGases[54] == 'xenon');

// 检索Map是否含有某Key
assert(nobleGases.containsKey(54));

//删除某个键值对
nobleGases.remove(54);
assert(!nobleGases.containsKey(54));
```
你可以用getKeys和getValues获取所有Key或者所有Values的迭代器
```
var keys = hawaiianBeaches.getKeys();
assert(keys.length == 3);
assert(new Set.from(keys).contains('oahu'));

var values = hawaiianBeaches.getValues();
assert(values.length == 3);

//迭代器中有一个有意思的函数any，用来检测迭代器中的数据
//当其中一个元素运行函数时return true，那么any的返回值就为true，否则为false
//与之相对的是函数every，要所有函数运行函数return true，那么every返回true
assert(values.any((v) => v.indexOf('waikiki') != -1));

// 你可以用foreach来遍历数据，但记住它是无序的
hawaiianBeaches.forEach((k,v) {
print('I want to visit $k and swim at $v');
});

//检索是否包含某个Key或Value
assert(hawaiianBeaches.containsKey('oahu'));
assert(!hawaiianBeaches.containsKey('florida'));

//V putIfAbsent(K key, Function V ifAbsent())函数，通过Key来查找Value
//当某个Key不存在的时候，会执行第二参数的Function来添加Value
var teamAssignments = {};
teamAssignments.putIfAbsent('Catcher', () => 'Catcher'.length);
assert(teamAssignments['Catcher'] != null);
```
5、迭代
Set、List、Map都继承自Iterable，是可以迭代的
```
//如果迭代的对象是容器，那么可以使用forEach或者for-in
var collection = [0, 1, 2];

collection.forEach((x) => print(x));//forEach的参数为Function

for(var x in collection) {
    print(x);
}
```