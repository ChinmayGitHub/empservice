package net.guides.springboot.crud.model;

public class EmployeeWithAddressAndBU {
	
	private Employee employee;
	private String address;
	private String BU;
	
	public EmployeeWithAddressAndBU(Employee employee, String address, String bU) {
		super();
		this.employee = employee;
		this.address = address;
		BU = bU;
	}
	
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBU() {
		return BU;
	}
	public void setBU(String bU) {
		BU = bU;
	}

	@Override
	public String toString() {
		return "EmployeeWithAddressAndBU [employee=" + employee + ", address=" + address + ", BU=" + BU + "]";
	}

}
