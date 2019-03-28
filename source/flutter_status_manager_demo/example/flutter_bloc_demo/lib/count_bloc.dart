import 'dart:async';
///第一步创建BLoC
class CountBloc{
  int _count;
  StreamController<int> _countController;
  Stream<int> get stream => _countController.stream;
  int get value => _count;

  CountBloc.init()
      :_count=0,
        _countController = StreamController<int>.broadcast();

  increment() {
    _countController.sink.add(++_count);
  }

  dispose() {
    _countController.close();
  }
}

CountBloc bloC = CountBloc.init();


