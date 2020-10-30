// Copyright 2018 The Flutter team. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'package:flutter/material.dart';
import 'package:skillslist/screens/login.dart';

import 'package:web_socket_channel/io.dart';
import 'package:web_socket_channel/status.dart' as status;

void main() async {
  var channel = IOWebSocketChannel.connect("ws://192.168.43.136:8080/api");
  channel.sink.add("Allo ???");
  channel.stream.listen((msg) {
    channel.sink.close(status.goingAway);
  });
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
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
          child: LoginPage(),
        ),
      ),
    );
  }
}
