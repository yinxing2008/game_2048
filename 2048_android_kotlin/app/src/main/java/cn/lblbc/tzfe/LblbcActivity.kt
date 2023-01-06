package cn.lblbc.tzfe

import android.graphics.Color
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import java.security.SecureRandom

/**
 * 厦门大学计算机专业 | 前华为工程师
 * 专注《零基础学编程系列》  http://lblbc.cn/blog
 * 包含：Java | 安卓 | 前端 | Flutter | iOS | 小程序 | 鸿蒙
 * 公众号：蓝不蓝编程
 */
class LblbcActivity : AppCompatActivity(), View.OnTouchListener, GestureDetector.OnGestureListener {
    private lateinit var detector: GestureDetectorCompat//监测用户手势
    private lateinit var cellArr: Array<IntArray>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lblbc)
        detector = GestureDetectorCompat(this, this)
        findViewById<View>(R.id.rootLayout).setOnTouchListener(this)
        findViewById<View>(R.id.startTextView).setOnClickListener { startGame() }
        startGame()
    }

    private fun startGame() {
        cellArr = arrayOf(
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
        )
        //随机生成两个单元格
        fillOneEmptyCell()
        fillOneEmptyCell()

        showCells()
    }

    /**
     * 找到可以用于生成新数字的单元格，并生成新的数字进行填充
     */
    private fun fillOneEmptyCell() {
        val emptyCellPosition = findOneEmptyCell()
        if (emptyCellPosition != -1) {
            cellArr[emptyCellPosition / 4][emptyCellPosition % 4] = getRandomValue()
        }
    }

    /**
     * 找到可以用于生成新数字的单元格，返回值为单元格在16个格子中的序号
     */
    private fun findOneEmptyCell(): Int {
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
    private fun canMove(): Boolean {
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

    private fun showCells() {
        updateTextView(R.id.textView0, cellArr[0][0])
        updateTextView(R.id.textView1, cellArr[0][1])
        updateTextView(R.id.textView2, cellArr[0][2])
        updateTextView(R.id.textView3, cellArr[0][3])
        updateTextView(R.id.textView4, cellArr[1][0])
        updateTextView(R.id.textView5, cellArr[1][1])
        updateTextView(R.id.textView6, cellArr[1][2])
        updateTextView(R.id.textView7, cellArr[1][3])
        updateTextView(R.id.textView8, cellArr[2][0])
        updateTextView(R.id.textView9, cellArr[2][1])
        updateTextView(R.id.textView10, cellArr[2][2])
        updateTextView(R.id.textView11, cellArr[2][3])
        updateTextView(R.id.textView12, cellArr[3][0])
        updateTextView(R.id.textView13, cellArr[3][1])
        updateTextView(R.id.textView14, cellArr[3][2])
        updateTextView(R.id.textView15, cellArr[3][3])
    }

    private fun updateTextView(textViewResId: Int, cellValue: Int) {
        val textView = findViewById<TextView>(textViewResId)
        if (cellValue == 0) {
            textView.text = ""
        } else {
            textView.text = cellValue.toString()
        }
        textView.setBackgroundColor(Color.parseColor(getCellBackgroundColor(cellValue)))
    }

    /**
     * 将单元格数向左或向右移动，移除零并对相邻相同数进行叠加
     * @param toLeft 表示是否是向左
     */
    private fun horizontalMoveCells(toLeft: Boolean) {
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
    private fun verticalMoveCells(toTop: Boolean) {
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

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        if (e1.y - e2.y > 100) {
            moveUp()
        } else if (e2.y - e1.y > 100) {
            moveDown()
        } else if (e1.x - e2.x > 100) {
            moveLeft()
        } else if (e2.x - e1.x > 100) {
            moveRight()
        }

        return true
    }

    private fun moveUp() {
        verticalMoveCells(true)
        checkGameOverOrContinue()
    }

    private fun moveDown() {
        verticalMoveCells(false)
        checkGameOverOrContinue()
    }

    private fun moveLeft() {
        horizontalMoveCells(true)
        checkGameOverOrContinue()
    }

    private fun moveRight() {
        horizontalMoveCells(false)
        checkGameOverOrContinue()
    }

    private fun checkGameOverOrContinue() {
        if (canMove()) {
            fillOneEmptyCell()
            showCells()
        } else {
            Toast.makeText(this, "游戏结束", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        detector.onTouchEvent(event)
        return true
    }

    override fun onDown(e: MotionEvent) = false
    override fun onShowPress(e: MotionEvent) {}
    override fun onLongPress(e: MotionEvent) {}
    override fun onSingleTapUp(e: MotionEvent) = false
    override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float) = false
}