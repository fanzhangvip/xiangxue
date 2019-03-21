import 'dart:io';
import 'dart:isolate';

int i;

void main() {
  i = 10;
  // 创建一个消息接收器
  var receivePort = new ReceivePort();

  //将消息接收器当中的发送器 发给 子 isolate
  Isolate.spawn(entryPoint, receivePort.sendPort);
//  Isolate.spawn(entryPoint2, receivePort.sendPort);

  //从消息接收器中读取消息

  receivePort.listen((t) {
    print("接收到其他isolate发过来的消息！");
    //接收到了 子isolate 的 发送器
    if (t is SendPort) {

    } else{
     // i = t;
    }

  });

  sleep(Duration(seconds: 10));


//  receivePort.sendPort.send("1");
//  receivePort.sendPort.send("2");
//  receivePort.sendPort.send(1);

  //receivePort.close();
}

void entryPoint(SendPort sendPort) {
  i = 100;
  print(i);
  var receivePort = new ReceivePort();

  var sendPort2 = receivePort.sendPort;

//  sendPort.send(sendPort2);
//  sendPort.send(i);
//
//  receivePort.listen((t){
//
//  });

  sendPort.send("1");
}

//void entryPoint2(SendPort sendPort) {
//  print(i);
//  var receivePort = new ReceivePort();
//
//  var sendPort2 = receivePort.sendPort;
//
//  sendPort.send(sendPort2);
//
//  receivePort.listen((t){
//
//  });
//}
