# 厦门大学计算机专业 | 前华为工程师
# 专注《零基础学编程系列》  http://lblbc.cn/blog
# 包含：Java | 安卓 | 前端 | Flutter | iOS | 小程序 | 鸿蒙
# 公众号：蓝不蓝编程
from tkinter import Frame, Label, CENTER
import logic
import constants as c


class GameView(Frame):
    def __init__(self):
        Frame.__init__(self)

        self.grid()
        self.master.title('2048')
        self.master.bind("<Key>", self.key_down)
        self.grid_cells = []
        self.init_grid()
        self.cell_arr = logic.start_game()
        self.update_cells()
        self.mainloop()

    def init_grid(self):
        background = Frame(self, bg=c.BACKGROUND_COLOR_GAME, width=400, height=400)
        background.grid()

        for i in range(4):
            grid_row = []
            for j in range(4):
                cell = Frame(
                    background,
                    bg=c.BACKGROUND_COLOR_CELL_EMPTY,
                    width=100,
                    height=100
                )
                cell.grid(
                    row=i,
                    column=j,
                    padx=10,
                    pady=10
                )
                t = Label(
                    master=cell,
                    text="",
                    bg=c.BACKGROUND_COLOR_CELL_EMPTY,
                    justify=CENTER,
                    font=c.FONT,
                    width=4,
                    height=2)
                t.grid()
                grid_row.append(t)
            self.grid_cells.append(grid_row)

    def update_cells(self):
        for i in range(4):
            for j in range(4):
                new_number = self.cell_arr[i][j]
                if new_number == 0:
                    self.grid_cells[i][j].configure(text="", bg=c.BACKGROUND_COLOR_CELL_EMPTY)
                else:
                    self.grid_cells[i][j].configure(
                        text=str(new_number),
                        bg=c.BACKGROUND_COLOR_DICT[new_number],
                        fg=c.CELL_COLOR_DICT[new_number]
                    )
        self.update_idletasks()

    def key_down(self, event):
        key = event.keysym

        if not logic.can_move(self.cell_arr):
            self.grid_cells[1][0].configure(text="你", bg=c.BACKGROUND_COLOR_CELL_EMPTY)
            self.grid_cells[1][1].configure(text="输", bg=c.BACKGROUND_COLOR_CELL_EMPTY)
            self.grid_cells[1][2].configure(text="了", bg=c.BACKGROUND_COLOR_CELL_EMPTY)
            self.grid_cells[1][3].configure(text="！", bg=c.BACKGROUND_COLOR_CELL_EMPTY)
        else:
            if key == c.KEY_UP:
                logic.move_up(self.cell_arr)
            if key == c.KEY_DOWN:
                logic.move_down(self.cell_arr)
            if key == c.KEY_LEFT:
                logic.move_left(self.cell_arr)
            if key == c.KEY_RIGHT:
                logic.move_right(self.cell_arr)
            self.update_cells()


game_view = GameView()
