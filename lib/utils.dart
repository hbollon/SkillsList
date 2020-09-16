import 'package:flutter/material.dart';

class Utils {
  static Color getColorFromProgress(double value) {
    if (value >= 0.8)
      return Colors.green;
    else if (value >= 0.4) return Colors.orange[600];

    return Colors.red[700];
  }
}
