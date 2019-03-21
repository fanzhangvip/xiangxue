//async 表示这是一个异步方法,await必须再async方法中使用
//异步方法只能返回 void和Future
import 'dart:io';
import 'dart:ui';

//只能返回void与 Future
Future<String> readFile() async {
  //await 等待future执行完成再执行后续代码
  String content = await new File("./lib/dart_async/a.txt").readAsString();
  String content2 = await  new File("./lib/dart_async/b.txt").readAsString();
  //自动转换为 future
  return "$content$content2";
}


void readFile2(void callback(s))  {
  String result;
  new File("./lib/dart_async/a.txt").readAsString().then((s){
    result += s;
    return new File("./lib/dart_async/b.txt").readAsString();
  }).then((s){
    result+=s;
  }).whenComplete((){
    print(result);
    callback(result);
  });
}

void main(){
 //readFile().then(onValue)

//readFile().then(print);


readFile2(print);

}