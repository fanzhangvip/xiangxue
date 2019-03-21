

import 'dart:io';

void main(){
//  Future f = Future.delayed(Duration(seconds: 3));
//  // future的执行记过 通过then可以获取
//  f.then((t){
//    print(t);
//  });

  new File(r"./lib/dart_async/a.txt").readAsString().then((String s){
    //返回void 或者Future
    print(s);
    return 1;
  }).catchError((e,s){

  });


  Future.delayed(Duration(seconds: 10));

}