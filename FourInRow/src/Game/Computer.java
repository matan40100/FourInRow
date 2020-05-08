package Game;

import java.awt.Image;
import java.util.Arrays;

public class Computer {
	private int ID;
	private String name;
	private int level;
	private int algorithm;
	private Image image;
	private Image winnerImage;

	private int bestCol;
	private int count = 0;
	private int Iterations = 0;

	//Constructor
	public Computer(int ID, String name, int level, int algorithm, Image image, Image winnerImage) {
		this.ID = ID;
		this.name = name;
		this.level = level;
		this.algorithm = algorithm;
		this.image = image;
		this.winnerImage = winnerImage;
	}

	public int gradeRow(int row, int column) {
		int gradeHuman = 0, gradeComputer = 0;
		int countHuman = 0, countComputer = 0;
		int i = 0;

		// Right.
		for (i = 0; i < Game.sequence && column + i < Board.numOfColumn; i++) {
			if (Board.logicalBoard[row][column + i] == ID) {
				countComputer++;
			} else if (Board.logicalBoard[row][column + i] == Game.HUMAN) {
				countHuman++;
			}
		}

		if (i == Game.sequence) {
			if (countHuman > 0 && countComputer == 0) {
				gradeHuman += (int) Math.pow(10, countHuman);
			}

			if (countComputer > 0 && countHuman == 0) {
				gradeComputer += (int) Math.pow(10, countComputer);
			}
		}

		// #1-Smart if
		if (column + Game.sequence < Board.numOfColumn && Board.logicalBoard[row][column] == Game.FREE_SPACE
				&& Board.logicalBoard[row][column + (Game.sequence - 1)] != Game.HUMAN
				&& Board.logicalBoard[row][column + Game.sequence] != Game.HUMAN && countHuman == (Game.sequence - 2)) {
			gradeHuman += 900;
		}

		// Left
		countComputer = countHuman = 0;
		for (i = 0; i < Game.sequence && column - i > -1; i++) {
			if (Board.logicalBoard[row][column - i] == ID) {
				countComputer++;
			} else if (Board.logicalBoard[row][column - i] == Game.HUMAN) {
				countHuman++;
			}
		}

		if (i == Game.sequence) {
			if (countHuman > 0 && countComputer == 0) {
				gradeHuman += (int) Math.pow(10, countHuman);
			}

			if (countComputer > 0 && countHuman == 0) {
				gradeComputer += (int) Math.pow(10, countComputer);
			}
		}

		// #1-Smart if
		if (column - Game.sequence > -1 && Board.logicalBoard[row][column] == Game.FREE_SPACE
				&& Board.logicalBoard[row][column - (Game.sequence - 1)] != Game.HUMAN
				&& Board.logicalBoard[row][column - Game.sequence] != Game.HUMAN && countHuman == (Game.sequence - 2)) {
			gradeHuman += 900;
		}

		return gradeComputer - gradeHuman;
	}

	public int gradeColumn(int row, int column) {
		int gradeHuman = 0, gradeComputer = 0;
		int countHuman = 0, countComputer = 0;
		int i = 0;

		for (i = 0; i < Game.sequence && row + i < Board.numOfRow; i++) {
			if (Board.logicalBoard[row + i][column] == ID) {
				countComputer++;
			} else if (Board.logicalBoard[row + i][column] == Game.HUMAN) {
				countHuman++;
			}
		}

		if (i == Game.sequence) {

			if (countHuman > 0 && countComputer == 0) {
				gradeHuman += (int) Math.pow(10, countHuman);
			}

			if (countComputer > 0 && countHuman == 0) {
				gradeComputer += (int) Math.pow(10, countComputer);
			}
		}

		return gradeComputer - gradeHuman;
	}

