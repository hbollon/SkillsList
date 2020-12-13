import 'package:flutter/material.dart';
import 'package:skillslist/screens/login.dart';
import 'package:skillslist/screens/student/skill_block_list.dart';
import 'package:skillslist/screens/student/profile.dart';
import 'package:skillslist/screens/teacher/skill_block_list_teacher.dart';
import 'package:skillslist/screens/teacher/student_list.dart';
import 'package:skillslist/models/User.dart';

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
          ListTile(
            leading: Icon(Icons.person),
            title: Text("Profile"),
            onTap: () {
              Navigator.pop(context);
              Navigator.push(context,
                  MaterialPageRoute(builder: (context) => ProfilePage()));
            },
          ),
          ListTile(
            leading: Icon(Icons.list),
            title: Text("Your Skills"),
            onTap: () {
              Navigator.pop(context);
              Navigator.push(context,
                  MaterialPageRoute(builder: (context) => SkillBlockList()));
            },
          ),
          ListTile(
            leading: Icon(Icons.exit_to_app),
            title: Text("Logout"),
            onTap: () {
              User.loggedInUser = null;
              Navigator.pop(context);
              Navigator.push(context,
                  MaterialPageRoute(builder: (context) => LoginPage()));
            },
          )
        ],
      ),
    ));
  }
}

class MenuDrawerTeacher extends StatelessWidget {
  const MenuDrawerTeacher({Key key}) : super(key: key);

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
          ListTile(
            leading: Icon(Icons.list),
            title: Text("All Skillblocks"),
            onTap: () {
              Navigator.pop(context);
              Navigator.push(
                  context,
                  MaterialPageRoute(
                      builder: (context) => SkillBlockListTeacher()));
            },
          ),
          ListTile(
              leading: Icon(Icons.people),
              title: Text("Students"),
              onTap: () {
                User.loggedInUser = null;
                Navigator.pop(context);
                Navigator.push(context,
                    MaterialPageRoute(builder: (context) => StudentListPage()));
              }),
          ListTile(
            leading: Icon(Icons.exit_to_app),
            title: Text("Logout"),
            onTap: () {
              User.loggedInUser = null;
              Navigator.pop(context);
              Navigator.push(context,
                  MaterialPageRoute(builder: (context) => LoginPage()));
            },
          )
        ],
      ),
    ));
  }
}
