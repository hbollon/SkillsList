import 'package:flutter/material.dart';
import 'package:skillslist/main.dart';
import 'package:skillslist/models/User.dart';

import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:skillslist/screens/login.dart';
import 'package:sprintf/sprintf.dart';

Future<bool> register(String username, String password, String firstName,
    String lastName, String role) async {
  print("Sending registering form...");

  Map regRole = {
    'roleName': role,
  };

  Map regUser = {
    'username': username,
    'password': password,
    'firstName': firstName,
    'lastName': lastName,
    'userRole': regRole,
  };

  final url = sprintf("http://%s:8080/register", [MyApp.ip]);
  final response = await http.post(url,
      headers: {"Content-Type": "application/json"},
      body: json.encode(regUser));

  print(json.encode(regUser));
  print(response.body);

  var jsonResponse = json.decode(response.body);
  if (jsonResponse["content"] == "" || jsonResponse["success"] == false) {
    print("Error: Could not register user");
    return false;
  } else {
    final userData = json.decode(jsonResponse["content"]);
    print("Successfully registered!");
    return true;
  }
}

class RegisterPage extends StatefulWidget {
  RegisterPage({Key key}) : super(key: key);
  @override
  _RegisterPageState createState() => _RegisterPageState();
}

class _RegisterPageState extends State<RegisterPage> {
  TextStyle style =
      TextStyle(fontFamily: 'Montserrat', fontSize: 20.0, color: Colors.black);
  bool _buttonIsActivated = true;
  TextEditingController usernameController = new TextEditingController();
  TextEditingController passwordController = new TextEditingController();
  TextEditingController firstNameController = new TextEditingController();
  TextEditingController lastNameController = new TextEditingController();
  var formKey = GlobalKey<FormState>();

  int roleValue = 0;

  @override
  Widget build(BuildContext context) {
    return Scaffold(body: Builder(builder: (BuildContext context) {
      var roleField = DropdownButton(
          style: style,
          value: roleValue,
          hint: Text("Select a role"),
          items: [
            DropdownMenuItem(value: 0, child: Text("Student", style: style)),
            DropdownMenuItem(value: 1, child: Text("Teacher", style: style))
          ],
          onChanged: (value) {
            setState(() {
              roleValue = value;
            });
          });

      var usernameField = TextField(
        controller: usernameController,
        obscureText: false,
        style: style,
        decoration: InputDecoration(
            contentPadding: EdgeInsets.fromLTRB(20.0, 15.0, 20.0, 15.0),
            hintText: "Username",
            border:
                OutlineInputBorder(borderRadius: BorderRadius.circular(32.0))),
      );

      var passwordField = TextField(
        controller: passwordController,
        obscureText: true,
        style: style,
        decoration: InputDecoration(
            contentPadding: EdgeInsets.fromLTRB(20.0, 15.0, 20.0, 15.0),
            hintText: "Password",
            border:
                OutlineInputBorder(borderRadius: BorderRadius.circular(32.0))),
      );

      var firstNameField = TextField(
        controller: firstNameController,
        obscureText: false,
        style: style,
        decoration: InputDecoration(
            contentPadding: EdgeInsets.fromLTRB(20.0, 15.0, 20.0, 15.0),
            hintText: "First name",
            border:
                OutlineInputBorder(borderRadius: BorderRadius.circular(32.0))),
      );

      var lastNameField = TextField(
        controller: lastNameController,
        obscureText: false,
        style: style,
        decoration: InputDecoration(
            contentPadding: EdgeInsets.fromLTRB(20.0, 15.0, 20.0, 15.0),
            hintText: "Last Name",
            border:
                OutlineInputBorder(borderRadius: BorderRadius.circular(32.0))),
      );

      var registerButton = Material(
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

                  if (usernameController.text != "" &&
                      passwordController.text != "" &&
                      firstNameController.text != "" &&
                      lastNameController.text != "") {
                    Future<bool> output = register(
                        usernameController.text,
                        passwordController.text,
                        firstNameController.text,
                        lastNameController.text,
                        (roleValue == 0) ? "Student" : "Teacher");
                    output.then((value) {
                      if (value) {
                        Scaffold.of(context).showSnackBar(SnackBar(
                          content: Text("Successfully registered !"),
                        ));
                        Navigator.push(
                          context,
                          MaterialPageRoute(builder: (context) => LoginPage()),
                        );
                      } else {
                        Scaffold.of(context).showSnackBar(SnackBar(
                          content:
                              Text("Registration failure, please try again"),
                        ));
                      }
                    });
                  } else {
                    Scaffold.of(context).showSnackBar(SnackBar(
                      content: Text("Please enter valid credentials"),
                    ));
                  }
                  setState(() {
                    _buttonIsActivated = true;
                  });
                },
          child: Text("Register",
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
                roleField,
                SizedBox(height: 25.0),
                usernameField,
                SizedBox(height: 25.0),
                passwordField,
                SizedBox(height: 25.0),
                firstNameField,
                SizedBox(height: 25.0),
                lastNameField,
                SizedBox(
                  height: 64.0,
                ),
                registerButton,
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
