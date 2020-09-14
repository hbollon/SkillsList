import 'package:flutter/material.dart';
import 'package:skillslist/widgets/skill_card.dart';

class SkillBlockCard extends StatelessWidget {
  SkillBlockCard({Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    TextStyle style = TextStyle(fontFamily: 'Montserrat', fontSize: 20.0);
    return Center(
      child: ExpansionTile(
        title: Text(
          "Gestion de projet",
          style: style,
        ),
        children: [SkillCard(), SkillCard()],
      ),
    );
  }
}
