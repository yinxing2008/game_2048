/*
  厦门大学计算机专业 | 前华为工程师
  专注《零基础学编程系列》  http://lblbc.cn/blog
  包含：Java | 安卓 | 前端 | Flutter | iOS | 小程序 | 鸿蒙
  公众号：蓝不蓝编程
*/

package main

import (
	"fmt"
	"math/rand"
	"time"

	"github.com/nsf/termbox-go"
)

func convertPrintStr(x, y int, str string, fg, bg termbox.Attribute) error {
	xx := x
	for n, c := range str {
		if c == '\n' {
			y++
			xx = x - n - 1
		}
		termbox.SetCell(xx+n, y, c, fg, bg)
	}
	termbox.Flush()
	return nil
}

// 2048游戏中的16个格子使用4x4二维数组表示
var cellArr = [4][4]int{
	{0, 0, 0, 0},
	{0, 0, 0, 0},
	{0, 0, 0, 0},
	{0, 0, 0, 0},
}

/*
   生成新的数字，90%几率生成2，10%几率生成4
*/
func getRandomValue() int {
	rand := random(10)
	var value = 2
	if rand >= 9 {
		value = 4
	}
	return value
}

func random(maxValue int) int {
	return rand.Intn(maxValue)
}

/*
   判断是否还可以移动。
   1、当前单元格是否为0；
   2、当前单元格和右侧单元格是否相等；
   3、当前单元格和下方单元格是否相等。
*/
func canMove() bool {
	for i := 0; i < 4; i++ {
		for j := 0; j < 4; j++ {
			if cellArr[j][i] == 0 {
				return true
			}
			if j < 3 && cellArr[i][j] == cellArr[i][j+1] { //和右侧单元格比较，是否相等
				return true
			}
			if i < 3 && cellArr[i][j] == cellArr[i+1][j] { //和下方单元格比较，是否相等
				return true
			}
		}
	}

	return false
}

// 初始化游戏界面
func initialize(ox, oy int) error {
	fg := termbox.ColorYellow
	bg := termbox.ColorBlack
	termbox.Clear(fg, bg)
	str := "ESC:退出 " + "Enter:重新开始"
	for n, c := range str {
		termbox.SetCell(ox+n, oy-2, c, fg, bg)
	}
	str = "    按方向键开始玩"
	for n, c := range str {
		termbox.SetCell(ox+n, oy-3, c, fg, bg)
	}
	fg = termbox.ColorBlack
	bg = termbox.ColorGreen
	for i := 0; i <= len(cellArr); i++ {
		for x := 0; x < 5*len(cellArr); x++ {
			termbox.SetCell(ox+x, oy+i*2, '-', fg, bg)
		}
		for x := 0; x <= 2*len(cellArr); x++ {
			if x%2 == 0 {
				termbox.SetCell(ox+i*5, oy+x, '+', fg, bg)
			} else {
				termbox.SetCell(ox+i*5, oy+x, '|', fg, bg)
			}
		}
	}
	fg = termbox.ColorYellow
	bg = termbox.ColorBlack
	for i := range cellArr {
		for j := range cellArr[i] {
			if cellArr[i][j] > 0 {
				str := fmt.Sprint(cellArr[i][j])
				for n, char := range str {
					termbox.SetCell(ox+j*5+1+n, oy+i*2+1, char, fg, bg)
				}
			}
		}
	}
	return termbox.Flush()
}

func moveUp() {
	verticalMoveCells(true)
	checkGameOverOrContinue()
}

func moveDown() {
	verticalMoveCells(false)
	checkGameOverOrContinue()
}

func moveLeft() {
	horizontalMoveCells(true)
	checkGameOverOrContinue()
}

func moveRight() {
	horizontalMoveCells(false)
	checkGameOverOrContinue()
}

/*
   将单元格数向左或向右移动，移除零并对相邻相同数进行叠加
   toLeft 表示是否是向左
*/
func horizontalMoveCells(toLeft bool) {
	for i := 0; i < 4; i++ {
		newArr := []int{0, 0, 0, 0}
		for j := 0; j < 4; j++ {
			newArr[j] = cellArr[i][j]
		}
		resultArr := removeZerosAndAdd(newArr, toLeft)
		for j := 0; j < 4; j++ {
			cellArr[i][j] = resultArr[j]
		}
	}
}

/*
   将单元格数向下或向上移动，移除零并对相邻相同数进行叠加
   toTop 表示是否是向上
*/
func verticalMoveCells(toTop bool) {
	for i := 0; i < 4; i++ {
		newArr := []int{0, 0, 0, 0}
		for j := 0; j < 4; j++ {
			newArr[j] = cellArr[j][i]
		}
		resultArr := removeZerosAndAdd(newArr, toTop)
		for j := 0; j < 4; j++ {
			cellArr[j][i] = resultArr[j]
		}
	}
}

