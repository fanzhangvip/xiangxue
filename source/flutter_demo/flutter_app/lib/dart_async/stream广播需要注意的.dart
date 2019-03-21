
import 'dart:async';
import 'dart:io';

void main(){
  var stream = Stream.fromIterable([1,2,3,4]);
  //由单订阅转换而来的  ： 和单订阅唯一的区别就是 允许多订阅
  var broadcastStream = stream.asBroadcastStream();

  broadcastStream.listen((i){
    print("订阅者1：${i}");
  });

  broadcastStream.listen((i){
    print("订阅者2：${i}");
  });


  //===========================
  //直接创建一个广播
  var streamController = StreamController.broadcast();
  //发送一条广播
  streamController.add("1");

  //不能获得数据
  streamController.stream.listen((i){
    print("广播:"+i);
  });
  streamController.close();
  //============================

  broadcastStream.listen((i){
    print("转换的广播：$i");
  });

}
