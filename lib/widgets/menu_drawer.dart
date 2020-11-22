import 'package:flutter/material.dart';
import 'package:skillslist/screens/skill_block_list.dart';

class MenuDrawer extends StatelessWidget {
  const MenuDrawer({Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    TextStyle style = TextStyle(
        fontFamily: 'Montserrat', color: Colors.white, fontSize: 32.0);
    return Container(
        child: Drawer(
      child: ListView(
        padding: EdgeInsets.zero,
        children: [
          DrawerHeader(
              decoration: BoxDecoration(color: Colors.deepPurple),
              child: Text(
                "Menu",
                style: style,
              )),
          ListTile(leading: Icon(Icons.person), title: Text("Profile")),
          ListTile(
            leading: Icon(Icons.list),
            title: Text("Your Skills"),
            onTap: () {
              Navigator.pop(context);
              Navigator.push(context,
                  MaterialPageRoute(builder: (context) => SkillBlockList()));
            },
          ),
          ListTile(leading: Icon(Icons.stars), title: Text("Achievements")),
          ListTile(leading: Icon(Icons.exit_to_app), title: Text("Logout"))
        ],
      ),
    ));
  }
}
