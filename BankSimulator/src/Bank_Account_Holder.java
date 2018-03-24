
public class Bank_Account_Holder {
	private String name;
	private int bank_account_IBAN;
	private String debit_card_passward;
	private int debit_card_expiration_date;
	private String phone_number; 
	private String company_working_in;
	private int employee_ID;
	
	private int idle_rate, deposit_rate, withdrawal_rate, wire_transfer_rate, forgeting_password_rate;
		
	public Bank_Account_Holder(String name,int bank_account_IBAN,String bedit_card_passward,int bedit_card_expiration_date,
							   String phone_number, String company_working_in,int employee_ID,
							   int deposit_rate, int withdrawal_rate, int wire_transfer_rate, int forgeting_password_rate){
		setName(name);
		setBank_account_IBAN(bank_account_IBAN);
		setDebit_card_passward(bedit_card_passward);
		setDebit_card_expiration_date(bedit_card_expiration_date);
		setPhone_number(phone_number);
		set_company_working_in(company_working_in, employee_ID);
		
		this.deposit_rate = deposit_rate;
		this.withdrawal_rate = this.deposit_rate + withdrawal_rate;
		this.wire_transfer_rate = this.withdrawal_rate + wire_transfer_rate;
		this.forgeting_password_rate = this.wire_transfer_rate + forgeting_password_rate;
		idle_rate = 100 - (deposit_rate + withdrawal_rate + wire_transfer_rate + forgeting_password_rate);
		
		System.out.println(name + "'s rate of operations is: " + deposit_rate + " deposit - " 
															   + withdrawal_rate + " withdraw - "
															   + wire_transfer_rate + " wire transfer - "
															   + forgeting_password_rate + " forgeting password - "
															   + idle_rate + " idle."
				);
	}
	
	public String do_operation(int persent) {
		if ( persent <= deposit_rate) return "Deposit";
		else if ( persent <= withdrawal_rate) return "Withdraw";
		else if ( persent <= wire_transfer_rate) return "Wire transfer";
		else if ( persent <= forgeting_password_rate) return "Forgeting password";
		else return "Idle";
	}
	
	public String get_company_working_in() {
		return company_working_in;
	}
	
	public int get_employee_ID() {
		return employee_ID;
	}
	
	public void set_company_working_in(String company_working_in,int employee_ID) {
		this.company_working_in = company_working_in;
		this.employee_ID = employee_ID;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBank_account_IBAN() {
		return bank_account_IBAN;
	}

	public void setBank_account_IBAN(int bank_account_IBAN) {
		this.bank_account_IBAN = bank_account_IBAN;
	}

	public String getDebit_card_passward() {
		return debit_card_passward;
	}

	public void setDebit_card_passward(String debit_card_passward) {
		this.debit_card_passward = debit_card_passward;
	}

	public int getDebit_card_expiration_date() {
		return debit_card_expiration_date;
	}

	public void setDebit_card_expiration_date(int debit_card_expiration_date) {
		this.debit_card_expiration_date = debit_card_expiration_date;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	
}
