
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

/**
 * @author yy121
 *
 */
/**
 * @author yy121
 *
 */
public class searchDB {

	/**
	 * @param date     ����Φ��p0628���Υ[.csv
	 * @param train    �u�ݭn�����s��(�p0861)
	 * @param start    �u�ݭn���I�s��(�p0990) �n��
	 * @param end      �P�W
	 * @param number   ���ʲ��ƶq
	 * @return �y�쪺�s��,�y�쪺�s��,�y�쪺�s��,.....
	 * @throws IOException
	 */
	public static String getSeatno(String date, String train, String start, String end, int number) throws IOException {

		BufferedReader rDB = new BufferedReader(new FileReader("Data/" + date + ".csv"));

		String line = " ";

		while (true) {
			line = rDB.readLine();

			if (line != null) {
				String[] tt = line.split(",");
				if (tt[0].contains(train)) break;
			}
		}

		String[] seatnos = null; // �y�츹�X
		ArrayList<List<String>> seats = new ArrayList<List<String>>(); // �Ҧ��y�챡�Ϊ��G��ArrayList/array

		boolean in = false;
		int    way = 0;

		for (int st = 0; st < 15; st++) {

			String tmp = rDB.readLine();

			if (tmp.contains("Seats")) {
				seatnos = tmp.split(",");
			}

			if (in) {
				seats.add(Arrays.asList(tmp.split(",")));
				way++;
				if (tmp.contains(end)) {
					break;
				}
			}

			if (tmp.contains(start)) {
				seats.add(Arrays.asList(tmp.split(",")));
				in = true;
				way++;
			}
		}

		// �����O�ۦP�y��A��O�ۦP��
		// ���H�@�Ӧ�l�����(�j��for)�A�����V�U�[����(����for)�A�A�i�J�U�@�����j��
		
		String seatno = "";
		
		int Tnumber = 0;
		
		for(int i = 1; i < seatnos.length - 1 ; i++) {
			T: for(int j = 0; j < way; j++) {
				if (Tnumber == number) {
					break T;
				}
				if (seats.get(j).get(i).equals("1") ) {
					break T;
				}
				else if(j == way - 1) {
					if(seatno.equals("")) {
						seatno = seatnos[i];
					}
					else {
						seatno = seatno + "," + seatnos[i];
					}
					Tnumber++;
				}
			}
		}
		
		rDB.close();
		
		return seatno;
	}

	/**
	 * @param date(����Φ��p0628���Υ[.csv )
	 * @param train(�C�����X)
	 * @param start
	 * @param end
	 * @param number(����)
	 * @param kind(�u���window        or aisle ���ڨS���g����)
	 * @return �y�쪺�s��,�y�쪺�s��,�y�쪺�s��,.....(�N��u���@�i���k��]�|���r��)
	 * @throws IOException
	 */
	public static String getSeatnoSpecial(String date, String train, String start, String end, String kind)
			throws IOException {
		
		BufferedReader rDB = new BufferedReader(new FileReader("Data/" + date + ".csv"));
// �����wtrain
		String line = " ";

		while (true) {
			line = rDB.readLine();

			if (line != null) {
				String[] tt = line.split(",");
				if (tt[0].contains(train)) break;
			}
		}
		
//��_�l���B���I��
		// �ؤ@��array�����O�U�ӯ���string�A�w�]�Ŷ��Q�Ө���
		String[] seatnos = null; // �y�츹�X
		ArrayList<List<String>> seats = new ArrayList<List<String>>(); // �Ҧ��y�챡�Ϊ��G��ArrayList/array

		boolean in = false;
		int    way = 0;

		for (int st = 0; st < 15; st++) {

			String tmp = rDB.readLine();

			if (tmp.contains("Seats")) {
				seatnos = tmp.split(",");
			}

			if (in) {
				seats.add(Arrays.asList(tmp.split(",")));
				way++;
				if (tmp.contains(end)) {
					break;
				}
			}

			if (tmp.contains(start)) {
				seats.add(Arrays.asList(tmp.split(",")));
				in = true;
				way++;
			}
		}

		// �����O�ۦP�y��A��O�ۦP��
		// ���H�@�Ӧ�l�����(�j��for)�A�����V�U�[����(����for)�A�A�i�J�U�@�����j��
		
		String seatno = "";
		
		String currentSeat;
		
		for(int i = 1; i < seatnos.length - 1 ; i++) {
			currentSeat = seatnos[i];
			
			T: for(int j = 0; j < way; j++) {
				if (((currentSeat.contains("C") || currentSeat.contains("D")) && kind.equals("aisle")) || 
						((currentSeat.contains("A") || currentSeat.contains("E")) && kind.equals("window"))) {
					break;
				}
		
				if (seats.get(j).get(i).equals("1") ) {
					break T;
				}
				else if(j == way - 1) {
					seatno = seatnos[i];
				}
			}
		}
		
		rDB.close();
		if (seatno.equals("")) {
			seatno = "no seats beside " + kind;
		}

		return seatno;
	}

