import java.util.*;


public class Bank {
	private ArrayList<Bank_Account> Bank_Account_DB;
	private int deduction_fee = 50;
	private int bank_SWIFT_code = 123;
	private enum account_Status { ACTIVE, INACTIVE }
	private int cash_flow;
	
	public Bank(){
		Bank_Account_DB = new ArrayList<Bank_Account>(10);
	}
	
	
	public int create_Bank_Account( int beanch_ID_plus_account_ID, String caller, String account_holder_name, 
										int account_initial_balance, int current_day, String debit_card_passward){
		int IBAN = new_IBAN(beanch_ID_plus_account_ID);
		if( get_Bank_Account(IBAN) != null | get_Bank_Account(account_holder_name) != null ) return -1;
		Bank_Account new_Account = new Bank_Account(IBAN,account_holder_name,account_initial_balance, current_day,debit_card_passward);
		Bank_Account_DB.add(new_Account);
		add_new_action(IBAN, caller, "Account created with initial balance.", current_day, account_initial_balance);
		deposit_money_to_Bank(account_initial_balance);
		return IBAN;		
	}
	
	//------------------------------------------------------------------------------------------------
	private void deposit_money_to_Bank(int new_deposit) { cash_flow += new_deposit; }
	
	private void withdrawal_money_from_Bank(int new_deposit) { cash_flow += new_deposit; }
	
	private int new_IBAN(int beanch_ID_plus_account_ID){return (bank_SWIFT_code*100000)+beanch_ID_plus_account_ID;}

	private String new_action_format(String caller, String action, int day, int amount,int balance){
		return String.format("|%4d|%15s|%-50s|%,10d S.R.|%,10d S.R.|", day,caller,action,amount,balance); 
	}
	
	private Bank_Account get_Bank_Account(int IBAN){
		for (Bank_Account current_Bank_Account : Bank_Account_DB){
			if (IBAN == current_Bank_Account.get_account_IBAN()){
				return current_Bank_Account;
			}
		}
		return null;
	}	
	
	private void add_new_action(int IBAN,String caller, String new_action, int day, int amount){
		Bank_Account requested_account = get_Bank_Account(IBAN);
		requested_account.add_new_action(new_action_format(caller, new_action, day, amount,get_account_balance(IBAN)));
	}
	
	private Bank_Account get_Bank_Account(String account_holder_name){
		for (Bank_Account current_Bank_Account : Bank_Account_DB){
			if (Objects.equals(account_holder_name,current_Bank_Account.get_account_holder_name())){
				return current_Bank_Account;
			}
		}
		return null;
	}
	
	//------------------------------------------------------------------------------------------------
	
	public int get_cash_flow() {return cash_flow;}
	
	public void check_debit_card_expiration(int current_day) {
		for (Bank_Account current_Bank_Account : Bank_Account_DB){
			if (current_day >= current_Bank_Account.get_debit_card_expiration_day() & 
					current_Bank_Account.get_account_status() == account_Status.ACTIVE.name()){
				current_Bank_Account.change_account_status(account_Status.INACTIVE.name());
				add_new_action(current_Bank_Account.get_account_IBAN(), "BANK", "Debit card expired. Your account is on hold.", current_day, 0);
			}
		}
	}
	
	public boolean deposit(int IBAN, String caller, int current_day,  int deposit_amount){
		Bank_Account requested_account = get_Bank_Account(IBAN);
		if (Objects.equals(requested_account.get_account_status(),account_Status.INACTIVE.name())){
			return false;
		}else{
			requested_account.deposit(deposit_amount);
			add_new_action(IBAN, caller, "Deposit operation.", current_day, deposit_amount);
			deposit_money_to_Bank(deposit_amount);
			return true;
		}
	}
	public boolean withdrawal(int IBAN, String caller, int current_day, int withdrawal_amount){
		Bank_Account requested_account = get_Bank_Account(IBAN);
		if (Objects.equals(requested_account.get_account_status(),account_Status.INACTIVE.name())){
			return false;
		}else{
			if (!requested_account.withdrawal(withdrawal_amount)){
				return false;
			}else{
				add_new_action(IBAN, caller, "Withdrawal operation.", current_day, -withdrawal_amount);
				withdrawal_money_from_Bank(withdrawal_amount);
				return true;
			}
		}
	}
	
