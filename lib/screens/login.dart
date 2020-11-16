import 'package:flutter/material.dart';
import 'package:skillslist/main.dart';
import 'package:skillslist/models/User.dart';
import 'package:skillslist/screens/register.dart';
import 'package:skillslist/screens/skill_block_list.dart';

import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:sprintf/sprintf.dart';

Future<bool> login(String username, String password) async {
  print("Logging in...");

  Map regUser = {
    'username': username,
    'password': password,
  };

  final url = sprintf(
      "http://%s:8080/login",
      [MyApp.ip]);
  final response = await http.post(url,
    headers: {"Content-Type": "application/json"}, 
    body: json.encode(regUser));
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

    final loginButton = Material(
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

    final registerButon = Material(
      elevation: 5.0,
      borderRadius: BorderRadius.circular(30.0),
      color: Colors.deepPurple,
      child: MaterialButton(
        minWidth: MediaQuery.of(context).size.width,
        padding: EdgeInsets.fromLTRB(20.0, 15.0, 20.0, 15.0),
        onPressed: () {
          Navigator.push(
            context,
            MaterialPageRoute(builder: (context) => RegisterPage()),
          );
        },
        child: Text("Register",
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
                    height: 140.0,
                    child: Icon(
                      Icons.supervised_user_circle,
                      size: 140.0,
                    )),
                SizedBox(height: 45.0),
                usernameField,
                SizedBox(height: 25.0),
                passwordField,
                SizedBox(
                  height: 35.0,
                ),
                loginButton,
                SizedBox(
                  height: 15.0,
                ),
                registerButon,
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
