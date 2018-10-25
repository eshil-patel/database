import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

public class Generator {

	public static final Random RANDOM = new Random();
	
	public static final String[] BEERS = {"HAZE","UNRELIABLE NARRATOR","SPOTTED COW","THREE PHILOSOPHERS","PRIMA PILS","TEMPTATION","PSEUDOSUE","SEIZOEN BRETTA",
			  "SAMUEL ADAMS"," GOSE GONE WILD"," ZOMBIE DUST","NARRAGANSETT LAGER"," OLD CHUB","HEADY TOPPER","MIDAS TOUCH","RUMPKIN","PM DAWN","CORONA","BUDWEISER","COORS","PBR","MILLER","MICHELOB","HEINEKEN","BUSCH"};
	
	public static String[] drinkers = new String[500];
	
	public static String[][] addresses = new String[500][3];
	
	public static String[] phoneNumbers = new String[500];
	
	public static String[] times = {"00:00","01:00","02:00","03:00","04:00","05:00","06:00","07:00","08:00","09:00","10:00","11:00",
					"12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00","22:00","23:00"}
		
	
	@SuppressWarnings("resource")
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
			
			sc = new Scanner(new File("PhoneNumbers.txt"));
			i = 0;
			while(sc.hasNextLine()) {
				phoneNumbers[i] = sc.nextLine();
				i++;
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
	
		generateDrinkerCSV();
//		for (int i = 0; i < 500; i++) {
//			System.out.println(addresses[i][0] + addresses[i][1] + addresses[i][2]);
//		}
	}
	
	public static void generateDrinkerCSV() {
		try {
			PrintWriter writer = new PrintWriter(new File("drinkers.csv"));
			StringBuilder sb = new StringBuilder();
			sb.append("drinkerId").append(',').append("drinkerName").append(',').append("phone").append(',')
			.append("address").append(',').append("city").append(',').append("state").append('\n');
			for (int i = 0; i < 500; i++) {
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

}
