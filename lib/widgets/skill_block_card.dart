import 'package:flutter/material.dart';
import 'package:skillslist/widgets/skill_card.dart';

class SkillBlockCard extends StatelessWidget {
  SkillBlockCard({Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Center(
      child: ExpansionTile(
        title: Text("Gestion de projet"),
        children: [SkillCard(), SkillCard()],
      ),
    );
  }
}
