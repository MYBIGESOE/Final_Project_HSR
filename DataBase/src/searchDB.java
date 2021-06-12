
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class searchDB {

	/**
	 * @param train(�C�����X)
	 * @param start
	 * @param end
	 * @param number(����)
	 * @return
	 * @throws IOException
	 */
	public static String getSeatno(String train, String start, String end,int number) throws IOException {
		String sss = "table";
		BufferedReader rDB = new BufferedReader(new FileReader("C://NTU/" + sss + ".csv"));
		// �����wtrain
		// System.out.println("here");
		String line = rDB.readLine();
//		int lineCount++;
		boolean found = false;
		while (found == false) {

			line = rDB.readLine();
			String[] tt = line.split(",");
			if (tt[0].equals(train)) {
				found = true;
			}

			else {
//				lineCount++;
			}
		}
		// �ؤ@��array�����O�U�ӯ���string�A�w�]�Ŷ��Q�Ө���
		String[] stationSeat = new String[10];

		// �n�O���O����ӯ�
		int st = 0;
		int s = 0, e = 0;
		boolean matchS = false, matchE = false;
		// ���}�l���A�@��assign���쬰��
		int sts=0,ste=0;
		try {
			
			while (matchS == false) {
				stationSeat[st] = rDB.readLine();
				if (stationSeat[st].contains(start)) {
					e++;
					sts=st;
					matchS = true;
				} else {
					s++;
					e++;
				}
				st++;
			}
			while (matchE == false) {
				stationSeat[st] = rDB.readLine();
				if (stationSeat[st].contains(end)) {
					ste=st;
					matchE = true;
				} else {
					e++;
				}
				st++;
			}
		} catch (Exception ee) {
			//System.out.println(ee.getMessage());
		}
		if ((sts >= ste)) {
			rDB.close();
			return "wrong direction"; // ��V���~
		}

		// ��Ĥ@�Ƴ]�w���y��
		String[] num = stationSeat[0].split(",");
		// �⦳����m��i�@�ӤG�����y��array
		String[][] seats = new String[e - s + 1][986];
		for (int n = 0; n <= e - s; n++) {
			seats[n] = stationSeat[n + s].split(",");
		}

		// �����O�ۦP�y��A��O�ۦP��
		// ���H�@�Ӧ�l�����(�j��for)�A�����V�U�[����(����for)�A�A�i�J�U�@�����j��
		int col=0;
		String seatno = "";
		String currentSeat="no seat available";
		for(int t=1;t<=number;t++) {
		for (int i = col; i <= 985; i++) {
			int checkseat = 0;

			for (int n = 0; n <= e - s; n++) {
				
				if (seats[n][i].equals("0")) {
				} else {
					checkseat++;
				}
			}
			if (checkseat == 0) {
				currentSeat=num[i];
				seatno = seatno+currentSeat+", "; // �nreturn���F��
				col=i+1;
				break;
			}

		}
		}
		rDB.close();
		return seatno;
	}

	/**
	 * @param train(�C�����X)
	 * @param start
	 * @param end
	 * @param number(����)
	 * @param kind(�u���window or aisle ���ڨS���g����)
	 * @return
	 * @throws IOException
	 */
	public static String getSeatnoSpecial(String train, String start, String end,int number,String kind) throws IOException {
		String sss = "table";
		BufferedReader rDB = new BufferedReader(new FileReader("C://NTU/" + sss + ".csv"));
// �����wtrain
		String line = rDB.readLine();
		boolean found = false;
		while (found == false) {
			line = rDB.readLine();
			String[] tt = line.split(",");
			if (tt[0].equals(train)) {
				found = true;
			}
			else {}
		}
//��_�l���B���I��
		// �ؤ@��array�����O�U�ӯ���string�A�w�]�Ŷ��Q�Ө���
		String[] stationSeat = new String[10];
		// �n�O���O����ӯ�
		int st = 0;
		int s = 0, e = 0;
		boolean matchS = false, matchE = false;
		// ���}�l���A�@��assign���쬰��
		int sts=0,ste=0;
		try {
			
			while (matchS == false) {
				stationSeat[st] = rDB.readLine();
				if (stationSeat[st].contains(start)) {
					e++;
					sts=st;
					matchS = true;
				} else {
					s++;
					e++;
				}
				st++;
			}
			while (matchE == false) {
				stationSeat[st] = rDB.readLine();
				if (stationSeat[st].contains(end)) {
					ste=st;
					matchE = true;
				} else {
					e++;
				}
				st++;
			}
		} catch (Exception ee) {
			//System.out.println(ee.getMessage());
		}
		if ((sts >= ste)) {
			rDB.close();
			return "wrong direction"; // ��V���~
		}
		// ��Ĥ@�Ƴ]�w���y��
		String[] num = stationSeat[0].split(",");
		// �⦳����m��i�@�ӤG�����y��array
		String[][] seats = new String[e - s + 1][986];
		for (int n = 0; n <= e - s; n++) {
			seats[n] = stationSeat[n + s].split(",");
		}

		// �����O�ۦP�y��A��O�ۦP��
		// ���H�@�Ӧ�l�����(�j��for)�A�����V�U�[����(����for)�A�A�i�J�U�@�����j��
		int col=0;
		String seatno = "";
		String currentSeat="no seats available";
		for(int t=1;t<=number;t+=0) {		
		for (int i = col; i <= 985; i++) {
			int checkseat = 0;
			for (int n = 0; n <= e - s; n++) {
				if (seats[n][i].equals("0")) {
				} else {
					checkseat++;
				}
			}
			if (checkseat == 0) {
				currentSeat=num[i];
				if((currentSeat.contains("C")||currentSeat.contains("D"))&&(kind.equals("aisle"))) {
					seatno =seatno+ currentSeat+", "; // �nreturn���F��
					col=i+1;
					t++;
					break;
				}
				else if((currentSeat.contains("A")||currentSeat.contains("E"))&&(kind.equals("window"))) {
					seatno =seatno+currentSeat+", "; // �nreturn���F��
					col=i+1;
					t++;
					break;
				}
				else {
					
				}
				
				
			}
		}
		}
		rDB.close();
		if(seatno.equals("")) {
			seatno ="no seats beside "+kind;
		}
		
		return seatno;
	}
}	