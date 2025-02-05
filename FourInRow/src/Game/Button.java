package Game;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JButton;

public class Button extends JButton {
	private Image image;

	public Button(Image image) {
		this.image = image;
	}
	
	public void setImage(Image image) {
		this.image = image;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}
}
