package cn.lblbc.tzfe.ui

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
/**
 * 厦门大学计算机专业 | 前华为工程师
 * 专注《零基础学编程系列》  http://lblbc.cn/blog
 * 包含：Java | 安卓 | 前端 | Flutter | iOS | 小程序 | 鸿蒙
 * 公众号：蓝不蓝编程
 */
@Composable
fun GameOverDialog(
    title: String,
    onConfirmListener: () -> Unit,
    onDismissListener: () -> Unit,
) {
    AlertDialog(
        title = { Text(text = title) },
        confirmButton = { TextButton(onClick = { onConfirmListener.invoke() }) { Text("OK") } },
        onDismissRequest = { onDismissListener.invoke() },
    )
}
