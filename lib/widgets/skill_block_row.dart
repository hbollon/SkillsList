import 'package:flutter/material.dart';
import 'package:skillslist/widgets/skill_list.dart';

import '../utils.dart';

class SkillBlockRow extends StatelessWidget {
  SkillBlockRow(this.title, this.imgPath, this.value, {Key key})
      : super(key: key);

  final String imgPath;
  final String title;
  final double value;

  final TextStyle style = TextStyle(
      fontFamily: 'Montserrat',
      fontSize: 20.0,
      color: Colors.white,
      fontWeight: FontWeight.bold);

  @override
  Widget build(BuildContext context) {
    final blockThumbnail = new Container(
      margin: new EdgeInsets.symmetric(horizontal: 12.0),
      alignment: FractionalOffset.centerLeft,
      child: new Image(
        image: new AssetImage(imgPath),
        height: 64.0,
        width: 64.0,
      ),
    );
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
              blockThumbnail,
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
