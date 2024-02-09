package finalexampleUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class BingoTestMain extends JFrame {// 예외처리를 잘 본다

	Container frame = this.getContentPane();
	JPanel panel1, panel2;
	JTextField tf = new JTextField(20);
	Random random = new Random();
	BingoManager bingo;
	JLabel[][] label;
	String str;

	public BingoTestMain(String title) {
		this.setTitle(title);
		this.setSize(500, 500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		BingoGame();
		this.setVisible(true);
	}

	private void play1(String[][] bingoelement) {
		// TODO Auto-generated method stub
		panel1 = new JPanel(new GridLayout(bingo.bingoIndex, bingo.bingoIndex)); // 영단어
		panel1.setBackground(Color.WHITE);
		for (int i = 0; i < bingo.bingoIndex; i++) {
			for (int j = 0; j < bingo.bingoIndex; j++) {
				label[i][j] = new JLabel(bingoelement[i][j], SwingConstants.CENTER);
				panel1.add(label[i][j]);
			}

		}

		frame.add(panel1, BorderLayout.CENTER);
	}

	private void play2(BingoUser computer, BingoUser user) {
		// TODO Auto-generated method stub
		panel2 = new JPanel();
		panel2.setBackground(Color.YELLOW);
		panel2.add(new JLabel("영단어 입력"));
		tf.addActionListener(e -> {
			str = tf.getText();
			if (str.length() > 0) {
				for (int i = 0; i < bingo.bingoIndex; i++) {
					for (int j = 0; j < bingo.bingoIndex; j++) {
						if (str.equals(label[i][j].getText())) {
							label[i][j].setText("O");
						}
					}
				}
				tf.setText("");
			}
			bingo.userStart(computer.bingo, user.bingo);
			if (bingo.bingoResult(computer.bingoNum(computer.bingo), user.bingoNum(user.bingo))) // 승패가 나뉘면 게임을 종료한다
				bingo.bingoGameReplay();
			bingo.comStart(computer.bingo, user.bingo);
			if (bingo.bingoResult(computer.bingoNum(computer.bingo), user.bingoNum(user.bingo))) // 위와 같다
				bingo.bingoGameReplay();

		});
		panel2.add(tf);
		frame.add(panel2, BorderLayout.SOUTH);
	}

	public void BingoGame() {

		bingo = new BingoManager(this);
		if (bingo.fileMake("quiz.txt")) { // 파일을 열고, 잘 열리면 true값을 반환한다
			bingo.bingoMake(); // n*n 빙고판을 구성한다
			label = new JLabel[bingo.bingoIndex][bingo.bingoIndex];
			BingoUser computer = new BingoUser(bingo.bingoIndex, bingo.bingoElement);
			BingoUser user = new BingoUser(bingo.bingoIndex, bingo.bingoElement);

			computer.bingoElementMake(); // computer와 user 빙고판에 영어단어를 중복없이 넣는다
			user.bingoElementMake();

			play1(user.bingo);
			int current_turn = random.nextInt(2); // 먼저 진행할 순서를 정하고, 빙고게임을 진행한다
			if (current_turn == 0) {
				JOptionPane.showMessageDialog(null, "사용자가 먼저 진행합니다.", "순서", JOptionPane.PLAIN_MESSAGE);
				play2(computer, user);

			} else { // 위와 같다
				JOptionPane.showMessageDialog(null, "컴퓨터가 먼저 진행합니다.", "순서", JOptionPane.PLAIN_MESSAGE);
				bingo.comStart(computer.bingo, user.bingo);
				play2(computer, user);
			}
		}

	}

	public static void main(String[] args) { // 영어단어
		// TODO Auto-generated method stub
		new BingoTestMain("202211394 편강");
	}
}
