import 'package:flutter/material.dart';
import 'package:skillslist/widgets/skill_block_row.dart';

class SkillBlockList extends StatelessWidget {
  SkillBlockList({Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    TextStyle style = TextStyle(fontFamily: 'Montserrat', fontSize: 20.0);
    return MaterialApp(
      title: 'Welcome to Flutter',
      theme: ThemeData(primarySwatch: Colors.deepPurple),
      home: Scaffold(
          appBar: AppBar(
            title: Text('Welcome to Flutter'),
          ),
          body: ListView(
            children: [
              SkillBlockRow("Python", "assets/python.png", 0.3),
              SkillBlockRow("Java", "assets/java.png", 0.6),
              SkillBlockRow("C++", "assets/cpp.png", 0.9),
            ],
          )),
    );
  }
}
