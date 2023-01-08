# 厦门大学计算机专业 | 前华为工程师
# 专注《零基础学编程系列》  http://lblbc.cn/blog
# 包含：Java | 安卓 | 前端 | Flutter | iOS | 小程序 | 鸿蒙
# 公众号：蓝不蓝编程
import random


def start_game():
    cell_arr = [[0, 0, 0, 0], [0, 0, 0, 0], [0, 0, 0, 0], [0, 0, 0, 0]]
    fill_one_emptyCell(cell_arr)
    fill_one_emptyCell(cell_arr)
    return cell_arr


# 找到可以用于生成新数字的单元格，并生成新的数字进行填充
def fill_one_emptyCell(cell_arr):
    empty_cell_position = find_one_empty_cell(cell_arr)
    if empty_cell_position != -1:
        cell_arr[int(empty_cell_position / 4)][empty_cell_position % 4] = get_random_value()


# 找到可以用于生成新数字的单元格，返回值为单元格在16个格子中的序号
def find_one_empty_cell(cell_arr):
    result_list = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
    count = 0
    position = -1
    for i in range(0, 4):
        for j in range(0, 4):
            if cell_arr[i][j] == 0:
                result_list[count] = i * 4 + j
                count += 1

    if count > 0:
        position = result_list[random_val(count)]

    return position


# 生成新的数字，90%几率生成2，10%几率生成4
def get_random_value():
    rand = random_val(10)
    value = 2
    if rand >= 9:
        value = 4
    return value


def random_val(max_value):
    return random.randint(0, max_value - 1)


def move_up(cell_arr):
    vertical_move_cells(cell_arr, True)
    check_game_over_or_continue(cell_arr)


def move_down(cell_arr):
    vertical_move_cells(cell_arr, False)
    check_game_over_or_continue(cell_arr)


def move_left(cell_arr):
    horizontalMoveCells(cell_arr, True)
    check_game_over_or_continue(cell_arr)


def move_right(cell_arr):
    horizontalMoveCells(cell_arr, False)
    check_game_over_or_continue(cell_arr)


# 将单元格数向左或向右移动，移除零并对相邻相同数进行叠加
# toLeft 表示是否是向左
def horizontalMoveCells(cell_arr, to_left):
    for i in range(0, 4):
        new_arr = []
        for j in range(0, 4):
            new_arr.append(cell_arr[i][j])
        result_arr = remove_zeros_and_add(new_arr, to_left)
        for j in range(0, 4):
            cell_arr[i][j] = result_arr[j]


# 将单元格数向下或向上移动，移除零并对相邻相同数进行叠加
# toTop 表示是否是向上
def vertical_move_cells(cell_arr, to_top):
    for i in range(0, 4):
        new_arr = []
        for j in range(0, 4):
            new_arr.append(cell_arr[j][i])
        result_arr = remove_zeros_and_add(new_arr, to_top)
        for j in range(0, 4):
            cell_arr[j][i] = result_arr[j]


# 1、去掉数组中的零，向头或向尾压缩数组。
# 0,4,0,4向左压缩变成：4,4,0,0. 向右压缩变成：0,0,4,4
# 2、相邻的数如果相同，则进行相加运算。
# 4,4,0,0向左叠加变成：8,0,0,0. 向右叠加变成：0,0,0,8
# toHead表示是否是头压缩
def remove_zeros_and_add(arr, to_head):
    new_arr = [0, 0, 0, 0]
    arr_without_zero = []
    non_zero_count = 0

    # 去掉所有的零
    for i in range(0, len(arr)):
        if arr[i] != 0:
            non_zero_count += 1
    for i in range(0, len(arr)):
        if arr[i] != 0:
            arr_without_zero.append(arr[i])

    if to_head:
        for i in range(0, len(arr_without_zero)):
            new_arr[i] = arr_without_zero[i]
        # 对相邻相同的数进行叠加
        for i in range(0, len(new_arr) - 1):
            if new_arr[3 - i] == new_arr[2 - i] and new_arr[3 - i] != 0:
                new_arr[3 - i] = 0
                new_arr[2 - i] *= 2
    else:
        for i in range(0, len(arr_without_zero)):
            new_arr[len(new_arr) - i - 1] = arr_without_zero[len(arr_without_zero) - i - 1]
        # 对相邻相同的数进行叠加
        for i in range(0, len(new_arr) - 1):
            if new_arr[i] == new_arr[i + 1] and new_arr[i] != 0:
                new_arr[i] = 0
                new_arr[i + 1] *= 2
    return new_arr


# 判断是否还可以移动。
# 1、当前单元格是否为0；
# 2、当前单元格和右侧单元格是否相等；
# 3、当前单元格和下方单元格是否相等。
def can_move(cell_arr):
    for i in range(0, 4):
        for j in range(0, 4):
            if cell_arr[j][i] == 0:
                return True
            if j < 3 and cell_arr[i][j] == cell_arr[i][j + 1]:  # 和右侧单元格比较，是否相等
                return True
            if i < 3 and cell_arr[i][j] == cell_arr[i + 1][j]:  # 和下方单元格比较，是否相等
                return True
    return False


def check_game_over_or_continue(cell_arr):
    if can_move(cell_arr):
        fill_one_emptyCell(cell_arr)
