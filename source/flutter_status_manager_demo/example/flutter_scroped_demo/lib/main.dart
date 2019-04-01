//import 'package:flutter/material.dart';
//import 'package:flutter_scroped_demo/first_page.dart';
//import 'package:scoped_model/scoped_model.dart';
//
//void main() => runApp(MyApp());
//
/////第二步创建Model
//class CountModel extends Model{
//  int _count = 0;
//  get count => _count;
//  void increment(){
//    _count++;
//    notifyListeners();
//  }
//
//  CountModel of(context) =>
//      ScopedModel.of<CountModel>(context);
//}
//
//class MyApp extends StatelessWidget {
//  //创建顶层状态
//  CountModel countModel = CountModel();
//
//  @override
//  Widget build(BuildContext context) {
//    return ScopedModel<CountModel>(
//      model: countModel,
//      child: MaterialApp(
//        title: 'Flutter Demo',
//        theme: ThemeData(
//
//          primarySwatch: Colors.blue,
//        ),
//        home: FirstPage(),
//      ),
//    );
//  }
//}
