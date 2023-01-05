/// 厦门大学计算机专业 | 前华为工程师
/// 专注《零基础学编程系列》  http://lblbc.cn/blog
/// 包含：Java | 安卓 | 前端 | Flutter | iOS | 小程序 | 鸿蒙
/// 公众号：蓝不蓝编程
import 'dart:math';

import 'package:flutter/material.dart';

import 'color_util.dart';

class GamePanel extends StatefulWidget {
  final ValueChanged<int>? onScoreChanged;

  const GamePanel({Key? key, this.onScoreChanged}) : super(key: key);

  @override
  GamePanelState createState() => GamePanelState();
}

class GamePanelState extends State<GamePanel> {
  /// 每行每列的个数
  static const int size = 4;

  /// 当上下滑动时，左右方向的偏移应该小于这个阈值，左右滑动亦然
  final double _crossAxisMaxLimit = 20.0;

  /// 当上下滑动时，上下方向的偏移应该大于这个阈值，左右滑动亦然
  final double _mainAxisMinLimit = 40.0;

  /// onPanUpdate 会回调多次，只需要第一次有效的就可以了，
  /// 在 onPanDown 时设为 true，第一次有效滑动后，设为 false
  bool _firstValidPan = true;

  final List _cellArr = List.generate(size, (_) => List<int>.generate(size, (_) => 0));

  /// 判断是否游戏结束
  bool _isGameOver = false;

  @override
  initState() {
    super.initState();
    startGame();
  }

