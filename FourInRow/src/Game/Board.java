package Game;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class Board {
	protected static JFrame boardFrame;
	protected static int numOfRow;
	protected static int numOfColumn;
	protected static Button[][] graphicsBoard;
	protected static int[][] logicalBoard;
	protected static int[] currentRowIndex;

	protected JPanel topPanel;
	protected JPanel mainPanel;
	protected GridLayout gridLayout;
	protected static JLabel turnIcon;
	protected static Timer timer;
	protected static Queue<Integer> winnerSequenceIndex;

	// Builder for game.
	public Board(int numOfRow, int numOfColumn) {

		Board.winnerSequenceIndex = new LinkedList<>();

		Board.numOfRow = numOfRow;
		Board.numOfColumn = numOfColumn;

		// Create Main Frame
		boardFrame = new JFrame("N In row");
		boardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		boardFrame.setResizable(false);
		createTopPanel();
		createButtomPanel();
		initBoard();
		boardFrame.pack();
		boardFrame.setLocationRelativeTo(null);
		boardFrame.setVisible(true);

	}

	// Create the Top Panel - Buttons,Show turn,Timer, Game Settings
	public void createTopPanel() {
		this.topPanel = new JPanel(new BorderLayout());
		this.topPanel.setBackground(Color.WHITE);
		boardFrame.add(topPanel, BorderLayout.NORTH);

		JPanel gameInfoPanel = new JPanel(new GridLayout(1, 3));

		topPanel.add(gameInfoPanel, BorderLayout.NORTH);

		// Create Options panel - Menu / reset
		JPanel optionsJPanel = new JPanel();
		optionsJPanel.setBorder(new TitledBorder(null, "Options", TitledBorder.CENTER, TitledBorder.CENTER,
				new Font("Arial", Font.PLAIN, 18)));

		optionsJPanel.setBackground(Color.WHITE);

		// Menu button
		JButton menuButton = new JButton("Menu");
		menuButton.addActionListener(e -> {
			boardFrame.dispose();
			Game.main(null);

		});
		optionsJPanel.add(menuButton);

		// Rest button
		JButton resetButton = new JButton("Reset");
		resetButton.addActionListener(e -> {
			boardFrame.dispose();
			if (Game.gameType == Game.HUMAN_VS_HUMAN) {
				new Game(numOfRow, numOfColumn, Game.sequence, Game.gameType);
			} else {
				new Game(numOfRow, numOfColumn, Game.sequence, Game.gameType, Game.computer.getLevel(),
						Game.computer.getAlgorithm());
			}
		});
		optionsJPanel.add(resetButton);

		gameInfoPanel.add(optionsJPanel);

		// Create turn panel
		JPanel turnJPanel = new JPanel();
		turnJPanel.setBackground(Color.WHITE);
		turnJPanel.setBorder(new TitledBorder(null, "Turn:", TitledBorder.CENTER, TitledBorder.CENTER,
				new Font("Arial", Font.BOLD, 18)));

		Board.turnIcon = new JLabel();
		Board.turnIcon.setHorizontalTextPosition(JLabel.CENTER);
		Board.turnIcon.setVerticalTextPosition(JLabel.BOTTOM);
		Board.turnIcon.setFont(new Font("Arial", Font.BOLD, 14));

		turnJPanel.add(Board.turnIcon);
		gameInfoPanel.add(turnJPanel);

		// Create Timer panel
		JPanel timerJPanel = new JPanel();
		timerJPanel.setBackground(Color.WHITE);
		timerJPanel.setBorder(new TitledBorder(null, "Timer", TitledBorder.CENTER, TitledBorder.CENTER,
				new Font("Arial", Font.PLAIN, 18)));

		JLabel timerJLabel = new JLabel("0:00");
		timerJLabel.setFont(new Font("Arial", Font.BOLD, 30));
		timerJPanel.add(timerJLabel);
		Board.timer = new Timer(1000, new ActionListener() {
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
		Board.timer.start();
		gameInfoPanel.add(timerJPanel);

		// Game Settings panel
		JPanel gameSettingPanel = new JPanel(new GridLayout(1, 5));
		gameSettingPanel.add(new CustomJlabel("Row", Board.numOfRow, Color.BLACK, Color.WHITE, true,
				new Font("Arial", Font.PLAIN, 18)));
		gameSettingPanel.add(new CustomJlabel("Column", Board.numOfColumn, Color.BLACK, Color.WHITE, true,
				new Font("Arial", Font.PLAIN, 18)));
		gameSettingPanel.add(new CustomJlabel("Sequence", Game.sequence, Color.BLACK, Color.WHITE, true,
				new Font("Arial", Font.PLAIN, 18)));
		gameSettingPanel.add(new CustomJlabel("Mode", Game.gameType, Color.BLACK, Color.WHITE, true,
				new Font("Arial", Font.PLAIN, 18)));
		gameSettingPanel.add(new CustomJlabel("Level", ((Game.computer == null) ? 0 : Game.computer.getLevel()),
				Color.BLACK, Color.WHITE, true, new Font("Arial", Font.PLAIN, 18)));

		topPanel.add(gameSettingPanel, BorderLayout.CENTER);
	}

	public void createButtomPanel() {
		CustomJlabel numberOfColumn;
		JPanel butoomJPanel = new JPanel(new GridLayout(1, Board.numOfColumn));
		boardFrame.add(butoomJPanel, BorderLayout.SOUTH);
		for (int i = 0; i < Board.numOfColumn; i++) {
			numberOfColumn = new CustomJlabel(null, i + 1, Color.WHITE, new Color(63, 63, 63), false,
					new Font("Arial", Font.BOLD, 24));
			numberOfColumn.setBorder(new EmptyBorder(4, 0, 4, 0));
			butoomJPanel.add(numberOfColumn);
		}
	}

	// Initializes the board
	public void initBoard() {
		this.mainPanel = new JPanel();
		this.gridLayout = new GridLayout(Board.numOfRow, Board.numOfColumn);
		this.mainPanel.setLayout(gridLayout);
		boardFrame.add(mainPanel, BorderLayout.CENTER);

		Board.currentRowIndex = new int[Board.numOfColumn];
		Arrays.fill(Board.currentRowIndex, Board.numOfRow - 1); // Initialize the array with the number of rows
		Board.graphicsBoard = new Button[Board.numOfRow][Board.numOfColumn];
		Board.logicalBoard = new int[Board.numOfRow][Board.numOfColumn];

		// Fill the graphicsBoard with buttons and add action listener.
		for (int row = 0; row < Board.numOfRow; row++) {
			for (int column = 0; column < Board.numOfColumn; column++) {
				Board.graphicsBoard[row][column] = new Button(Game.imgFreeSpace);
				Board.graphicsBoard[row][column].setPreferredSize(new Dimension(90, 90));
				Board.graphicsBoard[row][column].addActionListener(new AL(column));
				this.mainPanel.add(Board.graphicsBoard[row][column]);
			}
		}
	}

	// Return a duplicate of matrix
	public static int[][] dupBoard(int[][] board) {
		int dup[][] = new int[numOfRow][numOfColumn];
		for (int row = 0; row < numOfRow; row++) {
			for (int col = 0; col < numOfColumn; col++) {
				dup[row][col] = board[row][col];
			}
		}
		return dup;
	}

	// Change the icon turn every turn.
	public static void changeTurnIcon(int turn) {
		if (turn == Game.PLAYER_ONE || turn == Game.COMPUTER) {
			Board.turnIcon.setIcon(new ImageIcon(Game.iconRed));
			Board.turnIcon.setText((Game.gameType == Game.HUMAN_VS_HUMAN ? "Player One" : "Computer"));
		} else if (turn == Game.PLAYER_TWO || turn == Game.HUMAN) {
			Board.turnIcon.setIcon(new ImageIcon(Game.iconBlue));
			Board.turnIcon.setText((Game.gameType == Game.HUMAN_VS_HUMAN ? "Player Two" : "Human"));
		}
	}

	// Change the image to show the sequence win
	public static void showWinnerSequence(Image tropyImage) {
		System.out.println(winnerSequenceIndex);
		int row, column;
		while (!Board.winnerSequenceIndex.isEmpty()) {
			row = winnerSequenceIndex.remove();
			column = winnerSequenceIndex.remove();
			graphicsBoard[row][column].setImage(tropyImage);
			graphicsBoard[row][column].repaint();
		}
	}

	// Check if the board is full. False exit, True - open dialog.
	public static boolean isBoardFull() {
		for (int i = 0; i < Board.numOfColumn; i++) {
			if (Board.logicalBoard[0][i] == Game.FREE_SPACE) {
				return false;
			}
		}
		return true;
	}

	// Update board(GUI & Logical)
	public static void updateBoard(int column, int player, int gameType, Image image) {
		logicalBoard[currentRowIndex[column]][column] = player;
		graphicsBoard[currentRowIndex[column]][column].setImage(image);
		graphicsBoard[currentRowIndex[column]][column].repaint();

		System.out.println();
		System.out.println(Arrays.deepToString(logicalBoard).replace("[", " ").replace("],", " \n").replace("]]", "")
				.replace(", ", " "));

		if (gameType == Game.HUMAN_VS_COMPUTER) {
			Game.turn = 7 - Game.turn;

		} else {
			Game.turn = 3 - Game.turn;
		}
		changeTurnIcon(Game.turn);
	}

	public static boolean checkRow(int[][] board, int row, int column, int player, String name) {
		int count = 0;
		int testRange = Game.sequence - 3;
		// Right.
		for (int funcColumn = column - testRange; funcColumn < Board.numOfColumn; funcColumn++) {
			if (funcColumn >= 0) {
				if (board[row][funcColumn] == player) {
					count++;
					Board.winnerSequenceIndex.add(row);
					Board.winnerSequenceIndex.add(funcColumn);
					if (count == Game.sequence) {
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
		Board.winnerSequenceIndex.clear();
		for (int funcColumn = column + testRange; funcColumn >= 0; funcColumn--) {
			if (funcColumn < Board.numOfColumn) {

				if (board[row][funcColumn] == player) {
					count++;
					Board.winnerSequenceIndex.add(row);
					Board.winnerSequenceIndex.add(funcColumn);
					if (count == Game.sequence) {
						System.out.println(name + " WIN(Left)");
						return true;
					}
				} else {
					count = 0;
				}
			}
		}
		Board.winnerSequenceIndex.clear();
		return false;
	}

	public static boolean checkColumn(int[][] board, int row, int column, int player, String name) {
		int count = 0;
		for (int funcRow = row; funcRow < Board.numOfRow; funcRow++) {
			if (board[funcRow][column] == player) {
				count++;
				Board.winnerSequenceIndex.add(funcRow);
				Board.winnerSequenceIndex.add(column);
				if (count == Game.sequence) {
					System.out.println(name + " WIN(Down)");
					return true;
				}
			} else {
				return false;
			}
		}
		Board.winnerSequenceIndex.clear();
		return false;
	}

	public static boolean checkDiagonal(int[][] board, int row, int column, int player, String name) {
		int count = 0;
		int testRange = Game.sequence - 3;
		// Up.
		for (int funcRow = row + testRange, funcColumn = column - testRange; funcRow >= 0
				&& funcColumn < Board.numOfColumn; funcRow--, funcColumn++) {
			if (funcRow < Board.numOfRow && funcColumn >= 0) {
				if (board[funcRow][funcColumn] == player) {
					count++;
					Board.winnerSequenceIndex.add(funcRow);
					Board.winnerSequenceIndex.add(funcColumn);

					if (count == Game.sequence) {
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
		Board.winnerSequenceIndex.clear();
		for (int funcRow = row - testRange, funcColumn = column + testRange; funcRow < Board.numOfRow
				&& funcColumn >= 0; funcRow++, funcColumn--) {

			if (funcRow >= 0 && funcColumn < Board.numOfColumn) {
				if (board[funcRow][funcColumn] == player) {
					count++;
					Board.winnerSequenceIndex.add(funcRow);
					Board.winnerSequenceIndex.add(funcColumn);
					if (count == Game.sequence) {
						System.out.println(name + " WIN(Slash down)");
						return true;
					}
				} else {
					count = 0;

				}
			}
		}
		Board.winnerSequenceIndex.clear();
		return false;
	}

	public static boolean checkBackDiagonal(int[][] board, int row, int column, int player, String name) {
		int count = 0;
		int testRange = Game.sequence - 3;

		// Up.
		for (int funcRow = row + testRange, funcColumn = column + testRange; funcRow >= 0
				&& funcColumn >= 0; funcRow--, funcColumn--) {

			if (funcRow < Board.numOfRow && funcColumn < Board.numOfColumn) {
				if (board[funcRow][funcColumn] == player) {
					count++;
					Board.winnerSequenceIndex.add(funcRow);
					Board.winnerSequenceIndex.add(funcColumn);
					if (count == Game.sequence) {
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
		Board.winnerSequenceIndex.clear();
		for (int funcRow = row - testRange, funcColumn = column - testRange; funcRow < Board.numOfRow
				&& funcColumn < Board.numOfColumn; funcRow++, funcColumn++) {
			if (funcRow >= 0 && funcColumn >= 0) {
				if (board[funcRow][funcColumn] == player) {
					count++;
					Board.winnerSequenceIndex.add(funcRow);
					Board.winnerSequenceIndex.add(funcColumn);
					if (count == Game.sequence) {
						System.out.println(name + " WIN(Back Slash down)");
						return true;
					}
				} else {
					count = 0;
				}
			}
		}
		Board.winnerSequenceIndex.clear();
		return false;
	}

	// Group all the functions to check who win.
	public static boolean checkWinner(int[][] board, int column, int player, String name) {
		if (checkRow(board, currentRowIndex[column], column, player, name)
				|| checkColumn(board, currentRowIndex[column], column, player, name)
				|| checkDiagonal(board, currentRowIndex[column], column, player, name)
				|| checkBackDiagonal(board, currentRowIndex[column], column, player, name)) {
			timer.stop();
			return true;
		}
		return false;
	}

	class AL implements ActionListener {
		protected int col;

		public AL(int col) {
			this.col = col;
		}

		public void actionPerformed(ActionEvent e) {

			if (Game.gameType == Game.HUMAN_VS_HUMAN) {

				if (Game.turn == Game.PLAYER_ONE && currentRowIndex[this.col] > -1) {
					Game.humanOne.playerMove(this.col);
				} else if (Game.turn == Game.PLAYER_TWO && currentRowIndex[this.col] > -1) {
					Game.humanTwo.playerMove(this.col);
				}
			} else if (Game.gameType == Game.HUMAN_VS_COMPUTER) {
				if (Game.turn == Game.HUMAN && currentRowIndex[this.col] > -1) {
					Game.humanOne.playerMove(this.col);

					new Thread(new Runnable() {
						public void run() {
							try {
								Thread.sleep(1000);
								Game.computer.computerMove();
							} catch (InterruptedException ex) {
							}
						}
					}).start();
				}
			}
		}
	}
}