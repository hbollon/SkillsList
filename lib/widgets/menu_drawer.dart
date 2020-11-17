import 'package:flutter/material.dart';

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
          ListTile(leading: Icon(Icons.stars), title: Text("Trophies"))
        ],
      ),
    ));
  }
}
