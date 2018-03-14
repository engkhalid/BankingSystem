
public class Bank_Account_Holder {
	private String name;
	private int bank_account_IBAN;
	private String bedit_card_passward;
	private int bedit_card_expiration_date;
	private String phone_number; 
	private String company_working_in;
	private int employee_ID;
		
	public Bank_Account_Holder(String name,int bank_account_IBAN,String bedit_card_passward,int bedit_card_expiration_date,
							   String phone_number, String company_working_in,int employee_ID){
		setName(name);
		setBank_account_IBAN(bank_account_IBAN);
		setBedit_card_passward(bedit_card_passward);
		setBedit_card_expiration_date(bedit_card_expiration_date);
		setPhone_number(phone_number);
		set_company_working_in(company_working_in, employee_ID);
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

	public String getBedit_card_passward() {
		return bedit_card_passward;
	}

	public void setBedit_card_passward(String bedit_card_passward) {
		this.bedit_card_passward = bedit_card_passward;
	}

	public int getBedit_card_expiration_date() {
		return bedit_card_expiration_date;
	}

	public void setBedit_card_expiration_date(int bedit_card_expiration_date) {
		this.bedit_card_expiration_date = bedit_card_expiration_date;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	
}
