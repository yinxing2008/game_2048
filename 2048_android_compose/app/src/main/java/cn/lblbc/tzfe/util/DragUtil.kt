package cn.lblbc.tzfe.util

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
/**
 * 厦门大学计算机专业 | 前华为工程师
 * 专注《零基础学编程系列》  http://lblbc.cn/blog
 * 包含：Java | 安卓 | 前端 | Flutter | iOS | 小程序 | 鸿蒙
 * 公众号：蓝不蓝编程
 */

@Composable
internal fun rememberDragOffset() = remember { mutableStateOf(Offset(0f, 0f)) }

internal fun Modifier.dragDetector(
    dragOffset: MutableState<Offset>,
    onDragFinished: (Offset) -> Unit,
) = pointerInput(Unit) {
    detectDragGestures(
        onDragStart = { dragOffset.value = Offset(0f, 0f) },
        onDragEnd = { onDragFinished(dragOffset.value) })
    { change, dragAmount ->
        dragOffset.value += Offset(dragAmount.x, dragAmount.y)
    }
}
