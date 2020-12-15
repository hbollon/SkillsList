import 'package:flutter/material.dart';
import 'package:skillslist/main.dart';
import 'package:skillslist/models/User.dart';

import 'package:skillslist/models/Skills.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:skillslist/widgets/menu_drawer.dart';
import 'package:sprintf/sprintf.dart';

class ProfilePage extends StatefulWidget {
  ProfilePage({Key key}) : super(key: key);
  @override
  _ProfilePageState createState() => _ProfilePageState();
}

class _ProfilePageState extends State<ProfilePage> {
  TextStyle style = TextStyle(fontFamily: 'Montserrat', fontSize: 20.0);

  int nbSubscribedBlocks = 0;
  int nbValidatedSkills = 0;
  int nbSkillsAvailable = 0;

  Future<bool> getData() async {
    nbSubscribedBlocks = 0;
    nbValidatedSkills = 0;
    nbSkillsAvailable = 0;

    String url;

    url = sprintf("http://%s:8080/getSubscribedSkillBlock?userId=%s",
        [MyApp.ip, User.loggedInUser.dbId]);
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
    nbSubscribedBlocks = skillblocks.length;

    for (var sb in skillblocks) {
      url = sprintf(
          "http://%s:8080/getAllSkillOfSkillblockByUser?username=%s&skillblockName=%s",
          [MyApp.ip, User.loggedInUser.username, sb.name]);

      print(url);
      final response2 = await http.get(url,
          headers: {"Content-Type": "application/json; charset=UTF-8"});
      print(response2.body);

      var jsonResponse2 = json.decode(response2.body);
      if (jsonResponse2["content"] != "") {
        var data = json.decode(jsonResponse2["content"]);
        for (var skill in data) {
          int validate = skill["validate"];
          if (validate == 1) {
            ++nbValidatedSkills;
          }
          ++nbSkillsAvailable;
        }
      }
    }
    return true;
  }

  @override
  Widget build(BuildContext context) {
    var fetch = getData();
    Image bronze = Image.asset("bronze.png");
    Image silver = Image.asset("silver.png");
    Image gold = Image.asset("gold.png");
    return Scaffold(
      appBar: AppBar(
        title: Text("Profile"),
      ),
      body: Center(
        child: Container(
            child: FutureBuilder(
                future: fetch,
                builder: (BuildContext context, AsyncSnapshot<bool> snapshot) {
                  Widget lv;
                  if (snapshot.hasData) {
                    List<Widget> medals = new List<Widget>();
                    if (nbSkillsAvailable > 0) {
                      if (nbValidatedSkills > 0) {
                        medals.add(bronze);
                      }
                      if (nbValidatedSkills / nbSkillsAvailable >= 0.5) {
                        medals.add(silver);
                      }
                      if (nbValidatedSkills == nbSkillsAvailable) {
                        medals.add(gold);
                      }
                    }
                    lv = Padding(
                      padding: const EdgeInsets.all(20.0),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.center,
                        mainAxisAlignment: MainAxisAlignment.start,
                        children: <Widget>[
                          SizedBox(
                              height: 140.0,
                              child: Icon(
                                Icons.person,
                                size: 140.0,
                              )),
                          SizedBox(height: 25.0),
                          Text(
                            "Username : " + User.loggedInUser.username,
                            style: style,
                          ),
                          SizedBox(height: 25.0),
                          Text(
                            "First name : " + User.loggedInUser.firstName,
                            style: style,
                          ),
                          SizedBox(
                            height: 25.0,
                          ),
                          Text(
                            "Last Name : " + User.loggedInUser.lastName,
                            style: style,
                          ),
                          SizedBox(
                            height: 25.0,
                          ),
                          Text(
                            "Subscribed Blocks : " +
                                nbSubscribedBlocks.toString(),
                            style: style,
                          ),
                          SizedBox(
                            height: 25.0,
                          ),
                          Text(
                            "Progress : " +
                                nbValidatedSkills.toString() +
                                "/" +
                                nbSkillsAvailable.toString() +
                                " Skills",
                            style: style,
                          ),
                          SizedBox(
                            height: 40.0,
                          ),
                          Row(
                            children: medals,
                            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                          ),
                        ],
                      ),
                    );
                  } else {
                    lv = Center(child: Container());
                  }
                  return lv;
                })),
      ),
      drawer: MenuDrawer(),
    );
  }
}
