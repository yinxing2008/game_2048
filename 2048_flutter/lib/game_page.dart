import 'package:flutter/material.dart';
import 'package:tzfe/game_panel.dart';

import 'color_util.dart';

class GamePage extends StatefulWidget {
  const GamePage({Key? key}) : super(key: key);

  @override
  _GamePageState createState() => _GamePageState();
}

class _GamePageState extends State<GamePage> {
  final GlobalKey _gamePanelKey = GlobalKey<GamePanelState>();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      extendBodyBehindAppBar: true,
      body: Container(
        padding: const EdgeInsets.only(top: 30),
        color: ColorUtil.bgColor1,
        child: OrientationBuilder(
          builder: (context, orientation) {
            if (orientation == Orientation.portrait) {
              return Column(
                children: [
                  Flexible(child: buildHeaderContainer()),
                  Flexible(flex: 2, child: buildGamePanel()),
                ],
              );
            } else {
              return Row(
                children: [
                  Flexible(child: buildHeaderContainer()),
                  Flexible(flex: 2, child: buildGamePanel()),
                ],
              );
            }
          },
        ),
      ),
    );
  }

  Widget buildHeaderContainer() {
    return Container(
      padding: const EdgeInsets.all(20),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  "2048",
                  style: TextStyle(color: ColorUtil.textColor1, fontSize: 56, fontWeight: FontWeight.bold),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget buildGamePanel() {
    return GamePanel(key: _gamePanelKey, onScoreChanged: (score) {});
  }
}
