import java.util.Objects;


public class Bank_Account {
	
	private int account_IBAN;
	private String account_holder_name;
	private int account_balance; 
	private int account_creation_day; 
	private int debit_card_expiration_day; 
	private String account_history;
	private String account_status;
	private String debit_card_passward;
	
	public Bank_Account(int IBAN, String customer_name, int initial_balance,int current_day, String debit_card_passward){
		account_IBAN = IBAN;
		account_holder_name = customer_name;
		account_balance = initial_balance;
		account_creation_day = current_day;
		debit_card_expiration_day = current_day + 90;
		account_status = "ACTIVE";
		this.debit_card_passward = debit_card_passward;
	}
	
	public void deposit(int deposit_amount){account_balance	+= deposit_amount;}
	public boolean withdrawal(int withdrawal_amount){
		if(withdrawal_amount > account_balance) return false;
		account_balance -= withdrawal_amount;
		return true;
	}
	public void deduction(int deduction_amount){ account_balance -= deduction_amount;}
	public void change_account_holder_name(String account_holder_name){this.account_holder_name = account_holder_name;}
	public void change_account_status(String new_status){account_status = new_status;}
	public void renew_debit_card(int current_day, String new_passward){
		debit_card_expiration_day = current_day + 90;
		debit_card_passward = new_passward;
	}
	public void add_new_action(String new_action){
		if(account_history == null ) account_history = new_action;
		else account_history += "\n" + new_action;
	}
	public boolean check_passward(String entered_passward){return Objects.equals(debit_card_passward, entered_passward);}
	public int get_account_IBAN(){return account_IBAN;}
	public String get_account_holder_name(){return account_holder_name;}
	public int get_account_balance(){return account_balance;}
	public int get_account_creation_day(){return account_creation_day;} 
	public int get_debit_card_expiration_day(){return debit_card_expiration_day;} 
	public String get_account_history(){return account_history;}
	public String get_account_status(){return account_status;}
	
	public static void main(String[] args){
		Bank_Account account1 = new Bank_Account(123456789, "Khalid Alkhulayfi", 1000, 1,"1234");
		
		System.out.println(account1.get_account_holder_name() + " account balance is " + 
						   account1.get_account_balance());
		account1.deposit(1000);
		System.out.println(account1.get_account_holder_name() + " account balance is " + 
				   account1.get_account_balance());
	}
}
