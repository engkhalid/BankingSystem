import java.util.ArrayList;

public class Employer {
	private String company_name;
	private ArrayList<Integer> employees_IBAN= new ArrayList<Integer>(100);
	

	private int salary_amount;
	
	public Employer(String company_name, int salary_amount){
		this.company_name = company_name; 
		this.salary_amount = salary_amount;
	}
	
	public void setSalary_amount(int salary_amount) {
		this.salary_amount = salary_amount;
	}

	public int get_salary_amount() {
		return salary_amount;
	}
	
	public int get_employee_ID(Integer employee_IBAN){
		return employees_IBAN.indexOf(employee_IBAN);
	}
	
	public void add_emplyee(Integer new_employee_IBAN){
		employees_IBAN.add(new_employee_IBAN);
	}
	
	public void remove_emplyee(int employee_ID){
		employees_IBAN.remove(employee_ID);
	}
	
	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	
	public ArrayList<Integer> getEmployees_IBAN() {
		return employees_IBAN;
	}

}
