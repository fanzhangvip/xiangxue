import 'package:flutter/material.dart';
import 'package:flutter_redux/flutter_redux.dart';
import 'package:flutter_redux_demo/action_reducer.dart';
import 'package:flutter_redux_demo/second_page.dart';
import 'package:flutter_redux_demo/count_state.dart';
import 'package:redux/redux.dart';

class FirstPage extends StatelessWidget {
  String title;

  FirstPage({this.title}) : super();

  @override
  Widget build(BuildContext context) {
    return Material(
      child: Container(
        child: Column(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: <Widget>[
                Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: Text(
                    title,
                    maxLines: 1,
                    overflow: TextOverflow.fade,
                    style: TextStyle(
                        color: Colors.blue,
                        fontSize: 30,
                        letterSpacing: 10,
                        fontWeight: FontWeight.bold,
                        background: Paint()..color = Colors.lightGreen),
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.all(8.0),
                  ///获取store中的state
                  child: StoreConnector<CountState, int>(
                    converter: (store) => store.state.count,
                    builder: (context, count) {
                      return Text(
                        "现在的Count: $count",
                      );
                    },
                  ),
                ),
                ///发出action
                StoreConnector<CountState, VoidCallback>(
                    builder: (BuildContext context, VoidCallback callback) {
                      return new RaisedButton(
                        onPressed: callback,
                        child: new Text("加三"),
                      );
                    }, converter: (Store<CountState> store) {
                  return () => store.dispatch(IncreAction(3));
                }),
                AddAsyncButton(),
                Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: RaisedButton(
                    child: Text("跳转第二页"),
                    onPressed: () {
                      debugPrint("跳转到第二页");
                      Navigator.of(context).push(new MaterialPageRoute(builder: (BuildContext context){
                        return SecondPage(title:"第二页");
                      }));
                    },
                  ),
                ),
              ],
            ),
          ),
        );
  }
}

class AddAsyncButton extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return StoreConnector<CountState, VoidCallback>(
        builder: (BuildContext context, VoidCallback callback) {
          return new RaisedButton(
            onPressed: callback,
            child: new Text("异步加二"),
          );
        }, converter: (Store<CountState> store) {
      return () => store.dispatch(asyncIncrement(2));
    });
  }
}
