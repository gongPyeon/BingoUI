package finalexampleUI;

import java.awt.HeadlessException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class BingoManager extends JFrame {

	public int bingoIndex;
	public int totalGame = 0;
	public int winCom = 0;
	public int winUser = 0;
	public boolean first = true;
	public Map<String, Word> bingoElement = new HashMap<>();
	public Scanner scan = new Scanner(System.in);
	public Random random = new Random();
	BingoTestMain parent;

	public BingoManager(BingoTestMain frame) throws HeadlessException {
		this.parent = frame;
	}

	public boolean fileMake(String file) {
		try (Scanner filename = new Scanner(new File(file));) {
			while (filename.hasNext()) {
				String line = filename.nextLine();
				String[] temp = line.split("\t");
				bingoElement.put(temp[0].trim(), new Word(temp[0].trim(), temp[1].trim()));
			}
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "파일이 생성되지 않았습니다.", "file", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		return true;
	}

	public void bingoMake() {
		while (true) {
			try {
				String a = JOptionPane.showInputDialog("N*N 빙고판을 생성하기 위해 N을 입력해주세요(범위 3~7) :");
				if (a != null) {
					bingoIndex = Integer.parseInt(a);
					totalGame++;
					if (bingoIndex >= 3 && bingoIndex <= 7)
						break;
					else
						JOptionPane.showMessageDialog(null, "범위 밖 숫자를 입력하셨습니다.", "error", JOptionPane.ERROR_MESSAGE);
				} else
					System.exit(0);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "숫자가 아닌 다른 문자를 입력하셨습니다.", "error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public boolean bingoResult(int computerbingoNum, int userbingoNum) {

		boolean gameover = false;
		if (computerbingoNum >= bingoIndex | userbingoNum >= bingoIndex) { // 컴퓨터가 체크했는데 컴퓨터와 사용자 동시에 3빙고가 되면?
			if (computerbingoNum == userbingoNum) {
				JOptionPane.showMessageDialog(null, "무승부입니다.", "result", JOptionPane.PLAIN_MESSAGE);
			} else if (computerbingoNum > userbingoNum) {
				JOptionPane.showMessageDialog(null, "computer가 이겼습니다.", "result", JOptionPane.PLAIN_MESSAGE);
				winCom++;
			} else {
				JOptionPane.showMessageDialog(null, "user가 이겼습니다.", "result", JOptionPane.PLAIN_MESSAGE);
				winUser++;
			}
			gameover = true;
		}

		return gameover;
	}

	public void userStart(String[][] computer, String[][] user) {

		String eng = parent.str;

		for (int i = 0; i < bingoIndex; i++) {
			for (int j = 0; j < bingoIndex; j++) {
				if (user[i][j].equals(eng)) {
					System.out.println(eng + "(" + bingoElement.get(eng).kor + ")를 선택하였습니다."); 
					user[i][j] = "O";
				}
				if (computer[i][j].equals(eng)) {
					computer[i][j] = "O";
				}
			}
		}
		
		//show(computer);
		//show(user);

	}

	public void comStart(String[][] computer, String[][] user) {

		String engCom = winComAlgorithm(computer);
		first = false; // 음
		for (int i = 0; i < bingoIndex; i++) {
			for (int j = 0; j < bingoIndex; j++) {
				if (user[i][j].equals(engCom)) {
					user[i][j] = "O";
					parent.label[i][j].setText("O");
				}
				if (computer[i][j].equals(engCom)) {
					System.out.println("\n" + engCom + "(" + bingoElement.get(engCom).kor + ")를 선택하였습니다."); 
					computer[i][j] = "O";
				}
			}
		}
		//show(computer);
		//show(user);
	}

	public String winComAlgorithm(String[][] bingo) {
		int i, j;
		int[] row = new int[bingoIndex];
		int[] col = new int[bingoIndex];
		int leftdiagonal = 0;
		int rightdiagonal = 0;
		String engCom = null;

		if (!first) {
			for (i = 0; i < bingoIndex; i++) {
				for (j = 0; j < bingoIndex; j++) {
					if (i == j) {
						if (bingo[i][j].equals("O")) {
							leftdiagonal++;
						}
					}

					if (i + j == bingoIndex - 1) {
						if (bingo[i][j].equals("O")) {
							rightdiagonal++;
						}
					}

					if (bingo[i][j].equals("O")) {
						row[i]++;
					}

					if (bingo[j][i].equals("O")) {
						col[i]++;
					}

				}

			}

			int maxrow = row[0];
			int maxIndexrow = 0;
			int maxcol = col[0];
			int maxIndexcol = 0;

			for (int a = 0; a < bingoIndex; a++) {
				if (row[a] > maxrow && row[a] != bingoIndex) {
					maxrow = row[a];
					maxIndexrow = a;
				}

				if (col[a] > maxcol && col[a] != bingoIndex) {
					maxcol = col[a];
					maxIndexcol = a;
				}

			}

			int[] array = { maxrow, maxcol, leftdiagonal, rightdiagonal };
			int max = array[0];
			for (int a = 0; a < array.length; a++) {
				if (array[a] > max && array[a] != bingoIndex)
					max = array[a];
			}

			if (max == maxrow) {
				while (true) {
					engCom = bingo[maxIndexrow][random.nextInt(bingoIndex)];
					if (!engCom.equals("O"))
						break;
				}
			} else if (max == maxcol) {
				while (true) {
					engCom = bingo[random.nextInt(bingoIndex)][maxIndexcol];
					if (!engCom.equals("O"))
						break;
				}

			} else if (max == leftdiagonal) {
				while (true) {
					int a = 0;
					int b = 0;
					a = random.nextInt(bingoIndex);
					b = random.nextInt(bingoIndex);
					if (a == b) {
						engCom = bingo[a][b];
						if (!engCom.equals("O"))
							break;
					}
				}
			} else if (max == rightdiagonal) {
				while (true) {
					int a = 0;
					int b = 0;
					a = random.nextInt(bingoIndex);
					b = random.nextInt(bingoIndex);
					if (a + b == bingoIndex - 1) {
						engCom = bingo[a][b];
						if (!engCom.equals("O"))
							break;
					}
				}
			}

		} else {
			while (true) {
				engCom = bingo[random.nextInt(bingoIndex)][random.nextInt(bingoIndex)];
				if (!engCom.equals("O"))
					break;
			}
		}

		return engCom;
	}

	public void bingoGameReplay() { // 여기서 그냥 게임을 진행해야겠다
		// TODO Auto-generated method stub
		while (true) {
			int answer = JOptionPane.showConfirmDialog(null, "빙고게임을 진행하시겠습니까?", "confirm", JOptionPane.YES_NO_OPTION);
			if (answer == JOptionPane.YES_OPTION) {
				parent.frame.remove(parent.panel1);
				parent.frame.remove(parent.panel2);
				parent.frame.revalidate();
				parent.frame.repaint();
				parent.dispose();
				parent.main(null);
				break;
			} else if (answer == JOptionPane.NO_OPTION) { // 또는 X를 눌렀을때
				fileScore("score.txt");
				System.exit(0);
				break;
			} else
				continue;
		}
	}

	public void show(String[][] bingoBoard) {
		for (int i = 0; i < bingoIndex; i++) {
			for (int j = 0; j < bingoIndex; j++) {
				System.out.print(bingoBoard[i][j] + "\t"); 
			}
			System.out.println();
			for (int j = 0; j < bingoIndex; j++) {
				System.out.print("-------------");
			}
			System.out.println();
		}
		System.out.println();
	}

	public void fileScore(String file) {
		int winComGame = 0;
		int winUserGame = 0;
		int gameNum = 0;
		try (Scanner filename = new Scanner(new File(file));) {
			String line = filename.nextLine();
			String[] temp = line.split("\t");
			winComGame = Integer.parseInt(temp[0].trim()) + winCom;
			winUserGame = Integer.parseInt(temp[1].trim()) + winUser;
			gameNum = Integer.parseInt(temp[2].trim()) + totalGame;

		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "파일이 생성되지 않았습니다.", "file", JOptionPane.ERROR_MESSAGE); // 좀 고민을 해볼까? 만약에
																										// 파일이 없으면 어떻게
																										// 되는데
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		File f = new File(file);
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(f));) { // 파일 내용을 지우고 넣어야하는데
			writer.append(Integer.toString(winComGame));
			writer.append("\t");
			writer.append(Integer.toString(winUserGame));
			writer.append("\t");
			writer.append(Integer.toString(gameNum));
		} catch (IOException e) {
			e.printStackTrace();
		}

		JOptionPane.showMessageDialog(null, "컴퓨터의 누적승률 : " + (float) winComGame / gameNum * 100 + "% 사용자의 누적 승률 : "
				+ (float) winUserGame / gameNum * 100 + "%", "result", JOptionPane.PLAIN_MESSAGE);
	}

}