	public int gradeDiagonal(int row, int column) {
		int gradeHuman = 0, gradeComputer = 0;
		int countHuman = 0, countComputer = 0;
		int i = 0;

		// Up.
		for (i = 0; i < Game.sequence && column + i < Board.numOfColumn && row - i > -1; i++) {

			if (Board.logicalBoard[row - i][column + i] == ID) {
				countComputer++;
			} else if (Board.logicalBoard[row - i][column + i] == Game.HUMAN) {
				countHuman++;
			}
		}
		if (i == Game.sequence) {
			if (countHuman > 0 && countComputer == 0) {
				gradeHuman += (int) Math.pow(10, countHuman);
			}

			if (countComputer > 0 && countHuman == 0) {
				gradeComputer += (int) Math.pow(10, countComputer);
			}
		}

		// #1-Smart if
		if (column + Game.sequence < Board.numOfColumn && row - Game.sequence > -1
				&& Board.logicalBoard[row][column] == Game.FREE_SPACE
				&& Board.logicalBoard[row - (Game.sequence - 1)][column + (Game.sequence - 1)] != Game.HUMAN
				&& Board.logicalBoard[row - Game.sequence][column + Game.sequence] != Game.HUMAN
				&& countHuman == (Game.sequence - 2)) {
			gradeHuman += 900;
		}

		// Down.
		countComputer = countHuman = 0;
		for (i = 0; i < Game.sequence && column - i > -1 && row + i < Board.numOfRow; i++) {
			if (Board.logicalBoard[row + i][column - i] == ID) {
				countComputer++;
			} else if (Board.logicalBoard[row + i][column - i] == Game.HUMAN) {
				countHuman++;
			}
		}

		if (i == Game.sequence) {
			if (countHuman > 0 && countComputer == 0) {
				gradeHuman += (int) Math.pow(10, countHuman);
			}

			if (countComputer > 0 && countHuman == 0) {
				gradeComputer += (int) Math.pow(10, countComputer);
			}
		}

		// #1-Smart if
		if (column - Game.sequence > -1 && row + Game.sequence < Board.numOfRow
				&& Board.logicalBoard[row][column] == Game.FREE_SPACE
				&& Board.logicalBoard[row + (Game.sequence - 1)][column - (Game.sequence - 1)] != Game.HUMAN
				&& Board.logicalBoard[row + Game.sequence][column - Game.sequence] != Game.HUMAN
				&& countHuman == (Game.sequence - 2)) {
			gradeHuman += 900;
		}
		return gradeComputer - gradeHuman;
	}

	public int gradeBackDiagonal(int row, int column) {
		int gradeHuman = 0, gradeComputer = 0;
		int countHuman = 0, countComputer = 0;
		int i = 0;

		// Up.
		for (i = 0; i < Game.sequence && column - i > -1 && row - i > -1; i++) {
			if (Board.logicalBoard[row - i][column - i] == ID) {
				countComputer++;
			} else if (Board.logicalBoard[row - i][column - i] == Game.HUMAN) {
				countHuman++;
			}
		}

		if (i == Game.sequence) {
			if (countHuman > 0 && countComputer == 0) {
				gradeHuman += (int) Math.pow(10, countHuman);
			}

			if (countComputer > 0 && countHuman == 0) {
				gradeComputer += (int) Math.pow(10, countComputer);
			}

		}
		// #1-Smart if
		if (column - Game.sequence > -1 && row - Game.sequence > -1
				&& Board.logicalBoard[row][column] == Game.FREE_SPACE
				&& Board.logicalBoard[row - (Game.sequence - 1)][column - (Game.sequence - 1)] != Game.HUMAN
				&& Board.logicalBoard[row - Game.sequence][column - Game.sequence] != Game.HUMAN
				&& countHuman == (Game.sequence - 2)) {
			gradeHuman += 900;
		}

		// Down.
		countComputer = countHuman = 0;
		for (i = 0; i < Game.sequence && column + i < Board.numOfColumn && row + i < Board.numOfRow; i++) {

			if (Board.logicalBoard[row + i][column + i] == ID) {
				countComputer++;
			} else if (Board.logicalBoard[row + i][column + i] == Game.HUMAN) {
				countHuman++;
			}
		}
		
		if (i == Game.sequence) {

			if (countHuman > 0 && countComputer == 0) {
				gradeHuman += (int) Math.pow(10, countHuman);
			}

			if (countComputer > 0 && countHuman == 0) {
				gradeComputer += (int) Math.pow(10, countComputer);
			}
		}

		// #1-Smart if
		if (column + Game.sequence < Board.numOfColumn && row + Game.sequence < Board.numOfRow
				&& Board.logicalBoard[row][column] == Game.FREE_SPACE
				&& Board.logicalBoard[row + (Game.sequence - 1)][column + (Game.sequence - 1)] != Game.HUMAN
				&& Board.logicalBoard[row + Game.sequence][column + Game.sequence] != Game.HUMAN
				&& countHuman == (Game.sequence - 2)) {
			gradeHuman += 900;
		}
		return gradeComputer - gradeHuman;
	}

