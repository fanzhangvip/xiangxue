

import 'package:flutter/widgets.dart';

void main() => runApp(MyWidget());

class MyWidget extends StatelessWidget {

  final String _msg = "Flutter 框架分析";
  @override
  Widget build(BuildContext context) {
    return ErrorWidget(_msg);
  }
}
