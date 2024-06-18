import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class GenSQL {

	public static void main(String[] args) {
		
		ArrayList<String> instructionArrayList = new ArrayList<>();
		
		// Customer
		int cNum = 10;
		String[] firstNames = new String[] {"James", "John", "Jackson", "Bob", "Mary", "Julia", "Emily", "Mike", "Peter", "Sarah"}; 
		String[] lastNames = new String[] {"Smith", "White", "Brown", "Green", "Doe", "Zara", "Chen", "Nawata", "Rozhok", "Wang" }; 
		String[] billingAddrs = new String[cNum];
		billingAddrs[0] = "370 Jay St Unit 5";
		billingAddrs[1] = "100 Berry Ct Unit 3";
		billingAddrs[2] = "54 Willoughby St Unit 2";
		billingAddrs[3] = "43 Main St Unit 13";
		billingAddrs[4] = "55 Broadway Unit 6";
		billingAddrs[5] = "76 Hall St Unit 8";
		billingAddrs[6] = "2 Metrotech Ct Unit 1";
		billingAddrs[7] = "20 Wall St Unit 4";
		billingAddrs[8] = "34 Canal St Unit 7";
		billingAddrs[9] = "27 Atlantic Ct Unit 3";
		String[] billingZcodes = new String[] {"11201", "90001","10013","12215","32357","09805","30013","51007","91200","43347"}; 
		
		for (int i = 0; i < cNum; i++) {
			instructionArrayList.add("insert into Customer (first_name, last_name, billing_addr, billing_zcode) values ('" 
		                       + firstNames[i] + "', '" + lastNames[i] + "', '" + billingAddrs[i] + "', '"
		                       + billingZcodes[i] + "');");

		}
		
		//System.out.println();
		//System.out.println("==========================================================================================================");
		//System.out.println();
		instructionArrayList.add("");
		instructionArrayList.add("======================================================================================");
		instructionArrayList.add("");
		
		// Location 
		int lNum = 20;
		int[] cids = new int[] {1,2,1,1,3,7,8,3,3,5,2,2,6,8,7,4,5,1,2,9};
		String[] zcodes = new String[] {"11201","90001","32357","51007","90001","43347","11201","32357","11201","51007",
				                        "90001","32357","11201","11201","51007","43347","32357","51007","09805","10013"}; 
		String[] address = new String[lNum];
		address[0] = "370 Jay St Unit 5";
		address[1] = "100 Berry Ct Unit 3";
		address[2] = "254 Parkway Unit 4";
		address[3] = "33 Degraw St Unit 10";
		address[4] = "2 Cheever Pl Unit 1";
		address[5] = "31 Kane St Unit 5";
		address[6] = "9 Warren St Unit 3";
		address[7] = "80 Henry St Unit 6";
		address[8] = "21 Livingston St Unit 2";
		address[9] = "28 Pacific St Unit 12";
		address[10] = "5 Jefferson Ave Unit 7";
		address[11] = "2 Dekalb Ave Unit 3";
		address[12] = "56 Vernon Ave Unit 8";
		address[13] = "101 George St Unit 3";
		address[14] = "55 Grattan St Unit 7";
		address[15] = "6 Seneca Ave Unit 4";
		address[16] = "21 Conduit Blvd Unit 7";
		address[17] = "33 Drew St Unit 1";
		address[18] = "27 Grant Ave Unit 9";
		address[19] = "76 McKinley Ave Unit 13";
		
		String[] startDates = new String[lNum];
		startDates[0] = "2019-08-07";
		startDates[1] = "2019-08-26";
		startDates[2] = "2019-09-05";
		startDates[3] = "2019-10-01";
		startDates[4] = "2019-11-25";
		startDates[5] = "2019-11-28";
		startDates[6] = "2019-12-01";	
		startDates[7] = "2019-12-13";
		startDates[8] = "2020-01-06";
		startDates[9] = "2020-06-05";
		startDates[10] = "2020-08-06";
		startDates[11] = "2020-09-13";
		startDates[12] = "2020-10-16";
		startDates[13] = "2020-11-10";
		startDates[14] = "2020-12-05";
		startDates[15] = "2021-02-06";
		startDates[16] = "2021-02-08";
		startDates[17] = "2021-03-05";
		startDates[18] = "2021-04-08";
		startDates[19] = "2021-04-10";
		
		int lAreas[] = new int[] {1000, 960, 1500, 970, 1470, 1910, 1030, 1540, 2000, 1500, 1870, 1930, 1240, 900, 2050, 1770, 1600, 1330, 1220, 1150};
		int bedrooms[] = new int[] {2,2,3,2,3,4,2,3,5,4,4,4,2,1,3,3,3,2,3,2};
		int occupants[] = new int[] {2,1,2,2,4,2,3,1,3,2,5,4,3,5,2,3,2,3,2,4};
		
		for (int i = 0; i < lNum; i++) {
			instructionArrayList.add("insert into Location (cid, zcode, address, start_date, area, num_bedrooms, num_occupants) values (" 
		                       + cids[i] + ", '" + zcodes[i] + "', '" + address[i] + "', '"
		                       + startDates[i] + "', " + lAreas[i] + ", " + bedrooms[i] 
		                       + ", " + occupants[i] + ");");

		}
		
		instructionArrayList.add("");
		instructionArrayList.add("======================================================================================");
		instructionArrayList.add("");
		
		// Device
		int dNum = 20;
		int lids[] = new int[] {1,2,2,1,3, 5,5,3,4,5, 2,3,1,1,4, 7,8,2,5,7};
		String dtypeOptions[] = new String[] {"AC system", "light", "refrigerator"};
		String dtypes[] = new String[dNum];
		dtypes[0] = dtypeOptions[0];
		dtypes[1] = dtypeOptions[0];
		dtypes[2] = dtypeOptions[2];
		dtypes[3] = dtypeOptions[2];
		dtypes[4] = dtypeOptions[1];
		dtypes[5] = dtypeOptions[2];
		dtypes[6] = dtypeOptions[1];
		dtypes[7] = dtypeOptions[2];
		dtypes[8] = dtypeOptions[2];
		dtypes[9] = dtypeOptions[1];
		dtypes[10] = dtypeOptions[2];
		dtypes[11] = dtypeOptions[0];
		dtypes[12] = dtypeOptions[1];
		dtypes[13] = dtypeOptions[1];
		dtypes[14] = dtypeOptions[2];
		dtypes[15] = dtypeOptions[0];
		dtypes[16] = dtypeOptions[1];
		dtypes[17] = dtypeOptions[1];
		dtypes[18] = dtypeOptions[1];
		dtypes[19] = dtypeOptions[2];
		String dmodelOptions[] = new String[] {"Toshiba K500 AC", "Hitachi G30 AC", 
				"Phillipes E50 Light", "Osram H7 Light", 
				"Siemens R45 Refrigerator", "Mitsubishi EC70 Refrigerator"};	
		String dmodels[] = new String[dNum];
		dmodels[0] = dmodelOptions[0];
		dmodels[1] = dmodelOptions[1];
		dmodels[2] = dmodelOptions[4];
		dmodels[3] = dmodelOptions[5];
		dmodels[4] = dmodelOptions[2];
		dmodels[5] = dmodelOptions[5];
		dmodels[6] = dmodelOptions[3];
		dmodels[7] = dmodelOptions[4];
		dmodels[8] = dmodelOptions[4];
		dmodels[9] = dmodelOptions[2];
		dmodels[10] = dmodelOptions[5];
		dmodels[11] = dmodelOptions[0];
		dmodels[12] = dmodelOptions[2];
		dmodels[13] = dmodelOptions[3];
		dmodels[14] = dmodelOptions[4];
		dmodels[15] = dmodelOptions[1];
		dmodels[16] = dmodelOptions[2];
		dmodels[17] = dmodelOptions[3];
		dmodels[18] = dmodelOptions[2];
		dmodels[19] = dmodelOptions[5];
		
		
		for (int i = 0; i < dNum; i++) {
			instructionArrayList.add("insert into Device (lid, dtype, dmodel, description) values (" 
		                       + lids[i] + ", '" + dtypes[i] + "', '" + dmodels[i] + "', "
		                       + "NULL);");

		}
		
		instructionArrayList.add("");
		instructionArrayList.add("======================================================================================");
		instructionArrayList.add("");
		

		
		instructionArrayList.add("");
		instructionArrayList.add("======================================================================================");
		instructionArrayList.add("");
		
		int hours[] = new int[24];
		for (int i = 0; i < 24; i++) {
			hours[i] = i;
		}
		String[] priceZcodes = new String[] {"11201", "90001","10013","12215","32357","09805","30013","51007","91200","43347"}; 
		
		
		double randomNum;
		int randomInt;
		double unitPrice;
		for (int i = 0; i < priceZcodes.length; i++) {
			for (int j = 0; j < 24; j++) {
				randomNum = Math.random();
				if (j < 7 || j > 20) {
					randomInt = (int)(randomNum * 3 + 18);
				}
				else {
					randomInt = (int)(randomNum * 4 + 22);
				}
				unitPrice = (double) randomInt / 100;
				
				
				instructionArrayList.add("insert into EnergyPrice (zcode, start_hour, unit_price) values ('" 
	                       + priceZcodes[i] + "', " + hours[j] + ", " + unitPrice
	                       + ");");
			}
			

		}
		
		instructionArrayList.add("");
		instructionArrayList.add("======================================================================================");
		instructionArrayList.add("");
		
		instructionArrayList.add("");
		instructionArrayList.add("======================================================================================");
		instructionArrayList.add("");

		//Events 1: open and close refrigerator doors
		
		//int fridgeIndex[] = new int[] {2,3,5,7,8,10,14,19};
		int fridgeIndex[] = new int[] {3,4,6,8,9,11,15,20};
		
		int day1,day2;
		int hr1,hr2;
		int min1,min2;
		int sec1,sec2;
		int secondRead, openTime;
		//double randomNum;
		//int randomInt;
		String date1, date2, time1, time2;
		int temp1, temp2, tempInc;
		
		for (int i = 0; i < fridgeIndex.length; i++) {
			for (int j = 0; j < 20; j++) {
				randomNum = Math.random();
				randomInt = (int)(randomNum * 31 + 1 ); //day of August
				day1 = randomInt;
				if (day1 < 10) {
					date1 = "2022-08-0" + day1;
				}
				else {
					date1 = "2022-08-" + day1;
				}
				randomNum = Math.random();
				secondRead = (int)(randomNum * 24 * 60 * 60); 
				sec1 = secondRead % 60;
				min1 = (secondRead / 60) % 60;
				hr1 = (secondRead / 3600) % 24;
				time1 = "";
				if (hr1 < 10) {
					time1 = time1.concat("0");
				}
				time1 = time1.concat(String.valueOf(hr1)).concat(":");
				if (min1 < 10) {
					time1 = time1.concat("0");
				}
				time1 = time1.concat(String.valueOf(min1)).concat(":");
				if (sec1 < 10) {
					time1 = time1.concat("0");
				}
				time1 = time1.concat(String.valueOf(sec1));
				
				randomNum = Math.random();
				temp1 = (int)(randomNum * 15 + 30);
				
				
				randomNum = Math.random();
				openTime = (int)(randomNum * 60 * 10); //close fridge door within 10 minutes
				randomNum = Math.random();
				tempInc = (int)(randomNum * 6 + 2);
				
				randomNum = Math.random();
				boolean forget = false;
				
				if (randomNum < 0.15) { //15% probability forgetting to close fridge door
					
					forget = true;
					
					randomNum = Math.random();
					openTime += (int)(randomNum * 20 + 30) * 60;
					randomNum = Math.random();
					tempInc += (int) (randomNum * 10 + 10);					
				}
				secondRead += openTime;
				//int second1 = secondRead;
				temp2 = temp1 + tempInc;
				if (secondRead / (24 * 60 * 60) > 0) {
					day2 = day1 + 1;
				}
				else {
					day2 = day1;
				}
				if (day2 > 31) {
					date2 = "2022-09-01";
				}
				else if (day2 < 10){
					date2 = "2022-08-0" + day2;
				}
				else {
					date2 = "2022-08-" + day2;
				}
				sec2 = secondRead % 60;
				min2 = (secondRead / 60) % 60;
				hr2 = (secondRead / 3600) % 24;
				time2 = "";
				if (hr2 < 10) {
					time2 = time2.concat("0");
				}
				time2 = time2.concat(String.valueOf(hr2)).concat(":");
				if (min2 < 10) {
					time2 = time2.concat("0");
				}
				time2 = time2.concat(String.valueOf(min2)).concat(":");
				if (sec2 < 10) {
					time2 = time2.concat("0");
				}
				time2 = time2.concat(String.valueOf(sec2));
				

				instructionArrayList.add("insert into Event (did, etimestamp, etype, value) values (" 
	                       + fridgeIndex[i] + ", '" + date1 + " " + time1 + "', 'door opened', " 
						   + temp1 + ");");
				instructionArrayList.add("insert into Event (did, etimestamp, etype, value) values (" 
	                       + fridgeIndex[i] + ", '" + date2 + " " + time2 + "', 'door closed', " 
						   + temp2 + ");");
				
				if (forget) {
					randomNum = Math.random();
					int energyWasteInt = (int)(randomNum * 10);
					double energyWaste = (double)energyWasteInt / 10 + 0.5;
					
					randomNum = Math.random();
					randomInt = (int)(randomNum * 45 + 30);
					secondRead += randomInt;
					
					if (secondRead / (24 * 60 * 60) > 0) {
						day2 = day1 + 1;
					}
					else {
						day2 = day1;
					}
					if (day2 > 31) {
						date2 = "2022-09-01";
					}
					else if (day2 < 10){
						date2 = "2022-08-0" + day2;
					}
					else {
						date2 = "2022-08-" + day2;
					}
					sec2 = secondRead % 60;
					min2 = (secondRead / 60) % 60;
					hr2 = (secondRead / 3600) % 24;
					time2 = "";
					if (hr2 < 10) {
						time2 = time2.concat("0");
					}
					time2 = time2.concat(String.valueOf(hr2)).concat(":");
					if (min2 < 10) {
						time2 = time2.concat("0");
					}
					time2 = time2.concat(String.valueOf(min2)).concat(":");
					if (sec2 < 10) {
						time2 = time2.concat("0");
					}
					time2 = time2.concat(String.valueOf(sec2));
					
					
					
					instructionArrayList.add("insert into EnergyUsage (did, utimestamp, kWh) values (" 
		                       + fridgeIndex[i] + ", '" + date2 + " " + time2 + "', " 
							   + energyWaste + ");");
					//System.out.println("sec2 - sec1 = " + (secondRead - second1));
					
				}
				
				
				
			}
			
			
		}
		
		for (int i = 0; i < fridgeIndex.length; i++) {
			for (int j = 1; j <= 31; j++) {
				String date3 = "2022-08-";
				if (j < 10) {
					date3 = date3.concat("0");
				}
				date3 = date3.concat(String.valueOf(j));
				
				for (int k = 3; k < 24; k+=6) {
					randomNum = Math.random();
					if (randomNum > 0.2) {
						secondRead = k * 60 * 60;
						randomNum = Math.random();
						randomInt = (int)((randomNum - 0.5) * 60 * 60);
						secondRead += randomInt;	
						
						int sec3 = secondRead % 60;
						int min3 = (secondRead / 60) % 60;
						int hr3 = (secondRead / 3600) % 24;
						String time3 = "";
						if (hr3 < 10) {
							time3 = time3.concat("0");
						}
						time3 = time3.concat(String.valueOf(hr3)).concat(":");
						if (min3 < 10) {
							time3 = time3.concat("0");
						}
						time3 = time3.concat(String.valueOf(min3)).concat(":");
						if (sec3 < 10) {
							time3 = time3.concat("0");
						}
						time3 = time3.concat(String.valueOf(sec3));
						
					    randomNum = Math.random();
						randomInt = (int)(randomNum * 15);
						double energyUsage = (double) randomInt / 10 + 0.5;
						
						
						
						instructionArrayList.add("insert into EnergyUsage (did, utimestamp, kWh) values (" 
			                       + fridgeIndex[i] + ", '" + date3 + " " + time3 + "', " 
								   + energyUsage + ");");
						
					}					
				}
			}
					
			
		}
		
		
		instructionArrayList.add("");
		instructionArrayList.add("======================================================================================");
		instructionArrayList.add("");
		
		//Sept
		
		
		for (int i = 0; i < fridgeIndex.length; i++) {
			for (int j = 0; j < 20; j++) {
				randomNum = Math.random();
				randomInt = (int)(randomNum * 30 + 1 ); //day of August
				day1 = randomInt;
				if (day1 < 10) {
					date1 = "2022-09-0" + day1;
				}
				else {
					date1 = "2022-09-" + day1;
				}
				randomNum = Math.random();
				secondRead = (int)(randomNum * 24 * 60 * 60); 
				sec1 = secondRead % 60;
				min1 = (secondRead / 60) % 60;
				hr1 = (secondRead / 3600) % 24;
				time1 = "";
				if (hr1 < 10) {
					time1 = time1.concat("0");
				}
				time1 = time1.concat(String.valueOf(hr1)).concat(":");
				if (min1 < 10) {
					time1 = time1.concat("0");
				}
				time1 = time1.concat(String.valueOf(min1)).concat(":");
				if (sec1 < 10) {
					time1 = time1.concat("0");
				}
				time1 = time1.concat(String.valueOf(sec1));
				
				randomNum = Math.random();
				temp1 = (int)(randomNum * 15 + 30);
				
				
				randomNum = Math.random();
				openTime = (int)(randomNum * 60 * 10); //close fridge door within 10 minutes
				randomNum = Math.random();
				tempInc = (int)(randomNum * 6 + 2);
				
				randomNum = Math.random();
				boolean forget = false;
				
				if (randomNum < 0.15) { //15% probability forgetting to close fridge door
					
					forget = true;
					
					randomNum = Math.random();
					openTime += (int)(randomNum * 20 + 30) * 60;
					randomNum = Math.random();
					tempInc += (int) (randomNum * 10 + 10);					
				}
				secondRead += openTime;
				//int second1 = secondRead;
				temp2 = temp1 + tempInc;
				if (secondRead / (24 * 60 * 60) > 0) {
					day2 = day1 + 1;
				}
				else {
					day2 = day1;
				}
				if (day2 > 31) {
					date2 = "2022-10-01";
				}
				else if (day2 < 10){
					date2 = "2022-09-0" + day2;
				}
				else {
					date2 = "2022-09-" + day2;
				}
				sec2 = secondRead % 60;
				min2 = (secondRead / 60) % 60;
				hr2 = (secondRead / 3600) % 24;
				time2 = "";
				if (hr2 < 10) {
					time2 = time2.concat("0");
				}
				time2 = time2.concat(String.valueOf(hr2)).concat(":");
				if (min2 < 10) {
					time2 = time2.concat("0");
				}
				time2 = time2.concat(String.valueOf(min2)).concat(":");
				if (sec2 < 10) {
					time2 = time2.concat("0");
				}
				time2 = time2.concat(String.valueOf(sec2));
				

				instructionArrayList.add("insert into Event (did, etimestamp, etype, value) values (" 
	                       + fridgeIndex[i] + ", '" + date1 + " " + time1 + "', 'door opened', " 
						   + temp1 + ");");
				instructionArrayList.add("insert into Event (did, etimestamp, etype, value) values (" 
	                       + fridgeIndex[i] + ", '" + date2 + " " + time2 + "', 'door closed', " 
						   + temp2 + ");");
				
				if (forget) {
					randomNum = Math.random();
					int energyWasteInt = (int)(randomNum * 10);
					double energyWaste = (double)energyWasteInt / 10 + 0.5;
					
					randomNum = Math.random();
					randomInt = (int)(randomNum * 45 + 30);
					secondRead += randomInt;
					
					if (secondRead / (24 * 60 * 60) > 0) {
						day2 = day1 + 1;
					}
					else {
						day2 = day1;
					}
					if (day2 > 31) {
						date2 = "2022-10-01";
					}
					else if (day2 < 10){
						date2 = "2022-09-0" + day2;
					}
					else {
						date2 = "2022-09-" + day2;
					}
					sec2 = secondRead % 60;
					min2 = (secondRead / 60) % 60;
					hr2 = (secondRead / 3600) % 24;
					time2 = "";
					if (hr2 < 10) {
						time2 = time2.concat("0");
					}
					time2 = time2.concat(String.valueOf(hr2)).concat(":");
					if (min2 < 10) {
						time2 = time2.concat("0");
					}
					time2 = time2.concat(String.valueOf(min2)).concat(":");
					if (sec2 < 10) {
						time2 = time2.concat("0");
					}
					time2 = time2.concat(String.valueOf(sec2));
					
					
					
					instructionArrayList.add("insert into EnergyUsage (did, utimestamp, kWh) values (" 
		                       + fridgeIndex[i] + ", '" + date2 + " " + time2 + "', " 
							   + energyWaste + ");");
					//System.out.println("sec2 - sec1 = " + (secondRead - second1));
					
				}
				
				
				
			}
			
			
		}
		
		for (int i = 0; i < fridgeIndex.length; i++) {
			for (int j = 1; j <= 30; j++) {
				String date3 = "2022-09-";
				if (j < 10) {
					date3 = date3.concat("0");
				}
				date3 = date3.concat(String.valueOf(j));
				
				for (int k = 3; k < 24; k+=6) {
					randomNum = Math.random();
					if (randomNum > 0.2) {
						secondRead = k * 60 * 60;
						randomNum = Math.random();
						randomInt = (int)((randomNum - 0.5) * 60 * 60);
						secondRead += randomInt;	
						
						int sec3 = secondRead % 60;
						int min3 = (secondRead / 60) % 60;
						int hr3 = (secondRead / 3600) % 24;
						String time3 = "";
						if (hr3 < 10) {
							time3 = time3.concat("0");
						}
						time3 = time3.concat(String.valueOf(hr3)).concat(":");
						if (min3 < 10) {
							time3 = time3.concat("0");
						}
						time3 = time3.concat(String.valueOf(min3)).concat(":");
						if (sec3 < 10) {
							time3 = time3.concat("0");
						}
						time3 = time3.concat(String.valueOf(sec3));
						
					    randomNum = Math.random();
						randomInt = (int)(randomNum * 15);
						double energyUsage = (double) randomInt / 10 + 0.5;
						
						
						
						instructionArrayList.add("insert into EnergyUsage (did, utimestamp, kWh) values (" 
			                       + fridgeIndex[i] + ", '" + date3 + " " + time3 + "', " 
								   + energyUsage + ");");
						
					}					
				}
			}
		}
		
		
		
		
		
		
		
		PrintWriter out = null;
        try {
            System.out.println("Entering try statement");            
            //out = new PrintWriter(new FileWriter(this.fileName));
            out = new PrintWriter(new FileWriter("sql.txt")); // change the outFile name here as requested
            for (String instruction : instructionArrayList) {
                out.println(instruction);
                out.flush();
            }
            out.close();
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Caught IndexOutOfBoundsException: " +
                                 e.getMessage());
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }
		
		
		
	}

}
