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
	
	// Need to test for failures in more than just an average desktop going to be testing
	// for other devices such as tablets, phones, networks and networking devices.
	
	int CPU = 0; 
	int GPU = 0; // Up to 2 for testing purposes
	int hardDrive = 0; // Up to 10 for testing purposes (4 for each device)
	int SSD = 0;  // Up to 10 for testing purposes 
	int RAM = 0; // Goes up to 4 per device
	int powerSupply = 0;
	int moBo = 0;
	int fans = 0; // up to 4, can be more for testing
	int wireless = 0;
	int ethernet = 0; 
	int audio = 0; // speakers or other output devices
	int USB = 0; // depending on how many
	int display = 0; // depending on port VGA, HDMI, DVI
	
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
		
		System.out.print("CPU is either none existent or broken");
		
	} else {
		
		System.out.println("CPU is in a working condition");
		
	}
	
	if (hardDrive == 0) {
		
		System.out.print("Hard Drive is either none existent or broken");
		
	} else {
		
		System.out.println("Hard Drive is in a working condition");
		
	}
	
	if (SSD == 0) {
		
		System.out.print("SDD is either none existent or broken");
		
	} else {
		
		System.out.println("SSD is in a working condition");
		
	}
	
	if (moBo != 0) {
		
		boolean deviceFailed = false;
		
		if (deviceFailed == false) {
			
			System.out.println("Motherboard is working correctly, test for other failures!");
			
		} else {
			
			if (deviceFailed == true) {
				
			System.out.println("Motherboard failed, please replace motherboard!");
				
			}
			
		}
		
	}
	
}
}

