
public class Bank_Customer_Care {
	private Bank_Branch bank_branch_link; 
	private String customer_care_ID;
	
	public Bank_Customer_Care(String customer_care_ID, Bank_Branch bank_branch_link){
		this.bank_branch_link = bank_branch_link;
		this.customer_care_ID = customer_care_ID;
	}
	public void print_message(String message){
		System.out.println("Customer Care#"+customer_care_ID+": " + message);
	}
	public int create_Bank_Account( String account_holder_name, int account_initial_balance, int current_day, String debit_card_passward){
		int new_IBAN = bank_branch_link.create_Bank_Account( customer_care_ID, account_holder_name, account_initial_balance, current_day, debit_card_passward);
		if(  new_IBAN == -1 ){ 
			print_message("Sorry, the account holder name was used before.");
			return -1;
		}else {
			print_message("A new account was created with IBAN#" + new_IBAN);
			return new_IBAN;
		}
	}
	
	public void change_account_holder_name(int IBAN, int current_day, String account_holder_name){
		if(!bank_branch_link.change_account_holder_name(IBAN, customer_care_ID, current_day, account_holder_name)) 
			print_message("Sorry, your account is inactive.");
		else print_message("Your name was successfully changed.");
	}
	public void change_account_status(int IBAN, int current_day, String new_status){
		bank_branch_link.change_account_status(IBAN, customer_care_ID, current_day, new_status);
	}
	public void renew_debit_card(int IBAN, int current_day,String entered_passward){
		if(!bank_branch_link.renew_debit_card(IBAN, customer_care_ID, current_day, entered_passward)) 
			print_message("Sorry, your account is inactive.");
		else print_message("Your debit card was successfully renewed.");
	}
	public int get_account_IBAN(String account_holder_name) {
		return bank_branch_link.get_account_IBAN(account_holder_name);
	}
	
	public int get_account_creation_day(int IBAN) {
		return bank_branch_link.get_account_creation_day(IBAN);
	}
	
	public int get_debit_card_expiration_day(int IBAN) {
		return bank_branch_link.get_debit_card_expiration_day(IBAN);
	}
	
	public String get_account_holder_name(int IBAN) {
		return bank_branch_link.get_account_holder_name(IBAN);
	}
	
	public String get_account_history(int IBAN) {
		return bank_branch_link.get_account_history(IBAN);
	}
}
