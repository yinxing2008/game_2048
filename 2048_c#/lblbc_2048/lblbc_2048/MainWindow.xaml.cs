using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection.Emit;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace lblbc_2048
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        private int[,] cellArr = new int[4, 4];
        public MainWindow()
        {
            InitializeComponent();
            ShowCells();
            StartGame();
        }

        private void StartGame()
        {
            cellArr = new int[4, 4] { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };
            FillOneEmptyCell();
            FillOneEmptyCell();
            ShowCells();
        }

        private void FillOneEmptyCell()
        {
            int emptyCellPosition = FindOneEmptyCell();
            if (emptyCellPosition != -1)
            {
                cellArr[emptyCellPosition / 4, emptyCellPosition % 4] = GetRandomValue();
            }
        }
        private int FindOneEmptyCell()
        {
            List<int> list = new List<int>();
            for (int i = 0; i < 4; i++)
            {
                for (int j = 0; j < 4; j++)
                {
                    if (cellArr[i, j] == 0)
                    {
                        list.Add(i * 4 + j);
                    }
                }
            }
            int position = -1;
            if (list.Count > 0)
            {
                position = list[RandomVal(list.Count)];
            }
            return position;
        }

        /**
     * 生成新的数字，90%几率生成2，10%几率生成4
     */
        private int GetRandomValue()
        {
            int rand = RandomVal(10);
            int value = 2;
            if (rand >= 9)
            {
                value = 4;
            }
            return value;
        }

        private int RandomVal(int max)
        {
            Random random = new Random();
            return random.Next(max);
        }
        /**
   * 判断是否还可以移动。
   * 1、当前单元格是否为0；
   * 2、当前单元格和右侧单元格是否相等；
   * 3、当前单元格和下方单元格是否相等。
   */
        private bool CanMove()
        {
            for (int i = 0; i < 4; i++)
            {
                for (int j = 0; j < 4; j++)
                {
                    if (cellArr[j, i] == 0)
                    {
                        return true;
                    }
                    if (j < 3 && cellArr[i, j] == cellArr[i, j + 1])
                    {//和右侧单元格比较，是否相等
                        return true;
                    }
                    if (i < 3 && cellArr[i, j] == cellArr[i + 1, j])
                    {//和下方单元格比较，是否相等
                        return true;
                    }
                }
            }

            return false;
        }

        /**
   * 将单元格数向左或向右移动，移除零并对相邻相同数进行叠加
   *
   * @param toLeft 表示是否是向左
   */
        private void HorizontalMoveCells(bool toLeft)
        {
            for (int i = 0; i < 4; i++)
            {
                int[] newArr = new int[4];

                for (int j = 0; j < 4; j++)
                {
                    newArr[j] = cellArr[i, j];
                }

                int[] resultArr = RemoveZerosAndAdd(newArr, toLeft);
                for (int j = 0; j < 4; j++)
                {
                    cellArr[i, j] = resultArr[j];
                }
            }
        }

        /**
         * 将单元格数向下或向上移动，移除零并对相邻相同数进行叠加
         *
         * @param toTop 表示是否是向上
         */
        private void VerticalMoveCells(bool toTop)
        {
            for (int i = 0; i < 4; i++)
            {
                int[] newArr = new int[4];

                for (int j = 0; j < 4; j++)
                {
                    newArr[j] = cellArr[j, i];
                }

                int[] resultArr = RemoveZerosAndAdd(newArr, toTop);
                for (int j = 0; j < 4; j++)
                {
                    cellArr[j, i] = resultArr[j];
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
        private int[] RemoveZerosAndAdd(int[] arr, bool toHead)
        {
            int[] newArr = new int[4];
            List<int> arrWithoutZero = new List<int>();
            for (int i = 0; i < arr.Length; i++)
            {
                if (arr[i] != 0)
                {
                    arrWithoutZero.Add(arr[i]);
                }
            }

            if (arrWithoutZero.Count == 0) return newArr;
            if (toHead)
            {
                for (int i = 0; i < arrWithoutZero.Count; i++)
                {
                    newArr[i] = arrWithoutZero[i];
                }

                //对相邻相同的数进行叠加
                for (int i = 0; i < newArr.Length - 1; i++)
                {
                    if (newArr[3 - i] == newArr[2 - i] && newArr[3 - i] != 0)
                    {
                        newArr[3 - i] = 0;
                        newArr[2 - i] *= 2;
                    }
                }
            }
            else
            {
                for (int i = 0; i < arrWithoutZero.Count; i++)
                {
                    newArr[newArr.Length - i - 1] = arrWithoutZero[arrWithoutZero.Count - i - 1];
                }

                //对相邻相同的数进行叠加
                for (int i = 0; i < newArr.Length - 1; i++)
                {
                    if (newArr[i] == newArr[i + 1] && newArr[i] != 0)
                    {
                        newArr[i] = 0;
                        newArr[i + 1] *= 2;
                    }
                }
            }

            return newArr;
        }

        private static String GetCellBackgroundColor(int cellValue)
        {
            switch (cellValue)
            {
                case 0:
                    return "#BBADA0";
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

        private void MoveUp()
        {
            VerticalMoveCells(true);
            CheckGameOverOrContinue();
        }

        private void MoveDown()
        {
            VerticalMoveCells(false);
            CheckGameOverOrContinue();
        }

        private void MoveLeft()
        {
            HorizontalMoveCells(true);
            CheckGameOverOrContinue();
        }

        private void MoveRight()
        {
            HorizontalMoveCells(false);
            CheckGameOverOrContinue();
        }

        private void CheckGameOverOrContinue()
        {
            if (CanMove())
            {
                FillOneEmptyCell();
                ShowCells();
            }
            else
            {
                MessageBox.Show("游戏结束");
                StartGame();
            }
        }

        private void ShowCells()
        {
            ShowCell(label0, cellArr[0, 0]);
            ShowCell(label1, cellArr[0, 1]);
            ShowCell(label2, cellArr[0, 2]);
            ShowCell(label3, cellArr[0, 3]);
            ShowCell(label4, cellArr[1, 0]);
            ShowCell(label5, cellArr[1, 1]);
            ShowCell(label6, cellArr[1, 2]);
            ShowCell(label7, cellArr[1, 3]);
            ShowCell(label8, cellArr[2, 0]);
            ShowCell(label9, cellArr[2, 1]);
            ShowCell(label10, cellArr[2, 2]);
            ShowCell(label11, cellArr[2, 3]);
            ShowCell(label12, cellArr[3, 0]);
            ShowCell(label13, cellArr[3, 1]);
            ShowCell(label14, cellArr[3, 2]);
            ShowCell(label15, cellArr[3, 3]);
        }

        private static void ShowCell(System.Windows.Controls.Label label, int cellValue)
        {
            if (cellValue == 0)
            {
                label.Content = "";
            }
            else
            {
                label.Content = cellValue.ToString();
            }
            label.Background = new BrushConverter().ConvertFrom(GetCellBackgroundColor(cellValue)) as SolidColorBrush;
        }

        private void Window_KeyDown(object sender, KeyEventArgs e)
        {
            if (e.KeyStates == Keyboard.GetKeyStates(Key.Up))
            {
                MoveUp();
            }
            else if (e.KeyStates == Keyboard.GetKeyStates(Key.Down))
            {
                MoveDown();
            }
            else if (e.KeyStates == Keyboard.GetKeyStates(Key.Left))
            {
                MoveLeft();
            }
            else if (e.KeyStates == Keyboard.GetKeyStates(Key.Right))
            {
                MoveRight();
            }
        }
    }
}
