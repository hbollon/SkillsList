import 'package:flutter/material.dart';
import 'package:expansion_card/expansion_card.dart';
import 'package:skillslist/main.dart';
import 'package:skillslist/models/Skills.dart';
import 'package:skillslist/screens/login.dart';
import 'package:skillslist/widgets/menu_drawer.dart';

import 'package:skillslist/models/User.dart';
import 'package:skillslist/screens/teacher/add_skill.dart';
import 'package:skillslist/screens/teacher/student_requests.dart';

import 'package:skillslist/utils.dart';

import 'package:sprintf/sprintf.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;

class StudentListPage extends StatefulWidget {
  StudentListPage({Key key}) : super(key: key);

  @override
  _StudentListPageState createState() => _StudentListPageState();
}

class _StudentListPageState extends State<StudentListPage> {
  List<User> users = new List<User>();

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

  _StudentListPageState();

  Future<bool> fetchStudents() async {
    final url = sprintf("http://%s:8080/getAllUserWithRole", [MyApp.ip]);

    Map map = {
      'roleName': "Student",
    };

    print(url);
    final response = await http.post(url,
        headers: {"Content-Type": "application/json; charset=UTF-8"},
        body: json.encode(map));
    print(response.body);

    var jsonResponse = json.decode(response.body);
    final data = json.decode(jsonResponse["content"]);

    this.users.clear();

    for (var user in data) {
      String username = user["username"];
      int dbId = user["dbId"];
      String firstName = user["firstName"];
      String lastName = user["lastName"];

      User u = User(dbId, username, firstName, lastName, null);
      this.users.add(u);
    }
    return true;
  }

  @override
  Widget build(BuildContext context) {
    var fetch = fetchStudents();
    return MaterialApp(
        title: "SkillList",
        theme: ThemeData(primarySwatch: Colors.deepPurple),
        home: Scaffold(
          appBar: AppBar(
            title: Text("Students"),
          ),
          drawer: MenuDrawerTeacher(),
          body: Container(
              child: FutureBuilder(
                  future: fetch,
                  builder:
                      (BuildContext context, AsyncSnapshot<bool> snapshot) {
                    Widget lv;
                    if (snapshot.hasData) {
                      print(this.users.length);
                      lv = ListView.builder(
                        itemCount: this.users.length,
                        itemBuilder: (BuildContext context, int index) {
                          User user;
                          user = this.users.elementAt(index);

                          return GestureDetector(
                            child: Card(
                                color: new Color(0xFF333366),
                                margin: EdgeInsets.all(6.0),
                                child: Padding(
                                  padding: EdgeInsets.only(
                                      top: 8.0, right: 8.0, bottom: 8.0),
                                  child: Column(
                                    mainAxisSize: MainAxisSize.min,
                                    children: <Widget>[
                                      ListTile(
                                        leading: Icon(Icons.person,
                                            color: Colors.white),
                                        title: Text(
                                          user.firstName + " " + user.lastName,
                                          style: this.textStyleSkillName,
                                        ),
                                      ),
                                    ],
                                  ),
                                )),
                            onTap: () {
                              Navigator.push(
                                  context,
                                  MaterialPageRoute(
                                    builder: (context) =>
                                        StudentRequestsPage(users[index]),
                                  ));
                            },
                          );
                        },
                      );
                    } else {
                      lv = Center(child: Container());
                    }
                    return lv;
                  })),
        ));
  }
}
