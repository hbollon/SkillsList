import 'package:flutter/material.dart';

import '../utils.dart';

class SkillListPage extends StatefulWidget {
  double value;
  SkillListPage(this.value, {Key key}) : super(key: key);

  @override
  _SkillListPageState createState() => _SkillListPageState(value);
}

class _SkillListPageState extends State<SkillListPage> {
  double value;
  _SkillListPageState(this.value);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        title: 'Welcome to Flutter',
        theme: ThemeData(primarySwatch: Colors.deepPurple),
        home: Scaffold(
            appBar: AppBar(
              title: Text('Welcome to Flutter'),
            ),
            body: Container(
              child: Column(
                children: [BlockHeader(this.value)],
              ),
            )));
  }
}

class BlockHeader extends StatefulWidget {
  double value;
  BlockHeader(this.value, {Key key}) : super(key: key);

  @override
  _BlockHeaderState createState() => _BlockHeaderState(this.value);
}

class _BlockHeaderState extends State<BlockHeader> {
  double value;
  TextStyle styleProgress = TextStyle(
      fontFamily: 'Montserrat',
      fontSize: 14.0,
      color: Colors.white,
      fontWeight: FontWeight.bold);

  _BlockHeaderState(this.value);

  @override
  Widget build(BuildContext context) {
    final blockProgressBar = new Container(
        margin: new EdgeInsets.only(
            left: 28.0, right: 28.0, bottom: 20.0, top: 20.0),
        alignment: FractionalOffset.bottomCenter,
        child: Row(children: <Widget>[
          Expanded(
              child: LinearProgressIndicator(
            value: this.value,
            backgroundColor: Colors.black,
            minHeight: 8,
            valueColor: new AlwaysStoppedAnimation<Color>(
                Utils.getColorFromProgress(this.value)),
          )),
          Container(
            margin: EdgeInsets.only(left: 16),
            child: Text(
              (this.value * 100.0).toInt().toString() + "%",
              style: styleProgress,
              textAlign: TextAlign.left,
            ),
          )
        ]));
    return new Container(
      margin: new EdgeInsets.all(6.0),
      height: 180.0,
      decoration: new BoxDecoration(
        color: new Color(0xFF333366),
        shape: BoxShape.rectangle,
        borderRadius: new BorderRadius.circular(8.0),
        boxShadow: <BoxShadow>[
          new BoxShadow(
            color: Colors.black12,
            blurRadius: 10.0,
            offset: new Offset(0.0, 10.0),
          ),
        ],
      ),
      child: new Stack(
        children: <Widget>[blockProgressBar],
      ),
    );
  }
}
