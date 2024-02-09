package finalexampleUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BingoUser {

	public int bingoIndex;
	String[][] bingo;
	Map<String,Word> bingoElement = new HashMap<>();
	public BingoUser(int bingoIndex, Map<String,Word> a) {
		this.bingoIndex = bingoIndex;
		this.bingoElement = a;
		bingo = new String[bingoIndex][bingoIndex];
		for(int i=0; i<bingoIndex; i++)
			for(int j=0; j<bingoIndex; j++)
				bingo[i][j]= null;
	}

	public void bingoElementMake() {

		List<String> list = new ArrayList<>(bingoElement.keySet());
		Collections.shuffle(list);

		int k = 0;
		for (int i = 0; i < bingoIndex; i++) {
			k = i * bingoIndex;
			for (int j = 0; j < bingoIndex; j++) {
				bingo[i][j] = list.get(j + k);
			}
		}
	}

	public int bingoNum(String[][] bingo) {
		int i, j;
		int[] row = new int[bingoIndex];
		int[] col = new int[bingoIndex];
		for (int k = 0; k < bingoIndex; k++) {
			row[k] = 0;
			col[k] = 0;
		}
		int leftdiagonal = 0;
		int rightdiagonal = 0;
		int bingonum = 0;

		for (i = 0; i < bingoIndex; i++) {
			for (j = 0; j < bingoIndex; j++) {
				if (i == j) {
					if (bingo[i][j].equals("O")) {
						leftdiagonal++;
						if (leftdiagonal == bingoIndex)
							bingonum++;
					}
				}

				if (i + j == bingoIndex - 1) {
					if (bingo[i][j].equals("O")) {
						rightdiagonal++;
						if (rightdiagonal == bingoIndex)
							bingonum++;
					}
				}

				if (bingo[i][j].equals("O")) {
					row[i]++;
					if (row[i] == bingoIndex) {
						bingonum++;
					}
				}

				if (bingo[j][i].equals("O")) {
					col[i]++;
					if (col[i] == bingoIndex) {
						bingonum++;
					}
				}

			}

		}
		return bingonum;

	}

}
