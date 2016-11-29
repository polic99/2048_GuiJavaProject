import java.util.Arrays;

class Game {
	private int width;
	private long[][] board;
	private Direction dir;
	private enum Direction { UP, DOWN, LEFT, RIGHT, ERROR};
	
	Game(long[][] _board) {
		int exist;
		
		width = _board.length;
		board = new long[width][];
		for(int i=0; i<width; i++) {
			board[i] = new long[width];
			for(int j=0; j<width; j++) {
				board[i][j] = _board[i][j];
			}
		}
		
		exist = getNotExistNumber();
		setBoard(makeRandomNumber(exist, getNotExistPosition(exist)));
	}
	void setBoard(long[][] _board) {
		for(int i=0; i<width; i++) {
			for(int j=0; j<width; j++) {
				board[i][j] = _board[i][j];
			}
		}
	}
	// 방향을보기 쉬운 열거형의 값으로 변수에 대입합니다.
	void charToDirection(int keyChar) {
		if(keyChar==119) dir = Direction.UP;
		else if(keyChar==115) dir = Direction.DOWN;
		else if(keyChar==97) dir = Direction.LEFT;
		else if(keyChar==100) dir = Direction.RIGHT;
		else dir = Direction.ERROR;
	}
	// 비어있는 곳에 랜덤으로 값을 생성합니다.
	long[][] makeRandomNumber(int exist, int[][] emptyPos) {
		int length = emptyPos.length;
		int rand1=-1, rand2=-1;
		long[][] array = new long[width][width];
		
		for(int i=0; i<width; i++) {
			for(int j=0; j<width; j++) {
				array[i][j] = board[i][j];
			}
		}
		
		do {
			rand1 = (int)((Math.random()*length))+1;
			if(exist==1) {
				break;
			}
			rand2 = (int)((Math.random()*length));
		} while(rand1==rand2);
		
		if(exist == 1) {
			array[emptyPos[0][0]][emptyPos[0][1]] = 2;
			return array;
		}
		else {
			array[emptyPos[rand2][0]][emptyPos[rand2][1]] = 2;
			return array;
		}
		
	}
	
	// newBoard를 board에 대입합니다.
	void setGameBoard(long[][] newBoard) {
		for(int i=0; i<width; i++) {
			Arrays.fill(board[i], 0);
		}
		
		switch(dir) {
			case UP:
				for(int i=0; i<width; i++) {
					for(int j=0; j<width && newBoard[i][j]!=0; j++) {
						board[j][i] = newBoard[i][j];
					}
				}
				break;
			case DOWN:
				for(int i=0; i<width; i++) {
					for(int j=0; j<width && newBoard[i][j]!=0; j++) {
						board[width-j-1][i] = newBoard[i][j];
					}
				}
				break;
			case LEFT:
				for(int i=0; i<width; i++) {
					for(int j=0; j<width && newBoard[i][j]!=0; j++) {
						board[i][j] = newBoard[i][j];
					}
				}
				break;
			case RIGHT:
				for(int i=0; i<width; i++) {
					for(int j=0; j<width && newBoard[i][j]!=0; j++) {
						board[i][width-j-1] = newBoard[i][j];
					}
				}
		}
	}
	
	boolean checkEnding() {
		for(int y=0; y<width; y++) {
			for(int x=0; x<width; x++) {
				if(x+1 < width-1 && board[x+1][y] == board[x][y]) {
					return false;
				} 
				if(y+1 < width-1 && board[x][y+1] == board[x][y]) {
					return false;
				}
			}
		}
		return true;
	}
	String[][] boardToDoubleStringArray() {
		String[][] boardS = new String[width][width];
		for(int i=0; i<width; i++) {
			for(int j=0; j<width; j++) {
				if(board[i][j] == 0) 
					boardS[i][j] = "";
				else 
					boardS[i][j] = String.valueOf(board[i][j]);
			}
		}
		return boardS;
	}
	int getNotExistNumber() {
		int notExist=0;
		for(int i=0; i<width; i++) {
			for(int j=0; j<width; j++) {
				if(board[i][j] == 0) {
					notExist+=1;
				}
			}
		}
		return notExist;
	}
	int[][] getNotExistPosition(int _exist) {
		int index=0;
		int[][] pos = new int[_exist][2];
		
		for(int i=0; i<width; i++) {
			for(int j=0; j<width; j++) {
				if(board[i][j] == 0) {
					pos[index][0] = i;
					pos[index++][1] = j;
				}
			}
		}
		
		return pos;
	}
	long[][] getLineArray() {
		int x, y, i_start, i_end, j_start, j_end, iv, jv, index=0;
		long[][] lines = new long[width][width];
		
		// 인접원소를 초기화 할 배열 
		for(int i=0; i<width; i++) {
				Arrays.fill(lines[i], 0);
		}
		
		if(dir == Direction.UP || dir == Direction.LEFT) {
			i_start=0; i_end=width;
			j_start=0; j_end=width;
			iv=1; jv=1;
		} else if(dir == Direction.DOWN || dir == Direction.RIGHT) {
			i_start=0; i_end=width;
			j_start=width-1; j_end=-1;
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
	long[][] adjacentSameElementMerge(long[][] toMergeBoard) {
		int i,j,k,inc;
		for(i=0; i<width; i++) {
			for(j=0; j<width-1; j++) { 
				if(toMergeBoard[i][j] == 0) continue;
				for(k=j+1; k<width; k++) {
					if(toMergeBoard[i][k] == 0) continue;
					if(toMergeBoard[i][j] == toMergeBoard[i][k]){
						toMergeBoard[i][j] *= 2;
						toMergeBoard[i][k] = 0;
					} else {
						break;
					}
				}
				
			}
		}
		return toMergeBoard;
	}
	long[][] pullRealNumber(long[][] origin) {
		for(int i=0; i<width; i++) {
			for(int j=0; j<width-1; j++) {
				if(origin[i][j] == 0)
					for(int k=j+1; k<width; k++) {
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
}