  startGame() {
    setState(() {
      for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
          _cellArr[i][j] = 0;
        }
      }

      //随机生成两个单元格
      _fillOneEmptyCell();
      _fillOneEmptyCell();

      _isGameOver = false;
    });
  }

  /// 找到可以用于生成新数字的单元格，并生成新的数字进行填充
  _fillOneEmptyCell() {
    var emptyCellPosition = _findOneEmptyCell();
    if (emptyCellPosition != -1) {
      _cellArr[(emptyCellPosition / 4).toInt()][emptyCellPosition % 4] = _getRandomValue();
    }
  }

  /// 找到可以用于生成新数字的单元格，返回值为单元格在16个格子中的序号
  _findOneEmptyCell() {
    final List list = List<int>.generate(size, (_) => 0);
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        if (_cellArr[i][j] == 0) {
          list.add(i * 4 + j);
        }
      }
    }

    var position = -1;
    if (list.isNotEmpty) {
      position = list[_random(list.length)];
    }
    return position;
  }

  /// 生成新的数字，90%几率生成2，10%几率生成4
  _getRandomValue() {
    var rand = _random(10);
    var value = 2;
    if (rand >= 9) {
      value = 4;
    }
    return value;
  }

  /// 生成随机数，0=<结果<max
  _random(int maxValue) {
    return Random().nextInt(maxValue);
  }

  /// 判断Map中的数字是否都不为0
  bool isGameMapAllNotZero() {
    bool isAllNotZero = true;
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (_cellArr[i][j] == 0) {
          isAllNotZero = false;
          break;
        }
      }
    }
    return isAllNotZero;
  }

  /// 处理用户手势
  Offset _panelOnPanDown(DragDownDetails details) {
    // 后续得到的相应
    _firstValidPan = true;

    //按下后，记录当前 position
    return details.globalPosition;
  }

  _panelOnUpdate(DragUpdateDetails details, Offset lastPosition) {
    final currentPosition = details.globalPosition;

    /// 首先区分是垂直方向还是水平方向滑动
    if ((currentPosition.dx - lastPosition.dx).abs() > _mainAxisMinLimit &&
        (currentPosition.dy - lastPosition.dy).abs() < _crossAxisMaxLimit) {
      // 水平方向滑动
      if (!_firstValidPan) {
        return;
      }
      if (currentPosition.dx - lastPosition.dx > 0) {
        _moveRight();
      } else if (currentPosition.dx - lastPosition.dx < 0) {
        _moveLeft();
      }
      _firstValidPan = false;
    } else if ((currentPosition.dy - lastPosition.dy).abs() > _mainAxisMinLimit &&
        (currentPosition.dx - lastPosition.dx).abs() < _crossAxisMaxLimit) {
      // 垂直方向滑动
      if (!_firstValidPan) {
        return;
      }
      if (currentPosition.dy - lastPosition.dy > 0) {
        _moveDown();
      } else if (currentPosition.dy - lastPosition.dy < 0) {
        _moveUp();
      }
      _firstValidPan = false;
    }
  }

  void _moveRight() {
    setState(() {
      _horizontalMoveCells(false);
    });
    _checkGameOverOrContinue();
  }

  void _moveLeft() {
    setState(() {
      _horizontalMoveCells(true);
    });
    _checkGameOverOrContinue();
  }

  void _moveDown() {
    setState(() {
      verticalMoveCells(false);
    });
    _checkGameOverOrContinue();
  }

  void _moveUp() {
    setState(() {
      verticalMoveCells(true);
    });
    _checkGameOverOrContinue();
  }

  void _checkGameOverOrContinue() {
    if (canMove()) {
      _fillOneEmptyCell();
    } else {
      setState(() {
        _isGameOver = true;
      });
    }
  }

  /*
     判断是否还可以移动。
     1、当前单元格是否为0；
     2、当前单元格和右侧单元格是否相等；
     3、当前单元格和下方单元格是否相等。
     */
  canMove() {
    for (var i = 0; i < 4; i++) {
      for (var j = 0; j < 4; j++) {
        var cell = _cellArr[i][j];
        if (cell != 0) {
          //和右侧单元格比较，是否相等
          if (j < 3 && _cellArr[i][j] == _cellArr[i][j + 1]) {
            return true;
          }
          //和下方单元格比较，是否相等
          if (i < 3 && _cellArr[i][j] == _cellArr[i + 1][j]) {
            return true;
          }
        } else {
          return true;
        }
      }
    }
    return false;
  }

  /// 将单元格数向左或向右移动，移除undefined并对相邻相同数进行叠加
  /// toLeft表示是否是向左
  _horizontalMoveCells(bool toLeft) {
    for (var i = 0; i < 4; i++) {
      var newArr = List<int>.generate(4, (_) => 0);
      for (var j = 0; j < 4; j++) {
        newArr[j] = _cellArr[i][j];
      }
      var resultArr = _removeZerosAndAdd(newArr, toLeft);
      for (var j = 0; j < 4; j++) {
        _cellArr[i][j] = resultArr[j];
      }
    }
  }

  /// 将单元格数向下或向上移动，移除undefined并对相邻相同数进行叠加
  /// toUp表示是否是向上
  verticalMoveCells(bool toUp) {
    for (var i = 0; i < 4; i++) {
      var newArr = List<int>.generate(4, (_) => 0);
      for (var j = 0; j < 4; j++) {
        newArr[j] = _cellArr[j][i];
      }
      var resultArr = _removeZerosAndAdd(newArr, toUp);
      for (var j = 0; j < 4; j++) {
        _cellArr[j][i] = resultArr[j];
      }
    }
  }

  /*
    1、去掉数组中的零，向头或向尾压缩数组。
      0,4,0,4向左压缩变成：4,4,0,0. 向右压缩变成：0,0,4,4
    2、相邻的数如果相同，则进行相加运算。
      4,4,0,0向左叠加变成：8,0,0,0. 向右叠加变成：0,0,0,8
      toHead表示是否是头压缩
   */
  _removeZerosAndAdd(List arr, bool toHead) {
    var newArr = List<int>.generate(arr.length, (_) => 0);
    var arrWithoutZero = arr.where((element) => element != 0); //去掉所有的零

    if (arrWithoutZero.isEmpty) return newArr;
    if (toHead) {
      for (var i = 0; i < arrWithoutZero.length; i++) {
        newArr[i] = arrWithoutZero.elementAt(i);
      }

      //对相邻相同的数进行叠加
      for (var i = 0; i < newArr.length - 1; i++) {
        if (newArr[3 - i] == newArr[2 - i] && newArr[3 - i] != 0) {
          newArr[3 - i] = 0;
          newArr[2 - i] *= 2;
        }
      }
    } else {
      for (var i = 0; i < arrWithoutZero.length; i++) {
        newArr[newArr.length - i - 1] = arrWithoutZero.elementAt(arrWithoutZero.length - i - 1);
      }

      //对相邻相同的数进行叠加
      for (var i = 0; i < newArr.length - 1; i++) {
        if (newArr[i] == newArr[i + 1] && newArr[i] != 0) {
          newArr[i] = 0;
          newArr[i + 1] *= 2;
        }
      }
    }

    return newArr;
  }

  @override
  Widget build(BuildContext context) {
    if (_isGameOver) {
      return Stack(
        children: [_buildGamePanel(context), _buildGameOverMask(context)],
      );
    } else {
      return _buildGamePanel(context);
    }
  }

  Widget _buildGamePanel(BuildContext context) {
    double minSize = min(MediaQuery.of(context).size.width, MediaQuery.of(context).size.height);

    Offset lastPosition = Offset.zero;
    return GestureDetector(
      onPanDown: (DragDownDetails details) {
        lastPosition = _panelOnPanDown(details);
      },
      onPanUpdate: (DragUpdateDetails details) {
        _panelOnUpdate(details, lastPosition);
      },
      child: AspectRatio(
        aspectRatio: 1.0,
        child: Container(
          width: minSize,
          height: minSize,
          margin: const EdgeInsets.all(10),
          child: Container(
            padding: const EdgeInsets.all(10),
            decoration: BoxDecoration(
              borderRadius: BorderRadius.circular(10),
              color: ColorUtil.bgColor2,
            ),
            child: MediaQuery.removePadding(
              removeTop: true,
              context: context,
              child: GridView.builder(
                physics: const NeverScrollableScrollPhysics(),
                gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                    crossAxisCount: size, childAspectRatio: 1, mainAxisSpacing: 10, crossAxisSpacing: 10),
                itemCount: size * size,
                itemBuilder: (context, int index) {
                  int indexI = index ~/ size;
                  int indexJ = index % size;
                  return _buildGameCell(_cellArr[indexI][indexJ]);
                },
              ),
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildGameCell(int value) {
    return Container(
      decoration: BoxDecoration(
        color: ColorUtil.mapValueToColor(value),
        borderRadius: BorderRadius.circular(5),
      ),
      child: Center(
        child: Text(
          value == 0 ? "" : value.toString(),
          style: TextStyle(
            color: value == 2 || value == 4 ? ColorUtil.textColor1 : ColorUtil.textColor3,
            fontWeight: FontWeight.bold,
            fontSize: 30,
          ),
        ),
      ),
    );
  }

  Widget _buildGameOverMask(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(10),
        color: ColorUtil.bgColor1.withOpacity(0.5),
      ),
      child: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              "游戏结束！",
              style: TextStyle(
                color: ColorUtil.textColor1,
                fontSize: 24,
                fontWeight: FontWeight.bold,
              ),
            ),
            ElevatedButton(
                style: ButtonStyle(
                  backgroundColor: MaterialStateProperty.all<Color>(ColorUtil.bgColor3),
                ),
                onPressed: () {
                  startGame();
                },
                child: const Text("重新开始"))
          ],
        ),
      ),
    );
  }
}
