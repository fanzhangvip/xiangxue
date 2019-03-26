import 'dart:async';

import 'package:flutter/services.dart';

class Notebook {
  static const MethodChannel _channel =
      const MethodChannel('notebook');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
