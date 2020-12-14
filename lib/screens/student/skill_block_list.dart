import 'package:flutter/material.dart';
import 'package:skillslist/main.dart';
import 'package:skillslist/models/Skills.dart';
import 'package:skillslist/models/User.dart';
import 'package:skillslist/screens/student/skill_market.dart';
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

      final url = sprintf(
          "http://%s:8080/getAllSkillOfSkillblockByUser?username=%s&skillblockName=%s",
          [MyApp.ip, User.loggedInUser.username, name]);

      print(url);
      final response = await http.get(url,
          headers: {"Content-Type": "application/json; charset=UTF-8"});
      print(response.body);

      var jsonResponse = json.decode(response.body);
      final data = json.decode(jsonResponse["content"]);

      int nbVal = 0;
      int tot = 0;

      for (var skill in data) {
        String name = skill["skillName"];
        int dbId = skill["dbId"];
        String desc = skill["skillDesc"];
        int validate = skill["validate"];

        if (validate == 1) {
          ++nbVal;
        }
        ++tot;
      }

      double value;

      if (tot != 0) {
        value = nbVal / tot;
      } else {
        value = 0.0;
      }

      SkillBlock s = SkillBlock(name, desc, value, sbId);
      this.skillblocks.add(s);
    }
    return true;
  }

  Future<void> getRemoteData() async {
    setState(() {
      fetchUserSkillBlocks();
    });
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
                  lv = RefreshIndicator(
                      child: ListView.builder(
                        itemCount: this.skillblocks.length,
                        itemBuilder: (BuildContext context, int index) {
                          return SkillBlockRow.from(
                              this.skillblocks.elementAt(index), false);
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
