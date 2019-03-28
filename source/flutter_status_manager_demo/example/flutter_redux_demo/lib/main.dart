import 'package:flutter/material.dart';
import 'package:flutter_redux/flutter_redux.dart';
import 'package:flutter_redux_demo/action_reducer.dart';
import 'package:flutter_redux_demo/count_state.dart';
import 'package:flutter_redux_demo/first_page.dart';
import 'package:flutter_redux_demo/logger_middleware.dart';
import 'package:redux/redux.dart';
import 'package:redux_thunk/redux_thunk.dart';

void main() {
  ;
  ///第五步 创建store
  final store = Store<CountState>(reducers,
      initialState: CountState.initState(), middleware: initialMiddleware());
  runApp(MyApp(store));
}

class MyApp extends StatelessWidget {
  final Store<CountState> store;

  MyApp(this.store);

  @override
  Widget build(BuildContext context) {
    ///第六步 将Store放入顶层
    return StoreProvider<CountState>(
      store: store,
      child: MaterialApp(
        title: 'Flutter Demo',
        theme: ThemeData(
          primarySwatch: Colors.blue,
        ),
        home: FirstPage(title: "第一个页面"),
      ),
    );
  }
}