	public boolean wire_transfer_money(int IBAN, String caller, int current_day,
									   int receiver_account_IBAN, int transfer_amount){
		Bank_Account requested_account = get_Bank_Account(IBAN);
		Bank_Account receiver_account = get_Bank_Account(receiver_account_IBAN);
		if (Objects.equals(requested_account.get_account_status(),account_Status.INACTIVE.name()) | 
				Objects.equals(receiver_account.get_account_status(),account_Status.INACTIVE.name())){
			return false;
		}else{
			if (!requested_account.withdrawal(transfer_amount)){
				return false;
			}else{
				receiver_account.deposit(transfer_amount);
				add_new_action(IBAN, caller, "Transfering money to IBAN#"+receiver_account_IBAN,
							   current_day, -transfer_amount);
				add_new_action(receiver_account_IBAN, caller, "Transfered money from IBAN#"+IBAN,
						   current_day, transfer_amount);
				return true;
			}
		}
	}
	public boolean change_account_holder_name(int IBAN, String caller, int current_day, String account_holder_name){
		Bank_Account requested_account = get_Bank_Account(IBAN);
		if (Objects.equals(requested_account.get_account_status(),account_Status.INACTIVE.name())){
			return false;
		}else{
			requested_account.change_account_holder_name(account_holder_name);
			add_new_action(IBAN, caller, "Changing account holder name.", current_day, 0);
			return true;
		}
	}
	public void change_account_status(int IBAN, String caller, int current_day, String new_status){
		Bank_Account requested_account = get_Bank_Account(IBAN);
		requested_account.change_account_status(new_status);
		add_new_action(IBAN, caller, "Changing account status.", current_day, 0);
	}
	public void renew_debit_card(int IBAN, String caller, int current_day,String entered_passward){
		Bank_Account requested_account = get_Bank_Account(IBAN);
		requested_account.renew_debit_card(current_day, entered_passward);
		requested_account.deduction(deduction_fee);
		requested_account.change_account_status(account_Status.ACTIVE.name());
		add_new_action(IBAN, caller, "The debit card renewed.", current_day, -deduction_fee);
	}
	
	public boolean check_passward(int IBAN, String entered_passward){
		return get_Bank_Account(IBAN).check_passward(entered_passward);
	}

	
	
	public int get_account_balance(int IBAN) {
		Bank_Account requested_account = get_Bank_Account(IBAN);
		return requested_account.get_account_balance();
	}
	
	public String get_account_holder_name(int IBAN) {
		Bank_Account requested_account = get_Bank_Account(IBAN);
		return requested_account.get_account_holder_name();
	}
	
	public int get_account_creation_day(int IBAN) {
		Bank_Account requested_account = get_Bank_Account(IBAN);
		return requested_account.get_account_creation_day();
	}
	
	public int get_debit_card_expiration_day(int IBAN) {
		Bank_Account requested_account = get_Bank_Account(IBAN);
		return requested_account.get_debit_card_expiration_day();
	}
	
	public String get_account_history(int IBAN) {
		Bank_Account requested_account = get_Bank_Account(IBAN);
		String account_actions_summary = 
				String.format("%65s", "Account operations summary")
				+ "\n" + String.format("%20s %-40d", "IBAN:", IBAN)
				+ "\n" + String.format("%20s %-40s", "Account holder name:", requested_account.get_account_holder_name())
				+ "\n" + String.format("%20s %-40s", "Account status:", get_account_Status(IBAN))
				+ "\n\n" + String.format("|%4s|%15s|%-50s|%15s|%15s|", "Day" , "Via","Action description","Amount","Balance")
				+ "\n" + "--------------------------------------------------------------------------------------"
				+ "-------------------"
				+ "\n" + requested_account.get_account_history();
		return account_actions_summary;
	}
	
