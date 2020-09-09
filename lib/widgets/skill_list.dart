import 'package:flutter/material.dart';
import 'package:skillslist/widgets/skill_block_card.dart';
import 'package:skillslist/widgets/skill_card.dart';

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
    return buildList();
  }
}
