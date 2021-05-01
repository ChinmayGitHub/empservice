package net.guides.springboot.crud.model;

public class EmployeeWithBU {
	
	private Employee employee;
	private String BU;
	
	public EmployeeWithBU(Employee employee, String bU) {
		super();
		this.employee = employee;
		BU = bU;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public String getBU() {
		return BU;
	}

	public void setBU(String bU) {
		BU = bU;
	}

	@Override
	public String toString() {
		return "EmployeeWithBU [employee=" + employee + ", BU=" + BU + "]";
	}
	
}
