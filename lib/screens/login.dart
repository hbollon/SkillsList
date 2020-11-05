import 'package:flutter/material.dart';
import 'package:skillslist/models/User.dart';
import 'package:skillslist/screens/skill_block_list.dart';

import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:sprintf/sprintf.dart';

Future<bool> login(String username, String password) async {
  print("Logging in...");
  final url = sprintf("http://192.168.43.136:8080/login?login=%s&password=%s",
      [username, password]);
  final response = await http.get(url);
  print(response.body);

  var jsonResponse = json.decode(response.body);
  if (jsonResponse["content"] == "") {
    print("Error during logging, received data empty!");
    return false;
  } else {
    final userData = json.decode(jsonResponse["content"]);
    User.loggedInUser = new User(userData["dbId"], userData["username"],
        userData["firstName"], userData["lastName"]);
    print("Successfully logged in!");
    return true;
  }
}

class LoginPage extends StatefulWidget {
  LoginPage({Key key}) : super(key: key);
  @override
  _LoginPageState createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  TextStyle style = TextStyle(fontFamily: 'Montserrat', fontSize: 20.0);
  bool _buttonIsActivated = true;

  @override
  Widget build(BuildContext context) {
    TextEditingController usernameController = new TextEditingController();
    final usernameField = TextField(
      controller: usernameController,
      obscureText: false,
      style: style,
      decoration: InputDecoration(
          contentPadding: EdgeInsets.fromLTRB(20.0, 15.0, 20.0, 15.0),
          hintText: "Username",
          border:
              OutlineInputBorder(borderRadius: BorderRadius.circular(32.0))),
    );

    TextEditingController passwordController = new TextEditingController();
    final passwordField = TextField(
      controller: passwordController,
      obscureText: true,
      style: style,
      decoration: InputDecoration(
          contentPadding: EdgeInsets.fromLTRB(20.0, 15.0, 20.0, 15.0),
          hintText: "Password",
          border:
              OutlineInputBorder(borderRadius: BorderRadius.circular(32.0))),
    );

    final loginButon = Material(
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
                  _buttonIsActivated = !_buttonIsActivated;
                });
                Future<bool> output =
                    login(usernameController.text, passwordController.text);
                output.then((value) {
                  if (value) {
                    Navigator.push(
                      context,
                      MaterialPageRoute(builder: (context) => SkillBlockList()),
                    );
                  } else {
                    Scaffold.of(context).showSnackBar(SnackBar(
                      content: Text(
                          "Logging failed! Please check your credentials."),
                    ));
                  }
                });
                setState(() {
                  _buttonIsActivated = !_buttonIsActivated;
                });
              },
        child: Text("Login",
            textAlign: TextAlign.center,
            style: style.copyWith(
                color: Colors.white, fontWeight: FontWeight.bold)),
      ),
    );

    return Scaffold(
      body: Center(
        child: Container(
          color: Colors.white,
          child: Padding(
            padding: const EdgeInsets.all(36.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.center,
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                SizedBox(
                    height: 155.0,
                    child: Icon(
                      Icons.supervised_user_circle,
                      size: 155.0,
                    )),
                SizedBox(height: 45.0),
                usernameField,
                SizedBox(height: 25.0),
                passwordField,
                SizedBox(
                  height: 35.0,
                ),
                loginButon,
                SizedBox(
                  height: 15.0,
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
