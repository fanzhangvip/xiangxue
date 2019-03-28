import 'package:flutter/foundation.dart';
import 'package:flutter_redux_demo/action_reducer.dart';
import 'package:flutter_redux_demo/count_state.dart';
import 'package:redux/redux.dart';
import 'package:redux_thunk/redux_thunk.dart';

List<Middleware<CountState>> initialMiddleware() {
  List<Middleware<CountState>> middleware = [];
  List<MiddlewareFactory> factories = [
    LoggerMiddlewareFactory(),
    ThunkMiddlewareFactory(),
  ];
  factories.forEach((factory) => middleware.addAll(factory.generate()));
  return middleware;
}



abstract class MiddlewareFactory {
  MiddlewareFactory();

  List<Middleware<CountState>> generate();
}

class ThunkMiddlewareFactory extends MiddlewareFactory{
  ThunkMiddlewareFactory():super();

  @override
  List<Middleware<CountState>> generate() {
    return [
      TypedMiddleware<CountState, ThunkAction>(_thunkMiddleware),
    ];
  }

  void _thunkMiddleware(
      Store<CountState> store,
      dynamic action,
      NextDispatcher next,
      ) {
    if (action is ThunkAction<CountState>) {
      action(store);
      debugPrint("store: ${store.state.count}, 异步action: ${store.state.count}");
    } else {
      next(action);
    }
  }
}

class LoggerMiddlewareFactory extends MiddlewareFactory {
  LoggerMiddlewareFactory() : super();

  @override
  List<Middleware<CountState>> generate() {
    return [
      TypedMiddleware<CountState, IncreAction>(_doIncreActionLogger),
      TypedMiddleware<CountState, DecreAction>(_doDecreActionLogger),
    ];
  }

  void _doIncreActionLogger(
      Store<CountState> store, IncreAction action, NextDispatcher next) {
    next(action);
    debugPrint("store: ${store.state.count},action: ${action.value}");
  }

  void _doDecreActionLogger(
      Store<CountState> store, DecreAction action, NextDispatcher next) {
    next(action);
    debugPrint("store: ${store.state.count},action: ${action.type.toString()}");
  }
}
