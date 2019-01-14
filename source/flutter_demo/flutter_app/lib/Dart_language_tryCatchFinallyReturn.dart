String throwException(bool flag) {
  if (flag) {
    throw "This is a Exception";
  } else {
    return "flag = $flag";
  }
}

int NoException() {
//        情况1：try块中没有抛出异常try和finally块中都有return语句
//        执行顺序：
//        1. 执行try块，执行到return语句时，先执行return的语句，--i，但是不返回到main 方法，
//        2. 执行finally块，遇到finally块中的return语句，执行--i,并将值返回到main方法，这里就不会再回去返回try块中计算得到的值
  int i = 10;
  try {
    print("i 现在是try代码块: $i");
    int j = 100;
    return i = (--i + j);
  } on Exception catch (e) {
    int j = 1000;
    --i;
    print("i 现在是在catch代码块: $i");
    return i = (--i + j);
  } finally {
    print("i 现在是在finally代码块: $i");
    int j = 10000;
    return i = (--i + j);
  }
}

int NoException1() {
//        情况2：try块中没有抛出异常，仅try中有return语句
//        执行顺序：
//        try中执行完return的语句后，不返回，执行finally块，finally块执行结束后，
//        返回到try块中，返回i在try块中最后的值
  int i = 10;
  try {
    print("i 现在是try代码块: $i");
    int j = 100;
    return i = (--i + j);
  } on Exception catch (e) {
    int j = 1000;
    --i;
    print("i 现在是在catch代码块: $i");
    return i = (--i + j);
  } finally {
    print("i 现在是在finally代码块before: $i");
    --i;
    print("i 现在是在finally代码块after: $i");
  }
}

int WithException() {
//        情况3：try块中抛出异常try,catch,finally中都有return语句
//        执行顺序：
//        抛出异常后，执行catch块，在catch块的return的--i执行完后，并不直接返回而是执行finally，
//        因finally中有return语句，所以，执行，返回结果6
  int i = 10;
  try {
    print("i 现在是try代码块: $i");
    final k = i ~/ 0;
//    throw 'This a try Exception!';
    int j = 100;
    return i = (--i + j);
  } on Exception catch (e) {
    print("i 现在是在catch代码块before: $i");
    int j = 1000;
    --i;
    print("i 现在是在catch代码块after: $i");
    return i = (--i + j);
  } finally {
    print("i 现在是在finally代码块before: $i");
    int j = 10000;
    --i;
    print("i 现在是在finally代码块after: $i");
    return i = (--i + j);
  }
//        情况4，catch中有return,finally中没有，同上，执行完finally语句后，依旧返回catch中的执行return语句后的值，而不是finally中修改的值
}

int CatchException() {
//        情况5：try和catch中都有异常，finally中无return语句
//        在try块中出现异常，到catch中，执行到异常，到finally中执行，finally执行结束后判断发现异常，抛出
  int i = 10;
  try {
    print("i 现在是try代码块: $i");
    final k = i ~/ 0;
//    throw 'This a try Exception!';
    int j = 100;
    return i = (--i + j);
  } on Exception catch (e) {
    print("i 现在是在catch代码块before: $i , e = $e");
    final k = i ~/ 0;
//    throw 'This a catch Exception!';
    int j = 1000;
    print("i 现在是在catch代码块after: $i");
    return i = (--i + j);
  } finally {
    print("i 现在是在finally代码块before: $i");
    --i;
    print("i 现在是在finally代码块after: $i");
    //return --i;
  }
}

int CatchException1() {
//        情况6：try,catch中都出现异常，在finally中有返回
//        try块中出现异常到catch，catch中出现异常到finally，finally中执行到return语句返回，不检查异常
  int i = 10;
  try {
    print("i 现在是try代码块: $i");
    final k = i ~/ 0;
//    throw 'This a try Exception!';
    int j = 100;
    return i = (--i + j);
  } on Exception {
    print("i 现在是在catch代码块before: $i");
    final k = i ~/ 0;
//    throw 'This a catch Exception!';
    int j = 1000;
    print("i 现在是在catch代码块after: $i");
    return i = (--i + j);
  } finally {
    print("i 现在是在finally代码块before: $i");
    --i;
    print("i 现在是在finally代码块after: $i");
    int j = 10000;
    return i = (--i + j);
  }
}

main() {
//  final k = 1~/0;
//  print("k = $k,会抛出异常");
  print(
      "-----------------------------------情况1---------------------------------");
  final result = NoException();
  print("try块中没有抛出异常try和finally块中都有return语句: $result");

  print(
      "-----------------------------------情况2---------------------------------");
  final result1 = NoException1();
  print("try块中没有抛出异常，仅try中有return语句: $result1");

  print(
      "-----------------------------------情况3---------------------------------");
  final result2 = WithException();
  print("try块中抛出异常try,catch,finally中都有return语句: $result2");

  print(
      "-----------------------------------情况6---------------------------------");
  final result4 = CatchException1();
  print("try,catch中都出现异常，在finally中有返回: $result4");

  print(
      "-----------------------------------情况5---------------------------------");
  final result3 = CatchException();
  print("try和catch中都有异常，finally中无return语句: $result3");
}
