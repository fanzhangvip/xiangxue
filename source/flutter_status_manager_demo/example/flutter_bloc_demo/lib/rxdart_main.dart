import 'package:flutter/material.dart';
import 'package:rxdart/rxdart.dart';

void main() => runApp(MyApp());

class CountBLoC {
  int _count = 0;
  var _subject = BehaviorSubject<int>();

  Stream<int> get stream => _subject.stream;
  int get value => _count;

  void increment() => _subject.add(++_count);

  void dispose() {
    _subject.close();
  }
}

class BlocProvider extends InheritedWidget{

  CountBLoC bloc = CountBLoC();

  BlocProvider({Key key,Widget child}):super(key:key,child:child);

  static CountBLoC of(BuildContext context) =>
      (context.inheritFromWidgetOfExactType(BlocProvider)as BlocProvider) .bloc;

  @override
  bool updateShouldNotify(InheritedWidget oldWidget) {
    // TODO: implement updateShouldNotify
    return true;
  }

}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return BlocProvider(
      child: MaterialApp(
        title: 'Flutter Demo',
        theme: ThemeData(

          primarySwatch: Colors.blue,
        ),
        home: FirstPage(title: 'Flutter rxdart model'),
      ),
    );
  }
}

class FirstPage extends StatefulWidget {
  FirstPage({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _FirstPageState createState() => _FirstPageState();
}

class _FirstPageState extends State<FirstPage> {

  void _forward() {
    Navigator.of(context).push(MaterialPageRoute(builder: (context) => SecondPage()));
  }

  @override
  Widget build(BuildContext context) {
    final bloc = BlocProvider.of(context);
    return Scaffold(
      appBar: AppBar(

        title: Text(widget.title),
      ),
      body: Center(
        child: Column(

          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(
              'You have pushed the button this many times:',
            ),
            StreamBuildWidget(context),

            Padding(
              padding: const EdgeInsets.all(8.0),
              child: RaisedButton(
                child: Text("加一"),
                onPressed: () => {
                bloc.increment()
                },
              ),
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _forward,
        tooltip: '跳转到第二页',
        child: Icon(Icons.forward),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}

class SecondPage extends StatefulWidget {
  @override
  _SecondPageState createState() => _SecondPageState();
}

class _SecondPageState extends State<SecondPage> {
  @override
  Widget build(BuildContext context) {
    final bloc = BlocProvider.of(context);
    return MaterialApp(
      title: "第二页",
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: Scaffold(
        appBar: AppBar(
          title: Text("第二页"),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              StreamBuildWidget(context),
              Padding(
                padding: const EdgeInsets.all(8.0),
                child: RaisedButton(
                  child: Text("加一"),
                  onPressed: () => {
                  bloc.increment()
                  },
                ),
              ),
            ],
          ),
        ),
        floatingActionButton: FloatingActionButton(
          onPressed: () => debugPrint("这是第二页"),
          tooltip: '这是第二页',
          child: Icon(Icons.ac_unit),
        ), // This trailing comma makes auto-formatting nicer for build methods.
      ),
    );
  }
}

class StreamBuildWidget extends StatelessWidget {
  BuildContext context;
  final bloc;
  StreamBuildWidget(this.context):bloc = BlocProvider.of(context);
  @override
  Widget build(BuildContext context) {
    return Container(
      child: StreamBuilder<int>(
        stream:bloc.stream,
        initialData: bloc.value,
        builder: (BuildContext context,AsyncSnapshot<int> snapshot){
          return Text("You hit Count:${snapshot.data}",
            style: Theme.of(context).textTheme.display2,);
        },
      ),
    );
  }
}


