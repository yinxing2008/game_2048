/*
 厦门大学计算机专业 | 前华为工程师
 专注《零基础学编程系列》  http://lblbc.cn/blog
 包含：Java | 安卓 | 前端 | Flutter | iOS | 小程序 | 鸿蒙
 公众号：蓝不蓝编程
 */
import SwiftUI

struct ContentView: View {
    @State var cellArr = [[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0]]
    @State var gameOver = false
    
    var body: some View {
        VStack{
            Text("2048").fontWeight(.bold).font(.system(size: 30))
            RoundedRectangle(cornerRadius: 5)
                .fill(secondBackgroundColor)
                .aspectRatio(1.0, contentMode: .fit)
                .overlay(content)
                .padding(10)
        }
    }
    
    var content: some View{
        VStack {
            GeometryReader { geometry in
                CellBoardView(size: geometry.size,cellArr: cellArr)
            }
            .padding()
        }
        .alert(isPresented: $gameOver) {
            Alert(title: Text(NSLocalizedString("游戏结束", comment: "")),
                  dismissButton: .default(Text(NSLocalizedString("重新开始", comment: "")), action: {
                startGame()
            }))
        }
        .onAppear{startGame()}
        .gesture(DragGesture().onEnded(dragGesture))
    }
    
    /*
     监测滑动手势
     */
    func dragGesture(value: DragGesture.Value) {
        let x = value.translation.width
        let y = value.translation.height
        let threshold: CGFloat = 20
        guard abs(x) > threshold || abs(y) > threshold else {
            return
        }
        
        if abs(x) / abs(y) >= 1 {
            if x > 0 {
                horizontalMoveCells(toLeft: false)
            } else {
                horizontalMoveCells(toLeft: true)
            }
        } else {
            if y > 0 {
                verticalMoveCells(toTop: false)
            } else {
                verticalMoveCells(toTop: true)
            }
        }
        if(canMove()){
            fillOneEmptyCell()
        }else{
            gameOver = true
        }
    }
    
    func startGame(){
        cellArr =  [[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0]]
        
        //随机生成两个单元格
        fillOneEmptyCell()
        fillOneEmptyCell()
    }
    
    /*
     找到可以用于生成新数字的单元格，并生成新的数字进行填充
     */
    func fillOneEmptyCell() {
        let emptyCellPosition = findOneEmptyCell()
        if (emptyCellPosition != -1) {
            cellArr[emptyCellPosition / 4][emptyCellPosition % 4] = getRandomValue()
        }
    }
    
    /*
     找到可以用于生成新数字的单元格，返回值为单元格在16个格子中的序号
     */
    func findOneEmptyCell()->Int {
        var list = [Int]()
        
        for i in 0...3 {
            for j in 0...3 {
                if (cellArr[i][j] == 0) {
                    list.append(i * 4 + j)
                }
            }
        }
        
        var position = -1
        if (!list.isEmpty) {
            position = list[random(max:list.count)]
        }
        return position
    }
    
    /*
     生成新的数字，90%几率生成2，10%几率生成4
     */
    func getRandomValue()-> Int {
        let rand = random(max:10)
        var value = 2
        if (rand >= 9) {
            value = 4
        }
        return value
    }
    
    //生成随机数，0=<结果<max
    func random(max: Int)-> Int {
        return Int(arc4random_uniform(UInt32(max)))
    }
    
    /*
     判断是否还可以移动。
     1、当前单元格是否为0；
     2、当前单元格和右侧单元格是否相等；
     3、当前单元格和下方单元格是否相等。
     */
    func canMove()-> Bool {
        for i in 0...3 {
            for j in 0...3 {
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
    
    /*
     将单元格数向左或向右移动，移除零并对相邻相同数进行叠加
     toLeft 表示是否是向左
     */
    func horizontalMoveCells(toLeft: Bool) {
        for i in 0...3 {
            var newArr = [0,0,0,0]
            for j in 0...3 {
                newArr[j] = cellArr[i][j]
            }
            let resultArr = removeZerosAndAdd(arr: newArr, toHead:toLeft)
            for j in 0...3 {
                cellArr[i][j] = resultArr[j]
            }
        }
    }
    
    /*
     将单元格数向下或向上移动，移除零并对相邻相同数进行叠加
     toTop 表示是否是向上
     */
    func verticalMoveCells(toTop: Bool) {
        for i in 0...3 {
            var newArr = [0,0,0,0]
            for j in 0...3 {
                newArr[j] = cellArr[j][i]
            }
            let resultArr = removeZerosAndAdd(arr: newArr, toHead:toTop)
            for j in 0...3 {
                cellArr[j][i] = resultArr[j]
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
    func removeZerosAndAdd(arr: [Int], toHead: Bool)-> [Int] {
        var newArr = [0,0,0,0]
        let arrWithoutZero = arr.filter { $0 != 0 }//去掉所有的零
        if (arrWithoutZero.isEmpty) {return newArr}
        if (toHead) {
            for i in 0...arrWithoutZero.count-1 {
                newArr[i] = arrWithoutZero[i]
            }
            //对相邻相同的数进行叠加
            for i in 0...newArr.count-2 {
                if (newArr[3 - i] == newArr[2 - i] && newArr[3 - i] != 0) {
                    newArr[3 - i] = 0
                    newArr[2 - i] *= 2
                }
            }
        } else {
            
            for i in 0...arrWithoutZero.count-1 {
                newArr[newArr.count - i - 1] = arrWithoutZero[arrWithoutZero.count - i - 1]
            }
            //对相邻相同的数进行叠加
            for i in 0...newArr.count-2 {
                if (newArr[i] == newArr[i + 1] && newArr[i] != 0) {
                    newArr[i] = 0
                    newArr[i + 1] *= 2
                }
            }
        }
        
        return newArr
    }
    
    struct ContentView_Previews: PreviewProvider {
        static var previews: some View {
            ContentView()
        }
    }
}
