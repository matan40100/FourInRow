import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.lang.Math;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class Game extends JFrame {
	// VERSION: 2.7
	// LAST EDIT: 5.4.20

	// Free space - 0, Player One(RED) - 1, Player Two(BLUE) - 2, Computer - 3
	// HUMAN - 1, COMPUTER - 2
	private static final int HUMAN_VS_HUMAN = 0;
	private static final int HUMAN_VS_COMPUTER = 1;

	private int gameType;
	private int turn;

	private static final int FREE_SPACE = 0;
	private static final int PLAYER_ONE = 1;
	private static final int PLAYER_TWO = 2;

	private final int COMPUTER = 3;
	private final int HUMAN = 4;

	private final int EASY = 0;
	private final int MEDIUM = 1;
	private final int HARD = 2;
	private int difficultyLevel;

	private int numOfRow;
	private int numOfColumn;
	private int sequence;
	private Button[][] graphicsBoard;
	private int[][] logicalBoard;
	private int[] currentRowIndex;

	private File[] fileArray;

	private Image imgRed;
	private Image iconRed;
	private Image imgBlue;
	private Image iconBlue;
	private Image imgFreeSpace;

	private JPanel topPanel;
	private JPanel mainPanel;
	private GridLayout gridLayout;
	private JLabel turnIcon;
	private Timer timer;

	// Builder for game.
	public Game(int numOfRow, int numOfColumn, int sequence, int gameType, int difficultyLevel) {

		Random rand = new Random();

		this.numOfRow = numOfRow;
		this.numOfColumn = numOfColumn;
		this.sequence = sequence;
		this.turn = rand.nextInt(2 - 1 + 1) + 1;
		this.gameType = gameType;
		this.difficultyLevel = difficultyLevel;
		System.out.println("Game Settings:");
		System.out.println("Rows: " + this.numOfRow);
		System.out.println("Columns: " + this.numOfColumn);
		System.out.println("Sequence: " + this.sequence);
		System.out.println("Game Type: " + this.gameType);
		System.out.println("Difficult Level: " + this.difficultyLevel);

		if (gameType == HUMAN_VS_COMPUTER) {
			turn = 4;
		}

		initBoard();
		setTitle("N In Row");

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		pack();
		setVisible(true);

		changeTurnIcon(turn);
	}

	// Initializes the board - Create all the pannels,buttons,timer.
	public void initBoard() {
		try {
			createImages();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Font customFont = new Font("Arial", Font.PLAIN, 18);

		// Top panel
		this.topPanel = new JPanel();
		this.topPanel.setBackground(Color.WHITE);
		add(topPanel, BorderLayout.NORTH);
		this.topPanel.setLayout(new GridLayout(1, 3));

		// Create Options panel - Menu / reset
		JPanel optionsJPanel = new JPanel();

		JButton menuButton = new JButton("Menu");
		menuButton.addActionListener(e -> {
			
			Main.main(null);
			dispose();
		});
		optionsJPanel.add(menuButton);

		JButton resetButton = new JButton("Reset");
		resetButton.addActionListener(e -> {
			new Game(numOfRow, numOfColumn, sequence, gameType, difficultyLevel);
			dispose();

		});
		optionsJPanel.add(resetButton);

		optionsJPanel.setBackground(Color.WHITE);
		optionsJPanel
				.setBorder(new TitledBorder(null, "Options", TitledBorder.CENTER, TitledBorder.CENTER, customFont));
		this.topPanel.add(optionsJPanel);

		// Create turn panel
		JPanel turnJPanel = new JPanel();
		turnJPanel.setBackground(Color.WHITE);
		turnJPanel.setBorder(new TitledBorder(null, "Turn:", TitledBorder.CENTER, TitledBorder.CENTER,
				new Font("Arial", Font.BOLD, 18)));
		this.turnIcon = new JLabel();
		turnJPanel.add(this.turnIcon);
		this.topPanel.add(turnJPanel);

		// Create Timer + Panel
		JPanel timerJPanel = new JPanel();
		timerJPanel.setBackground(Color.WHITE);
		timerJPanel.setBorder(new TitledBorder(null, "Timer", TitledBorder.CENTER, TitledBorder.CENTER, customFont));
		JLabel timerJLabel = new JLabel("0:00");
		timerJLabel.setFont(new Font("Arial", Font.BOLD, 30));
		timerJPanel.add(timerJLabel);
		this.timer = new Timer(1000, new ActionListener() {
			int secOne = 0;
			int secTwo = 0;
			int min = 0;

			@Override
			public void actionPerformed(ActionEvent e) {
				secOne++;
				if (secOne == 10) {

					secOne = 0;
					secTwo++;
				}

				if (secTwo == 6) {
					min++;
					secOne = 0;
					secTwo = 0;
				}
				timerJLabel.setText(min + ":" + secTwo + secOne);
			}
		});
		this.timer.start();
		this.topPanel.add(timerJPanel);

		// Main panel
		this.mainPanel = new JPanel();
		this.gridLayout = new GridLayout(this.numOfRow, this.numOfColumn);
		add(mainPanel, BorderLayout.CENTER);
		this.mainPanel.setLayout(gridLayout);

		this.currentRowIndex = new int[this.numOfColumn];
		Arrays.fill(this.currentRowIndex, this.numOfRow - 1); // Initialize the array with the number of rows
		this.graphicsBoard = new Button[this.numOfRow][this.numOfColumn];
		this.logicalBoard = new int[this.numOfRow][this.numOfColumn];

		// Fill the graphicsBoard with buttons.
		for (int row = 0; row < this.numOfRow; row++) {
			for (int column = 0; column < this.numOfColumn; column++) {

				this.graphicsBoard[row][column] = new Button(imgFreeSpace);
				this.graphicsBoard[row][column].setPreferredSize(new Dimension(90, 90));
				this.graphicsBoard[row][column].addActionListener(new AL(column));
				this.mainPanel.add(this.graphicsBoard[row][column]);
			}
		}

	}

	// Make array with player icons
	public void createImages() throws IOException {
		this.fileArray = new File[5];
		fileArray[0] = new File("Images/free_cell.png");
		fileArray[1] = new File("Images/player_one.png");
		fileArray[2] = new File("Images/player_two.png");
		fileArray[3] = new File("Images/player_one_icon.png");
		fileArray[4] = new File("Images/player_two_icon.png");
		this.imgFreeSpace = ImageIO.read(fileArray[0]).getScaledInstance(90, 90, Image.SCALE_SMOOTH);
		this.imgRed = ImageIO.read(fileArray[1]).getScaledInstance(90, 90, Image.SCALE_SMOOTH);
		this.imgBlue = ImageIO.read(fileArray[2]).getScaledInstance(90, 90, Image.SCALE_SMOOTH);
		this.iconRed = ImageIO.read(fileArray[3]).getScaledInstance(60, 60, Image.SCALE_SMOOTH);
		this.iconBlue = ImageIO.read(fileArray[4]).getScaledInstance(60, 60, Image.SCALE_SMOOTH);
	}

	// Create a duplicate of matrix
	public int[][] dupBoard(int[][] board) {
		int dup[][] = new int[this.numOfRow][this.numOfColumn];
		for (int row = 0; row < this.numOfRow; row++) {
			for (int col = 0; col < this.numOfColumn; col++) {
				dup[row][col] = board[row][col];
			}
		}
		return dup;
	}

	// Change the icon turn every turn.
	public void changeTurnIcon(int turn) {
		if (turn == PLAYER_ONE || turn == COMPUTER) {

			this.turnIcon.setIcon(new ImageIcon(iconRed));

		} else if (turn == PLAYER_TWO || turn == HUMAN) {

			this.turnIcon.setIcon(new ImageIcon(iconBlue));

		}
	}

	// Open dialog that show who win the game with options to reset or exit.
	public void winnerDialog(String winnerName) {
		ImageIcon icon = new ImageIcon("Images/trophy.png");
		String[] options = { "Play again", "Exit" };
		if (winnerName.equals("Player One") || winnerName.equals("Computer")) {
			UIManager.put("OptionPane.messageForeground", Color.RED);
			UIManager.put("OptionPane.messageFont", new Font("Arial", Font.BOLD, 14));
		} else {
			UIManager.put("OptionPane.messageForeground", Color.BLUE);
			UIManager.put("OptionPane.messageFont", new Font("Arial", Font.BOLD, 14));

		}

		int response = JOptionPane.showOptionDialog(null, winnerName + " WIN.", "Game Settings",
				JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, icon, options, options[0]);

		switch (response) {
			case -1:
				System.out.println("Option Dialog Window Was Closed");
				System.exit(0);

			case 0:
				dispose();
				new Game(numOfRow, numOfColumn, sequence, gameType, difficultyLevel);
				break;
			case 1:
				System.exit(0);
				break;

			default:
				break;
		}
	}

	// Check if the board is full. False - exit, True - open dialog.
	public void isBoardFull() {

		for (int i = 0; i < this.numOfColumn; i++) {
			if (this.logicalBoard[0][i] == FREE_SPACE) {
				return;
			}
		}

		String[] options = { "Play again", "Exit" };
		int response = JOptionPane.showOptionDialog(null, "Tie", "The board is full", JOptionPane.WARNING_MESSAGE,
				JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

		switch (response) {
			case -1:
				System.out.println("Option Dialog Window Was Closed");
				System.exit(0);

			case 0:
				dispose();
				new Game(numOfRow, numOfColumn, sequence, gameType, difficultyLevel);
				break;
			case 1:
				System.exit(0);
				break;

			default:
				break;
		}

	}


	public boolean checkRow(int[][] board, int row, int column, int player, String name) {
		int count = 0;
		int testRange = this.sequence - 3;

		// Right.
		for (int funcColumn = column - testRange; funcColumn < this.numOfColumn; funcColumn++) {
			if (funcColumn >= 0) {
				if (board[row][funcColumn] == player) {
					count++;
					if (count == this.sequence) {
						System.out.println(name + " WIN(Right)");
						return true;
					}
				} else {
					count = 0;
				}
			}
		}

		// Left.
		count = 0;
		for (int funcColumn = column + testRange; funcColumn >= 0; funcColumn--) {
			if (funcColumn < this.numOfColumn) {

				if (board[row][funcColumn] == player) {
					count++;

					if (count == this.sequence) {
						System.out.println(name + " WIN(Left)");
						return true;
					}

				} else {
					count = 0;
				}
			}
		}

		return false;
	}

	public boolean checkColumn(int[][] board, int row, int column, int player, String name) {
		int count = 0;
		for (int funcRow = row; funcRow < this.numOfRow; funcRow++) {
			if (board[funcRow][column] == player) {
				count++;

				if (count == this.sequence) {
					System.out.println(name + " WIN(Down)");
					return true;
				}
			} else {
				return false;
			}
		}
		return false;
	}

	public boolean checkDiagonal(int[][] board, int row, int column, int player, String name) {

		int count = 0;
		int testRange = this.sequence - 3;

		// Up.
		for (int funcRow = row + testRange, funcColumn = column - testRange; funcRow >= 0
				&& funcColumn < this.numOfColumn; funcRow--, funcColumn++) {
			if (funcRow < this.numOfRow && funcColumn >= 0) {
				if (board[funcRow][funcColumn] == player) {
					count++;

					if (count == this.sequence) {
						System.out.println(name + " WIN(Slash up)");
						return true;

					}
				} else {
					count = 0;
				}
			}
		}

		// Down.
		count = 0;
		for (int funcRow = row - testRange, funcColumn = column + testRange; funcRow < this.numOfRow
				&& funcColumn >= 0; funcRow++, funcColumn--) {

			if (funcRow >= 0 && funcColumn < this.numOfColumn) {
				if (board[funcRow][funcColumn] == player) {
					count++;

					if (count == this.sequence) {
						System.out.println(name + " WIN(Slash down)");
						return true;
					}
				} else {
					count = 0;
				}
			}
		}
		return false;
	}

	public boolean checkBackDiagonal(int[][] board, int row, int column, int player, String name) {
		int count = 0;
		int testRange = this.sequence - 3;

		// Up.
		for (int funcRow = row + testRange, funcColumn = column + testRange; funcRow >= 0
				&& funcColumn >= 0; funcRow--, funcColumn--) {

			if (funcRow < this.numOfRow && funcColumn < this.numOfColumn) {
				if (board[funcRow][funcColumn] == player) {
					count++;

					if (count == this.sequence) {
						System.out.println(name + " WIN(Back Slash up)");
						return true;
					}
				} else {
					count = 0;
				}
			}
		}

		// Down.
		count = 0;
		for (int funcRow = row - testRange, funcColumn = column - testRange; funcRow < this.numOfRow
				&& funcColumn < this.numOfColumn; funcRow++, funcColumn++) {
			if (funcRow >= 0 && funcColumn >= 0) {
				if (board[funcRow][funcColumn] == player) {
					count++;

					if (count == this.sequence) {
						System.out.println(name + " WIN(Back Slash down)");
						return true;
					}
				} else {
					count = 0;
				}
			}
		}
		return false;
	}

	// Group all the functions to check who win.
	public boolean checkWinner(int[][] board, int column, int player, String name) {
		if (checkRow(board, currentRowIndex[column], column, player, name)
				|| checkColumn(board, currentRowIndex[column], column, player, name)
				|| checkDiagonal(board, currentRowIndex[column], column, player, name)
				|| checkBackDiagonal(board, currentRowIndex[column], column, player, name)) {
			timer.stop();
			return true;

		}
		return false;

	}


	public int gradeRow(int row, int column) {
		int gradeHuman = 0, gradeComputer = 0;
		int countHuman = 0, countComputer = 0;
		int i = 0;

		// Right.
		for (i = 0; i < this.sequence && column + i < this.numOfColumn; i++) {
			if (this.logicalBoard[row][column + i] == COMPUTER) {
				countComputer++;
			} else if (this.logicalBoard[row][column + i] == HUMAN) {
				countHuman++;
			}

		}

		if (i == this.sequence) {

			if (countHuman > 0 && countComputer == 0) {
				gradeHuman += (int) Math.pow(10, countHuman);

			}

			if (countComputer > 0 && countHuman == 0) {
				gradeComputer += (int) Math.pow(10, countComputer);
			}
		}

		// #1-Smart if
		if (column + this.sequence < this.numOfColumn && this.logicalBoard[row][column] == FREE_SPACE
				&& this.logicalBoard[row][column + (this.sequence - 1)] == FREE_SPACE
				&& this.logicalBoard[row][column + this.sequence] == FREE_SPACE && countHuman == (this.sequence - 2)) {
			gradeHuman += 800;
		}

		// Left
		countComputer = countHuman = 0;
		for (i = 0; i < this.sequence && column - i > -1; i++) {
			if (this.logicalBoard[row][column - i] == COMPUTER) {
				countComputer++;
			} else if (this.logicalBoard[row][column - i] == HUMAN) {
				countHuman++;
			}
		}

		if (i == this.sequence) {

			if (countHuman > 0 && countComputer == 0) {
				gradeHuman += (int) Math.pow(10, countHuman);
			}

			if (countComputer > 0 && countHuman == 0) {
				gradeComputer += (int) Math.pow(10, countComputer);
			}
		}

		// #1-Smart if
		if (column - this.sequence > -1 && this.logicalBoard[row][column] == FREE_SPACE
				&& this.logicalBoard[row][column - (this.sequence - 1)] == FREE_SPACE
				&& this.logicalBoard[row][column - this.sequence] == FREE_SPACE && countHuman == (this.sequence - 2)) {
			gradeHuman += 800;
		}

		return gradeComputer - gradeHuman;
	}

	public int gradeColumn(int row, int column) {
		int gradeHuman = 0, gradeComputer = 0;
		int countHuman = 0, countComputer = 0;
		int i = 0;

		for (i = 0; i < this.sequence && row + i < this.numOfRow; i++) {
			if (this.logicalBoard[row + i][column] == COMPUTER) {
				countComputer++;
			} else if (this.logicalBoard[row + i][column] == HUMAN) {
				countHuman++;
			}
		}

		if (i == this.sequence) {

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
		for (i = 0; i < this.sequence && column + i < this.numOfColumn && row - i > -1; i++) {

			if (this.logicalBoard[row - i][column + i] == COMPUTER) {
				countComputer++;
			} else if (this.logicalBoard[row - i][column + i] == HUMAN) {
				countHuman++;
			}
		}
		if (i == this.sequence) {

			if (countHuman > 0 && countComputer == 0) {
				gradeHuman += (int) Math.pow(10, countHuman);
			}

			if (countComputer > 0 && countHuman == 0) {
				gradeComputer += (int) Math.pow(10, countComputer);
			}

		}
		// #1-Smart if
		if (column + this.sequence < this.numOfColumn && row - this.sequence > -1
				&& this.logicalBoard[row][column] == FREE_SPACE
				&& this.logicalBoard[row - (this.sequence - 1)][column + (this.sequence - 1)] == FREE_SPACE
				&& this.logicalBoard[row - this.sequence][column + this.sequence] == FREE_SPACE
				&& countHuman == (this.sequence - 2)) {
			gradeHuman += 800;

		}

		// Down.
		countComputer = countHuman = 0;

		for (i = 0; i < this.sequence && column - i > -1 && row + i < this.numOfRow; i++) {

			if (this.logicalBoard[row + i][column - i] == COMPUTER) {
				countComputer++;
			} else if (this.logicalBoard[row + i][column - i] == HUMAN) {
				countHuman++;
			}
		}

		if (i == this.sequence) {

			if (countHuman > 0 && countComputer == 0) {
				gradeHuman += (int) Math.pow(10, countHuman);
			}

			if (countComputer > 0 && countHuman == 0) {
				gradeComputer += (int) Math.pow(10, countComputer);
			}
		}

		// #1-Smart if
		if (column - this.sequence > -1 && row + this.sequence < this.numOfRow
				&& this.logicalBoard[row][column] == FREE_SPACE
				&& this.logicalBoard[row + (this.sequence - 1)][column - (this.sequence - 1)] == FREE_SPACE
				&& this.logicalBoard[row + this.sequence][column - this.sequence] == FREE_SPACE
				&& countHuman == (this.sequence - 2)) {
			gradeHuman += 800;
		}

		return gradeComputer - gradeHuman;
	}

	public int gradeBackDiagonal(int row, int column) {
		int gradeHuman = 0, gradeComputer = 0;
		int countHuman = 0, countComputer = 0;
		int i = 0;

		// Up.
		for (i = 0; i < this.sequence && column - i > -1 && row - i > -1; i++) {

			if (this.logicalBoard[row - i][column - i] == COMPUTER) {
				countComputer++;
			} else if (this.logicalBoard[row - i][column - i] == HUMAN) {
				countHuman++;
			}
		}

		if (i == this.sequence) {

			if (countHuman > 0 && countComputer == 0) {
				gradeHuman += (int) Math.pow(10, countHuman);
			}

			if (countComputer > 0 && countHuman == 0) {
				gradeComputer += (int) Math.pow(10, countComputer);
			}

		}
		// #1-Smart if
		if (column - this.sequence > -1 && row - this.sequence >-1
				&& this.logicalBoard[row][column] == FREE_SPACE
				&& this.logicalBoard[row - (this.sequence - 1)][column - (this.sequence - 1)] == FREE_SPACE
				&& this.logicalBoard[row - this.sequence][column - this.sequence] == FREE_SPACE
				&& countHuman == (this.sequence - 2)) {
			gradeHuman += 800;
		}

		// Down.
		countComputer = countHuman = 0;

		for (i = 0; i < this.sequence && column + i < this.numOfColumn && row + i < this.numOfRow; i++) {

			if (this.logicalBoard[row + i][column + i] == COMPUTER) {
				countComputer++;
			} else if (this.logicalBoard[row + i][column + i] == HUMAN) {
				countHuman++;
			}
		}
		if (i == this.sequence) {

			if (countHuman > 0 && countComputer == 0) {
				gradeHuman += (int) Math.pow(10, countHuman);
			}

			if (countComputer > 0 && countHuman == 0) {
				gradeComputer += (int) Math.pow(10, countComputer);
			}
		}

		// #1-Smart if
		if (column + this.sequence < this.numOfColumn && row +this.sequence <this.numOfRow
				&& this.logicalBoard[row][column] == FREE_SPACE
				&& this.logicalBoard[row + (this.sequence - 1)][column + (this.sequence - 1)] == FREE_SPACE
				&& this.logicalBoard[row + this.sequence][column + this.sequence] == FREE_SPACE
				&& countHuman == (this.sequence - 2)) {
			gradeHuman += 800;
			System.out.println("here");
		}

		return gradeComputer - gradeHuman;
	}

	//Give grade to the whole board by checking every free spcae
	public int gradeBoard() {
		int grade = 0;
		for (int row = 0; row < this.numOfRow; row++) {
			for (int column = 0; column < this.numOfColumn; column++) {
				grade += gradeRow(row, column);
				grade += gradeColumn(row, column);
				grade += gradeDiagonal(row, column);
				grade += gradeBackDiagonal(row, column);
			}
		}
		return grade;
	}

	public int[] computerMove() {
		int[] gradeArray = new int[numOfColumn];
		for (int column = 0; column < this.numOfColumn; column++) {
			if (this.currentRowIndex[column] == -1) {
				break;
			}
			this.logicalBoard[this.currentRowIndex[column]][column] = COMPUTER;
			gradeArray[column] = gradeBoard();
			this.logicalBoard[this.currentRowIndex[column]][column] = FREE_SPACE;
		}
		return gradeArray;

	}

	// Find if Computer has a potential to win, if true - win
	public boolean findPotentialWin(int row, int column) {
		if (row == -1) {
			return false;
		}
		int dupLogicalBoard[][] = dupBoard(this.logicalBoard);

		dupLogicalBoard[row][column] = COMPUTER;
		return (checkWinner(dupLogicalBoard, column, COMPUTER, "Computer"));

	}

	// Find if Human has a potential to win, if true - block
	public boolean findPotentialLose(int row, int column) {
		if (row == -1) {
			return false;
		}
		int dupLogicalBoard[][] = dupBoard(this.logicalBoard);

		dupLogicalBoard[row][column] = HUMAN;
		return (checkWinner(dupLogicalBoard, column, HUMAN, "Human"));

	}

	class AL implements ActionListener {
		private int col;

		public AL(int col) {
			this.col = col;
		}

		public void actionPerformed(ActionEvent e) {
			if (gameType == HUMAN_VS_HUMAN) {

				if (turn == PLAYER_ONE && currentRowIndex[this.col] > -1) {

					logicalBoard[currentRowIndex[this.col]][this.col] = PLAYER_ONE;
					graphicsBoard[currentRowIndex[this.col]][this.col].setImage(imgRed);
					graphicsBoard[currentRowIndex[this.col]][this.col].repaint();

					System.out.println();
					System.out.println(Arrays.deepToString(logicalBoard).replace("[", " ").replace("],", " \n")
							.replace("]]", "").replace(", ", " "));

					if (checkWinner(logicalBoard, this.col, PLAYER_ONE, "Player One")) {

						winnerDialog("Player One");
						return;
					}
					currentRowIndex[this.col]--;
					isBoardFull();
					System.out.println(Arrays.toString(currentRowIndex));
					turn = 3 - turn;
					changeTurnIcon(turn);

				} else if (turn == PLAYER_TWO && currentRowIndex[this.col] > -1) {

					logicalBoard[currentRowIndex[this.col]][this.col] = PLAYER_TWO;
					graphicsBoard[currentRowIndex[this.col]][this.col].setImage(imgBlue);
					graphicsBoard[currentRowIndex[this.col]][this.col].repaint();

					System.out.println();
					System.out.println(Arrays.deepToString(logicalBoard).replace("[", " ").replace("],", " \n")
							.replace("]]", "").replace(", ", " "));

					if (checkWinner(logicalBoard, this.col, PLAYER_TWO, "Player Two")) {

						winnerDialog("Player Two");
						return;
					}

					currentRowIndex[this.col]--;
					isBoardFull();
					System.out.println(Arrays.toString(currentRowIndex));
					turn = 3 - turn;
					changeTurnIcon(turn);

				}

			} else if (gameType == HUMAN_VS_COMPUTER) {
				// turn = HUMAN;
				if (turn == HUMAN && currentRowIndex[this.col] > -1) {

					logicalBoard[currentRowIndex[this.col]][this.col] = HUMAN;
					graphicsBoard[currentRowIndex[this.col]][this.col].setImage(imgBlue);
					graphicsBoard[currentRowIndex[this.col]][this.col].repaint();

					System.out.println();
					System.out.println(Arrays.deepToString(logicalBoard).replace("[", " ").replace("],", " \n")
							.replace("]]", "").replace(", ", " "));

					if (checkWinner(logicalBoard, this.col, HUMAN, "Human")) {

						winnerDialog("Human");
						return;
					}
					currentRowIndex[this.col]--;
					isBoardFull();
					System.out.println(Arrays.toString(currentRowIndex));
					turn = 7 - turn;
					changeTurnIcon(turn);

					int[] gradeArray = new int[numOfColumn];
					for (int col = 0; col < numOfColumn; col++) {
						if (findPotentialWin(currentRowIndex[col], col)) {
							logicalBoard[currentRowIndex[col]][col] = COMPUTER;
							graphicsBoard[currentRowIndex[col]][col].setImage(imgRed);
							graphicsBoard[currentRowIndex[col]][col].repaint();
							winnerDialog("Computer");
							return;
						} else if (findPotentialLose(currentRowIndex[col], col)) {
							logicalBoard[currentRowIndex[col]][col] = COMPUTER;
							graphicsBoard[currentRowIndex[col]][col].setImage(imgRed);
							graphicsBoard[currentRowIndex[col]][col].repaint();
							currentRowIndex[col]--;
							turn = 7 - turn;
							changeTurnIcon(turn);
							return;
						}
						gradeArray = computerMove();

					}
					System.out.println(Arrays.toString(gradeArray));
					int max = gradeArray[0];
					for (int i = 1; i < gradeArray.length; i++) {
						if (gradeArray[i] > max) {

							max = gradeArray[i];
						}
					}
					System.out.println(max);

					/*
					 * new Thread(new Runnable() { public void run() { try { Thread.sleep(1500);
					 * findPotentialWinLose(); } catch (InterruptedException ex) { } } }).start();
					 */

				} else if (turn == COMPUTER && currentRowIndex[this.col] > -1) {

					logicalBoard[currentRowIndex[this.col]][this.col] = COMPUTER;
					graphicsBoard[currentRowIndex[this.col]][this.col].setImage(imgRed);
					graphicsBoard[currentRowIndex[this.col]][this.col].repaint();

					System.out.println();
					System.out.println(Arrays.deepToString(logicalBoard).replace("[", " ").replace("],", " \n")
							.replace("]]", "").replace(", ", " "));

					if (checkWinner(logicalBoard, this.col, HUMAN, "Computer")) {

						winnerDialog("Computer");
						return;
					}
					currentRowIndex[this.col]--;
					isBoardFull();
					System.out.println(Arrays.toString(currentRowIndex));
					turn = 7 - turn;
					changeTurnIcon(turn);

				}

			}
		}

	}

}
