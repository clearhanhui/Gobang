package Client;

import java.util.Arrays;

public class Gobang {
    private int max_rows_cols = 15;
    private int board[][] = new int [max_rows_cols][max_rows_cols];
    
    public Gobang() {
        initializeBoard();
    }

    public void initializeBoard() {
        for (int[] line : board) {
            Arrays.fill(line, 0);
        }
    }
    public boolean isLegalLoc(int row, int col) {
        return row <= max_rows_cols && col <= max_rows_cols && board[row - 1][col - 1] == 0;
    }

    // 落子函数
    public void adhere(int row, int col, int toss) {
        if (isLegalLoc(row, col)) {
            board[row - 1][col - 1] = toss;
        } else {
            System.out.println("Check coordinate");
        }
    }
    public boolean checkSingleLine(int srow, int scol, int erow, int ecol, int drow, int dcol, int toss) {
        for (int count = 0; srow <= erow && scol <= ecol; srow += drow, scol += dcol) {
            if (board[srow - 1][scol - 1] == toss) {
                count++;
            } else {
                // reset count if not toss
                count = 0;
            }

            if (count == 5) {
                return true;
            }
        }
        return false;
    }
    public boolean checkFiveInLine(int row, int col, int toss) {
        if (checkSingleLine(row, 1, row, max_rows_cols, 0, 1, toss)){
            return true;
        }

        if (checkSingleLine(1, col, max_rows_cols, col, 1, 0, toss)){
            return true;
        }

        if (row >= col) {
            if (checkSingleLine(1 + row - col, 1, max_rows_cols, max_rows_cols - row + col, 1, 1, toss)) {
                return true;
            }
        } else {
            if (checkSingleLine(1, 1 + col - row, max_rows_cols - col + row, max_rows_cols, 1, 1, toss)) {
                return true;
            }
        }

        if (row + col - 1 >= max_rows_cols) {
            if (checkSingleLine(row + col - max_rows_cols, max_rows_cols, max_rows_cols, row + col - max_rows_cols, 1, -1, toss)) {
                return true;
            }
        } else {
            if (checkSingleLine(1, row + col - 1, row + col - 1, 1, 1, -1, toss)) {
                return true;
            }
        }
        return false;
    }
    public void printBoard() {
        int r, c;
        System.out.print("  ");
        for (c = 1; c <= max_rows_cols; c++) {
            System.out.print(String.format("%2d ", c));
        }
        System.out.print("\n");
        for (r = 1; r <= max_rows_cols; r++) {
            System.out.print(String.format("%2d ", r));
            for (c = 1; c <= max_rows_cols; c++) {
                if (board[r - 1][c - 1] == 1) {
                    System.out.print("*  ");
                } else if (board[r - 1][c - 1] == -1) {
                    System.out.print("o  ");
                } else {
                    System.out.print(".  ");
                }
            }
            System.out.print("\n");
        }
    }
}
