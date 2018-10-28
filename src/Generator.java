import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.HashMap;

public class Generator {

	public static final Random RANDOM = new Random();
	
	public static final String[] BEERS = {"HAZE","UNRELIABLE NARRATOR","SPOTTED COW","THREE PHILOSOPHERS","PRIMA PILS","TEMPTATION","PSEUDOSUE","SEIZOEN BRETTA",
			  "SAMUEL ADAMS","GOSE GONE WILD","ZOMBIE DUST","NARRAGANSETT LAGER","OLD CHUB","HEADY TOPPER","MIDAS TOUCH","RUMPKIN","PM DAWN","CORONA","BUDWEISER","COORS","PBR","MILLER","MICHELOB","HEINEKEN","BUSCH"};
	
	public static final String[] food = {"Chicken wings","Flatbread","Steak","Chicken burrito","Pulled pork","Alfredo pasta","Fries","Spagetti","Pizza","Garlic bread","Noodles","Turkey sub","Taco","Churro","Burger","Chicken platter"};
	
	public static final String[] drinks = {"Coke","Sprite","Seltzer","Orange Juice","Red Bull","Milkshake","Bottled Water","Fanta","Dr.Pepper"};
	
	public static String[] drinkers = new String[1000];
	
	public static final String[] manufacturer = {"AB_inbev","Heineken Holding","Asahi Group","Molson Coors Brewing","Calsberg Group"};
	
	public static String[][] addresses = new String[1000][3];
	
	public static String[] phoneNumbers = new String[1000];
	
	public static String[] bar_name = new String[100];
	
	public static String[] times = {"00:00","01:00","02:00","03:00","04:00","05:00","06:00","07:00","08:00","09:00","10:00","11:00",
					"12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00","22:00","23:00"};
		
	public static String[] openTimes = { "12:00","13:00","14:00","15:00" };
	
	public static String[] closeTimes = { "01:00","02:00","03:00","04:00" };
	public static String[] licenses = new String[100];
	public static String[][] barAddresses = new String[100][3];
	public static String[] barPhoneNumbers = new String[100];
	public static Double[] taxRates = new Double[50];
	public static String[] state= {"AL","AK","AZ","AR","CA","CO","CT","DE","FL","GA","HI","ID","IL","IN","IA","KS","KY","LA","ME",
			"MD","MA","MI","MN","MS","MO","MT","NE","NV","NH","NJ","NM","NY","NC","ND","OH","OK","OR","PA","RI","SC",
			"SD","TN","TX","UT","VT","VA","WA","WV","WI","WY"};

	static int billId = 0;
	
	static int transactionId = 0;
	
	public static void main(String[] args) {
		File file = new File("Names.txt");
		try {
			Scanner sc = new Scanner(file);
			int i = 0;
			while (sc.hasNextLine()) {
				String name = sc.nextLine();
				drinkers[i] = name;
				i++;
			}
			sc.close();
			
			sc = new Scanner(new File("Addresses.txt"));
			i = 0;
			while(sc.hasNextLine()) {
				String street = sc.nextLine().trim();
				String data = sc.nextLine();
				String city = data.split(",")[0];
				String state = data.split(",")[1].split(" ")[1];
				addresses[i] = new String[] { street, city, state };
				i++;
			}
			
			sc.close();
			
			sc = new Scanner(new File("bar_address.txt"));
			i = 0;
			while(sc.hasNextLine()) {
				String street = sc.nextLine().trim();
				String data = sc.nextLine();
				String city = data.split(",")[0];
				String state = data.split(",")[1].split(" ")[1];
				barAddresses[i] = new String[] { street, city, state };
				i++;
			}
			
			sc.close();
			
			sc=new Scanner(new File("bar_name.txt"));
			int j=0;
			while(sc.hasNextLine()) 
			{
				String line = sc.nextLine();
				String[] inter = line.split(" ",2);
				String[] finish = inter[1].split("\\(",2);
				bar_name[j]=finish[0];
				j=j+1;
			}
			
			sc = new Scanner(new File("PhoneNumbers.txt"));
			i = 0;
			while(sc.hasNextLine()) {
				phoneNumbers[i] = sc.nextLine();
				i++;
			}
			sc.close();
			
			sc = new Scanner(new File("bar_phone_number.txt"));
			i = 0;
			while(sc.hasNextLine()) {
				barPhoneNumbers[i] = sc.nextLine();
				i++;
			}
			sc.close();
			
			sc = new Scanner(new File("license"));
			i = 0;
			while(sc.hasNextLine()) {
				licenses[i++] = sc.nextLine();
			}
			sc.close();
			sc = new Scanner(new File("tax_rate.txt"));
			i = 0;
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] data = line.split(" ");
				String taxRate = data[1].substring(0, 4);
				taxRates[i] = Double.parseDouble(taxRate);
				i++;
			}
			sc.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
	
