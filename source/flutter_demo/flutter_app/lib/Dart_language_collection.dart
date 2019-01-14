main() {
  ///StringBuffer可以特别高效的构建多个字符串
  StringBuffer sb = new StringBuffer();

  sb.write("Use a StringBuffer ");
  sb.writeAll(['for', 'efficient', 'string', 'creation']);
  sb..write('if you are')..write('building lots of string.');

  print(sb.toString());
  sb.clear();

  ///List
  // 使用List的构造函数，也可以添加int参数，表示List固定长度
  var vegetables = List();

  //或者简单的用List来赋值
  var fruits = ['apples', 'oranges'];

  assert(fruits is List);

  // 添加元素
  fruits.add('kiwis');

  // 添加多个元素
  fruits.addAll(['grapes', 'bananas']);

  //获取list的长度
  print('fruits.length = ${fruits.length}');

  // 利用索引获取元素
  print("fruits[0]=${fruits[0]}");

  // 查找某个元素的索引号
  print("fruits.indexOf('apples')=${fruits.indexOf('apples')}");

  // 利用索引号删除某个元素
  var appleIndex = fruits.indexOf('apples');
  fruits.removeAt(appleIndex);
  assert(fruits.length == 4);

  // 删除所有的元素
  fruits.clear();
  assert(fruits.length == 0);

  ///使用sort()对List的元素进行排序
  ///并且必须制定比较两个对象的函数，函数的返回值中
  ///return <0 表示小于，0表示相同，>0表示大于
  var fruits1 = ['bananas', 'apples', 'oranges'];
  fruits1.sort((a, b) => a.compareTo(b));
  assert(fruits1[0] == 'apples');

  ///List以及其他的容器可以指定参数类型
  var fruits2 = List<String>();
  fruits2.add('apples');
  var fruit = fruits2[0];
  assert(fruit is String);

//  fruits2.add(5);// 错误: 在checked mode中会抛出异常

  ///Set 集合在Dart中无序，并且每个元素具有唯一性
  ///因为它是无序的，因此你不能像List那样用索引来访问元素
  var set1 = Set();
  set1.addAll(['gold', 'titanium', 'xenon']);
  assert(set1.length == 3);

  //添加已存在的元素无效
  set1.add('gold');
  assert(set1.length == 3);

  //删除元素
  set1.remove('gold');
  assert(set1.length == 2);

  // 检查在Set中是否包含某个元素
  assert(set1.contains('titanium'));

  // 检查在Set中是否包含多个元素
  assert(set1.containsAll(['titanium', 'xenon']));
  set1.addAll(['gold', 'titanium', 'xenon']);

  // 获取两个集合的交集
  var set2 = Set.from(['xenon', 'argon']);
  var set3 = set1.intersection(set2);
  assert(set3.length == 1);
  assert(set3.contains('xenon'));

  // 检查一个Set是否是另一个Set的子集
  var set4 = [
    'hydrogen',
    'helium',
    'lithium',
    'beryllium',
    'gold',
    'titanium',
    'xenon'
  ];
//  assert(set1.isSubsetOf(set4));//这个方法没啦
//  assert(set4 is Set);//这里会报错

  ///Map 映射，也有人称之为字典
  ///Map是一个无序的键值对容器
  //Map的声明
  var map1 = {
    'oahu': ['waikiki', 'kailua', 'waimanalo'],
    'big island': ['wailea bay', 'pololu beach'],
    'kauai': ['hanalei', 'poipu']
  };

  var map2 = new Map();

  //指定键值对的参数类型
  var map3 = Map<int, String>();

  // Map的赋值，中括号中是Key，这里可不是数组
  map3[54] = 'dart';

  //Map中的键值对是唯一的
//同Set不同，第二次输入的Key如果存在，Value会覆盖之前的数据
  map3[54] = 'xenon';
  assert(map3[54] == 'xenon');

  // 检索Map是否含有某Key
  assert(map3.containsKey(54));

  //删除某个键值对
  map3.remove(54);
  assert(!map3.containsKey(54));

  ///你可以用getKeys和getValues获取所有Key或者所有Values的迭代器
  var keys = map1.keys;
  assert(keys.length == 3);
  assert(new Set.from(keys).contains('oahu'));

  var values = map1.values;
  assert(values.length == 3);

//迭代器中有一个有意思的函数any，用来检测迭代器中的数据
//当其中一个元素运行函数时return true，那么any的返回值就为true，否则为false
//与之相对的是函数every，要所有函数运行函数return true，那么every返回true
  assert(values.any((v) => v.indexOf('waikiki') != -1));

  // 你可以用foreach来遍历数据，但记住它是无序的
  map1.forEach((k, v) {
    print('I want to visit $k and swim at $v');
  });

  //检索是否包含某个Key或Value
  assert(map1.containsKey('oahu'));
  assert(!map1.containsKey('florida'));

  //V putIfAbsent(K key, Function V ifAbsent())函数，通过Key来查找Value
//当某个Key不存在的时候，会执行第二参数的Function来添加Value

  var map4 = {};
  map4.putIfAbsent('Catcher', () => 'Catcher'.length);
  assert(map4['Catcher'] != null);

  //迭代 Set、List、Map都继承自Iterable，是可以迭代的
  var collection = [0, 1, 2];

  collection.forEach((x) => print(x)); //forEach的参数为Function

  for (var x in collection) {
    print(x);
  }
}
