import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

import javax.swing.*;

public class Game extends JFrame {
	//VERSION: 1.3 
	//LAST EDIT: 13.01.20
	
	// Free space - 0, Player one(RED) - 1, Player two(BLUE) - 2, Computer - 3
	// HUMAN - 1, COMPUTER - 2
	private final int FREE_SPACE = 0;
	private final int PLAYER_ONE = 1;
	private final int PLAYER_TWO = 2;
	private final int COMPUTER = 3;
	private int turn = 1;

	private int numOfRow = 6;
	private int numOfColumn = 7;
	private int sequence = 4;
	private Button[][] graphicsBoard;
	private int[][] logicalBoard;
	private int[] currentRowIndex;

	public Game() {
		setTitle("FourInRow");
		initBoard();
		setSize(500, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

	}

	public void initBoard() {
		ImageIcon icon = new ImageIcon("free_cell.jpg"); // Icon of free cell
		Image img = icon.getImage();

		this.currentRowIndex = new int[this.numOfColumn];
		Arrays.fill(this.currentRowIndex, this.numOfRow); // Initialize the array with the number of rows
		System.out.println(Arrays.toString(this.currentRowIndex));
		
		this.graphicsBoard = new Button[this.numOfRow][this.numOfColumn];
		this.logicalBoard = new int[this.numOfRow + 2][this.numOfColumn + 2];

		setLayout(new GridLayout(this.numOfRow, this.numOfColumn));

		for (int row = 0; row < this.numOfRow; row++) {
			for (int column = 0; column < this.numOfColumn; column++) {

				this.graphicsBoard[row][column] = new Button(img);
				this.graphicsBoard[row][column].addActionListener(new AL(row, column));

				add(this.graphicsBoard[row][column]);
			}
		}

		createBorder();

	}

	public void createBorder() {
		for (int borderUpdDown = 0; borderUpdDown < this.numOfColumn + 2; borderUpdDown++) // Numbering the matrix row
		// and column
		{
			logicalBoard[0][borderUpdDown] = 9;
			logicalBoard[this.numOfRow + 1][borderUpdDown] = 9;

		}

		for (int borderRightLeft = 0; borderRightLeft < this.numOfRow + 2; borderRightLeft++) // Numbering the matrix
		// row and column
		{
			logicalBoard[borderRightLeft][0] = 9;
			logicalBoard[borderRightLeft][this.numOfColumn + 1] = 9;

		}
	}

	public boolean isBoardFull() {

		for (int i = 0; i < this.numOfColumn; i++) {
			if (this.logicalBoard[0][i] == this.FREE_SPACE) {
				return false;
			}
		}
		return true;
	}

	public boolean checkRight(int row, int column, int player) {

		int count = 0;
		for (int funcColumn = column - 1; funcColumn < this.numOfColumn + 2; funcColumn++) {

			if (this.logicalBoard[row][funcColumn] == player) {
				count++;
				if (count == this.sequence) {
					System.out.println("WIN(Right) - Row " + row + " Column " + column + " Sequence " + this.sequence);
					return true;
				}
			}
		}
		return false;
	}

	public boolean checkLeft(int row, int column, int player) {
		int count = 0;
		
		for (int funcColumn = column + 1; funcColumn  > 0; funcColumn--) {
			
			if (this.logicalBoard[row][funcColumn] == player) {
				count++;
				if (count == this.sequence) {
					System.out.println("WIN(Left) - Row " + row + " Column " + column + " Sequence " + this.sequence);
					return true;
				}
			}
		}
		return false;
	}

	class AL implements ActionListener {
		private int row;
		private int col;

		public AL(int row, int col) {
			this.row = row;
			this.col = col;
		}

		public void actionPerformed(ActionEvent e) {
			ImageIcon icon;
			Image img;
			
			icon = new ImageIcon("player_one_cell.jpg");
			img = icon.getImage();
			Button button = (Button) e.getSource();
			if (logicalBoard[currentRowIndex[this.col]][this.col+1] != 9) {
				logicalBoard[currentRowIndex[this.col]][this.col+1] = PLAYER_ONE;
				graphicsBoard[currentRowIndex[this.col]-1][this.col].setImage(img);
				repaint();
				

				System.out.println();
				System.out.println(Arrays.deepToString(logicalBoard).replace("[", " ").replace("],", " \n")
						.replace("]]", "").replace(", ", " "));

				checkRight(row+1, col+1, PLAYER_ONE);
				checkLeft(row+1, col+1, PLAYER_ONE);
				currentRowIndex[this.col]--;
				
				System.out.println(Arrays.toString(currentRowIndex));

				icon = new ImageIcon("player_one_cell.jpg");
				img = icon.getImage();
				button.setImage(img);
			}

		}

	}

}
