package net.guides.springboot.crud.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.guides.springboot.crud.exception.ResourceNotFoundException;
import net.guides.springboot.crud.model.Employee;
import net.guides.springboot.crud.model.EmployeeWithAddress;
import net.guides.springboot.crud.model.EmployeeWithAddressAndBU;
import net.guides.springboot.crud.model.EmployeeWithBU;
import net.guides.springboot.crud.repository.EmployeeRepository;
import net.guides.springboot.crud.service.SequenceGeneratorService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class EmployeeController {
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;

	@GetMapping("/employees")
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Long employeeId)
			throws ResourceNotFoundException {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));
		return ResponseEntity.ok().body(employee);
	}
	
	@GetMapping("/employees/{id}/WithBU")
	public ResponseEntity<EmployeeWithBU> getEmployeeByIdWithBU(@PathVariable(value = "id") Long employeeId)
			throws ResourceNotFoundException, IOException {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response
		  = restTemplate.getForEntity("http://budataprovider:8081/BUdata" , String.class);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(response.getBody());
		JsonNode BU = root.path("bu_name");
		
		EmployeeWithBU employeeWithBU = new EmployeeWithBU(employee, BU.asText());
		return ResponseEntity.ok().body(employeeWithBU);
	}
	
	@GetMapping("/employees/{id}/WithAddress")
	public ResponseEntity<EmployeeWithAddress> getEmployeeByIdWithAddress(@PathVariable(value = "id") Long employeeId)
			throws ResourceNotFoundException, IOException {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response
		  = restTemplate.getForEntity("http://addresssupplier:3000/getAddress" , String.class);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(response.getBody());
		JsonNode address = root.path("address");
		
		EmployeeWithAddress employeeWithAddress = new EmployeeWithAddress(employee, address.asText());
		return ResponseEntity.ok().body(employeeWithAddress);
	}
	
	
	@GetMapping("/employees/{id}/WithAddressAndBU")
	public ResponseEntity<EmployeeWithAddressAndBU> getEmployeeByIdWithAddressAndBU(@PathVariable(value = "id") Long employeeId)
			throws ResourceNotFoundException, IOException {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response
		  = restTemplate.getForEntity("http://addresssupplier:3000/getAddress" , String.class);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(response.getBody());
		JsonNode address = root.path("address");
		
		RestTemplate restTemplate2 = new RestTemplate();
		ResponseEntity<String> response2
		  = restTemplate2.getForEntity("http://budataprovider:8081/BUdata" , String.class);
		ObjectMapper mapper2 = new ObjectMapper();
		JsonNode root2 = mapper2.readTree(response2.getBody());
		JsonNode BU = root2.path("bu_name");
		
		EmployeeWithAddressAndBU employeeWithAddressAndBU = new EmployeeWithAddressAndBU(employee, address.asText(),BU.asText());
		return ResponseEntity.ok().body(employeeWithAddressAndBU);
	}


	@PostMapping("/employees")
	public Employee createEmployee(@Valid @RequestBody Employee employee) {
		employee.setId(sequenceGeneratorService.generateSequence(Employee.SEQUENCE_NAME));
		return employeeRepository.save(employee);
	}

	@PutMapping("/employees/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") Long employeeId,
			@Valid @RequestBody Employee employeeDetails) throws ResourceNotFoundException {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

		employee.setEmailId(employeeDetails.getEmailId());
		employee.setLastName(employeeDetails.getLastName());
		employee.setFirstName(employeeDetails.getFirstName());
		final Employee updatedEmployee = employeeRepository.save(employee);
		return ResponseEntity.ok(updatedEmployee);
	}

	@DeleteMapping("/employees/{id}")
	public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") Long employeeId)
			throws ResourceNotFoundException {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

		employeeRepository.delete(employee);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
