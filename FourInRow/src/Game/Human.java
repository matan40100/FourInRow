package Game;

import java.awt.Image;

public class Human {
    private int ID;
    private String name;
    private Image image;
    private Image winnerImage;

    //Constructor
    public Human(int iD, String name, Image image, Image winnerImage) {
        this.ID = iD;
        this.name = name;
        this.image = image;
        this.winnerImage = winnerImage;
    }

    public void playerMove(int column) {
        int currentTurn = Game.turn;
        Game.turn = 0;
        new Thread(new Runnable() {
            public void run() {
                Board.animate(column, image);
                Game.turn = currentTurn;
                Board.updateBoard(column, ID, Game.gameType);
                
                if (Board.checkWinner(Board.logicalBoard, column, ID, name)) {
                    Board.showWinnerSequence(winnerImage);
                    Game.winnerDialog(name);

                    return;
                }
                Board.currentRowIndex[column]--;
                if (Board.isBoardFull()) {
                    Game.endGameDialog();
                }

                if (Game.gameType == Game.HUMAN_VS_COMPUTER) {
                    Board.resetButton.setEnabled(false);
                    Board.undoButton.setEnabled(false);
                    Board.redoButton.setEnabled(false);
                    Board.saveGameButton.setEnabled(false);
                }

            }
        }).start();
    }
}
