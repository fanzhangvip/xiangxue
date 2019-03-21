import 'dart:io';

void main() {
//  Future f = Future.delayed(Duration(seconds: 3));
//  // future的执行记过 通过then可以获取
//  f.then((t){
//    print(t);
//  });

  // then：可以得到Future的结果并且能够返回一个新的Future
  Future<String> then = new File(r"./lib/dart_async/a.txt").readAsString()
      .then((String s) {
    //返回void 或者Future
    print(s);
    return "1";
  });

  then.then((String i) {
    print(i);
  });

  //UI操作

  //通过then可以完成 有序任务的执行，一个任务执行完成之后，下一个任务根据上个任务的结果 执行不同的操作

  //如果需要一组任务都执行完毕之后 再统一执行相同的一些处理
  Future.wait([Future.delayed(Duration(seconds: 1)),Future.delayed(Duration
    (seconds: 1))]).then((_){
      print(1);
  });

  //
  Future.forEach([1,2,3,4],(i){
    print("i= $i");
  });



}
