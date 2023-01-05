/*
 厦门大学计算机专业 | 前华为工程师
 专注《零基础学编程系列》  http://lblbc.cn/blog
 包含：Java | 安卓 | 前端 | Flutter | iOS | 小程序 | 鸿蒙
 公众号：蓝不蓝编程
 */
import SwiftUI


func CellBoardView(size: CGSize,cellArr: [[Int]]) -> some View {
    VStack{
        ForEach(cellArr.indices , id: \.self){ i in
            HStack{
                ForEach(cellArr.indices , id: \.self){ j in
                    CellView(width: size.width / 4-5,
                                height: size.width / 4-5, value: cellArr[i][j])
                }
            }
        }
    }
}

struct CellView: View {
    var width: CGFloat
    var height: CGFloat
    
    var value: Int
    
    var body: some View {
        RoundedRectangle(cornerRadius: 3)
            .fill(colorPalette[value]!)
            .frame(width: width, height: height)
            .overlay(Text("\(convertIntToStr(valueue:value))")
                .font(value < 1024 ? .system(size: width / 3) : .system(size: width / 4))
                .fontWeight(.bold)
                .foregroundColor(value >= 8 ? Color.white : secondBackgroundColor))
    }
    
    func convertIntToStr(valueue:Int)->String{
        var str = ""
        if(valueue != 0){
            str = String(valueue)}
        return str
    }
}
