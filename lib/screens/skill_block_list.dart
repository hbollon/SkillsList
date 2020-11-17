import 'package:flutter/material.dart';
import 'package:skillslist/main.dart';
import 'package:skillslist/models/SkillBlock.dart';
import 'package:skillslist/widgets/menu_drawer.dart';
import 'package:skillslist/widgets/skill_block_row.dart';

import 'package:sprintf/sprintf.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;

class SkillBlockList extends StatefulWidget {
  SkillBlockList({Key key}) : super(key: key);

  @override
  _SkillBlockListState createState() => _SkillBlockListState();
}

class _SkillBlockListState extends State<SkillBlockList> {
  List<SkillBlock> skillblocks;

  _SkillBlockListState() {
    this.skillblocks = new List();
  }

  Future<bool> fetchUserSkillBlocks() async {
    print("Fetching Skill Blocks...");

    final url = sprintf("http://%s:8080/getAllSkillBlocks", [MyApp.ip]);
    final response = await http.get(url, headers: {"Content-Type": "text"});
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

  @override
  Widget build(BuildContext context) {
    TextStyle style = TextStyle(
        fontFamily: 'Montserrat', color: Colors.white, fontSize: 20.0);
    var fetch = fetchUserSkillBlocks();
    return MaterialApp(
      title: 'SkillList',
      theme: ThemeData(primarySwatch: Colors.deepPurple),
      home: Scaffold(
          appBar: AppBar(
            title: Text('Your Skill Blocks'),
          ),
          drawer: MenuDrawer(),
          body: FutureBuilder(
              future: fetch,
              builder: (BuildContext context, AsyncSnapshot<bool> snapshot) {
                Widget lv;
                if (snapshot.hasData) {
                  lv = ListView.builder(
                    itemCount: this.skillblocks.length,
                    itemBuilder: (BuildContext context, int index) {
                      return SkillBlockRow.from(
                          this.skillblocks.elementAt(index));
                    },
                  );
                } else {
                  lv = Text("Fetching skill blocks...");
                }
                return lv;
              }),
          floatingActionButton: FloatingActionButton(
              onPressed: () {},
              child: Icon(Icons.add),
              backgroundColor: Colors.deepPurple)),
    );
  }
}
