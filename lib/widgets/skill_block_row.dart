import 'package:flutter/material.dart';
import 'package:skillslist/models/Skills.dart';
import 'package:skillslist/screens/student/skill_list.dart';
import 'package:skillslist/screens/teacher/skill_list_teacher.dart';

import '../utils.dart';

class SkillBlockRow extends StatelessWidget {
  SkillBlockRow(this.title, this.imgPath, this.value, {Key key})
      : super(key: key);

  SkillBlockRow.from(SkillBlock sb, bool teacherMode, {Key key})
      : super(key: key) {
    this.imgPath = null;
    this.title = sb.name;
    this.value = sb.value;
    this.teacherMode = teacherMode;
  }

  String imgPath;
  String title;
  double value;
  bool teacherMode;

  final TextStyle style = TextStyle(
      fontFamily: 'Montserrat',
      fontSize: 20.0,
      color: Colors.white,
      fontWeight: FontWeight.bold);

  @override
  Widget build(BuildContext context) {
    Container blockThumbnail;
    if (this.imgPath != null) {
      blockThumbnail = new Container(
        margin: new EdgeInsets.symmetric(horizontal: 12.0),
        alignment: FractionalOffset.centerLeft,
        child: new Image(
          image: new AssetImage(imgPath),
          height: 64.0,
          width: 64.0,
        ),
      );
    }
    final blockProgress = new Container(
        margin: new EdgeInsets.symmetric(horizontal: 16.0),
        alignment: FractionalOffset.centerRight,
        child: new SizedBox(
            height: 52,
            width: 52,
            child: new CircularProgressIndicator(
              value: this.value,
              strokeWidth: 6,
              backgroundColor: Colors.black,
              valueColor: new AlwaysStoppedAnimation<Color>(
                  Utils.getColorFromProgress(this.value)),
            )));
    final blockTitle = new Container(
        margin: new EdgeInsets.symmetric(horizontal: 16.0),
        alignment: FractionalOffset.center,
        child: new Text(
          this.title,
          style: this.style,
        ));
    final blockCard = new Container(
      height: 124.0,
      decoration: new BoxDecoration(
        color: new Color(0xFF333366),
        shape: BoxShape.rectangle,
        borderRadius: new BorderRadius.circular(8.0),
        boxShadow: <BoxShadow>[
          new BoxShadow(
            color: Colors.black12,
            blurRadius: 10.0,
            offset: new Offset(0.0, 10.0),
          ),
        ],
      ),
    );
    if (this.imgPath == null) {
      return new GestureDetector(
        child: Container(
            height: 120.0,
            margin: const EdgeInsets.symmetric(
              vertical: 16.0,
              horizontal: 24.0,
            ),
            child: new Stack(
              children: (teacherMode)
                  ? <Widget>[
                      blockCard,
                      blockTitle,
                    ]
                  : <Widget>[
                      blockCard,
                      blockTitle,
                      blockProgress,
                    ],
            )),
        onTap: () {
          Navigator.push(
            context,
            MaterialPageRoute(
                builder: (context) => (teacherMode)
                    ? SkillListTeacherPage(this.value, this.title, this.imgPath)
                    : SkillListPage(this.value, this.title, this.imgPath)),
          );
        },
      );
    } else {
      return new GestureDetector(
        child: Container(
            height: 120.0,
            margin: const EdgeInsets.symmetric(
              vertical: 16.0,
              horizontal: 24.0,
            ),
            child: new Stack(
              children: <Widget>[
                blockCard,
                blockTitle,
                blockProgress,
              ],
            )),
        onTap: () {
          Navigator.push(
            context,
            MaterialPageRoute(
                builder: (context) =>
                    SkillListPage(this.value, this.title, this.imgPath)),
          );
        },
      );
    }
  }
}
