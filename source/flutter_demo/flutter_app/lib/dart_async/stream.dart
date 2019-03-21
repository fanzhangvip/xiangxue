
import 'dart:io';

void main(){

  new File(r"./lib/dart_async/a.txt").readAsBytes().then((_){
    print("future");
  });

  //写这个文件
  var dst = new File(r"./lib/dart_async/a.txt");
  var write = dst.openWrite();
  Stream<List<int>> stream = new File(r"./lib/dart_async/a.txt").openRead();
  var listen = stream.listen((s){
    print("stream");
    write.add(s);
  });

  //替代掉listen的方法
//  listen.onData((s){
//    print("strema2");
//  });
//  listen.onDone((){
//    print("读完整个文件");
//  });
//  listen.pause();
//  listen.resume();


}