func removeZerosAndAdd(arr []int, toHead bool) []int {
	var newArr = []int{0, 0, 0, 0}
	var arrWithoutZero []int

	//去掉所有的零
	var nonZeroCount = 0
	for i := 0; i < len(arr); i++ {
		if arr[i] != 0 {
			nonZeroCount++
		}
	}
	arrWithoutZero = make([]int, nonZeroCount)
	var index = 0
	for i := 0; i < len(arr); i++ {
		if arr[i] != 0 {
			arrWithoutZero[index] = arr[i]
			index++
		}
	}

	if toHead {
		for i := 0; i < len(arrWithoutZero); i++ {
			newArr[i] = arrWithoutZero[i]
		}
		//对相邻相同的数进行叠加
		for i := 0; i < len(newArr)-1; i++ {
			if newArr[3-i] == newArr[2-i] && newArr[3-i] != 0 {
				newArr[3-i] = 0
				newArr[2-i] *= 2
			}
		}
	} else {
		for i := 0; i < len(arrWithoutZero); i++ {
			newArr[len(newArr)-i-1] = arrWithoutZero[len(arrWithoutZero)-i-1]
		}
		//对相邻相同的数进行叠加
		for i := 0; i < len(newArr)-1; i++ {
			if newArr[i] == newArr[i+1] && newArr[i] != 0 {
				newArr[i] = 0
				newArr[i+1] *= 2
			}
		}
	}

	return newArr
}

func fillOneEmptyCell() {
	emptyCellPosition := findOneEmptyCell()
	if emptyCellPosition != -1 {
		cellArr[emptyCellPosition/4][emptyCellPosition%4] = getRandomValue()
	}
}

/**
 * 找到可以用于生成新数字的单元格，返回值为单元格在16个格子中的序号
 */
func findOneEmptyCell() int {
	var list [16]int
	var count = 0
	for i := 0; i < 4; i++ {
		for j := 0; j < 4; j++ {
			if cellArr[i][j] == 0 {
				list[count] = i*4 + j
				count++
			}
		}
	}

	var position = -1
	if count > 0 {
		position = list[random(count)]
	}

	return position
}

func checkGameOverOrContinue() {
	if canMove() {
		fillOneEmptyCell()
	}
}

// 响应用户按键行为（上下左右）
func checkKeyboardAction() termbox.Key {
	var changed bool
Lable:
	changed = false
	event_queue := make(chan termbox.Event)
	go func() {
		for {
			event_queue <- termbox.PollEvent() // 开始监听键盘事件
		}
	}()

	ev := <-event_queue

	switch ev.Type {
	case termbox.EventKey:
		switch ev.Key {
		case termbox.KeyArrowUp:
			moveUp()
			changed = true
		case termbox.KeyArrowDown:
			moveDown()
			changed = true
		case termbox.KeyArrowLeft:
			moveLeft()
			changed = true
		case termbox.KeyArrowRight:
			moveRight()
			changed = true
		case termbox.KeyEsc, termbox.KeyEnter:
			changed = true
		default:
			changed = false
		}

		// 如果元素的值没有任何更改，则重新开始循环
		if !changed {
			goto Lable
		}

	case termbox.EventResize:
		x, y := termbox.Size()
		initialize(x/2-10, y/2-4)
		goto Lable
	case termbox.EventError:
		panic(ev.Err)
	}
	return ev.Key
}

// 重置
func clear() {
	cellArr = [4][4]int{
		{0, 0, 0, 0},
		{0, 0, 0, 0},
		{0, 0, 0, 0},
		{0, 0, 0, 0},
	}
}

// 开始游戏
func startGame() {
	err := termbox.Init()
	if err != nil {
		panic(err)
	}
	defer termbox.Close()

	rand.Seed(time.Now().UnixNano())
	play()
}

func play() {
	clear()
	fillOneEmptyCell()
	fillOneEmptyCell()
	for {
		x, y := termbox.Size()
		initialize(x/2-10, y/2-4)
		if !canMove() {
			str := "游戏结束"
			strl := len(str)
			convertPrintStr(x/2-strl/2, y/2, str, termbox.ColorBlack, termbox.ColorRed)
		}
		// 检查用户按键
		key := checkKeyboardAction()
		// 如果按键是Esc，则退出游戏
		if key == termbox.KeyEsc {
			return
		}
		// 如果按键是Enter，则重新开始游戏
		if key == termbox.KeyEnter {
			play()
		}
	}
}

func main() {
	startGame()
}
