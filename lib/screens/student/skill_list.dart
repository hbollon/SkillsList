import 'package:flutter/material.dart';
import 'package:expansion_card/expansion_card.dart';
import 'package:skillslist/main.dart';
import 'package:skillslist/models/Skills.dart';
import 'package:skillslist/widgets/menu_drawer.dart';

import 'package:skillslist/models/User.dart';

import 'package:skillslist/utils.dart';

import 'package:sprintf/sprintf.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;

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

String getStringFromValidation(int val) {
  if (val == -1)
    return "Ask for validation";
  else if (val == 0) return "Pending";
  return "Validated";
}

Color getColorFromValidation(int val) {
  if (val == 1) return Colors.green;
  return Colors.yellow;
}

Color getColor2FromValidation(int val) {
  if (val == 1) return Colors.lightGreenAccent;
  return Colors.yellowAccent;
}

class _SkillListPageState extends State<SkillListPage> {
  double value;
  String title;
  String desc = "Test";
  String imgpath;
  List<Skill> skills = new List<Skill>();
  List<bool> switchStates = new List<bool>();

  final TextStyle textStyleSkillName = new TextStyle(
      fontFamily: 'Montserrat',
      color: Colors.white,
      fontSize: 13.0,
      fontWeight: FontWeight.bold);

  final TextStyle textStyleSkillDesc = new TextStyle(
    fontFamily: 'Montserrat',
    color: Colors.white,
    fontSize: 13.0,
  );

  _SkillListPageState(this.value, this.title, this.imgpath);

  Future<bool> fetchSkills() async {
    final url = sprintf(
        "http://%s:8080/getAllSkillOfSkillblockByUser?username=%s&skillblockName=%s",
        [MyApp.ip, User.loggedInUser.username, this.title]);

    print(url);
    final response = await http
        .get(url, headers: {"Content-Type": "application/json; charset=UTF-8"});
    print(response.body);

    var jsonResponse = json.decode(response.body);
    final data = json.decode(jsonResponse["content"]);

    this.skills.clear();
    int nbVal = 0;

    for (var skill in data) {
      String name = skill["skillName"];
      int dbId = skill["dbId"];
      String desc = skill["skillDesc"];
      int validate = skill["validate"];

      Skill s = Skill(name, desc, validate);
      if (validate == 1) {
        ++nbVal;
      }
      this.skills.add(s);
      this.switchStates.add(validate >= 0);
    }
    this.value = nbVal / this.skills.length;
    print("Value : " + this.value.toString());
    return true;
  }

  Future<bool> requestSkillUpdate(Skill skill, bool validated) async {
    String url;
    if (validated) {
      url = sprintf("http://%s:8080/requestSkill", [MyApp.ip]);
    } else {
      url = sprintf("http://%s:8080/cancelSkillRequest", [MyApp.ip]);
    }

    Map map = {
      'connectedUsername': "admin",
      'username': User.loggedInUser.username,
      'skillblockName': this.title,
      'skillName': skill.name,
    };

    print(url);
    final response = await http.post(url,
        headers: {"Content-Type": "application/json; charset=UTF-8"},
        body: json.encode(map));
    print(response.body);

    return true;
  }

  @override
  Widget build(BuildContext context) {
    var fetch = fetchSkills();
    return MaterialApp(
        title: "SkillList",
        theme: ThemeData(primarySwatch: Colors.deepPurple),
        home: Scaffold(
            appBar: AppBar(
              title: Text("Skills : " + this.title),
            ),
            drawer: MenuDrawer(),
            body: Container(
                child: FutureBuilder(
                    future: fetch,
                    builder:
                        (BuildContext context, AsyncSnapshot<bool> snapshot) {
                      Widget lv;
                      if (snapshot.hasData) {
                        print(this.skills.length);
                        lv = ListView.builder(
                          itemCount: this.skills.length + 1,
                          itemBuilder: (BuildContext context, int index) {
                            Skill skill;
                            if (index > 0) {
                              skill = this.skills.elementAt(index - 1);
                            }
                            return (index == 0)
                                ? new BlockHeader(this.value, this.desc,
                                    this.title, this.imgpath)
                                : Card(
                                    color: new Color(0xFF333366),
                                    margin: EdgeInsets.all(6.0),
                                    child: Padding(
                                      padding: EdgeInsets.only(
                                          top: 8.0, right: 8.0, bottom: 8.0),
                                      child: Column(
                                        mainAxisSize: MainAxisSize.min,
                                        children: <Widget>[
                                          ListTile(
                                            leading: Icon(Icons.star,
                                                color: Colors.white),
                                            title: Text(
                                              skill.name,
                                              style: this.textStyleSkillName,
                                            ),
                                          ),
                                          ExpansionTile(
                                            title: Text("See more details",
                                                style: this.textStyleSkillName),
                                            children: [
                                              Padding(
                                                  padding: EdgeInsets.only(
                                                      top: 20.0,
                                                      bottom: 20.0,
                                                      left: 4.0,
                                                      right: 4.0),
                                                  child: Text(
                                                    skill.desc,
                                                    style:
                                                        this.textStyleSkillDesc,
                                                  ))
                                            ],
                                          ),
                                          Row(
                                            mainAxisAlignment:
                                                MainAxisAlignment.end,
                                            children: <Widget>[
                                              Text(
                                                getStringFromValidation(
                                                    skill.validation),
                                                style: this.textStyleSkillDesc,
                                              ),
                                              const SizedBox(width: 8),
                                              Switch(
                                                value: this
                                                    .switchStates
                                                    .elementAt(index - 1),
                                                onChanged: (value) {
                                                  Future<bool> output =
                                                      requestSkillUpdate(
                                                          skill, value);
                                                  output.whenComplete(() {
                                                    setState(() {
                                                      this.switchStates[
                                                          index - 1] = value;
                                                    });
                                                  });
                                                },
                                                activeColor:
                                                    getColorFromValidation(
                                                        skill.validation),
                                                activeTrackColor:
                                                    getColor2FromValidation(
                                                        skill.validation),
                                              )
                                            ],
                                          ),
                                        ],
                                      ),
                                    ));
                          },
                        );
                      } else {
                        lv = new BlockHeader(
                            this.value, this.desc, this.title, this.imgpath);
                      }
                      return lv;
                    }))));
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
    Container blockThumbnail;
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
    if (this.imgpath != null) {
      blockThumbnail = new Container(
        margin: new EdgeInsets.symmetric(horizontal: 12.0),
        alignment: FractionalOffset.centerLeft,
        child: new Image(
          image: new AssetImage(this.imgpath),
          height: 96.0,
          width: 96.0,
        ),
      );
    }

    final blockTitle = new Container(
        margin: new EdgeInsets.symmetric(horizontal: 16.0),
        alignment: FractionalOffset.center,
        child: new Text(
          this.title,
          style: this.styleTitle,
        ));

    if (this.imgpath != null) {
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
                mainAxisAlignment: MainAxisAlignment
                    .center, //Center Row contents horizontally,
                crossAxisAlignment:
                    CrossAxisAlignment.center, //Center Row contents vertically,
              ),
              margin: EdgeInsets.symmetric(horizontal: 12.0),
            )),
            blockProgressBar
          ],
        ),
      );
    } else {
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
                  new Expanded(
                    child: blockTitle,
                  ),
                ],
                mainAxisAlignment: MainAxisAlignment
                    .center, //Center Row contents horizontally,
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
}
