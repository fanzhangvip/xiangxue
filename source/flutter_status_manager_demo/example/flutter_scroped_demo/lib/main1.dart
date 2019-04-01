import 'package:flutter/material.dart';
import 'package:scoped_model/scoped_model.dart';


 class CountModel12 extends Model{
  int _count = 0;
  get count => _count;
  void increment(){
    _count++;
    debugPrint("_count++ $_count");
    notifyListeners();
  }

  CountModel12 of(context,{rebuildOnChange = true}) =>
      ScopedModel.of<CountModel12>(context,rebuildOnChange:rebuildOnChange);
}

main(){

  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  CountModel12 countModel = CountModel12();

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return ScopedModel<CountModel12>(
      model: countModel,
      child:
      MaterialApp(
      title: 'Scoped model',
      home: NewFirstPage(),
    )
    );
  }
}

//class FirstPage extends StatelessWidget {
//  @override
//  Widget build(BuildContext context) {
//    // TODO: implement build
//    return ScopedModelDescendant<CountModel>(
//      builder: (context,child,model){
//        return Scaffold(
//          body: Column(
//            mainAxisAlignment: MainAxisAlignment.spaceAround,
//            children:<Widget>[
//              Center(child: Text(model.count.toString(),style: TextStyle(fontSize: 48.0),),),
//
//          RaisedButton(
//            child: Text("跳转到第二页"),
//            onPressed: (){
//              Navigator.of(context).push(MaterialPageRoute(builder: (BuildContext context){
//                return SecondPage();
//              }));
//              debugPrint("跳转到第二页");
//            },
//          ),
//
//            ],
//          ),
//        );
//      },
//    );
//  }
//}
////可以实现
//class SecondPage extends StatelessWidget {
//  @override
//  Widget build(BuildContext context) {
//    // TODO: implement build
//    return ScopedModelDescendant<CountModel>(
//      builder:(context,child,model){
//        return Scaffold(
//
//          body: Column(
//            mainAxisAlignment: MainAxisAlignment.spaceAround,
//            children:<Widget>[
//              Center(child: Text(model.count.toString(),style: TextStyle(fontSize: 48.0),),),
//
//              RaisedButton(
//                child: Text("加1"),
//                onPressed: (){
//                  model.increment();
//                },
//              ),
//
//              RaisedButton(
//                child: Text("跳回第一页"),
//                onPressed: (){
//                  Navigator.of(context).pop();
//                  debugPrint("跳回第一页");
//                },
//              ),
//
//            ],
//          ),
//        );
//      }
//
//    );
//  }
//}

class NewFirstPage extends StatelessWidget{

  //静态获取model用法实例
  CountModel12 getModel(BuildContext context){
    //直接使用of
//    final countModel = ScopedModel.of<CountModel12>(context);
    //使用CountModel中重写的of
    final countModel2 = CountModel12().of(context,rebuildOnChange:true);

//    countModel.increment();
//    countModel2.increment();
    return countModel2;
    //    return countMode2;
  }



  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    final CountModel12 model = getModel(context);
    return Scaffold(
      body: Column(
        mainAxisAlignment: MainAxisAlignment.spaceAround,
        children:<Widget>[
          Center(child: Text(model.count.toString(),style: TextStyle(fontSize: 48.0),),),

          RaisedButton(
            child: Text("跳转到新第二页"),
            onPressed: (){
              Navigator.of(context).push(MaterialPageRoute(builder: (BuildContext context){
                return ThreePage();
              }));
              debugPrint("跳转到新第二页");
            },
          ),

        ],
      ),
    );
  }

}

//第二种获取model方式，不可以实现
class ThreePage extends StatelessWidget{

  CountModel12 getModel(BuildContext context){
    //直接使用of
//    final countModel = ScopedModel.of<CountModel12>(context);
    //使用CountModel中重写的of
    final countModel2 = CountModel12().of(context,rebuildOnChange:true);

//    countModel.increment();
//    countModel2.increment();
    return countModel2;
    //    return countMode2;
  }

  @override
  Widget build(BuildContext context) {
    final countModel = getModel(context);
    // TODO: implement build
    return Scaffold(
      body: Column(
        mainAxisAlignment: MainAxisAlignment.spaceAround,
        children:<Widget>[
          Center(child: Text(countModel.count.toString(),
            style: TextStyle(fontSize: 48.0),),),

          RaisedButton(
            child: Text("加1"),
            onPressed: (){
              countModel.increment();
              debugPrint("加一");
            },
          ),

          RaisedButton(
            child: Text("跳回新第一页"),
            onPressed: (){
              Navigator.of(context).pop();
              debugPrint("跳回新第一页");
            },
          ),

        ],
      ),
    );
  }

}