package cn.lblbc.tzfe;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
/**
 * 厦门大学计算机专业 | 前华为工程师
 * 专注《零基础学编程系列》  http://lblbc.cn/blog
 * 包含：Java | 安卓 | 前端 | Flutter | iOS | 小程序 | 鸿蒙
 * 公众号：蓝不蓝编程
 */
public class LblbcActivity extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener {
    private GestureDetectorCompat detector;
    private int[][] cellArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lblbc);
        detector = new GestureDetectorCompat(this, this);
        this.findViewById(R.id.rootLayout).setOnTouchListener(this);
        this.findViewById(R.id.startTextView).setOnClickListener(view -> startGame());
        startGame();
    }

    private void startGame() {
        cellArr = new int[][]{{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};
        fillOneEmptyCell();
        fillOneEmptyCell();
        showCells();
    }

    private void fillOneEmptyCell() {
        int emptyCellPosition = findOneEmptyCell();
        if (emptyCellPosition != -1) {
            cellArr[emptyCellPosition / 4][emptyCellPosition % 4] = getRandomValue();
        }
    }

    private int findOneEmptyCell() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (cellArr[i][j] == 0) {
                    list.add(i * 4 + j);
                }
            }
        }
        int position = -1;
        if (!list.isEmpty()) {
            position = list.get(random(list.size()));
        }
        return position;
    }

    /**
     * 生成新的数字，90%几率生成2，10%几率生成4
     */
    private int getRandomValue() {
        int rand = random(10);
        int value = 2;
        if (rand >= 9) {
            value = 4;
        }
        return value;
    }

    /**
     * 生成随机数，0=<结果<max
     */
    private int random(int max) {
        return new SecureRandom().nextInt(max);
    }

    /**
     * 判断是否还可以移动。
     * 1、当前单元格是否为0；
     * 2、当前单元格和右侧单元格是否相等；
     * 3、当前单元格和下方单元格是否相等。
     */
    private boolean canMove() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (cellArr[j][i] == 0) {
                    return true;
                }
                if (j < 3 && cellArr[i][j] == cellArr[i][j + 1]) {//和右侧单元格比较，是否相等
                    return true;
                }
                if (i < 3 && cellArr[i][j] == cellArr[i + 1][j]) {//和下方单元格比较，是否相等
                    return true;
                }
            }
        }

        return false;
    }

    private void showCells() {
        updateTextView(R.id.textView0, cellArr[0][0]);
        updateTextView(R.id.textView1, cellArr[0][1]);
        updateTextView(R.id.textView2, cellArr[0][2]);
        updateTextView(R.id.textView3, cellArr[0][3]);
        updateTextView(R.id.textView4, cellArr[1][0]);
        updateTextView(R.id.textView5, cellArr[1][1]);
        updateTextView(R.id.textView6, cellArr[1][2]);
        updateTextView(R.id.textView7, cellArr[1][3]);
        updateTextView(R.id.textView8, cellArr[2][0]);
        updateTextView(R.id.textView9, cellArr[2][1]);
        updateTextView(R.id.textView10, cellArr[2][2]);
        updateTextView(R.id.textView11, cellArr[2][3]);
        updateTextView(R.id.textView12, cellArr[3][0]);
        updateTextView(R.id.textView13, cellArr[3][1]);
        updateTextView(R.id.textView14, cellArr[3][2]);
        updateTextView(R.id.textView15, cellArr[3][3]);
    }

    private void updateTextView(int textViewResId, int cellValue) {
        TextView textView = this.findViewById(textViewResId);
        if (cellValue == 0) {
            textView.setText("");
        } else {
            textView.setText(String.valueOf(cellValue));
        }
        textView.setBackgroundColor(Color.parseColor(getCellBackgroundColor(cellValue)));
    }

    /**
     * 将单元格数向左或向右移动，移除零并对相邻相同数进行叠加
     *
     * @param toLeft 表示是否是向左
     */
    private void horizontalMoveCells(boolean toLeft) {
        for (int i = 0; i < 4; i++) {
            int[] newArr = new int[4];

            for (int j = 0; j < 4; j++) {
                newArr[j] = cellArr[i][j];
            }

            int[] resultArr = removeZerosAndAdd(newArr, toLeft);
            for (int j = 0; j < 4; j++) {
                cellArr[i][j] = resultArr[j];
            }
        }
    }

    /**
     * 将单元格数向下或向上移动，移除零并对相邻相同数进行叠加
     *
     * @param toTop 表示是否是向上
     */
    private void verticalMoveCells(boolean toTop) {
        for (int i = 0; i < 4; i++) {
            int[] newArr = new int[4];

            for (int j = 0; j < 4; j++) {
                newArr[j] = cellArr[j][i];
            }

            int[] resultArr = removeZerosAndAdd(newArr, toTop);
            for (int j = 0; j < 4; j++) {
                cellArr[j][i] = resultArr[j];
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
    private int[] removeZerosAndAdd(int[] arr, boolean toHead) {
        int[] newArr = new int[4];
        List<Integer> arrWithoutZero = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != 0) {
                arrWithoutZero.add(arr[i]);
            }
        }

        if (arrWithoutZero.isEmpty()) return newArr;
        if (toHead) {
            for (int i = 0; i < arrWithoutZero.size(); i++) {
                newArr[i] = arrWithoutZero.get(i);
            }

            //对相邻相同的数进行叠加
            for (int i = 0; i < newArr.length - 1; i++) {
                if (newArr[3 - i] == newArr[2 - i] && newArr[3 - i] != 0) {
                    newArr[3 - i] = 0;
                    newArr[2 - i] *= 2;
                }
            }
        } else {
            for (int i = 0; i < arrWithoutZero.size(); i++) {
                newArr[newArr.length - i - 1] = arrWithoutZero.get(arrWithoutZero.size() - i - 1);
            }

            //对相邻相同的数进行叠加
            for (int i = 0; i < newArr.length - 1; i++) {
                if (newArr[i] == newArr[i + 1] && newArr[i] != 0) {
                    newArr[i] = 0;
                    newArr[i + 1] *= 2;
                }
            }
        }

        return newArr;
    }


    private String getCellBackgroundColor(int cellValue) {
        switch (cellValue) {
            case 2:
                return "#EEE4DA";
            case 4:
                return "#EDE0C8";
            case 8:
                return "#F26179";
            case 16:
                return "#F59563";
            case 32:
                return "#F67C5F";
            case 64:
                return "#F65E36";
            case 128:
                return "#EDCF72";
            case 256:
                return "#EDCC61";
            case 512:
                return "#90C000";
            case 1024:
                return "#3365A5";
            case 2048:
                return "#90C000";
            case 4096:
                return "#60B0C0";
            case 8192:
                return "#9030C0";
            default:
                return "#CDC1B4";
        }
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getY() - e2.getY() > (float) 100) {
            this.moveUp();
        } else if (e2.getY() - e1.getY() > (float) 100) {
            this.moveDown();
        } else if (e1.getX() - e2.getX() > (float) 100) {
            this.moveLeft();
        } else if (e2.getX() - e1.getX() > (float) 100) {
            this.moveRight();
        }

        return true;
    }

    private void moveUp() {
        verticalMoveCells(true);
        checkGameOverOrContinue();
    }

    private void moveDown() {
        verticalMoveCells(false);
        checkGameOverOrContinue();
    }

    private void moveLeft() {
        horizontalMoveCells(true);
        checkGameOverOrContinue();
    }

    private void moveRight() {
        horizontalMoveCells(false);
        checkGameOverOrContinue();
    }

    private void checkGameOverOrContinue() {
        if (canMove()) {
            fillOneEmptyCell();
            showCells();
        } else {
            Toast.makeText((Context) this, (CharSequence) "游戏结束", Toast.LENGTH_LONG).show();
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        detector.onTouchEvent(event);
        return true;
    }

    public boolean onDown(MotionEvent e) {
        return false;
    }

    public void onShowPress(MotionEvent e) {
    }

    public void onLongPress(MotionEvent e) {
    }

    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

}