	// Return grade to the whole board by checking every space
	public int gradeBoard() {
		int grade = 0;
		for (int row = 0; row < Board.numOfRow; row++) {
			for (int column = 0; column < Board.numOfColumn; column++) {
				grade += gradeRow(row, column);
				grade += gradeColumn(row, column);
				grade += gradeDiagonal(row, column);
				grade += gradeBackDiagonal(row, column);
			}
		}
		return grade;
	}

	public int recCheckRow(int row, int column, int startColumn) {
		if ((column - startColumn == Game.sequence) || (row > Board.numOfRow - 1 || column > Board.numOfColumn - 1)
				|| (Board.logicalBoard[row][column] == Game.HUMAN)) {
			return 0;
		}

		for (int i = Game.sequence - 1; i > 0; i--) {
			if (column - i < 0 || Board.logicalBoard[row][column - i] == Game.HUMAN) {
				return 0 + recCheckRow(row, column + 1, startColumn);
			}
		}
		return 1 + recCheckRow(row, column + 1, startColumn);
	}

	public int recCheckCol(int row, int column, int startRow) {
		if ((row - startRow == Game.sequence) || (row > Board.numOfRow - 1 || column > Board.numOfColumn - 1)
				|| (Board.logicalBoard[row][column] == Game.HUMAN)) {
			return 0;
		}

		for (int i = Game.sequence - 1; i > 0; i--) {
			if (row - i < 0 || Board.logicalBoard[row - i][column] == Game.HUMAN) {
				return 0 + recCheckCol(row + 1, column, startRow);
			}
		}
		return 1 + recCheckCol(row + 1, column, startRow);
	}

	public int recCheckDiagonal(int row, int column, int startRow, int startColumn) {
		if ((column - startColumn == Game.sequence) || (row > Board.numOfRow - 1 || column > Board.numOfColumn - 1)
				|| row < 0 || (Board.logicalBoard[row][column] == Game.HUMAN)) {
			return 0;
		}

		for (int i = Game.sequence - 1; i > 0; i--) {

			if ((column - i < 0 || row + i > Board.numOfRow - 1)
					|| Board.logicalBoard[row + i][column - i] == Game.HUMAN) {
				return 0 + recCheckDiagonal(row - 1, column + 1, startRow, startColumn);
			}
		}
		return 1 + recCheckDiagonal(row - 1, column + 1, startRow, startColumn);
	}

	public int recCheckBackDiagonal(int row, int column, int startRow, int startColumn) {
		if ((column - startColumn == Game.sequence) || (row > Board.numOfRow - 1 || column > Board.numOfColumn - 1)
				|| row < 0 || (Board.logicalBoard[row][column] == Game.HUMAN)) {
			return 0;
		}

		for (int i = Game.sequence - 1; i > 0; i--) {

			if ((column - i < 0 || row - i < 0) || Board.logicalBoard[row - i][column - i] == Game.HUMAN) {
				return 0 + recCheckBackDiagonal(row + 1, column + 1, startRow, startColumn);
			}
		}
		return 1 + recCheckBackDiagonal(row + 1, column + 1, startRow, startColumn);
	}

	// Returns a grade based on the potential of the cell being tested
	public int gradeCell(int row, int column, int startRow, int startColumn) {
		return recCheckRow(row, column, startColumn) + recCheckCol(row, column, startRow)
				+ recCheckDiagonal(row, column, startRow, startColumn)
				+ recCheckBackDiagonal(row, column, startRow, startColumn);
	}

	// Return the best potential space
	public int findPotentialSpace() {
		int bestValue = 0;
		int bestIndex = 0;
		int[] potentialSpace = new int[Board.numOfColumn];

		for (int column = 0; column < Board.numOfColumn; column++) {
			if (Board.logicalBoard[Board.currentRowIndex[column]][column] == Game.COMPUTER
					|| Board.logicalBoard[Board.currentRowIndex[column]][column] == Game.HUMAN) {
				potentialSpace[column] = 0;
				continue;
			}

			potentialSpace[column] += gradeCell(Board.currentRowIndex[column], column, Board.currentRowIndex[column],
					column);

			if (bestValue < potentialSpace[column]) {
				bestValue = potentialSpace[column];
				bestIndex = column;
			}
		}
		System.out.println(Arrays.toString(potentialSpace));
		return bestIndex;
	}

