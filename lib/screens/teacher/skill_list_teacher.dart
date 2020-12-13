import 'package:flutter/material.dart';
import 'package:expansion_card/expansion_card.dart';
import 'package:skillslist/main.dart';
import 'package:skillslist/models/Skills.dart';
import 'package:skillslist/widgets/menu_drawer.dart';

import 'package:skillslist/models/User.dart';
import 'package:skillslist/screens/teacher/add_skill.dart';

import 'package:skillslist/utils.dart';

import 'package:sprintf/sprintf.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;

class SkillListTeacherPage extends StatefulWidget {
  double value;
  String title;
  String imgpath;
  SkillListTeacherPage(this.value, this.title, this.imgpath, {Key key})
      : super(key: key);

  @override
  _SkillListTeacherPageState createState() =>
      _SkillListTeacherPageState(value, title, imgpath);
}

class _SkillListTeacherPageState extends State<SkillListTeacherPage> {
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

  _SkillListTeacherPageState(this.value, this.title, this.imgpath);

  Future<bool> fetchSkills() async {
    final url = sprintf(
        "http://%s:8080/getSkills?blockName=%s", [MyApp.ip, this.title]);

    print(url);
    final response = await http
        .get(url, headers: {"Content-Type": "application/json; charset=UTF-8"});
    print(response.body);

    var jsonResponse = json.decode(response.body);
    final data = json.decode(jsonResponse["content"]);

    this.skills.clear();

    for (var skill in data) {
      String name = skill["skillName"];
      int dbId = skill["dbId"];
      String desc = skill["skillDesc"];
      int validate = skill["validate"];

      Skill s = Skill(name, desc, validate);
      this.skills.add(s);
    }
    return true;
  }

  Future<bool> deleteSkill(int index) async {
    final url = sprintf("http://%s:8080/deleteSkill", [MyApp.ip]);

    Skill s = this.skills[index];

    Map skillMap = {
      'skillName': s.name,
      'skillDesc': s.desc,
      'autoValidate': false,
    };

    Map map = {
      'skillblockName': this.title,
      'skill': skillMap,
    };

    print(json.encode(map));

    print(url);
    final response = await http.post(url,
        headers: {"Content-Type": "application/json; charset=UTF-8"},
        body: json.encode(map));
    print(response.body);

    //var jsonResponse = json.decode(response.body);
    //final data = json.decode(jsonResponse["content"]);

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
            drawer: MenuDrawerTeacher(),
            body: Container(
                child: FutureBuilder(
                    future: fetch,
                    builder:
                        (BuildContext context, AsyncSnapshot<bool> snapshot) {
                      Widget lv;
                      if (snapshot.hasData) {
                        print(this.skills.length);
                        lv = ListView.builder(
                          itemCount: this.skills.length,
                          itemBuilder: (BuildContext context, int index) {
                            Skill skill;
                            skill = this.skills.elementAt(index);

                            return Card(
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
                                        trailing: IconButton(
                                          icon: Icon(Icons.delete,
                                              size: 20.0, color: Colors.white),
                                          onPressed: () {
                                            Future<bool> output =
                                                deleteSkill(index);
                                            output.whenComplete(() {
                                              setState(() {
                                                skills.removeAt(index);
                                              });
                                            });
                                          },
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
                                                style: this.textStyleSkillDesc,
                                              ))
                                        ],
                                      ),
                                    ],
                                  ),
                                ));
                          },
                        );
                      } else {
                        lv = Center(child: CircularProgressIndicator());
                      }
                      return lv;
                    })),
            floatingActionButton: FloatingActionButton(
                onPressed: () {
                  Navigator.pop(context);
                  Navigator.push(
                      context,
                      MaterialPageRoute(
                          builder: (context) => AddSkillPage(this.title)));
                },
                child: Icon(Icons.add),
                backgroundColor: Colors.deepPurple)));
  }
}
