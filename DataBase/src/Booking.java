import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.*;

public class Booking {
	JSONObject obj = JSONUtils.getJSONObjectFromFile("/timeTable.json");
	JSONArray jsonArray = obj.getJSONArray("Array");
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HHmm");
	
	private int Direction; //�h�{��V
	
	
	public String NormalBooking(String UID, String Ddate, String Rdate, // Ddate�X�o�ɶ�, Rdate��{�ɶ�
			String SStation, String DStation, //S�l��, D�ׯ�
			int normalT, int concessionT, int studentT, //�@�벼, �u�ݲ�(5��), �j�ǥͲ�
			int AorW, boolean BorS) // ���Dor�a��, �ӰȩμзǨ��[
	{
		//�ˬd�g��exception?
		
		//�ˬd���Ʀ��S���W�L		
		if ((normalT+concessionT+studentT > 10) || ((Rdate != null)&&(normalT+concessionT+studentT > 5))) {
			return "���ѡA�]�q��w�w�L�h����(�C���̦h10�i�A�Ӧ^�����W�߭p��)";
		}
		
		//�B�z��V
		this.trainDirection(SStation, DStation);
		
		//�B�z�ɶ�

			//���Ѯɶ�
			long current = System.currentTimeMillis();
			Date ttoday = new Date(current);
			Calendar today = Calendar.getInstance();
			today.setTime(ttoday);
		
			//�h�{
			Date Dedate  = null; //Date object
			String DoWD  = null; //day of week
			String Dtime = null; //time
			
			if(Ddate != null) {
				try {
					Dedate = sdf.parse(Ddate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				DoWD = getWeekofDay(Dedate);
				
				Dtime = Ddate.substring(11);
			}
		
			//�^�{
			Date Redate  = null; //Date object
			String DoWR  = null; //day of week
			String Rtime = null; //time
			
			if(Rdate != null) {
				try {
					Redate = sdf.parse(Rdate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				DoWR = getWeekofDay(Redate);
				
				Rtime = Rdate.substring(11);
			}
		
			/*
			 * ���ѹw�q���Υ���28��H���������C
			 * �q��}��ɶ���������(�t)�e28����0�I�}�l�A
			 * ��騮�����w�q�Ȩ��z�ܦC���X�o�ɶ��e1�p�ɬ���
			 */
			
			//������� (����᪺28��)
			Calendar Limitdate = today;
			Limitdate.add(Calendar.DAY_OF_MONTH, 28);
			
			//�T�{���ɶ��O�_�i�ѭq��
			if(Limitdate.before(Dedate)) {
				if(Limitdate.before(Redate)) {
					return "�h�^�{�C���ҩ|���}��q��";
				}
				else {
					return "�h�{�C���ҩ|���}��q��";
				}
			}
			else {
				//�L���D
			}

		//�N�ŦX���󪺦C���s������JSONArray
		JSONArray Davailable = new JSONArray();
		
		JSONArray Ravailable = new JSONArray();
		
		/*    �T�{���Ǭ��G
		 * 1. �T�{��V Direction
		 * 2. �T�{�P�� DayofWeek
		 * 3. �T�{�O�_�ŦX���u(���L��F�l���P�ׯ�) Stations
		 * 4. �T�{�l���X�o�ɶ� Date
		 */
		
		for(int i = 0; i < jsonArray.length(); i++) {
			
			JSONObject train = jsonArray.getJSONObject(i);
			JSONObject timetable = train.getJSONObject("GeneralTimetable");
			
			//�h�{
			if ((timetable.getJSONObject("GeneralTrainInfo").getInt("Direction") == Direction)
				//�T�{��V
				&& (timetable.getJSONObject("ServiceDay").getInt(DoWD) != 1) 
				//�T�{�P��
				&& (trainroutehas(train, SStation, DStation)) 
				//�T�{���u
				&& (dparturetime(Dtime, SStation, timetable.getJSONArray("StopTimes")))) 
				//�T�{�X�o�ɶ�
			{
				Davailable.put(train);
			}
			
			//�^�{
			if (Rdate != null) {
				//�T�{�O�_���^�{
				if ((timetable.getJSONObject("GeneralTrainInfo").getInt("Direction") != Direction)
					//�T�{��V
					&& (timetable.getJSONObject("ServiceDay").getInt(DoWR) != 1) 
					//�T�{�P��
					&& (trainroutehas(train, SStation, DStation)) 
					//�T�{���u
					&& (dparturetime(Rtime, DStation, timetable.getJSONArray("StopTimes")))) 
					//�T�{�X�o�ɶ�
				{
					Ravailable.put(train);
				}
			}
		}
		
		//�B�z���q���D
		
		if ((normalT+concessionT+studentT > 10) || ((Rdate != null)&&(normalT+concessionT+studentT > 5))) {
			return "���ѡA�]�q��w�w�L�h����(�C���̦h10�i�A�Ӧ^�����W�߭p��)";
		}
		
		this.normalticketenough(Davailable, Ddate, normalT);
		this.normalticketenough(Ravailable, Rdate, normalT);


		//�ڻݭndatabase�������k
		
		
		//�������B�z
		ArrayList<Double> DEDdiscount = new ArrayList<Double>();
		ArrayList<Double> REDdiscount = new ArrayList<Double>();
		
		//�ǥͲ��B�z
		ArrayList<Double> DUDdiscount = new ArrayList<Double>();
		ArrayList<Double> RUDdiscount = new ArrayList<Double>();
		
		//�u�ݲ��B�z
		
		//�Ӱȫh�S���U���u�ݲ�
		if (BorS == false) {
			
		//������ (���馩�򲼼�) ***************(���ƭn�B�z)******************
		/*
		 * �����u�f�ȾA�Ω�зǨ��[�︹�y�����C
		 * �����u�f�����A�ۭ�����]�t�^�e 28 ��_�}�l���q�o��A�̱ߵo��ܭ�����]�t�^�e 5 ��I��C
		 * �U���q��̡A�U�����|�q�o 65 ���u�f�����C
		 * ���� 65 ��P�⧹���Y��o�⦭�� 8 ��A���� 8 ��P�⧹���Y��o�⦭�� 9 ��A���� 9 ��P�⧹���Y���e�I��ç�o���������C
		 */
			
			JSONObject earlyDiscount = JSONUtils.getJSONObjectFromFile("/earlyDiscount.json");
			JSONArray EDTrains = earlyDiscount.getJSONArray("DiscountTrains");
			
			//��Llimitdate�אּ����᤭��(28-23)
			Limitdate.add(Calendar.DAY_OF_MONTH, -23);
			
			//�h�{
			
			
			//�T�{�O�_�󤭤�e
			if (Limitdate.after(Dedate)) {
				System.out.println("�h�{����C���w�����Ѧ������q��");
			}
			else {
				//�~�鬰�h�{��JSONArray
				for(int j = 0; j < Davailable.length(); j++) {
					//����1���Ҧ�ED��JSONArray
					ED: for(int i = 0; i < EDTrains.length(); i++) {
						//�Y���������C��
						if (TrainNoof(EDTrains, i) == TrainNoofAv(Davailable, j)) {
							//�N�ӦC����ED��T���X
							JSONArray thatdayED = EDTrains.getJSONObject(i).getJSONObject("ServiceDayDiscount").getJSONArray(DoWD);
							
							//���X�ӬP���������ƨìd�߬O�_�٦��Ѳ��A�ñN����s�iREDdiscount���A�ø��XED�j��A�Y���S���h�s�J1.0(�L���)�A�P�˸��XED�j��
							for (int k = 0; k < thatdayED.length(); k++) {
								if (thatdayED.getJSONObject(k).getInt("tickets") > 0) {
									DEDdiscount.add(thatdayED.getJSONObject(k).getDouble("0.65"));
									break ED;
								}
								else if (k == thatdayED.length()) {
									DEDdiscount.add(1.0);
									break ED;
								}
								else;
							}
						}
						else if (i == EDTrains.length()) {
							DEDdiscount.add(1.0);
							break ED;
						}
						else;
					}
				}
				//�j�M�Z��������P�H�άO�_�٦���l
			}
			
			
			
			//�^�{ ���T�{�O�_���^�{
			if (Rdate != null) {
				//�T�{�O�_�󤭤�e
				if (Limitdate.after(Redate)) {
					System.out.println("�^�{����C���w�����Ѧ������q��");
				}
				else {
					//�~�鬰�^�{��JSONArray
					for(int j = 0; j < Ravailable.length(); j++) {
						//����1���Ҧ�ED��JSONArray
						ED: for(int i = 0; i < EDTrains.length(); i++) {
							//�Y���������C��
							if (TrainNoof(EDTrains, i) == TrainNoofAv(Ravailable, j)) {
								//�N�ӦC����ED��T���X
								JSONArray thatdayED = EDTrains.getJSONObject(i).getJSONObject("ServiceDayDiscount").getJSONArray(DoWR);
								
								//���X�ӬP���������ƨìd�߬O�_�٦��Ѳ��A�ñN����s�iREDdiscount���A�ø��XED�j��A�Y���S���h�s�J1.0(�L���)�A�P�˸��XED�j��
								for (int k = 0; k < thatdayED.length(); k++) {
									if (thatdayED.getJSONObject(k).getInt("tickets") > 0) {
										REDdiscount.add(thatdayED.getJSONObject(k).getDouble("0.65"));
										break ED;
									}
									else if (k == thatdayED.length()) {
										REDdiscount.add(1.0);
										break ED;
									}
									else;
								}
							}
							else if (i == EDTrains.length()) {
								REDdiscount.add(1.0);
								break ED;
							}
							else;
						}
					}
					//�j�M�Z��������P�H�άO�_�٦���l
				}
			}
			else;
		
		//�j�ǥͲ� (�u���馩)
		/*
		 * �j�ǥ��u�f�]5��/75��/88��^�����L�k�P��L�u�f�X�֨ϥΡC
		 */
			if (studentT > 0) {
				JSONObject universityDiscount = JSONUtils.getJSONObjectFromFile("/universityDiscount.json");
				JSONArray UDTrains = universityDiscount.getJSONArray("DiscountTrains");
				//studentT
				
				//�~�鬰�h�{��JSONArray
				for(int j = 0; j < Davailable.length(); j++) {
					//����1���Ҧ�ED��JSONArray
					for(int i = 0; i < UDTrains.length(); i++) {
						//�Y���������C��
						if (TrainNoof(UDTrains, i) == TrainNoofAv(Davailable, j)) {
							//�N�ӦC������ӬP�����馩��JDUDdiscount��
							DUDdiscount.add(UDTrains.getJSONObject(i).getJSONObject("ServiceDayDiscount").getDouble(DoWD));
						}
						else if (i == UDTrains.length()) {
							//�Y���䤣��h�������add(1.0)
							DUDdiscount.add(1.0);
						}
						else;
					}
				}
				
				//�~�鬰�h�{��JSONArray
				for(int j = 0; j < Ravailable.length(); j++) {
					//����1���Ҧ�ED��JSONArray
					for(int i = 0; i < UDTrains.length(); i++) {
						//�Y���������C��
						if (TrainNoof(UDTrains, i) == TrainNoofAv(Ravailable, j)) {
							//�N�ӦC������ӬP�����馩��JDUDdiscount��
							RUDdiscount.add(UDTrains.getJSONObject(i).getJSONObject("ServiceDayDiscount").getDouble(DoWR));
						}
						else if (i == UDTrains.length()) {
							//�Y���䤣��h�������add(1.0)
							RUDdiscount.add(1.0);
						}
						else;
					}
				}
				//�j�M�Z��������P�H�άO�_�٦���l
			}
			
			else;
		
			
			
			
			
		//�u�ݲ� (�U������)
		
		//�㨮���`��(���@��P�Ӱ�)
			
			//��X�j�M���G
			
			System.out.println("�h�{�C���p�U�G\n");
			
			for (int i = 0; i < Davailable.length(); i++) {
				System.out.println(Davailable.get(i));
			}
			
			System.out.println("�^�{�C���p�U�G\n");
			
			if (Rdate != null) {
				for (int j = 0; j < Ravailable.length(); j++) {
					System.out.println(Ravailable.get(j));
				}
			}
			
			return "�q���j�M���G��ܧ���";
		}
		
		// END -----> ��X�ӰȨ��[����������B�óB�z����(����excel�y���ɮ�)
		else {
			//��X�j�M���G
			
			System.out.println("�h�{�C���p�U�G\n");
			
			for (int i = 0; i < Davailable.length(); i++) {
				System.out.println(Davailable.get(i));
			}
			
			System.out.println("�^�{�C���p�U�G\n");
			
			if (Rdate != null) {
				for (int j = 0; j < Ravailable.length(); j++) {
					System.out.println(Ravailable.get(j));
				}
			}
			else;
			
			return "�q���j�M���G��ܧ���";
		}
		
		/*
		 * �y��ߦn�\��Y�̦C���q�췧�p�C�X�z���檺�y��A�p�Ө����w�L�X�A�y��ɡA�����z���i���Q�����q���@�~�A�N�Ѩt�Φ۰ʰt�m�y��C�y��ߦn�\��ȾA�Ω��H���ȡC
		 */
	}
	
	
	private boolean normalticketenough(JSONArray davailable, String ddate, int normalT) {
		
		//�ڻݭndatabase���ާ@�覡
		return true;
	}

	/**
	 * ��method�ΨӽT�{�C��(�h�{ �p�G���Ӧ^����)��|��V
	 * @param sStation
	 * @param dStation
	 */
	
	private void trainDirection(String sStation, String dStation) {
		if (Integer.valueOf(sStation) < Integer.valueOf(dStation)) {
			Direction = 0; //�n�V
		}
		else{
			Direction = 1; //�_�V
		}
	}


	/**
	 * ��method��K��davailable ArrayList����TrainNo
	 * 
	 * @param Ravailable
	 * @param which �ĴX��
	 * @return �Ӧ�m��TrainNo
	 */
	
	public static String TrainNoofAv(JSONArray Ravailable, int which) {
		return Ravailable.getJSONObject(which).getJSONObject("GeneralTimetable").getJSONObject("GeneralTimeInfo").getString("TrainNo");
	}
	
	/**
	 * ��method��K��dDiscount table����TrainNo
	 * 
	 * @param DTrains discount trains
	 * @param which �ĴX��
	 * @return �Ӧ�m��TrainNo
	 */
	
	public static String TrainNoof(JSONArray EDTrains, int which) {
		return EDTrains.getJSONObject(which).getString("TrainNo");
	}
	
	/**
	 * @param time ��J�ɶ�
	 * @param SStation �_��
	 * @param StopTimes �ӦC��������
	 * @return �Y�ӦC���ӯ����X���ɶ� �b ��J�ɶ� �� �h�^��true �Ϥ��^��false
	 */
	
	private boolean dparturetime(String time, String SStation, JSONArray StopTimes) {
		for (int i=0 ; i < StopTimes.length(); i++) {
			if (StopTimes.getJSONObject(i).getString("StationID") == SStation){
				String DepartureTime = StopTimes.getJSONObject(i).getString("DepartureTime").replace(":", "");
				if (Integer.valueOf(DepartureTime) >= Integer.valueOf(time)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param train �ӦC����JSONobject
	 * @param SStation �l��
	 * @param DStation �ׯ�
	 * @return true �Y���u���T false �Ϥ�
	 */
	
	private boolean trainroutehas(JSONObject train, String SStation, String DStation) {
		boolean S = false;
		boolean D = false;
		
		for (int j = 0; j < train.getJSONArray("StopTimes").length(); j++) {
			String station = train.getJSONArray("StopTimes").getJSONObject(j).getString("StationID");
			if (station	== SStation) {
				S = true;
			}
			if (station	== DStation) {
				D = true;
			}
		}

		if (S && D) {
			return true;
		}
		else return false;
	}
	
	/**
	 * @param date
	 * @return �Ӥ�����P��
	 */
	
	private String getWeekofDay(Date date) {
		
		String[] weekDays = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        
		return weekDays[w];
	}
}