	/**
	 * @param date
	 * @param train
	 * @param (String)discount
	 * @return �^��ArrayList �榡��:{} (�]�N�O�S��) �� {�馩, �i�R} �� {�馩, �i�R��, �馩, �i�R��}
	 * @throws IOException
	 */
	public static ArrayList<Object> checkEarly(String date, String train, int number) throws IOException {
		
		BufferedReader rDB = new BufferedReader(new FileReader("Data/" + date + ".csv"));
		
		String line = " ";

		while (true) {
			line = rDB.readLine();

			if (line != null) {
				String[] tt = line.split(",");
				if (tt[0].contains(train)) break;
			}
		}
		
		// �ӦC��������P�Ѿl����
		String[] dis = rDB.readLine().split(",");
		
		String[] tickets = rDB.readLine().split(",");
		
		// �^��ArrayList �@�ӧ�h�� �@�ӯள�X�i
		ArrayList<Object> A = new ArrayList<Object>();
		

		if(dis[0].equals("Seats")) {
			A.add("");
			rDB.close();
			return A;
		}
		

		int num = number;

		for (int t = 0; t < dis.length; t++) {
			int remain = Integer.parseInt(tickets[t]);

			if (remain == 0) {
				
			} else if (remain < number && remain > 0) {
				if (t == dis.length - 1) {
					A.add(dis[t]);
					A.add(remain);
					break;
				} else {
					A.add(dis[t]);
					A.add(remain);
					num = num - remain;
				}
			} else {
				A.add(dis[t]);
				A.add(num);
				break;
			}
			
			if (t == dis.length - 1) {
				break;
			}
		}

		rDB.close();

		return A;
	}

	/**
	 * @param date
	 * @param train
	 * @param start
	 * @param end
	 * @param seatNO
	 * @throws IOException
	 */
	public static void setSeatno(String date, String train, String start, String end, String seatNO)
			throws IOException {

		String ttt = "tabble";

		BufferedReader rDB = new BufferedReader(new FileReader("Data/" + date + ".csv")); // here
		BufferedWriter sDB = new BufferedWriter(new FileWriter("Data/" + ttt + ".csv")); // here

		// �����wtrain
		String line = rDB.readLine();
		sDB.write(line);
		boolean found = false;
		while (found == false) {
			line = rDB.readLine();
			sDB.newLine();
			sDB.write(line);
			String[] tt = line.split(",");
			if (tt[0].contains(train)) {
				found = true;
			} else {
			}
		}
		// ���_�l�B���I
		String[] rows = new String[15];
		// �n�O���O����ӯ�
		int st = 0;
		int s = 0, e = 0;
		boolean matchS = false, matchE = false;
		// ���}�l���A�@��write���쬰��
		try {

			while (matchS == false) {
				rows[st] = rDB.readLine();
				if (rows[st].contains(start)) {
					s = st;
					e++;
					matchS = true;
				} else {
					s++;
					e++;
					sDB.newLine();
					sDB.write(rows[st]);
				}
				st++;
			}
			while (matchE == false) {
				rows[st] = rDB.readLine();
				if (rows[st].contains(end)) {
					matchE = true;
				} else {
					e++;
				}
				st++;
			}
		} catch (Exception ee) {
		}

		// rows[s]�N�O�q�_�l��
		// rows[e]�N�O���I��

		// ����y�쪺��s��X
		int x = 0;
		String[] num = rows[2].split(","); // here
		for (int t = 1; t <= 985; t++) {
			if (num[t].contains(seatNO)) {
				x = t;
				break;
			}
		}
		// �����������ܦ�array�A������x�Ӥ�����1
		String[] seats = new String[985];

		for (int tt = s; tt <= e; tt++) {
			seats = rows[tt].split(",");
			seats[x] = "1";
			rows[tt] = String.join(",", seats);
			sDB.newLine();
			sDB.write(rows[tt]);
		}

		// �ѤU���A���swrite�@��
		try {
			int fake = 1;
			while (fake == 1) {
				sDB.newLine();
				sDB.write(rDB.readLine());

			}
		} catch (Exception ee) {
			ee.getMessage();
		} finally {
			rDB.close();
			sDB.close();
		}

		BufferedReader rDB2 = new BufferedReader(new FileReader("Data/" + ttt + ".csv")); // here
		BufferedWriter sDB2 = new BufferedWriter(new FileWriter("Data/" + date + ".csv")); // here
		try {
			int fake2 = 1;
			sDB2.write(rDB2.readLine());
			while (fake2 == 1) {
				sDB2.newLine();
				sDB2.write(rDB2.readLine());
			}
		} catch (Exception ee) {
			ee.getMessage();
		} finally {
			rDB2.close();
			sDB2.close();
		}
		File file_dulplicate = new File("Data/" + ttt + ".csv");
		file_dulplicate.delete();
	}

