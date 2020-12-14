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

class StudentRequestsPage extends StatefulWidget {
  final User student;
  StudentRequestsPage(this.student, {Key key}) : super(key: key);

  @override
  _StudentRequestsPageState createState() => _StudentRequestsPageState(student);
}

class _StudentRequestsPageState extends State<StudentRequestsPage> {
  User student;
  List<Skill> skills = new List<Skill>();

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

  _StudentRequestsPageState(this.student);

  Future<bool> fetchSkillRequests() async {
    String url;

    url = sprintf("http://%s:8080/getSubscribedSkillBlock?userId=%s",
        [MyApp.ip, student.dbId]);
    final response = await http
        .get(url, headers: {"Content-Type": "application/json; charset=UTF-8"});
    print(response.body);

    var jsonResponse = json.decode(response.body);
    final sbData = json.decode(jsonResponse["content"]);

    List<SkillBlock> skillblocks = new List<SkillBlock>();

    for (var sb in sbData) {
      String name = sb["blockName"];
      int sbId = sb["dbId"];
      String desc = sb["blockDesc"];
      double value = 0.0;

      SkillBlock s = SkillBlock(name, desc, value, sbId);
      skillblocks.add(s);
    }

    this.skills.clear();

    for (var sb in skillblocks) {
      url = sprintf(
          "http://%s:8080/getAllSkillOfSkillblockByUser?username=%s&skillblockName=%s",
          [MyApp.ip, student.username, sb.name]);

      print(url);
      final response2 = await http.get(url,
          headers: {"Content-Type": "application/json; charset=UTF-8"});
      print(response2.body);

      var jsonResponse2 = json.decode(response2.body);
      final data = json.decode(jsonResponse2["content"]);

      for (var skill in data) {
        String name = skill["skillName"];
        int dbId = skill["dbId"];
        String desc = skill["skillDesc"];
        int validate = skill["validate"];

        Skill s = Skill.bis(name, desc, sb.name);
        if (validate == 0) {
          this.skills.add(s);
        }
      }
    }
    return true;
  }

  Future<bool> validateSkill(int index) async {
    final url = sprintf("http://%s:8080/validateSkill", [MyApp.ip]);

    Skill s = this.skills[index];

    Map map = {
      'connectedUsername': "admin",
      'username': student.username,
      'skillblockName': s.block,
      'skillName': s.name,
    };

    print(url);
    final response = await http.post(url,
        headers: {"Content-Type": "application/json; charset=UTF-8"},
        body: json.encode(map));
    print(response.body);

    //var jsonResponse = json.decode(response.body);
    //final data = json.decode(jsonResponse["content"]);

    return true;
  }

  Future<bool> denySkill(int index) async {
    final url = sprintf("http://%s:8080/cancelSkillRequest", [MyApp.ip]);

    Skill s = this.skills[index];

    Map map = {
      'connectedUsername': "admin",
      'username': student.username,
      'skillblockName': s.block,
      'skillName': s.name,
    };

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
    var fetch = fetchSkillRequests();
    return MaterialApp(
        title: "SkillList",
        theme: ThemeData(primarySwatch: Colors.deepPurple),
        home: Scaffold(
          appBar: AppBar(
            title: Text("Requests from " + student.firstName),
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
                                      leading:
                                          Icon(Icons.star, color: Colors.white),
                                      title: Text(
                                        skill.name,
                                        style: this.textStyleSkillName,
                                      ),
                                      trailing: Row(
                                          mainAxisSize: MainAxisSize.min,
                                          children: <Widget>[
                                            IconButton(
                                              icon: Icon(Icons.clear,
                                                  size: 20.0,
                                                  color: Colors.white),
                                              onPressed: () {
                                                Future<bool> output =
                                                    denySkill(index);
                                                output.whenComplete(() {
                                                  setState(() {
                                                    skills.removeAt(index);
                                                  });
                                                });
                                              },
                                            ),
                                            IconButton(
                                              icon: Icon(Icons.done,
                                                  size: 20.0,
                                                  color: Colors.white),
                                              onPressed: () {
                                                Future<bool> output =
                                                    validateSkill(index);
                                                output.whenComplete(() {
                                                  setState(() {
                                                    skills.removeAt(index);
                                                  });
                                                });
                                              },
                                            ),
                                          ]),
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
        ));
  }
}
