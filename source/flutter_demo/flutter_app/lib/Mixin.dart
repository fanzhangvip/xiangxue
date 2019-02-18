mixin AnswerMixin{
  int get answer => 42;

  @override
  String toString() => '[ $runtimeType ]';
}

class Answer with AnswerMixin{}

void printAnswer(AnswerMixin obj, String description) {
  print(obj);
  print('- $description');
  print('answer: ${obj.answer}');
  print('');
}


mixin LoggingAnswerMixin on AnswerMixin {
  @override
  int get answer {
    var value = super.answer;
    print('  LOG: `answer` property was accessed');
    return value;
  }
}

class LogAnswer with AnswerMixin, LoggingAnswerMixin {}

mixin VerifyingAnswerMixin on AnswerMixin {
  @override
  int get answer {
    var value = super.answer;
    if (value == 42) {
      print('  VERIFY: Invalid Result!');
    } else {
      print('  VERIFY: valid result');
    }
    return value;
  }
}

class LogVerifyAnswer
    with AnswerMixin, LoggingAnswerMixin, VerifyingAnswerMixin {}

class VerifyLogAnswer
    with AnswerMixin, VerifyingAnswerMixin, LoggingAnswerMixin {}

abstract class DeltaAnswer with AnswerMixin {
  final int delta;

  DeltaAnswer(this.delta);

  @override
  int get answer => super.answer + delta;
}

class DeltaLogVerifyAnswer extends DeltaAnswer
    with LoggingAnswerMixin, VerifyingAnswerMixin {
  DeltaLogVerifyAnswer([int delta = 0]) : super(delta);
}

main(){
  Answer();

//  printAnswer(Answer(), 'Use `with` to include a mixin');
//
//  printAnswer(
//  LogAnswer(),
//  'Include many mixins by separating with commas. '
//  '`$LoggingAnswerMixin` prints every time `answer` is accessed.');
//
//  printAnswer(LogVerifyAnswer(), 'In this case, log then verify.');
//  printAnswer(VerifyLogAnswer(), 'In this case, verify then log.');
//
//  printAnswer(DeltaLogVerifyAnswer(), 'Verify will fail.');
//  printAnswer(DeltaLogVerifyAnswer(1), 'Verify will succeed.');
print("VerifyLogAnswer ${VerifyLogAnswer is AnswerMixin}");
}
