import 'package:flutter/material.dart';
import 'package:expansion_card/expansion_card.dart';

import '../utils.dart';

class SkillListPage extends StatefulWidget {
  double value;
  String title;
  String imgpath;
  SkillListPage(this.value, this.title, this.imgpath, {Key key})
      : super(key: key);

  @override
  _SkillListPageState createState() =>
      _SkillListPageState(value, title, imgpath);
}

class _SkillListPageState extends State<SkillListPage> {
  double value;
  String desc =
      "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.";
  String title;
  String imgpath;
  List<bool> selected = List<bool>.generate(10, (index) => false);

  _SkillListPageState(this.value, this.title, this.imgpath);

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
                child:
                    //BlockHeader(this.value, this.desc, this.title, this.imgpath),
                    ListView.separated(
              itemCount: 10,
              itemBuilder: (context, index) => (index == 0)
                  ? new BlockHeader(
                      this.value, this.desc, this.title, this.imgpath)
                  : ExpansionCard(
                      background: Image.asset(
                        "assets/test.gif",
                        fit: BoxFit.cover,
                      ),
                      title: Container(
                        margin: EdgeInsets.symmetric(horizontal: 8),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: <Widget>[
                            Text(
                              "Header",
                            ),
                            Text(
                              "Sub",
                            ),
                          ],
                        ),
                      ),
                      children: <Widget>[
                        Container(
                          margin: EdgeInsets.symmetric(horizontal: 7),
                          child: Text(
                            "Content goes over here !",
                          ),
                        )
                      ],
                    ),
              separatorBuilder: (BuildContext context, int index) {
                return SizedBox(
                  height: 10,
                );
              },
            ))));

    /* new InkWell(
                                child: ExpansionTile(
                                    title: new Card(
                                        color: selected[index]
                                            ? Color(0xffc5b1e7)
                                            : Colors.white,
                                        shape: RoundedRectangleBorder(
                                            side: new BorderSide(
                                                color: selected[index]
                                                    ? Color(0xffc5b1e7)
                                                    : Colors.white,
                                                width: 2.0),
                                            borderRadius:
                                                BorderRadius.circular(4.0)),
                                        child: new Container(
                                          margin: EdgeInsets.all(32.0),
                                          child: Row(
                                            children: [
                                              Text("Skill"),
                                              Checkbox(
                                                  value: selected[index],
                                                  onChanged: (bool newValue) {
                                                    setState(() {
                                                      selected[index] =
                                                          newValue;
                                                    });
                                                  }),
                                            ],
                                          ),
                                        ))),
                                onTap: () {
                                  setState(() {
                                    selected[index] = !selected[index];
                                  });
                                },
                                splashColor: Colors.deepPurpleAccent,
                              ) */
  }
}

class BlockHeader extends StatefulWidget {
  double value;
  String desc;
  String title;
  String imgpath;
  BlockHeader(this.value, this.desc, this.title, this.imgpath, {Key key})
      : super(key: key);

  @override
  _BlockHeaderState createState() =>
      _BlockHeaderState(this.value, this.desc, this.title, this.imgpath);
}

class _BlockHeaderState extends State<BlockHeader> {
  double value;
  String desc;
  String title;
  String imgpath;
  final TextStyle styleProgress = TextStyle(
      fontFamily: 'Montserrat',
      fontSize: 14.0,
      color: Colors.white,
      fontWeight: FontWeight.bold);

  final TextStyle styleDesc =
      TextStyle(fontFamily: 'Montserrat', fontSize: 12.0, color: Colors.white);

  final TextStyle styleTitle = TextStyle(
      fontFamily: 'Montserrat',
      fontSize: 20.0,
      color: Colors.white,
      fontWeight: FontWeight.bold);

  _BlockHeaderState(this.value, this.desc, this.title, this.imgpath);

  @override
  Widget build(BuildContext context) {
    final blockProgressBar = new Container(
        margin: new EdgeInsets.only(
            left: 28.0, right: 28.0, bottom: 20.0, top: 10.0),
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

    final blockDesc = new Container(
        margin: new EdgeInsets.symmetric(horizontal: 16.0),
        alignment: FractionalOffset.center,
        child: new Text(this.desc, style: this.styleDesc));

    final blockThumbnail = new Container(
      margin: new EdgeInsets.symmetric(horizontal: 12.0),
      alignment: FractionalOffset.centerLeft,
      child: new Image(
        image: new AssetImage(this.imgpath),
        height: 96.0,
        width: 96.0,
      ),
    );

    final blockTitle = new Container(
        margin: new EdgeInsets.symmetric(horizontal: 16.0),
        alignment: FractionalOffset.center,
        child: new Text(
          this.title,
          style: this.styleTitle,
        ));

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
      child: new Column(
        children: <Widget>[
          new Expanded(
              child: new Container(
            child: new Row(
              children: [
                blockThumbnail,
                new Expanded(
                  child: blockTitle,
                ),
              ],
              mainAxisAlignment:
                  MainAxisAlignment.center, //Center Row contents horizontally,
              crossAxisAlignment:
                  CrossAxisAlignment.center, //Center Row contents vertically,
            ),
            margin: EdgeInsets.symmetric(horizontal: 12.0),
          )),
          blockProgressBar
        ],
      ),
    );
  }
}
