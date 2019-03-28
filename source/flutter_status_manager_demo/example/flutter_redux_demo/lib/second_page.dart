import 'package:flutter/material.dart';
import 'package:flutter_redux/flutter_redux.dart';
import 'package:flutter_redux_demo/action_reducer.dart';
import 'package:flutter_redux_demo/count_state.dart';
import 'package:redux/redux.dart';

class SecondPage extends StatelessWidget {
  String title;

  SecondPage({this.title});

  @override
  Widget build(BuildContext context) {
    return Material(
        child: Container(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: <Widget>[
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Text(
              title,
              style: TextStyle(color: Colors.blue, fontSize: 30),
            ),
          ),
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: StoreConnector<CountState, int>(
              converter: (store) => store.state.count,
              builder: (context, count) {
                return Text(
                  "现在的Count: $count",
                  style: Theme.of(context).textTheme.display1,
                );
              },
            ),
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: <Widget>[
              Expanded(
                child: Padding(
                  padding: const EdgeInsets.all(8.0),
                  ///第七步 发出action
                  child: StoreConnector<CountState, VoidCallback>(
                      builder: (BuildContext context, VoidCallback callback) {
                    return new RaisedButton(
                      onPressed: callback,
                      child: new Text("加一"),
                    );
                  }, converter: (Store<CountState> store) {
                    return () => store.dispatch(IncreAction(1));
                  }),
                ),
              ),
              Expanded(
                child: Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: StoreConnector<CountState, VoidCallback>(
                      builder: (BuildContext context, VoidCallback callback) {
                    return new RaisedButton(
                      onPressed: callback,
                      child: new Text("减一"),
                    );
                  }, converter: (Store<CountState> store) {
                    return () => store.dispatch(DecreAction(-1));
                  }),
                ),
              ),
              Expanded(
                child: Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: StoreConnector<CountState, VoidCallback>(
                      builder: (BuildContext context, VoidCallback callback) {
                    return new RaisedButton(
                      onPressed: callback,
                      child: new Text("加二"),
                    );
                  }, converter: (Store<CountState> store) {
                    return () => store.dispatch(IncreAction(2));
                  }),
                ),
              ),
              Expanded(
                child: Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: StoreConnector<CountState, VoidCallback>(
                      builder: (BuildContext context, VoidCallback callback) {
                    return new RaisedButton(
                      onPressed: callback,
                      child: new Text("减二"),
                    );
                  }, converter: (Store<CountState> store) {
                    return () => store.dispatch(DecreAction(-2));
                  }),
                ),
              ),
            ],
          ),
          RaisedButton(
            child: Text("返回第一页"),
            onPressed: () {
              debugPrint("返回第一页");
              Navigator.of(context).pop();
            },
          )
        ],
      ),
    ));
  }
}
