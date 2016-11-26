import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Container;
import javax.swing.SwingConstants;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Main extends JFrame{
	Container cont;
	int labNum;
	int[][] board;
	JLabel[][] labels;
	KeyEventListener listener;
	enum Direction { UP, DOWN, LEFT, RIGHT, ERROR};
	
	Main(int _labNum) {
		int rand;
		Color color;
		
		cont = this.getContentPane();
		listener = new KeyEventListener();
		
		labNum = _labNum;
		labels = new JLabel[labNum][labNum];
		board = new int[labNum][labNum];
		for(int i=0; i<labNum; i++) {
			for(int j=0; j<labNum; j++) {
				rand = (int)((Math.random()*3)+2); 
				labels[i][j] = new JLabel(String.valueOf(rand));
				labels[i][j].setOpaque(true);
				labels[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				labels[i][j].setBackground( new Color((int)(Math.random() * 255.0),(int) (Math.random() * 255.0), (int) (Math.random() * 255.0)));
				cont.add(labels[i][j]);
			}
		}
		labelsToInts();
		
		
		this.setSize(1000, 1000);
		this.setVisible(true);
		this.setLayout(new GridLayout(labNum, labNum));
		this.addKeyListener(listener);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		int labelNumLocal = Integer.valueOf(args[0]);
		new Main(labelNumLocal);
	}

	void labelsToInts() {
		for(int i=0; i<labNum; i++) {
			for(int j=0; j<labNum; j++) {
				board[i][j] =  Integer.parseInt(labels[i][j].getText());
				System.out.print(labels[i][j].getText() + " ");
			}
			System.out.println("");
		}
	}
	
	public class KeyEventListener implements KeyListener{
		Direction charToDirection(int keyChar) {
			if(keyChar==119) return Direction.UP;
			else if(keyChar==115) return Direction.DOWN;
			else if(keyChar==97) return Direction.LEFT;
			else if(keyChar==100) return Direction.RIGHT;
			else return Direction.ERROR;
		}
		void setGameBoard(int[][] newBoard, Direction dir) {
			for(int i=0; i<labNum; i++) {
				Arrays.fill(board[i], 0);
			}
			
			switch(dir) {
				case UP:
					for(int i=0; i<labNum; i++) {
						for(int j=0; j<labNum && newBoard[i][j]!=0; j++) {
							board[j][i] = newBoard[i][j];
						}
					}
					break;
				case DOWN:
					for(int i=0; i<labNum; i++) {
						for(int j=0; j<labNum && newBoard[i][j]!=0; j++) {
							board[labNum-j-1][i] = newBoard[i][j];
						}
					}
					break;
				case LEFT:
					for(int i=0; i<labNum; i++) {
						for(int j=0; j<labNum && newBoard[i][j]!=0; j++) {
							board[i][j] = newBoard[i][j];
						}
					}
					break;
				case RIGHT:
					for(int i=0; i<labNum; i++) {
						for(int j=0; j<labNum && newBoard[i][j]!=0; j++) {
							board[i][labNum-j-1] = newBoard[i][j];
						}
					}
			}
		}
		void indicateBoardToLabel() {
			for(int i=0; i<labels.length; i++) {
				for(int j=0; j<labels[i].length; j++) {
					if(board[i][j] != 0) {
						labels[i][j].setText(String.valueOf(board[i][j]));
					} else {
						labels[i][j].setText("");
					}
					
				}
			}
		}
		int[][] getLineArray(Direction dir) {
			int x, y, i_start, i_end, j_start, j_end, iv, jv, index=0;
			int[][] lines = new int[labNum][labNum];
			
			// 인접원소를 초기화 할 배열 
			for(int i=0; i<labNum; i++) {
					Arrays.fill(lines[i], 0);
			}
			
			if(dir == Direction.UP || dir == Direction.LEFT) {
				i_start=0; i_end=labNum;
				j_start=0; j_end=labNum;
				iv=1; jv=1;
			} else if(dir == Direction.DOWN || dir == Direction.RIGHT) {
				i_start=0; i_end=labNum;
				j_start=labNum-1; j_end=-1;
				iv=1; jv=-1;
			} else {
				return null;
			}
		
			for(int i=i_start; i!=i_end; i+=iv) {
				index=0;
				for(int j=j_start; j!=j_end; j+=jv) {
					if (dir == Direction.UP || dir == Direction.DOWN) {
						x = j;
						y = i;
					} else {
						x = i;
						y = j;
					}
					if(board[x][y] >= 1) {
						lines[i][index++] = board[x][y];
					}
				}
			}
			return lines;
		}
		int[][] adjacentSameElementMerge(int[][] mergeBoard) {
			int i,j,k,inc;
			for(i=0; i<labNum; i++) {
				for(j=0; j<labNum-1; j++) { 
					if(mergeBoard[i][j] == 0) continue;
					for(k=j+1; k<labNum; k++) {
						if(mergeBoard[i][k] == 0) continue;
						if(mergeBoard[i][j] == mergeBoard[i][k]){
							mergeBoard[i][j] *= mergeBoard[i][k];
							mergeBoard[i][k] = 0;
						} else {
							break;
						}
					}
					
				}
			}
			return mergeBoard;
		}
		int[][] pullRealNumber(int[][] origin) {
			for(int i=0; i<labNum; i++) {
				for(int j=0; j<labNum-1; j++) {
					if(origin[i][j] == 0)
						for(int k=j+1; k<labNum; k++) {
							if(origin[i][k] != 0) {
								origin[i][j] = origin[i][k];
								origin[i][k] = 0;
								break;
							}
						}
				}
			}
			
			return origin;
		}
		public void keyTyped(KeyEvent e) {	
		}
		public void keyPressed(KeyEvent e) {
			int keyChar = e.getKeyChar();
			Direction keyCode = charToDirection(keyChar);
			int[][] lines;

			if(keyCode == Direction.ERROR) {
				JOptionPane.showMessageDialog(null, "WASD만을 눌러주세요.");
				return;
			}
			lines = getLineArray(keyCode);
			lines = adjacentSameElementMerge(lines);
			lines = pullRealNumber(lines);
			setGameBoard(lines, keyCode);
			indicateBoardToLabel();
		}
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
}
