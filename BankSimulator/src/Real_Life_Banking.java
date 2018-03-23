import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class Real_Life_Banking {

	private static int min_age_to_open_new_account = 15;
	private static int max_age_to_open_new_account = 100;
	private static int 				   sample_size = 50;//no more than 1000 because of the IBAN # generator.
	private static int 		  simulation_first_day = 100;
	private static int 		  	   simulation_days = 100;
	private static int 		   simulation_last_day = simulation_first_day + simulation_days;
	
	
	public static void main(String[] args) {
		Bank AlkhulayfiBANK = new Bank (); 
		Bank_Branch jeddah_branch = new Bank_Branch("JBB", AlkhulayfiBANK);
		Bank_ATM ATM = new Bank_ATM("ATM", AlkhulayfiBANK);
		Bank_Customer_Care customer_care = new Bank_Customer_Care("CC",jeddah_branch);
		Bank_Cashier cashier = new Bank_Cashier("C$",jeddah_branch);
		ArrayList<Bank_Account_Holder> Bank_Account_holders_DB = new ArrayList<Bank_Account_Holder>(sample_size);
		
		String companies_name[] = new String[]{"ARAMCO","SABIC","STC","SEC","AlRajhi"};
		int companies_salary_amount[] = new int[]{11500, 14000, 12000, 10000, 8000};
		Employer employer_companies[] = new Employer[companies_name.length] ;
		for (int current_company = 0 ; current_company < companies_name.length; current_company++){
			employer_companies[current_company] = new Employer(companies_name[current_company],companies_salary_amount[current_company]);
		}
		
		File names_file = new File("Arabic_names.txt");
		try{
			Scanner file_reader = new Scanner(names_file);
			String account_holder_name, bedit_card_passward, phone_number;
			int IBAN, company_working_in;
			for ( int current_account_num = 1 ; current_account_num <= sample_size ; current_account_num++){
				account_holder_name = file_reader.next();
				bedit_card_passward = ""+rand(1000,9999);
				phone_number = "0" + rand(500000000,599999999);
				company_working_in = rand(0,employer_companies.length-1);
				
				IBAN = customer_care.create_Bank_Account(account_holder_name, rand(1000,10000), rand(1,simulation_first_day), bedit_card_passward);
				employer_companies[company_working_in].add_emplyee(IBAN);
				Bank_Account_Holder new_account_holder = 
						new Bank_Account_Holder(account_holder_name, IBAN, bedit_card_passward, customer_care.get_debit_card_expiration_day(IBAN),
												phone_number, employer_companies[company_working_in].getCompany_name(),
												employer_companies[company_working_in].get_employee_ID(IBAN));
				Bank_Account_holders_DB.add(new_account_holder);
			}
		}catch(IOException e){
			System.out.println("Error! while opening the file.");
		}
		
		//Main life-cycle.
		for ( int current_day = simulation_first_day ; current_day <= simulation_last_day ; current_day++){
			
			
			//Salary day.
			if(current_day % 30  == 0){
				for (int current_company = 0 ; current_company < companies_name.length; current_company++){
					ArrayList<Integer> employees_IBAN= employer_companies[current_company].getEmployees_IBAN();
					for (Integer IBAN : employees_IBAN){
						cashier.deposit(IBAN, current_day, employer_companies[current_company].get_salary_amount());
					}
				}
			}
			
			//Renew expired debit-cards.
			for (Bank_Account_Holder current_account_holder : Bank_Account_holders_DB){
				if (current_account_holder.getBedit_card_expiration_date() <= current_day){
					String bedit_card_passward = ""+rand(1000,9999);
					customer_care.renew_debit_card(current_account_holder.getBank_account_IBAN(), current_day, bedit_card_passward);
					current_account_holder.setBedit_card_expiration_date(customer_care.get_debit_card_expiration_day(current_account_holder.getBank_account_IBAN()));
				}
			}
			
			// limit added.
			
		}
		
		//Clearing the log file.
		try {
			FileWriter fw = new FileWriter("log.txt");
			
	    } catch (IOException e) {
			System.out.println("Error while writing to file.");
		}
		
        //Logging and saving data.
		System.out.print("Saving new data");
		int IBAN;
		for (Bank_Account_Holder account_holder : Bank_Account_holders_DB){
			IBAN = account_holder.getBank_account_IBAN();
			log(customer_care.get_account_history(IBAN));
			log("debit card expiration day is: " + customer_care.get_debit_card_expiration_day(IBAN));
			log("The company he is working at is "+ account_holder.get_company_working_in() + " - ID#" + account_holder.get_employee_ID());
			log("His phone number is "+ account_holder.getPhone_number());
			log("=========================================================================================================");
			System.out.print(".");
		}
		System.out.println();
        System.out.println("Done saving.");

        
        
	}
	public static void log(String data){
		try{
			FileWriter fw = new FileWriter("log.txt",true);
	        BufferedWriter bw = new BufferedWriter(fw);
	        bw.write(data + " \n");
	        bw.close();
		}catch(Exception e){
			System.out.println("Error while writing to file.");
		}
	}
	public static void initilaizations(PrintWriter pen){
		//Fill out the ages of the account holders
		for(int line=1; line<=50;line++){
			pen.println(rand(min_age_to_open_new_account, max_age_to_open_new_account));
		}
				
		
	}
	
	public static int rand(int min,int max){
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}

}
