package hu.mta.sztaki.lpds.cloud.simulator.iaas;

import java.util.Scanner;

public class virtualMachineComponent {

	// Setting all of the computer components to 0 so that when the components is present
	// the value will be changed to 1, this will be changed when the virtual machine is 
	// turned on and it will search for the components using another class
	
	// Next stage is to make if statements for all of the components inside of a computer
	// and create a way of detection.
	
	// Create a log in method for the administrator to be able to access the failure
	// detection software because all users will not need to use the failure detection
	// software. 
	
	int CPU = 0;
	int GPU = 0;
	int hardDrive = 0;
	int SSD = 0;  
	
	{
		
		// Start of the log in method for security of the failure detection to be accessed
		// only by the administrators of the system. Going to be hard-coded to begin but if
		// there is going to be a database connection there will be connections to a database
		
		System.out.println("Welcome to dissect-cf failure Detection: ");
		System.out.println("Please select one of the options below: ");
		
	
	if (GPU == 0) {
	
		System.out.print("GPU is either none existent or broken");
		
	} else {
		
		System.out.println("GPU is in a working condition");
		
	}
	
	if (CPU == 0) {
		
		System.out.print("GPU is either none existent or broken");
		
	} else {
		
		System.out.println("GPU is in a working condition");
		
	}
	
	if (hardDrive == 0) {
		
		System.out.print("GPU is either none existent or broken");
		
	} else {
		
		System.out.println("GPU is in a working condition");
		
	}
	
	if (SSD == 0) {
		
		System.out.print("GPU is either none existent or broken");
		
	} else {
		
		System.out.println("GPU is in a working condition");
		
	}
	
	
}
}

