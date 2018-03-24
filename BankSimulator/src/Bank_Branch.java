
public class Bank_Branch {
	private Bank bank_link; 
	private String bank_branch_ID;
	private int bank_branch_IBAN_code = 11;
	private int account_ID_counter;
	
	public Bank_Branch(String bank_branch_ID, Bank bank_link){
		this.bank_link =bank_link;
		this.bank_branch_ID = bank_branch_ID;
		
	}
	private String add_Bank_Branch_ID(String caller){return bank_branch_ID + "-" + caller;}
	private int new_IBAN(){return (bank_branch_IBAN_code*1000)+(++account_ID_counter);}

	//Customer care work
	public int create_Bank_Account( String caller, String account_holder_name, int account_initial_balance, int current_day, String debit_card_passward){
		int IBAN = bank_link.create_Bank_Account(new_IBAN(), add_Bank_Branch_ID(caller), account_holder_name, account_initial_balance,
												 current_day, debit_card_passward); 
		if( IBAN == -1 ) 
			return -1;
		return IBAN;		
	}
	public boolean change_account_holder_name(int IBAN, String caller, int current_day, String account_holder_name){
		if(!bank_link.change_account_holder_name(IBAN, add_Bank_Branch_ID(caller), current_day, account_holder_name)) return false;
		return true;
	}
	
	public void renew_debit_card(int IBAN, String caller, int current_day,String entered_passward){
		bank_link.renew_debit_card(IBAN, add_Bank_Branch_ID(caller), current_day, entered_passward);
	}
	
	public int get_account_creation_day(int IBAN) {
		return bank_link.get_account_creation_day(IBAN);
	}
	
	public int get_debit_card_expiration_day(int IBAN) {
		return bank_link.get_debit_card_expiration_day(IBAN);
	}
	
	public String get_account_holder_name(int IBAN) {
		return bank_link.get_account_holder_name(IBAN);
	}
	
	public String get_account_history(int IBAN) {
		return bank_link.get_account_history(IBAN);
	}
	
	public int get_account_IBAN(String account_holder_name) {
		return bank_link.get_account_IBAN(account_holder_name);
	}
	
	//Casher work
	public boolean deposit(int IBAN, String caller, int current_day,  int deposit_amount){
		if(!bank_link.deposit(IBAN, add_Bank_Branch_ID(caller), current_day, deposit_amount)) return false;
		return true;
	}
	
	public boolean withdrawal(int IBAN, String caller, int current_day, int withdrawal_amount){
		if (!bank_link.withdrawal(IBAN, add_Bank_Branch_ID(caller), current_day, withdrawal_amount)) return false;
		return true;
	}
	
	public boolean wire_transfer_money(int IBAN, String caller, int current_day, int receiver_account_IBAN, int transfer_amount){
		if(!bank_link.wire_transfer_money(IBAN, add_Bank_Branch_ID(caller), current_day, receiver_account_IBAN, transfer_amount)) return false;
		return true;
	}
	
	public int get_account_balance(int IBAN) {
		return bank_link.get_account_balance(IBAN);
	}	
	
	//used by all employees.
	public String get_account_Status(int IBAN) {
		return bank_link.get_account_Status(IBAN);
	}
}
