import 'package:flutter_redux_demo/count_state.dart';
import 'package:redux/redux.dart';
import 'package:redux_thunk/redux_thunk.dart';
///第三步 创建Action
enum Actions{
  increment,
  decrement
}

// 定义所有action的基类
class Action{
  final Actions  type;

  Action({this.type});
}


//加
class IncreAction extends Action{
  int value;
  IncreAction(this.value):super(type:Actions.increment);
}

//减
class DecreAction extends Action{
  int value;
  DecreAction(this.value):super(type:Actions.decrement);
}

///第二步创建异步action
ThunkAction asyncIncrement(int value) {
  return (Store store) async {
    await Future.delayed(Duration(seconds: 3)); // 延迟 3 秒
    store.dispatch(IncreAction(value));
  };
}

///第四步 创建reducer
CountState increReducer(CountState state,dynamic action){
  switch (action.type) {
    case Actions.increment:
      return  CountState(state.count + action.value);
    default:
      return state;
  }
}

CountState decreReducer(CountState state,dynamic action){
  switch (action.type) {
    case Actions.decrement:
     return CountState(state.count +action.value);
    default:
      return state;
  }
}

///合并reducer
final reducers = combineReducers<CountState>([
  increReducer,
  decreReducer,
]);
