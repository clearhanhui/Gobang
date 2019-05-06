#include <stdio.h>
#include <string.h>

#define max_rows_cols 25

// 棋盘
int board[max_rows_cols][max_rows_cols];

// 初始化棋盘
void initializeBoard() {
    // reset board
    memset(board, 0, sizeof(board));
}

// 是否可下
int isLegal(int row, int col) {
    // empty and boundary
    if (row <= max_rows_cols && col <= max_rows_colsboard && [row - 1][col - 1] == 0) {
        // zero means it's empty in board
        return 1;
    } else {
        return 0;
    }
}

// 下棋
void adhere(int row, int col, int toss) {
    board[row - 1][col - 1] = toss;
}

// 检查一个方向
int checkSingleLine(int srow, int scol, int erow, int ecol, int drow, int dcol, int toss) {
    int count = 0;
    for (srow, scol; srow <= erow && scol <= ecol; srow += drow, scol += dcol) {
        if (board[srow - 1][scol - 1] == toss) {
            count++;
        } else {
            // reset count if not toss
            count = 0;
        }

        if (count == 5) {
            return 1;
        }
    }
    return 0;
}

/*
五子连珠
思路：
胜利的一方一定是最后落子的
检查最后落子的地方的四个方向
相同颜色的棋子是否连续出现五个
*/
int checkFiveInRow(int row, int col, int toss) {

    // left -> rigth, drow = 0, dcol = 1
    if (checkSingleLine(row, 1, row, max_rows_cols, 0, 1, toss)){
        return 1;
    }

    // above -> below, drow = 1, dcol = 0
    if (checkSingleLine(1, col, max_rows_cols, col, 1, 0, toss)){
        return 1;
    }

    // left_above -> right_below, drow = 1, dcol = -1
    if (row >= col) {
        if (checkSingleLine(1 + row - col, 1, max_rows_cols, max_rows_cols - row + col, 1, 1, toss)) {
            return 1;
        }
    } else {
        if (checkSingleLine(1, 1 + col - row, max_rows_cols - col + row, max_rows_cols, 1, 1, toss)) {
            return 1;
        }
    }

    // right_above -> left_below, drow = 1, dcol = -1
    if (row + col - 1 >= max_rows_cols) {
        if (checkSingleLine(row + col - max_rows_cols, max_rows_cols, max_rows_cols, row + col - max_rows_cols, 1, -1, toss)) {
            return 1;
        }
    } else {
        if (checkSingleLine(1, row + col - 1, row + col - 1, 1, 1, -1, toss)) {
            return 1;
        }
    }
    return 0;
}

// 打印棋盘
void printBoard() {
    int r, c;
    printf("  ");
    for (c = 1; c <= max_rows_cols; c++) {
        printf("%2d ", c);
    }
    printf("\n");
    for (r = 1; r <= max_rows_cols; r++) {
        printf("%2d ", r);
        for (c = 1; c <= max_rows_cols; c++) {
            if (board[r - 1][c -1 ] == 1) {
                printf("*  ");
            } else if (board[r - 1][c - 1] == -1) {
                printf("o  ");
            } else {
                printf(".  ");
            }
        }
        printf("\n");
    }
}


// 主函数
int main(void) {
    printf("Welcome to JiaoShou Han's Five in a Row!\n\n");
    while (1) {
        initializeBoard();
        printBoard();

        // define white_toss = -1, black_toss = 1
        int toss = 1; // first black
        int row, col;
        while (1) {
            printf("Input %s Coordinate:", toss > 0 ? "Black" : "White");
            scanf("%d %d", &row, &col);
            
            if (isLegal(row, col)) {
                adhere(row, col, toss);
            } else {
                printf("Wrong Selction\n");
                continue;
            }

            printBoard();

            if (checkFiveInRow(row, col, toss)) {
				if (toss == 1) {
					printf("Black Win\n");
					break;
				} else {
					printf("White Win\n");
					break;
				}
            } else {
                // next turn
                if (toss == 1) {
                    printf("Next is White\n");
                } else {
                    printf("Next is Black\n");
                }
            }
			toss = -toss;
        }
        printf("Want another match? (y/N)");
        char ch;
        scanf("%d", &ch);
        if (ch == 'N' || ch == 'n') {
            break;
        } else if (!(ch == 'Y' || ch == 'y')) {
            printf("Can not recongized\n");
        }
        getchar(); // get rid of last'\n', since scanf() will skip '\n'  '\t'  ' ', but they are still in the queue, and will read by getchar()
    }
    printf("\nBye~\n");
    return 0;
}