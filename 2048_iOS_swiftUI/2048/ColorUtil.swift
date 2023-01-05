/*
 厦门大学计算机专业 | 前华为工程师
 专注《零基础学编程系列》  http://lblbc.cn/blog
 包含：Java | 安卓 | 前端 | Flutter | iOS | 小程序 | 鸿蒙
 公众号：蓝不蓝编程
 */
import SwiftUI

extension Color {
    init(hex: UInt32, opacity:Double = 1.0) {
        let red = Double((hex & 0xff0000) >> 16) / 255.0
        let green = Double((hex & 0xff00) >> 8) / 255.0
        let blue = Double((hex & 0xff) >> 0) / 255.0
        self.init(.sRGB, red: red, green: green, blue: blue, opacity: opacity)
    }
}

let colorPalette: [Int: Color] = [
    0: Color(hex: 0xCDC1B4),
    2: Color(hex: 0xEEE4DA),
    4: Color(hex: 0xEDE0C8),
    8: Color(hex: 0xF2B179),
    16: Color(hex: 0xF59563),
    32: Color(hex: 0xF67C5F),
    64: Color(hex: 0xF65E36),
    128: Color(hex: 0xEDCF72),
    256: Color(hex: 0xEDCC61),
    512: Color(hex: 0x90C000),
    1024: Color(hex: 0x3365A5),
    2048: Color(hex: 0x90C000),
    4096: Color(hex: 0x60B0C0),
    8192: Color(hex: 0x9030C0),
    16384: Color(hex: 0x9030C0),
    32768: Color(hex: 0x9030C0),
    65536: Color(hex: 0x9030C0)
]

let backgroundColor: Color = Color(hex: 0xFAF8EF)
let secondBackgroundColor = Color(hex: 0xBEAC9E)
