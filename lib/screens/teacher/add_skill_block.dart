import 'package:flutter/material.dart';
import 'package:skillslist/main.dart';
import 'package:skillslist/models/User.dart';

import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:skillslist/screens/login.dart';
import 'package:skillslist/screens/teacher/skill_block_list_teacher.dart';
import 'package:sprintf/sprintf.dart';

class AddSkillBlockPage extends StatefulWidget {
  AddSkillBlockPage({Key key}) : super(key: key);
  @override
  _AddSkillBlockPageState createState() => _AddSkillBlockPageState();
}

class _AddSkillBlockPageState extends State<AddSkillBlockPage> {
  TextStyle style =
      TextStyle(fontFamily: 'Montserrat', fontSize: 20.0, color: Colors.black);
  bool _buttonIsActivated = true;
  TextEditingController nameController = new TextEditingController();
  TextEditingController descriptionController = new TextEditingController();
  final formKey = GlobalKey<FormState>();

  Future<bool> addBlockRequest(String name, String desc) async {}

  @override
  Widget build(BuildContext context) {
    return Scaffold(body: Builder(builder: (BuildContext context) {
      final nameField = TextField(
        controller: nameController,
        obscureText: false,
        style: style,
        decoration: InputDecoration(
            contentPadding: EdgeInsets.fromLTRB(20.0, 15.0, 20.0, 15.0),
            hintText: "Block Name",
            border:
                OutlineInputBorder(borderRadius: BorderRadius.circular(32.0))),
      );

      final descriptionField = TextField(
        controller: descriptionController,
        keyboardType: TextInputType.multiline,
        maxLines: 20,
        obscureText: false,
        style: style,
        decoration: InputDecoration(
            contentPadding: EdgeInsets.fromLTRB(20.0, 15.0, 20.0, 15.0),
            hintText: "Description",
            border:
                OutlineInputBorder(borderRadius: BorderRadius.circular(32.0))),
      );

      final createButton = Material(
        elevation: 5.0,
        borderRadius: BorderRadius.circular(30.0),
        color: _buttonIsActivated ? Colors.deepPurple : Colors.grey,
        child: MaterialButton(
          minWidth: MediaQuery.of(context).size.width,
          padding: EdgeInsets.fromLTRB(20.0, 15.0, 20.0, 15.0),
          onPressed: !_buttonIsActivated
              ? null
              : () {
                  setState(() {
                    _buttonIsActivated = false;
                  });

                  if (nameController.text != "" &&
                      descriptionController.text != "") {
                    Future<bool> output;
                    output.then((value) {
                      if (value) {
                        Scaffold.of(context).showSnackBar(SnackBar(
                          content: Text("New block created !"),
                        ));
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                              builder: (context) => SkillBlockListTeacher()),
                        );
                      } else {
                        Scaffold.of(context).showSnackBar(SnackBar(
                          content: Text("Failed to create skill block"),
                        ));
                      }
                    });
                  } else {
                    Scaffold.of(context).showSnackBar(SnackBar(
                      content: Text("Please enter valid name and description"),
                    ));
                  }
                  setState(() {
                    _buttonIsActivated = true;
                  });
                },
          child: Text("Create",
              textAlign: TextAlign.center,
              style: style.copyWith(
                  color: Colors.white, fontWeight: FontWeight.bold)),
        ),
      );
      return Center(
        child: Container(
          color: Colors.white,
          child: Padding(
            padding: const EdgeInsets.all(36.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.center,
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                SizedBox(height: 45.0),
                nameField,
                SizedBox(height: 25.0),
                SizedBox(child: descriptionField, height: 360),
                SizedBox(
                  height: 64.0,
                ),
                createButton,
                SizedBox(
                  height: 15.0,
                ),
              ],
            ),
          ),
        ),
      );
    }));
  }
}
