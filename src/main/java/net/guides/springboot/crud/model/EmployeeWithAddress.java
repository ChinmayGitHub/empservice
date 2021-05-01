package net.guides.springboot.crud.model;

public class EmployeeWithAddress {
	
	private Employee employee;
	private String address;
	
	public EmployeeWithAddress(Employee employee, String address) {
		super();
		this.employee = employee;
		this.address = address;
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

	public void setAddress(String Address) {
		address = Address;
	}

	@Override
	public String toString() {
		return "EmployeeWithAddress [employee=" + employee + ", address=" + address + "]";
	}
	
}
