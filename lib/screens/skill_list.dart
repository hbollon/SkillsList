import 'package:flutter/material.dart';
import 'package:skillslist/widgets/skill_block_card.dart';

class SkillList extends StatelessWidget {
  SkillList({Key key}) : super(key: key);

  Widget buildRow() {
    return SkillBlockCard();
  }

  Widget buildList() {
    return ListView.builder(
        padding: EdgeInsets.all(16.0),
        itemBuilder: (context, i) {
          return buildRow();
        });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Welcome to Flutter',
      theme: ThemeData(primarySwatch: Colors.deepPurple),
      home: Scaffold(
        appBar: AppBar(
          title: Text('Welcome to Flutter'),
        ),
        body: Center(
          child: buildList(),
        ),
      ),
    );
  }
}
