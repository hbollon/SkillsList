// Copyright 2018 The Flutter team. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'package:flutter/material.dart';
import 'package:skillslist/screens/login.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  static String ip = "192.168.0.13";

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'SkillList',
      theme: ThemeData(primarySwatch: Colors.deepPurple),
      home: Scaffold(
        appBar: AppBar(
          title: Text('SkillList'),
        ),
        body: Center(
          child: LoginPage(),
        ),
      ),
    );
  }
}
