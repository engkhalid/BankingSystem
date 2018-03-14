
public class Bank_Cashier {
	private Bank_Branch bank_branch_link; 
	private String cashier_ID;
	
	public Bank_Cashier(String cashier_ID, Bank_Branch bank_branch_link){
		this.bank_branch_link = bank_branch_link;
		this.cashier_ID = cashier_ID;
	}
	public void print_message(String message){
		System.out.println("Cashier#"+cashier_ID+": " + message);
	}
	
	// -------------------------------------- add console messages.
	
	public boolean deposit(int IBAN,int current_day,  int deposit_amount){
		if(!bank_branch_link.deposit(IBAN, cashier_ID, current_day, deposit_amount)) return false;
		return true;
	}
	
	public boolean withdrawal(int IBAN, int current_day, int withdrawal_amount){
		if (!bank_branch_link.withdrawal(IBAN, cashier_ID, current_day, withdrawal_amount)) return false;
		return true;
	}
	
	public boolean wire_transfer_money(int IBAN, int current_day, int receiver_account_IBAN, int transfer_amount){
		if(!bank_branch_link.wire_transfer_money(IBAN, cashier_ID, current_day, receiver_account_IBAN, transfer_amount)) return false;
		return true;
	}
	
	public int get_account_balance(int IBAN) {
		return bank_branch_link.get_account_balance(IBAN);
	}	
	
	public String get_account_Status(int IBAN) {
		return bank_branch_link.get_account_Status(IBAN);
	}
}
