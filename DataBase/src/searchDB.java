package jjson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class searchDB {
public static String getSeatno(String train ,String start,String end) throws IOException {
		
		BufferedReader rDB = new BufferedReader(new FileReader("C://NTU/table.csv"));
		 //�����wtrain
		 //System.out.println("here");
		 String line=rDB.readLine();
		 
		 boolean found=false;
		 while(found==false) {
			 
			 line=rDB.readLine();
			 String[] tt= line.split(",");
			 if (tt[0].equals(train)) {
				 found=true;
			 }
			 else {
			 }
		 }
		 //�ؤ@��array�����O�U�ӯ���string�A�w�]�Ŷ��Q�Ө���
		 String[] stationSeat =new String[10];
	
		 //�n�O���O����ӯ�
		 int st =0;
		 int s=0,e=0;
		 boolean matchS=false,matchE=false;
		 //���}�l���A�@��assign���쬰��
		 while(matchS==false) {
			 stationSeat[st]=rDB.readLine();
			 if (stationSeat[st].contains(start)) {
				 e++;
				 matchS=true;
			 }
			 else {
				 s++;
				 e++;
			 }
			 st++;
		 }	 
		 while(matchE==false) {
			 stationSeat[st]=rDB.readLine();
			 if (stationSeat[st].contains(end)) {
				 matchE=true;
			 }
			 else {
				 e++;
			 }
			 st++;
		 }	
		 //��Ĥ@�Ƴ]�w���y��
		 String[] num=stationSeat[0].split(",");
		 //�⦳����m��i�@�ӤG�����y��array
		 String[][] seats=new String[e-s+1][986];
		 for(int n=0;n<=e-s;n++) {
			 seats[n]=stationSeat[n+s].split(",");
		 }
		
		 //�����O�ۦP�y��A��O�ۦP��
		 //���H�@�Ӧ�l�����(�j��for)�A�����V�U�[����(����for)�A�A�i�J�U�@�����j��
		 String seatno="not found";
		 for(int i=1;i<=985;i++) {
			 int checkseat=0;
			 for(int n=0;n<=e-s;n++) {
				 if (seats[n][i].equals("0")) {
				 }
				 else {
					 checkseat++;
				 }
			 }
			 if (checkseat==0) {
				 seatno=num[i];						//�nreturn���F��
				 break;
			 }
		 }
		 rDB.close();
		 return	seatno;
	
	
	}
}
