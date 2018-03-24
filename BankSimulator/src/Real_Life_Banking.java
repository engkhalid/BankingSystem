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
	
	private static int            min_deposit_rate = 5;
	private static int         min_withdrawal_rate = 10;
	private static int            max_deposit_rate = 20;//Each account holder have a different rate of operations.  
	private static int         max_withdrawal_rate = 30;//So, when creating them, the random number will not exceed these numbers. 
	private static int      max_wire_transfer_rate = 10;
	private static int max_forgeting_password_rate = 5;

	public static void main(String[] args) {
		//================================================== Initializing all objects ==================================================
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
		//======================================================================================================================

		//================================================== Creating all accounts ==================================================
		File names_file = new File("Arabic_names.txt");
		try{
			Scanner file_reader = new Scanner(names_file);
			//String account_holder_name, bedit_card_passward, phone_number;
			int IBAN, company_working_in;
			for ( int current_account_num = 1 ; current_account_num <= sample_size ; current_account_num++){
				String account_holder_name = file_reader.next();
				String bedit_card_passward = ""+rand(1000,9999);
				String phone_number = "0" + rand(500000000,599999999);
				company_working_in = rand(0,employer_companies.length-1);
				
				IBAN = customer_care.create_Bank_Account(account_holder_name, rand(1000,10000), rand(1,simulation_first_day), bedit_card_passward);
				employer_companies[company_working_in].add_emplyee(IBAN);
				Bank_Account_Holder new_account_holder = 
						new Bank_Account_Holder(account_holder_name, IBAN, bedit_card_passward, customer_care.get_debit_card_expiration_day(IBAN),
												phone_number, employer_companies[company_working_in].getCompany_name(),
												employer_companies[company_working_in].get_employee_ID(IBAN),
												rand(min_deposit_rate,max_deposit_rate),rand(min_withdrawal_rate,max_withdrawal_rate),
												rand(1,max_wire_transfer_rate),rand(1,max_forgeting_password_rate));
				Bank_Account_holders_DB.add(new_account_holder);
			}
		}catch(IOException e){
			System.out.println("Error! while opening the file.");
		}
		//======================================================================================================================

		//================================================== Main life-cycle. ==================================================
		for ( int current_day = simulation_first_day ; current_day <= simulation_last_day ; current_day++){
			System.out.println("================== Day# " + current_day + " ==================");
			
			//Bank daily check: debit card expiration date.
			AlkhulayfiBANK.check_debit_card_expiration(current_day);
			System.out.println("Done checking debit card expiration date.");
			
			//Salary payment.
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
				if (current_account_holder.getDebit_card_expiration_date() < current_day){
					String debit_card_passward = "" + rand(1000,9999);
					customer_care.renew_debit_card(current_account_holder.getBank_account_IBAN(), current_day, debit_card_passward);
					current_account_holder.setDebit_card_passward(debit_card_passward);
					current_account_holder.setDebit_card_expiration_date(customer_care.get_debit_card_expiration_day(current_account_holder.getBank_account_IBAN()));
				}
			}
			
			//Each account holder will do one operation.
			for (Bank_Account_Holder current_account_holder : Bank_Account_holders_DB){
				String current_operation = current_account_holder.do_operation(rand(1,100));
				int cashier_ATM_choice = rand(1,2);// 1 for cashier & 2 for ATM
				
				switch (current_operation){
					case "Deposit":
						int deposit_amount = rand(1,1000);
						if(cashier_ATM_choice == 1) {
							System.out.println(current_account_holder.getName() + " is depositing to his account via cashier.");
							cashier.deposit(current_account_holder.getBank_account_IBAN(), current_day, deposit_amount);
						}
						else {
							System.out.println(current_account_holder.getName() + " is depositing to his account via ATM.");
							ATM.start_new_session(current_account_holder.getBank_account_IBAN(), current_account_holder.getDebit_card_passward(), current_day);
							ATM.deposit(deposit_amount);
						}
						break;
						
					case "Withdraw":
						int withdrawal_amount = rand(1,1000);
						if(cashier_ATM_choice == 1) {
							System.out.println(current_account_holder.getName() + " is withdrawing from his account via cashier.");
							cashier.withdrawal(current_account_holder.getBank_account_IBAN(), current_day, withdrawal_amount);
						}
						else {
							System.out.println(current_account_holder.getName() + " is withdrawing from his account via ATM.");
							ATM.start_new_session(current_account_holder.getBank_account_IBAN(), current_account_holder.getDebit_card_passward(), current_day);
							ATM.withdrawal(withdrawal_amount);
						}
						break;
						
					case "Wire transfer":
						int transfer_amount = rand(1,1000);
						int receiver_account_IBAN =  Bank_Account_holders_DB.get(rand(0,Bank_Account_holders_DB.size()-1)).getBank_account_IBAN();

						// there is a chance that the account holder will transfer money to himself 
						if(cashier_ATM_choice == 1) {
							System.out.println(current_account_holder.getName() + " is transfering money to IBAN# " + receiver_account_IBAN + " via cahier." );
							cashier.wire_transfer_money(current_account_holder.getBank_account_IBAN(), current_day, receiver_account_IBAN, transfer_amount);
						}
						else {
							System.out.println(current_account_holder.getName() + " is transfering money to IBAN# " + receiver_account_IBAN + " via ATM." );
							ATM.start_new_session(current_account_holder.getBank_account_IBAN(), current_account_holder.getDebit_card_passward(), current_day);
							ATM.wire_transfer_money(receiver_account_IBAN, transfer_amount);
						}
						break;
						
					case "Forgeting password":
						System.out.println(current_account_holder.getName() + " forgot his account password and he is getting a new debit card via customer care.");
						String debit_card_passward = "" + rand(1000,9999);
						customer_care.renew_debit_card(current_account_holder.getBank_account_IBAN(), current_day, debit_card_passward);
						current_account_holder.setDebit_card_passward(debit_card_passward);
						current_account_holder.setDebit_card_expiration_date(customer_care.get_debit_card_expiration_day(current_account_holder.getBank_account_IBAN()));
						break;
						
					case "Idle":
						//System.out.println(current_account_holder.getName() + " is doing nothing today.");
				}
			}
			
			

		}
		//======================================================================================================================
		
		//================================================== Logging all accounts ==================================================
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
		log("Cash flow in Alkhulayfi Bank is ( " + String.format("%,d ",AlkhulayfiBANK.get_cash_flow()) + " S.R. ).");
		System.out.println();
        System.out.println("Done saving.");
		//======================================================================================================================

        
        
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
