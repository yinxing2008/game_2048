package cn.lblbc.tzfe

import java.security.SecureRandom

/**
 * 厦门大学计算机专业 | 前华为工程师
 * 专注《零基础学编程系列》  http://lblbc.cn/blog
 * 包含：Java | 安卓 | 前端 | Flutter | iOS | 小程序 | 鸿蒙
 * 公众号：蓝不蓝编程
 */
fun startGame(cellArr: List<MutableList<Int>>) {
    for (i in 0 until 4) {
        for (j in 0 until 4) {
            cellArr[i][j] = 0
        }
    }
    fillOneEmptyCell(cellArr)
    fillOneEmptyCell(cellArr)
}

/**
 * 找到可以用于生成新数字的单元格，并生成新的数字进行填充
 */
private fun fillOneEmptyCell(cellArr: List<MutableList<Int>>) {
    val emptyCellPosition = findOneEmptyCell(cellArr)
    if (emptyCellPosition != -1) {
        cellArr[emptyCellPosition / 4][emptyCellPosition % 4] = getRandomValue()
    }
}

/**
 * 找到可以用于生成新数字的单元格，返回值为单元格在16个格子中的序号
 */
private fun findOneEmptyCell(cellArr: List<MutableList<Int>>): Int {
    val list = mutableListOf<Int>()
    for (i in 0 until 4) {
        for (j in 0 until 4) {
            if (cellArr[i][j] == 0) {
                list.add(i * 4 + j)
            }
        }
    }
    var position = -1
    if (list.isNotEmpty()) {
        position = list[random(list.size)]
    }
    return position
}

/**
 * 生成新的数字，90%几率生成2，10%几率生成4
 */
private fun getRandomValue(): Int {
    val rand = random(10)
    var value = 2
    if (rand >= 9) {
        value = 4
    }
    return value
}

/**
 * 生成随机数，0=<结果<max
 */
private fun random(max: Int): Int {
    val random = SecureRandom()
    return random.nextInt(max)
}

/**
 * 判断是否还可以移动。
 * 1、当前单元格是否为0；
 * 2、当前单元格和右侧单元格是否相等；
 * 3、当前单元格和下方单元格是否相等。
 */
fun canMove(cellArr: List<MutableList<Int>>): Boolean {
    for (i in 0 until 4) {
        for (j in 0 until 4) {
            if (cellArr[j][i] == 0) {
                return true
            }
            if (j < 3 && cellArr[i][j] == cellArr[i][j + 1]) {//和右侧单元格比较，是否相等
                return true
            }
            if (i < 3 && cellArr[i][j] == cellArr[i + 1][j]) {//和下方单元格比较，是否相等
                return true
            }
        }
    }

    return false
}

/**
 * 将单元格数向左或向右移动，移除零并对相邻相同数进行叠加
 * @param toLeft 表示是否是向左
 */
private fun horizontalMoveCells(cellArr: List<MutableList<Int>>, toLeft: Boolean) {
    for (i in 0 until 4) {
        val newArr = IntArray(4)
        for (j in 0 until 4) {
            newArr[j] = cellArr[i][j]
        }
        val resultArr = removeZerosAndAdd(newArr, toLeft)
        for (j in 0 until 4) {
            cellArr[i][j] = resultArr[j]
        }
    }
}

/**
 * 将单元格数向下或向上移动，移除零并对相邻相同数进行叠加
 * @param toTop 表示是否是向上
 */
private fun verticalMoveCells(cellArr: List<MutableList<Int>>, toTop: Boolean) {
    for (i in 0 until 4) {
        val newArr = IntArray(4)
        for (j in 0 until 4) {
            newArr[j] = cellArr[j][i]
        }
        val resultArr = removeZerosAndAdd(newArr, toTop)
        for (j in 0 until 4) {
            cellArr[j][i] = resultArr[j]
        }
    }
}

/**
 * 1、去掉数组中的零，向头或向尾压缩数组。
 * 0,4,0,4向左压缩变成：4,4,0,0. 向右压缩变成：0,0,4,4
 * 2、相邻的数如果相同，则进行相加运算。
 * 4,4,0,0向左叠加变成：8,0,0,0. 向右叠加变成：0,0,0,8
 * toHead表示是否是头压缩
 */
private fun removeZerosAndAdd(arr: IntArray, toHead: Boolean): IntArray {
    val newArr = IntArray(arr.size)
    val arrWithoutZero = arr.filter { it != 0 }//去掉所有的零
    if (arrWithoutZero.isEmpty()) return newArr
    if (toHead) {
        for (i in arrWithoutZero.indices) {
            newArr[i] = arrWithoutZero[i]
        }
        //对相邻相同的数进行叠加
        for (i in 0 until newArr.size - 1) {
            if (newArr[3 - i] == newArr[2 - i] && newArr[3 - i] != 0) {
                newArr[3 - i] = 0
                newArr[2 - i] *= 2
            }
        }
    } else {
        for (i in arrWithoutZero.indices) {
            newArr[newArr.size - i - 1] = arrWithoutZero[arrWithoutZero.size - i - 1]
        }
        //对相邻相同的数进行叠加
        for (i in 0 until newArr.size - 1) {
            if (newArr[i] == newArr[i + 1] && newArr[i] != 0) {
                newArr[i] = 0
                newArr[i + 1] *= 2
            }
        }
    }

    return newArr
}

private fun getCellBackgroundColor(cellValue: Int): String {
    when (cellValue) {
        2 -> return "#EEE4DA"
        4 -> return "#EDE0C8"
        8 -> return "#F26179"
        16 -> return "#F59563"
        32 -> return "#F67C5F"
        64 -> return "#F65E36"
        128 -> return "#EDCF72"
        256 -> return "#EDCC61"
        512 -> return "#90C000"
        1024 -> return "#3365A5"
        2048 -> return "#90C000"
        4096 -> return "#60B0C0"
        8192 -> return "#9030C0"
        else -> return "#CDC1B4"
    }
}

fun moveUp(cellArr: List<MutableList<Int>>) {
    verticalMoveCells(cellArr, true)
    checkGameOverOrContinue(cellArr)
}

fun moveDown(cellArr: List<MutableList<Int>>) {
    verticalMoveCells(cellArr, false)
    checkGameOverOrContinue(cellArr)
}

fun moveLeft(cellArr: List<MutableList<Int>>) {
    horizontalMoveCells(cellArr, true)
    checkGameOverOrContinue(cellArr)
}

fun moveRight(cellArr: List<MutableList<Int>>) {
    horizontalMoveCells(cellArr, false)
    checkGameOverOrContinue(cellArr)
}

private fun checkGameOverOrContinue(cellArr: List<MutableList<Int>>) {
    if (canMove(cellArr)) {
        fillOneEmptyCell(cellArr)
    } else {

//        Toast.makeText(this, "游戏结束", Toast.LENGTH_SHORT).show()
    }
}