		//generateDrinkerCSV();
		//generateBarCSV();
		//generateItemsCSV();
		//generateLikes();
		//generateSells();
		//generateTaxRateCSV();
		generateTransactionsAndBills();
//		for (int i = 0; i < 500; i++) {
//			System.out.println(addresses[i][0] + addresses[i][1] + addresses[i][2]);
//		}
	}
	
	public static void generateTransactionsAndBills() {
		try { 
			DecimalFormat df = new DecimalFormat("#.00"); 
			HashMap<Integer, ArrayList<Integer>> frequents = new HashMap<>();//drinkerId, List<barIds>
			HashMap<Integer, ArrayList<Double[]>> sells = new HashMap<>();//barId, double[] = { itemId, price }
			HashMap<Integer, String> bars = new HashMap<>();
			HashMap<String, Double> tax = new HashMap<>();
			PrintWriter bills = new PrintWriter(new File("bills.csv"));
			PrintWriter transactions = new PrintWriter(new File("transactions.csv"));
			StringBuilder sbBills = new StringBuilder();
			StringBuilder sbTrans = new StringBuilder();
			sbBills.append("billId").append(',').append("tax").append(',').append("total").append(',')
			.append("date").append(',').append("hour").append('\n');
			sbTrans.append("transactionId").append(',').append("drinkerId").append(',').append("barId").append(',')
			.append("billId").append(',').append("itemId").append(',').append("price").append('\n');
			BufferedReader reader = new BufferedReader(new FileReader("frequents.csv"));
			String line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				String data[] = line.split(",");
				ArrayList<Integer> list = frequents.getOrDefault(Integer.parseInt(data[1]), new ArrayList<Integer>());
				list.add(Integer.parseInt(data[0]));
				frequents.put(Integer.parseInt(data[1]), list);
			}
			reader = new BufferedReader(new FileReader("sells.csv"));
			line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				String data[] = line.split(",");
				Double[] vals = { (double) Integer.parseInt(data[1]), Double.parseDouble(data[2]) };
				ArrayList<Double[]> list = sells.getOrDefault(Integer.parseInt(data[0]), new ArrayList<Double[]>());
				list.add(vals);
				sells.put(Integer.parseInt(data[0]), list);
			}
			reader = new BufferedReader(new FileReader("bars.csv"));
			line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				String data[] = line.split(",");
				bars.put(Integer.parseInt(data[0]), data[8]);
			}
			reader = new BufferedReader(new FileReader("taxRates.csv"));
			line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				String data[] = line.split(",");
				tax.put(data[0], Double.parseDouble(data[1]));
			}
			reader.close();
			int transactionCount = 20000;
			while(transactionCount > 0) {
				int year = 2018;
				int month = RANDOM.nextInt(12) + 1;
				int day = RANDOM.nextInt(28) +  1;
				String date = "" + (month < 10 ? "0" + month : month) + "/" + (day < 10  ? "0" + day : day) + "/" + year;
				int hour = (RANDOM.nextInt(10) + 15) % 24;
				int minutes = RANDOM.nextInt(59) + 1;
				if (minutes>=10)
				{
					String time = "" + hour + ":" + minutes;
				}
				else
				{
					String time = ""+ hour + ":0" + minutes;
				}
				int transAmount = RANDOM.nextInt(4) + 1;
				double total = 0;
				int drinkerId = RANDOM.nextInt(1000);
				ArrayList<Integer> barIds = frequents.get(drinkerId);
				if (barIds == null) {
					continue;
				}
				int barId = barIds.get(RANDOM.nextInt(barIds.size()));
				String state = bars.get(barId);
				Double taxRate = tax.get(state);
				int id = billId++;
				for (int i = 0; i < transAmount; i++) {
					int transId = transactionId++;
					ArrayList<Double[]> items = sells.get(barId);
					int index = RANDOM.nextInt(items.size());
					Double[] data = items.get(index);
					int itemId = (int) (data[0] / 1);
					Double price = data[1];
					total += price;
					sbTrans.append(""+transId).append(',').append(""+ drinkerId).append(',').append("" + barId).append(',')
					.append("" + id).append(',').append("" + itemId).append(',').append("" + price).append('\n');
					transactionCount--;
				}
				double copyTax = taxRate / 100.0;
				total *= 1 + copyTax;
				String tot = df.format(total);
				sbBills.append("" + id).append(',').append("" + taxRate).append(',').append(tot).append(',')
				.append(date).append(',').append(time).append('\n');
			}
			bills.write(sbBills.toString());
			bills.close();
			transactions.write(sbTrans.toString());
			transactions.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void generateItemsCSV() {
		try {
			PrintWriter writer = new PrintWriter(new File("items.csv"));
			StringBuilder sb = new StringBuilder();
			sb.append("itemId").append(',').append("itemName").append(',').append("type").append(',')
			.append("price").append(',').append("manufacturer").append('\n');
			
			int count = 0;
			for (int i = 0; i < BEERS.length; i++) {
				String beer = BEERS[i].substring(0, 1) + BEERS[i].substring(1).toLowerCase();
				String type = "Alcoholic";
				int price = RANDOM.nextInt(6) + 5;
				String manu = manufacturer[RANDOM.nextInt(manufacturer.length)];
				sb.append("" + count++).append(',').append(beer).append(',').append(type).append(',')
				.append("" + price).append(',').append(manu).append('\n');
			}
			
			for (int i = 0; i < food.length; i++) {
				String beer = food[i];
				String type = "Food";
				int price = RANDOM.nextInt(6) + 5;
				String manu = "";
				sb.append("" + count++).append(',').append(beer).append(',').append(type).append(',')
				.append("" + price).append(',').append(manu).append('\n');
			}
			
			for (int i = 0; i < drinks.length; i++) {
				String beer = drinks[i];
				String type = "Soft drink";
				int price = RANDOM.nextInt(6) + 5;
				String manu = "";
				sb.append("" + count++).append(',').append(beer).append(',').append(type).append(',')
				.append("" + price).append(',').append(manu).append('\n');
			}
			
			writer.write(sb.toString());
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generate Bars CSV
	 */
	public static void generateTaxRateCSV() {
		try {
			PrintWriter writer = new PrintWriter(new File("taxRates.csv"));
			StringBuilder sb = new StringBuilder();
			sb.append("state").append(',').append("taxRate").append('\n');
			
			
			for (int i = 0; i < 50; i++) {
				sb.append(state[i]).append(',').append("" + taxRates[i]).append('\n');
			}
			writer.write(sb.toString());
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Generate Bars CSV
	 */
	public static void generateBarCSV() {
		try {
			PrintWriter writer = new PrintWriter(new File("bars.csv"));
			StringBuilder sb = new StringBuilder();
			sb.append("barId").append(',').append("barName").append(',').append("license").append(',')
			.append("open").append(',').append("close").append(',').append("phone")
			.append(',').append("address").append(',').append("city").append(',').append("state").append('\n');
			
			
			for (int i = 0; i < 100; i++) {
				String open = openTimes[RANDOM.nextInt(openTimes.length)];
				String close = closeTimes[RANDOM.nextInt(closeTimes.length)];
				String[] data = barAddresses[i];
				sb.append("" + i).append(',').append(bar_name[i]).append(',').append(data[2] + licenses[i]).append(',')
				.append(open).append(',').append(close).append(',').append(barPhoneNumbers[i])
				.append(',').append(data[0]).append(',').append(data[1]).append(',').append(data[2]).append('\n');
			}
			writer.write(sb.toString());
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Generates Drinker CSV.
	 */
	public static void generateDrinkerCSV() {
		try {
			PrintWriter writer = new PrintWriter(new File("drinkers.csv"));
			StringBuilder sb = new StringBuilder();
			sb.append("drinkerId").append(',').append("drinkerName").append(',').append("phone").append(',')
			.append("address").append(',').append("city").append(',').append("state").append('\n');
			for (int i = 0; i < 1000; i++) {
				String[] data = addresses[i];
				sb.append("" + i).append(',').append(drinkers[i]).append(',').append(phoneNumbers[i]).append(',')
				.append(data[0]).append(',').append(data[1]).append(',').append(data[2]).append('\n');
			}
			writer.write(sb.toString());
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Generates the Likes CSV
	 */
	public static void generateLikes() {
		try {
			PrintWriter writer = new PrintWriter(new File("likes.csv"));
			StringBuilder sb = new StringBuilder();
			sb.append("drinkerId").append(',').append("itemId").append('\n');
			HashMap<Integer, ArrayList<Integer>> map = new HashMap<>();
			for (int i = 0; i < 3000; i++) {
				int drinkerId = RANDOM.nextInt(1000);
				int foodId = RANDOM.nextInt(50);
				boolean cont = false;
				for (Integer check : map.getOrDefault(drinkerId, new ArrayList<Integer>())) {
					if (check == foodId) {
						cont = true;
						break;
					}
				}
				if (cont) {
					continue;
				}
				sb.append("" + drinkerId).append(',').append("" + foodId).append('\n');
			}
			writer.write(sb.toString());
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void generateSells() {
			try{
				DecimalFormat df = new DecimalFormat("#.00"); 
				HashMap<Integer, ArrayList<Integer>> map = new HashMap<>();
				BufferedReader reader = new BufferedReader(new FileReader("items.csv"));
				String line = reader.readLine();
				while ((line = reader.readLine()) != null) {
					String[] data = line.split(",");
					int id = Integer.parseInt(data[0]);
					int price = Integer.parseInt(data[3]);
					ArrayList<Integer> list = map.getOrDefault(price, new ArrayList<Integer>());
					list.add(id);
					map.put(price, list);
				}
				reader.close();
				PrintWriter writer = new PrintWriter(new File("sells.csv"));
				StringBuilder sb = new StringBuilder();
				sb.append("barId").append(',').append("itemId").append(',').append("price").append('\n');
				for (int i = 0; i < 10; i++) {
					for (int j = 0; j < 100; j++) {
						int price = RANDOM.nextInt(6) + 5;
						if (price < 5 || price > 10) {
							throw new Exception();
						}
						ArrayList<Integer> list = map.get(price);
						int totalSize = list.size();
						int index = RANDOM.nextInt(totalSize);
						double interval = (1.0 /totalSize);
						double lowerBound = interval * index;
						double actPrice = price + (RANDOM.nextDouble() * interval + lowerBound);
						String val = df.format(actPrice);
						int barId = RANDOM.nextInt(100);
						sb.append("" + barId).append(',').append("" + list.get(index)).append(',').append(val).append('\n');
					}
				}
				writer.write(sb.toString());
				writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
	}

}
