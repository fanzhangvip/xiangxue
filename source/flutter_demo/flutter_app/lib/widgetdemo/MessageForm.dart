import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

class MessageForm extends StatefulWidget {
  @override
  _MessageFormState createState() => _MessageFormState();
}

class _MessageFormState extends State<MessageForm> {
  var editController = TextEditingController();
  @override
  Widget build(BuildContext context) {
    return Row(
      children: <Widget>[
        buildRaisedButton(),
        Expanded(child: TextField(
          controller: editController,
        ))
      ],
    );
  }

  RaisedButton buildRaisedButton() {
    return RaisedButton(
        child: buildContainer(),
        onPressed: () => debugPrint("text inputteed: ${editController.text}"),
      );
  }

  Container buildContainer() => Container(child: Text("Click"));

  @override
  void dispose() {
    // TODO: implement dispose
    super.dispose();
    editController.dispose();
  }
}
