/**  
 * An ATM allows the customer to do one of the following:
 * 		1- Deposit money
 * 		2- Withdrawal money (up to 5000 S.R.)
 * 		3- Wire transfer money
 * 		4- Check account balance
 * 		5- Summary of the last 5 operations
 * 
 * @author Khalid Alkhulayfi
 * @version Feb 23, 2018. 
 */

public class Bank_ATM {

	private int count_500_bill; 
	private int count_100_bill; 
	private int count_50_bill; 
	private Bank bank_link; 
	private String ATM_ID;
	private int current_account_IBAN;
	private int current_day;
	private boolean serving_customer;
	private int withdrawal_amount_limit = 5000;
	private int deposit_amount_limit = 20000;
	private int transfer_amount_limit = 20000;

	public Bank_ATM(String ATM_ID, Bank bank_link){
		this.bank_link = bank_link;
		this.ATM_ID = ATM_ID;
	}
	
	public void cash_loading(int bills_500, int bills_100, int bills_50){
		count_500_bill += bills_500;
		count_100_bill += bills_100;
		count_50_bill  += bills_50;
	}
	
	public void print_message(String message){
		System.out.println("ATM#"+ATM_ID+": " + message);
	}
	public void start_new_session(int IBAN, String entered_passward, int current_day){
		this.current_day = current_day;
		if(bank_link.get_account_Status(IBAN) == null){
			print_message(" There is no account with the entered IBAN.");	
		}else if(bank_link.get_account_Status(IBAN) == "INACTIVE"){
			print_message("Sorry, your account is inactive.");
		}else{
			if(bank_link.check_passward(IBAN, entered_passward)){
				serving_customer = true;
				current_account_IBAN = IBAN;
			}else{
				print_message("Wrong passward!");
			}
		}
	}
	public void quit_current_session(){
		serving_customer = false;
	}
	
	public void account_balance(int account_ID){
		if (serving_customer){
			print_message(String.format("Your account balance is %,d ",bank_link.get_account_balance(current_account_IBAN)));
		}else print_message("Please start the session first.");
		quit_current_session();
	}
	
	public void deposit(int deposit_amount){
		if (serving_customer){
			if(deposit_amount <= deposit_amount_limit){
				if(bank_link.deposit(current_account_IBAN, ATM_ID, current_day, deposit_amount)){
					int money_left = deposit_amount;
					while(money_left>=500){
						money_left -= 500;
						count_500_bill++;
					}
					while(money_left>=100){
						money_left -= 100;
						count_100_bill++;
					}
					while(money_left>=50){
						money_left -= 50;
						count_50_bill++;
					}
					if(money_left!= 0){print_message("The " + money_left +" left is not accepted.");}
					print_message(String.format("%,d was added to your account",(deposit_amount-money_left)));
				}
			}else print_message("The maximum limit to deposit is " + deposit_amount_limit);
		}else print_message("Please start the session first.");
		quit_current_session();
	}

	public void withdrawal(int withdrawal_amount){
		if (serving_customer){
			if(withdrawal_amount <= withdrawal_amount_limit){
				if(bank_link.withdrawal(current_account_IBAN, ATM_ID, current_day, withdrawal_amount)){
					print_message(String.format("%,d was deducted from your account",withdrawal_amount));
					while(withdrawal_amount>=500){
						withdrawal_amount -= 500;
						count_500_bill--;
					}
					while(withdrawal_amount>=100){
						withdrawal_amount -= 100;
						count_100_bill--;
					}
					while(withdrawal_amount>=50){
						withdrawal_amount -= 50;
						count_50_bill--;
					}
				}else{
					print_message("Sorry, you do NOT have engouh balance.");
				}
			}else print_message("The maximum limit to withdraw is " + withdrawal_amount_limit);
		}else print_message("Please start the session first.");	
		quit_current_session();
	}
	
	public void wire_transfer_money(int receiver_account_IBAN, int transfer_amount){
		if (serving_customer){
			if(transfer_amount <= transfer_amount_limit){
				if(bank_link.wire_transfer_money(current_account_IBAN, ATM_ID, current_day, receiver_account_IBAN, transfer_amount)){
					print_message(String.format("%,d was deducted from your account",transfer_amount));
					print_message("The transaction has been made successfully to IBAN#" + receiver_account_IBAN);
				}else{
					print_message("Sorry, you do NOT have engouh balance.");
				}
			}else print_message("The maximum limit to wire transfer is " + transfer_amount_limit);
		}else print_message("Please start the session first.");	
		quit_current_session();
	}
	
	public String account_last_5_operations(){
		return bank_link.get_account_history_last_group(current_account_IBAN, 5);
	}
	
	public int get_count_500_bill(){return count_500_bill;}
	public int get_count_100_bill(){return count_100_bill;}
	public int get_count_50_bill(){return count_50_bill;}
	
	public static void main(String[] args) {
        
    }
}