	public String get_account_history_last_group(int IBAN, int number_of_operations) {
		Bank_Account requested_account = get_Bank_Account(IBAN);
		String account_actions_summary_head = 
				String.format("%65s", "Account operations summary")
				+ "\n" + String.format("%20s %-40d", "IBAN:", IBAN)
				+ "\n" + String.format("%20s %-40s", "Account holder name:", requested_account.get_account_holder_name())
				+ "\n" + String.format("%20s %-40s", "Account status:", get_account_Status(IBAN))
				+ "\n\n" + String.format("|%4s|%15s|%-50s|%15s|%15s|", "Day" , "Via","Action description","Amount","Balance")
				+ "\n" + "--------------------------------------------------------------------------------------"
				+ "-------------------"; 
		String account_actions_summary_tail = requested_account.get_account_history();
		int End_of_line = account_actions_summary_tail.length();
		for(int i = 0;i<number_of_operations;i++){
			End_of_line = account_actions_summary_tail.lastIndexOf('\n', End_of_line -1);
		}
		if(End_of_line == -1 )return
				account_actions_summary_head +"\n" + account_actions_summary_tail;
		else return account_actions_summary_head + 
				account_actions_summary_tail.substring(End_of_line,account_actions_summary_tail.length());
	}
	
	public String get_account_Status(int IBAN) {
		Bank_Account requested_account = get_Bank_Account(IBAN);
		return requested_account.get_account_status();
	}
	
	//-----------------------------------------------------------------------------------------
	
	
	
	
	public void deposit(String account_holder_name, int deposit_amount){
		Bank_Account requested_account = get_Bank_Account(account_holder_name);
		requested_account.deposit(deposit_amount);
	}

	public void change_account_status(String account_holder_name, String new_status){
		Bank_Account requested_account = get_Bank_Account(account_holder_name);
		requested_account.change_account_status(new_status);
	}
	public void renew_debit_card(String account_holder_name, int current_day,String entered_passward){
		Bank_Account requested_account = get_Bank_Account(account_holder_name);
		requested_account.renew_debit_card(current_day, entered_passward);
	}
	
	public int get_account_balance(String account_holder_name) {
		Bank_Account requested_account = get_Bank_Account(account_holder_name);
		return requested_account.get_account_balance();
	}
	
	public int get_account_IBAN(String account_holder_name) {
		Bank_Account requested_account = get_Bank_Account(account_holder_name);
		return requested_account.get_account_IBAN();
	}
	
	public int get_account_creation_day(String account_holder_name) {
		Bank_Account requested_account = get_Bank_Account(account_holder_name);
		return requested_account.get_account_creation_day();
	}
	
	public int get_debit_card_expiration_day(String account_holder_name) {
		Bank_Account requested_account = get_Bank_Account(account_holder_name);
		return requested_account.get_debit_card_expiration_day();
	}
	
	public String get_account_history(String account_holder_name) {
		Bank_Account requested_account = get_Bank_Account(account_holder_name);
		String account_ction_summary = 
				String.format("%55s", "Account operations summary")
				+ "\n" + String.format("%20s %-40d", "IBAN:", requested_account.get_account_IBAN())
				+ "\n" + String.format("%20s %-40s", "Account holder name:", account_holder_name)
				+ "\n\n" + String.format("|%4s|%15s|%-50s|%15s|", "Day" , "Via","Action description","Amount")
				+ "\n" + "----------------------------------------------------------------------"
				+ "-------------------"
				+ "\n" + requested_account.get_account_history();
		return account_ction_summary;
	}
	