	/**
	 * @param date
	 * @param train
	 * @param start
	 * @param end
	 * @param seatNO
	 * @param discount
	 * @throws IOException
	 * 
	 *                     �����@�Ӥ����ɮק��ƿ�i�h�A�駹����A�ͤ@�ӻP�쥻�ɮפ@�˪��W�r�A�⤤�~�ɮ׿�J �A�⤤�~�ɮקR��
	 */
	public static void setSeatnoEarly(String date, String train, String start, String end, String seatNO,
			String discount) throws IOException {

		String ttt = "tabble";

		BufferedReader rDB = new BufferedReader(new FileReader("Data/" + date + ".csv")); // here
		BufferedWriter sDB = new BufferedWriter(new FileWriter("Data/" + ttt + ".csv")); // here

		// �����wtrain
		String line = rDB.readLine();
		sDB.write(line);
		boolean found = false;
		while (found == false) {
			line = rDB.readLine();
			sDB.newLine();
			sDB.write(line);
			String[] tt = line.split(",");
			if (tt[0].contains(train)) {
				found = true;
			} else {
			}
		}

		// ���æ����������
		String[] caldiscount = new String[2];
		caldiscount[0] = rDB.readLine();
		sDB.newLine();
		sDB.write(caldiscount[0]);
		caldiscount[1] = rDB.readLine();
		String[] dis = caldiscount[0].split(",");
		String[] tickets = caldiscount[1].split(",");

		for (int t = 1; t <= 10; t++) {
			if (dis[t].contains(discount)) {
				int tick = Integer.parseInt(tickets[t]) - 1;
				tickets[t] = Integer.toString(tick);
				caldiscount[1] = String.join(",", tickets);
				sDB.newLine();
				sDB.write(caldiscount[1]);
				break;
			}
		}

		// ��ƪ�lString
		String[] rows = new String[15];

		// ��X�_���ׯ�
		int st = 0;
		int s = 0, e = 0;
		boolean matchS = false, matchE = false;

		// ���}�l���A�@��write���쬰��
		try {

			while (matchS == false) {
				rows[st] = rDB.readLine();
				if (rows[st].contains(start)) {
					e++;
					matchS = true;
				} else {
					s++;
					e++;
					sDB.newLine();
					sDB.write(rows[st]);
				}
				st++;
			}
			while (matchE == false) {
				rows[st] = rDB.readLine();
				if (rows[st].contains(end)) {
					matchE = true;
				} else {
					e++;
				}
				st++;
			}
		} catch (Exception ee) {
		}
		// rows[s]�N�O�q�_�l��
		// rows[e]�N�O���I��

		// ����y�쪺��s��X
		int x = 0;
		String[] num = rows[0].split(","); // here
		for (int t = 1; t <= 985; t++) {
			if (num[t].contains(seatNO)) {
				x = t;
				break;
			}
		}
		// �����������ܦ�array�A������x�Ӥ�����1
		String[] seats = new String[985];

		for (int tt = s; tt <= e; tt++) {
			seats = rows[tt].split(",");
			seats[x] = "1";
			rows[tt] = String.join(",", seats);
			sDB.newLine();
			sDB.write(rows[tt]);
		}

		// �ѤU���A���swrite�@��
		try {
			int fake = 1;
			while (fake == 1) {
				sDB.newLine();
				sDB.write(rDB.readLine());

			}
		} catch (Exception ee) {
			ee.getMessage();
		} finally {
			rDB.close();
			sDB.close();
		}

		BufferedReader rDB2 = new BufferedReader(new FileReader("Data/" + ttt + ".csv")); // here
		BufferedWriter sDB2 = new BufferedWriter(new FileWriter("Data/" + date + ".csv")); // here
		try {
			int fake2 = 1;
			sDB2.write(rDB2.readLine());
			while (fake2 == 1) {
				sDB2.newLine();
				sDB2.write(rDB2.readLine());
			}
		} catch (Exception ee) {
			ee.getMessage();
		} finally {
			rDB2.close();
			sDB2.close();
		}
		File file_dulplicate = new File("Data/" + ttt + ".csv");
		file_dulplicate.delete();

	}

	// �ܽd
	public static void main(String[] args) throws IOException {

		String ss = getSeatno("0612", "0565", "1000 : Taipei", "1035 : Miaoli", 8);
		System.out.println(ss);// 0101A, 0101B, 0101C, 0102A, 0102B, 0102C, 0102D, 0102E,

		setSeatno("0612", "0565", "1000 : Taipei", "1035 : Miaoli", "0102B");

		setSeatnoEarly("0612", "0565", "1000 : Taipei", "1035 : Miaoli", "0102D", "0.9");
	}

}