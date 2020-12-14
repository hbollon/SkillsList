import 'package:flutter/material.dart';
import 'package:skillslist/main.dart';
import 'package:skillslist/models/Skills.dart';
import 'package:skillslist/models/User.dart';
import 'package:skillslist/widgets/menu_drawer.dart';
import 'package:skillslist/widgets/skill_block_row.dart';

import 'package:skillslist/utils.dart';

import 'package:sprintf/sprintf.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;

class SkillMarketPage extends StatefulWidget {
  SkillMarketPage(List<int> subscribed, {Key key}) : super(key: key) {
    this.subscribed = subscribed;
  }

  List<int> subscribed;

  @override
  _SkillMarketPageState createState() => _SkillMarketPageState(subscribed);
}

class _SkillMarketPageState extends State<SkillMarketPage> {
  List<SkillBlock> skillblocks = new List<SkillBlock>();
  List<int> subscribed;
  List<bool> switches = new List<bool>();

  final TextStyle textStyleSkillBlockName = new TextStyle(
      fontFamily: 'Montserrat',
      color: Colors.white,
      fontSize: 13.0,
      fontWeight: FontWeight.bold);

  final TextStyle textStyleSkillDesc = new TextStyle(
    fontFamily: 'Montserrat',
    color: Colors.white,
    fontSize: 13.0,
  );

  _SkillMarketPageState(this.subscribed);

  Future<bool> subscriptionRequest(String sb, bool value) async {
    String url;
    if (value) {
      url = sprintf("http://%s:8080/subscribe", [MyApp.ip]);
    } else {
      url = sprintf("http://%s:8080/unsubscribe", [MyApp.ip]);
    }
    Map pair = {
      "first": User.loggedInUser.username,
      "second": sb,
    };
    final response = await http.post(url,
        headers: {"Content-Type": "application/json; charset=UTF-8"},
        body: json.encode(pair));

    print(response.body);

    return true;
  }

  Future<bool> fetchSkillBlocks() async {
    final url = sprintf("http://%s:8080/getAllSkillBlocks", [MyApp.ip]);

    print(url);
    final response = await http
        .get(url, headers: {"Content-Type": "application/json; charset=UTF-8"});
    print(response.body);

    var jsonResponse = json.decode(response.body);
    final data = json.decode(jsonResponse["content"]);

    this.skillblocks.clear();

    for (var sb in data) {
      String name = sb["blockName"];
      int sbId = sb["dbId"];
      String desc = sb["blockDesc"];

      SkillBlock s = SkillBlock(name, desc, 0.0, sbId);
      this.skillblocks.add(s);
    }
    return true;
  }

  @override
  Widget build(BuildContext context) {
    var fetch = fetchSkillBlocks();
    return MaterialApp(
        title: "SkillList",
        theme: ThemeData(primarySwatch: Colors.deepPurple),
        home: Scaffold(
            appBar: AppBar(
              title: Text("Subscribe to new skills"),
            ),
            drawer: MenuDrawer(),
            body: Container(
                child: FutureBuilder(
                    future: fetch,
                    builder:
                        (BuildContext context, AsyncSnapshot<bool> snapshot) {
                      Widget lv;
                      if (snapshot.hasData) {
                        for (var sb in skillblocks) {
                          if (subscribed.contains(sb.sbId)) {
                            switches.add(true);
                          } else {
                            switches.add(false);
                          }
                        }
                        lv = ListView.builder(
                          itemCount: this.skillblocks.length,
                          itemBuilder: (BuildContext context, int index) {
                            SkillBlock skillblock =
                                this.skillblocks.elementAt(index);
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
                                        leading: Icon(Icons.list,
                                            color: Colors.white),
                                        title: Text(
                                          skillblock.name,
                                          style: this.textStyleSkillBlockName,
                                        ),
                                      ),
                                      ExpansionTile(
                                        title: Text("See more details",
                                            style:
                                                this.textStyleSkillBlockName),
                                        children: [
                                          Padding(
                                              padding: EdgeInsets.only(
                                                  top: 20.0,
                                                  bottom: 20.0,
                                                  left: 4.0,
                                                  right: 4.0),
                                              child: Text(
                                                skillblock.desc,
                                                style: this.textStyleSkillDesc,
                                              ))
                                        ],
                                      ),
                                      Row(
                                        mainAxisAlignment:
                                            MainAxisAlignment.end,
                                        children: <Widget>[
                                          Text(
                                            "Subscribe",
                                            style: this.textStyleSkillDesc,
                                          ),
                                          const SizedBox(width: 8),
                                          Switch(
                                              value: switches.elementAt(index),
                                              activeTrackColor:
                                                  Colors.lightGreenAccent,
                                              activeColor: Colors.green,
                                              onChanged: (value) {
                                                setState(() {
                                                  this.switches[index] = value;
                                                  subscriptionRequest(
                                                      skillblock.name, value);
                                                });
                                              })
                                        ],
                                      ),
                                    ],
                                  ),
                                ));
                          },
                        );
                      } else {
                        lv = Center(child: Container());
                      }
                      return lv;
                    }))));
  }
}
