//import 'dart:html';
//import 'dart:math' as math;
//
//void main() {
//  Sunflower();
//}
//
//class Sunflower {
//  static const orange = 'orange';
//  static const seedRadius = 2;
//  static const scaleFactor = 4;
//  static const tau = math.pi * 2;
//  static const maxD = 300;
//
//  static final phi = (math.sqrt(5) + 1) / 2;
//  static final center = maxD / 2;
//
//  final context = (querySelector('#canvas') as CanvasElement).context2D;
//
//  int seeds;
//
//  Sunflower() {
//    InputElement slider = querySelector('#slider');
//
//    void update() {
//      seeds = int.parse(slider.value);
//      drawFrame();
//    }
//
//    slider.onChange.listen((_) => update());
//
//    update();
//  }
//
//  // Draw the complete figure for the current number of seeds.
//  void drawFrame() {
//    print('seed value = $seeds');
//
//    context.clearRect(0, 0, maxD, maxD);
//
//    for (var i = 0; i < seeds; i++) {
//      var theta = i * tau / phi;
//      var r = math.sqrt(i) * scaleFactor;
//      var x = center + r * math.cos(theta);
//      var y = center - r * math.sin(theta);
//      drawSeed(x, y);
//    }
//  }
//
//  // Draw a small circle representing a seed centered at (x,y).
//  void drawSeed(num x, num y) {
//    context
//      ..beginPath()
//      ..lineWidth = 2
//      ..fillStyle = orange
//      ..strokeStyle = orange
//      ..arc(x, y, seedRadius, 0, tau, false)
//      ..fill()
//      ..closePath()
//      ..stroke();
//  }
//}