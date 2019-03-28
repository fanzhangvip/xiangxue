import 'package:meta/meta.dart';
/// 第二步创建State
@immutable
class CountState{
  int _count;
  get count => _count;

  CountState(this._count);
  CountState.initState():_count = 0;
}