	// Find if Computer has a potential to win, if true - win
	public boolean findPotentialWin(int row, int column) {
		if (row == -1) {
			return false;
		}
		int dupLogicalBoard[][] = Board.dupBoard(Board.logicalBoard);

		dupLogicalBoard[row][column] = ID;
		return (Board.checkWinner(dupLogicalBoard, column, ID, name));
	}

	// Find if Human has a potential to win, if true - block
	public boolean findPotentialLose(int row, int column) {
		if (row == -1) {
			return false;
		}
		int dupLogicalBoard[][] = Board.dupBoard(Board.logicalBoard);

		dupLogicalBoard[row][column] = Game.HUMAN;
		return (Board.checkWinner(dupLogicalBoard, column, Game.HUMAN, "Human"));
	}

	// 1# Algorithm
	public int negMax(int depth, int turn) {
		int best = Integer.MIN_VALUE;
		int value, col;
		if (depth == 0) {
			return -gradeBoard();
		}
		for (col = 0; col < Board.numOfColumn; col++) {
			if (Board.currentRowIndex[col] > -1) {
				Board.logicalBoard[Board.currentRowIndex[col]][col] = turn; // Make move
				Board.currentRowIndex[col]--;
				value = -negMax(depth - 1, 7 - turn);
				Iterations++;
				Board.currentRowIndex[col]++;
				Board.logicalBoard[Board.currentRowIndex[col]][col] = Game.FREE_SPACE; // Undo move
				if (value > best) {
					best = value;
					// Only keep best move in the first game tree level
					if (depth == level) {
						bestCol = col;
					}
				}
			}
		}
		return (best);
	}

	// 2# Algorithm
	public int alphaBeta(int depth, int turn, int alpha, int beta) {
		int value, col;
		if (depth == 0) {
			return -gradeBoard();
		}
		for (col = 0; col < Board.numOfColumn; col++) {
			if (Board.currentRowIndex[col] > -1) {
				Board.logicalBoard[Board.currentRowIndex[col]][col] = turn; // Make move
				Board.currentRowIndex[col]--;
				value = -alphaBeta(depth - 1, 7 - turn, -beta, -alpha);
				Iterations++;
				Board.currentRowIndex[col]++;
				Board.logicalBoard[Board.currentRowIndex[col]][col] = Game.FREE_SPACE; // Undo move
				if (value >= beta && depth < level) {
					return beta;
				}
				if (value > alpha) {
					alpha = value;
					bestCol = col;
				}
			}
		}
		return alpha;
	}

	public void computerMove() {
		
		for (int column = 0; column < Board.numOfColumn; column++) {
			if (findPotentialWin(Board.currentRowIndex[column], column)) {
				Board.animate(column, this.image);
				Board.updateBoard(column, ID, Game.HUMAN_VS_COMPUTER);
				Board.showWinnerSequence(this.winnerImage);
				Game.winnerDialog(name);
				return;
			} else if (findPotentialLose(Board.currentRowIndex[column], column)) {
				Board.animate(column, image);
				Board.updateBoard(column, ID, Game.HUMAN_VS_COMPUTER);
				Board.currentRowIndex[column]--;
				return;
			}
		}
		//If Computer start then computer use diffrent game strategy in few moves
		if (Game.startPlayer == ID && count < Board.numOfRow / 2) {
			int bestColumn = findPotentialSpace();
			Board.animate(bestColumn, this.image);
			Board.updateBoard(bestColumn, ID, Game.HUMAN_VS_COMPUTER);
			Board.currentRowIndex[bestColumn]--;
			count++;
		} else {

			if (algorithm == Game.MINMAX) {
				negMax(level, Game.turn);
				System.out.println("Iterations: " + Iterations);
				Iterations = 0;
			} else if (algorithm == Game.ALPHA_BETA) {
				alphaBeta(level, Game.turn, Integer.MIN_VALUE, Integer.MAX_VALUE);
				System.out.println("Iterations: " + Iterations);
				Iterations = 0;
			}
			Board.animate(bestCol, this.image);
			Board.updateBoard(bestCol, ID, Game.HUMAN_VS_COMPUTER);
			Board.currentRowIndex[bestCol]--;	
			if (Board.isBoardFull()) {
				Game.endGameDialog();
			}
		}
		Board.resetButton.setEnabled(true);
		Board.undoButton.setEnabled(true);
		Board.redoButton.setEnabled(true);
		Board.saveGameButton.setEnabled(true);
	}

	public int getLevel() {
		return level;
	}

	public int getAlgorithm() {
		return algorithm;
	}

	public int getID() {
		return ID;
	}
}