	public String get_account_Status(String account_holder_name) {
		Bank_Account requested_account = get_Bank_Account(account_holder_name);
		return requested_account.get_account_status();
	}
	
	//----------------------------------------------------------------------------------
	
	public static void main(String[] args) {
		Bank AlkhulayfiBANK = new Bank ();
		
		System.out.println("1111111111111111111111111111111111111111111111111111111111111111111");
		AlkhulayfiBANK.create_Bank_Account(123456,"COSTUMER CARE:", "Kahlid"	, 1000, 5,"1234");
		System.out.println(AlkhulayfiBANK.get_account_history(123456));System.out.println("");
		
		System.out.println("2222222222222222222222222222222222222222222222222222222222222222222");
		AlkhulayfiBANK.change_account_holder_name(123456, "CUSTOMER CARE:", 6, "Khalid Abdullah");
		System.out.println(AlkhulayfiBANK.get_account_history(123456));System.out.println("");
		
		System.out.println("3333333333333333333333333333333333333333333333333333333333333333333");
		AlkhulayfiBANK.change_account_status(123456, "Bank Branch:", 7, "INACTIVE");
		System.out.println(AlkhulayfiBANK.get_account_history(123456));System.out.println("");

		System.out.println("4444444444444444444444444444444444444444444444444444444444444444444");
		AlkhulayfiBANK.withdrawal(123456, "ATM#1:", 7, 500);
		System.out.println(AlkhulayfiBANK.get_account_history(123456));System.out.println("");
		
		System.out.println("5555555555555555555555555555555555555555555555555555555555555555555");
		AlkhulayfiBANK.change_account_status(123456, "Bank Branch:", 7, "ACTIVE");
		System.out.println(AlkhulayfiBANK.get_account_history(123456));System.out.println("");
		
		System.out.println("6666666666666666666666666666666666666666666666666666666666666666666");
		AlkhulayfiBANK.withdrawal(123456, "ATM#1:", 8, 1500);
		System.out.println(AlkhulayfiBANK.get_account_history(123456));System.out.println("");

		System.out.println("7777777777777777777777777777777777777777777777777777777777777777777");
		AlkhulayfiBANK.withdrawal(123456, "ATM#1:", 8, 500);
		System.out.println(AlkhulayfiBANK.get_account_history(123456));System.out.println("");
		
		System.out.println("8888888888888888888888888888888888888888888888888888888888888888888");
		AlkhulayfiBANK.renew_debit_card(123456, "CUSTOMER CARE:", 9,"1234");
		System.out.println(AlkhulayfiBANK.get_account_history(123456));System.out.println("");
		
		System.out.println("999999999999999999999999999999999999999999999999999999999999999999");
		AlkhulayfiBANK.deposit(123456, "ATM#2:", 10, 450);
		System.out.println(AlkhulayfiBANK.get_account_history(123456));System.out.println("");
		
		System.out.println("******************************************************************");
		AlkhulayfiBANK.create_Bank_Account(456789,"CUSTOMER CARE:", "Alkhulayfi"	, 500, 10,"3456");
		System.out.println(AlkhulayfiBANK.get_account_history(456789));System.out.println("");
		
		System.out.println("******************************************************************");
		AlkhulayfiBANK.wire_transfer_money(123456, "Casher:", 11, 456789, 500);
		System.out.println(AlkhulayfiBANK.get_account_history(123456));System.out.println("");
		System.out.println(AlkhulayfiBANK.get_account_history(456789));System.out.println("");

		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		AlkhulayfiBANK.wire_transfer_money(456789, "ATM#2:", 12, 123456, 100);
		System.out.println(AlkhulayfiBANK.get_account_history(123456));System.out.println("");
		System.out.println(AlkhulayfiBANK.get_account_history(456789));System.out.println("");
		
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println(AlkhulayfiBANK.get_account_history_last_group(123456, 5));System.out.println("");
		
	}
}
