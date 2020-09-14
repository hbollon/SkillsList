import 'package:flutter/material.dart';

class SkillListNew extends StatelessWidget {
  SkillListNew({Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    TextStyle style = TextStyle(fontFamily: 'Montserrat', fontSize: 20.0);
    return MaterialApp(
      title: 'Welcome to Flutter',
      theme: ThemeData(primarySwatch: Colors.deepPurple),
      home: Scaffold(
          appBar: AppBar(
            title: Text('Welcome to Flutter'),
          ),
          body: new Column(
            children: <Widget>[
              new SkillBlockRow("Python", "assets/python.png"),
              new SkillBlockRow("Java", "assets/java.png"),
              new SkillBlockRow("C++", "assets/cpp.png"),
            ],
          )),
    );
  }
}

class SkillBlockRow extends StatelessWidget {
  SkillBlockRow(this.title, this.imgPath, {Key key}) : super(key: key);

  final String imgPath;
  final String title;

  @override
  Widget build(BuildContext context) {
    final blockThumbnail = new Container(
      margin: new EdgeInsets.symmetric(vertical: 16.0),
      alignment: FractionalOffset.centerLeft,
      child: new Image(
        image: new AssetImage(imgPath),
        height: 92.0,
        width: 92.0,
      ),
    );
    final blockCard = new Container(
      height: 124.0,
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
    );
    return new Container(
        height: 120.0,
        margin: const EdgeInsets.symmetric(
          vertical: 16.0,
          horizontal: 24.0,
        ),
        child: new Stack(
          children: <Widget>[
            blockCard,
            blockThumbnail,
          ],
        ));
  }
}
