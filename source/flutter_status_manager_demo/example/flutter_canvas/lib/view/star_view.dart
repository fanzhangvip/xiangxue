import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class StarView extends CustomPainter {

  Paint mHelpPaint;
  BuildContext context;

  StarView(this.context) {
    mHelpPaint = new Paint();
    mHelpPaint.style=PaintingStyle.stroke;
    mHelpPaint.color=Color(0xffBBC3C5);
    mHelpPaint.isAntiAlias=true;
  }

  /**
   * 绘制网格路径
   *
   * @param step    小正方形边长
   * @param winSize 屏幕尺寸
   */
  Path gridPath(int step, Size winSize) {
    Path path = new Path();

    for (int i = 0; i < winSize.height / step + 1; i++) {
      path.moveTo(0, step * i.toDouble());
      path.lineTo(winSize.width, step * i.toDouble());
    }

    for (int i = 0; i < winSize.width / step + 1; i++) {
      path.moveTo(step * i.toDouble(), 0);
      path.lineTo(step * i.toDouble(), winSize.height);
    }
    return path;
  }

  @override
  void paint(Canvas canvas, Size size) {
    var winSize = MediaQuery.of(context).size;
    canvas.drawPath(gridPath(20, winSize), mHelpPaint);
  }

  @override
  bool shouldRepaint(CustomPainter oldDelegate) {
    return true;
  }
}
