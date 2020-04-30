package Game;

import java.awt.*;


public class Human {
    private int ID;
    private String name;
    private Image image;
    private Image winnerImage;

    public Human(int iD, String name, Image image, Image winnerImage) {
        this.ID = iD;
        this.name = name;
        this.image = image;
        this.winnerImage = winnerImage;
    }

    public void playerMove(int column) {

        Board.updateBoard(column, ID, Game.gameType, image);
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
            Board.undoButton.setEnabled(false);
        }

    }
}
