import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;

import javax.swing.SwingConstants;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Main extends JFrame{
	Game game;
	Container cont;
	JLabel[][] labels;
	KeyEventListener listener;
	
	Main(int labNum) {
		long[][] board;
		String[][] init;
		Font font;
		
		font = new Font("바탕", Font.ITALIC, 30);
		cont = this.getContentPane();
		listener = new KeyEventListener();
		
		board = new long[labNum][labNum];
		labels = new JLabel[labNum][labNum];
		
		for(int i=0; i<labNum; i++) {
			for(int j=0; j<labNum; j++) {
				labels[i][j] = new JLabel("0");
				labels[i][j].setFont(font);
				labels[i][j].setOpaque(true);
				labels[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				cont.add(labels[i][j]);
			}
		}
		
		board = DoublelabelArrayToDoubleLongArray(labels);
		game = new Game(board);
		init = game.boardToDoubleStringArray();
				
		this.setSize(1000, 1000);
		this.setLayout(new GridLayout(labNum, labNum));
		this.addKeyListener(listener);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	public long[][] DoublelabelArrayToDoubleLongArray(JLabel labels[][]) {
		long[][] array = new long[labels.length][];
		
		for(int i=0; i<labels.length; i++) {
			array[i] = new long[labels[i].length];
		}
		
		for(int i=0; i<labels.length; i++) {
			for(int j=0; j<labels[i].length; j++) {
				if(labels[i][j].getText() == "")
					array[i][j] = 0;
				else 
					array[i][j] = Long.parseLong(labels[i][j].getText());
			}
		}
		return array;
	}
	public void setLabelsText(String array[][]) {
		for(int i=0; i<array.length; i++) {
			for(int j=0; j<array[i].length; j++) { 
				labels[i][j].setText(array[i][j]);
			}
		}
	}
	public static void main(String[] args) {
		int labelNumLocal = 4;
		new Main(labelNumLocal);
	}

	public class KeyEventListener implements KeyListener{
		public void keyTyped(KeyEvent e) {	
		}
		public void keyPressed(KeyEvent e) {
			int exist;
			long[][] tmp, newRandomArray;
			String[][] labelStringValue;
			char direction = e.getKeyChar();
			
			exist = game.getNotExistNumber();
			if( exist == 0 && game.checkEnding() == true) {
				JOptionPane.showMessageDialog(null, "게임이 종료되었습니다.");
				return;
			}			
			game.charToDirection(direction);
			tmp = game.getLineArray();
			tmp = game.adjacentSameElementMerge(tmp);
			tmp = game.pullRealNumber(tmp);
			game.setGameBoard(tmp);
			
			exist = game.getNotExistNumber();
			newRandomArray = game.makeRandomNumber(exist, game.getNotExistPosition(exist));
			game.setBoard(newRandomArray);
			
			labelStringValue = game.boardToDoubleStringArray();
			setLabelsText(labelStringValue);
		}
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
}

