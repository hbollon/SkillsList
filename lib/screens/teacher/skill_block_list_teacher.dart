import 'package:flutter/material.dart';
import 'package:skillslist/main.dart';
import 'package:skillslist/models/Skills.dart';
import 'package:skillslist/models/User.dart';
import 'package:skillslist/screens/teacher/add_skill_block.dart';
import 'package:skillslist/widgets/menu_drawer.dart';
import 'package:skillslist/widgets/skill_block_row.dart';

import 'package:sprintf/sprintf.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;

class SkillBlockListTeacher extends StatefulWidget {
  SkillBlockListTeacher({Key key}) : super(key: key);

  @override
  _SkillBlockListTeacherState createState() => _SkillBlockListTeacherState();
}

class _SkillBlockListTeacherState extends State<SkillBlockListTeacher> {
  List<SkillBlock> skillblocks;

  _SkillBlockListTeacherState() {
    this.skillblocks = new List();
  }

  Future<bool> fetchSkillBlocks() async {
    print("Fetching Skill Blocks...");

    final url = sprintf("http://%s:8080/getAllSkillBlocks", [MyApp.ip]);
    final response = await http
        .get(url, headers: {"Content-Type": "application/json; charset=UTF-8"});
    print(response.body);

    var jsonResponse = json.decode(response.body);
    final sbData = json.decode(jsonResponse["content"]);

    this.skillblocks.clear();

    for (var sb in sbData) {
      String name = sb["blockName"];
      int sbId = sb["dbId"];
      String desc = sb["blockDesc"];
      double value = 0.0;

      SkillBlock s = SkillBlock(name, desc, value, sbId);
      this.skillblocks.add(s);
    }
    return true;
  }

  Future<void> getRemoteData() async {
    setState(() {
      fetchSkillBlocks();
    });
  }

  @override
  Widget build(BuildContext context) {
    TextStyle style = TextStyle(
        fontFamily: 'Montserrat', color: Colors.white, fontSize: 20.0);
    var fetch = fetchSkillBlocks();
    return MaterialApp(
      title: 'SkillList',
      theme: ThemeData(primarySwatch: Colors.deepPurple),
      home: Scaffold(
          appBar: AppBar(
            title: Text('All Skill Blocks'),
          ),
          drawer: MenuDrawerTeacher(),
          body: FutureBuilder(
              future: fetch,
              builder: (BuildContext context, AsyncSnapshot<bool> snapshot) {
                Widget lv;
                if (snapshot.hasData) {
                  lv = RefreshIndicator(
                      child: ListView.builder(
                        itemCount: this.skillblocks.length,
                        itemBuilder: (BuildContext context, int index) {
                          return SkillBlockRow.from(
                              this.skillblocks.elementAt(index), true);
                        },
                      ),
                      onRefresh: getRemoteData);
                } else {
                  lv = Center(child: CircularProgressIndicator());
                }
                return lv;
              }),
          floatingActionButton: FloatingActionButton(
              onPressed: () {
                Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (context) => AddSkillBlockPage()));
              },
              child: Icon(Icons.add),
              backgroundColor: Colors.deepPurple)),
    );
  }
}
