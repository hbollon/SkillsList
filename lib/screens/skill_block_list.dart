import 'package:flutter/material.dart';
import 'package:skillslist/main.dart';
import 'package:skillslist/models/Skills.dart';
import 'package:skillslist/models/User.dart';
import 'package:skillslist/screens/skill_market.dart';
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

    final url = sprintf("http://%s:8080/getSubscribedSkillBlock?userId=%s",
        [MyApp.ip, User.loggedInUser.dbId]);
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
              onPressed: () {
                List<int> ids = new List();
                for (var i in skillblocks) {
                  ids.add(i.sbId);
                }
                Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (context) => SkillMarketPage(ids)));
              },
              child: Icon(Icons.add),
              backgroundColor: Colors.deepPurple)),
    );
  }
}
