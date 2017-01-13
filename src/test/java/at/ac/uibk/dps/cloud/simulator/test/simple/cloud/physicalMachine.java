package at.ac.uibk.dps.cloud.simulator.test.simple.cloud;


import java.io.File;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Assert;

import hu.mta.sztaki.lpds.cloud.simulator.iaas.IaaSService;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.pmscheduling.AlwaysOnMachines;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.vmscheduling.FirstFitScheduler;
import hu.mta.sztaki.lpds.cloud.simulator.util.CloudLoader;

/**
 * This class is for the creation of a physical machine. There is a physical machine
 * component helper at the top to show the user what the minimum and maximum values
 * are for each component in the physical machine. The 2nd stage of this class is 
 * creating 1000 physical machines with randomised values from a set array which is
 * commented right the way through this file
 * 
 * @author 
 * 		{Thomas Brooks, Liverpool John Moores University, Computer Foresnics Undergraduate}
 * @Date 
 * 		{2nd December 2016}
 * @Project
 * 		{Failure trace}
 * @E-mail
 * 		{t.s.brooks@2013.ljmu.ac.uk}
 * 
 */


public class physicalMachine {

	/**
	 * @param args
	 * @throws Exception
	 */
	
	public static void main(String[] args) throws Exception {
		
		
	
	/**
	 * 2nd stage creates the 1000 physical machines using the array's that are created
	 * above. The helper above just sets example minimums and maximums there will be
	 * different values used in the actual PM creator. 
	 * */	
		
		/** Start of the physical machine creator */
			
		/** 
		 * These array below which are numbers are for the different values set
		 * in the component helper such as cores, memory, disk space and power. 
		 * These are all measure in the same way that the helper is measured in.		 * 
		 */
	
		
		/** Cores Array */
	    List<String> cores = new ArrayList<String>();
	    cores.add("32");
	    cores.add("64");
	    cores.add("128");
	    
	    /** Memory Array */
	    List<String> memory = new ArrayList<String>();
	    memory.add("8000");
	    memory.add("1600");
	    memory.add("24000");
	    memory.add("32000");
	    
	    /** Disk Space Array */	    
	    List<String> diskSpace = new ArrayList<String>();
	    diskSpace.add("500000");
	    diskSpace.add("1000000");
	    diskSpace.add("2000000");
	    diskSpace.add("3000000");
	    
	    /** Power Array */	    
	    List<String> power = new ArrayList<String>();
	    power.add("0.9");
	    power.add("2.5");
	    power.add("5.0");
	    power.add("10.0");
	    
	    /** Hard drives array */
	    List<String> hD = new ArrayList<String>();
	    hD.add("1000000");
	    hD.add("2000000");
	    hD.add("3000000");
	    hD.add("4000000");
	    
	    /** Motherboard Array */
	    /** There will be different types of motherboards added at a later date. */
	    List<String> mobo = new ArrayList<String>();
	    mobo.add("Type 1"); 
	    mobo.add("Type 2");
	    mobo.add("Type 3");
	    mobo.add("Type 4");
	    
	    /**
	     * A normal speed for latency is  5 and 40 ms
	     * This array below differenciates the different speeds and
	     * distributes them over the 1000 physical machines 
	     * */
	    
	    List<String> latency = new ArrayList<String>();
	    latency.add("5");
	    latency.add("10");
	    latency.add("15");
	    latency.add("20");
	    latency.add("25");
	    latency.add("30");
	    latency.add("35");
	    latency.add("40");
	    
	    /**
	     * Power max array 
	     * 250w-750w*/
	    	    
	    List<String> maxPowerPM = new ArrayList<String>();
	    maxPowerPM.add("250");
	    maxPowerPM.add("350");
	    maxPowerPM.add("450");
	    maxPowerPM.add("550");
	    maxPowerPM.add("650");
	    maxPowerPM.add("750");
	    
	    /**
	     * Power max array 
	     * 25w-75w*/
	    
	    List<String> idlePowerPM = new ArrayList<String>();
	    idlePowerPM.add("25");
	    idlePowerPM.add("35");
	    idlePowerPM.add("45");
	    idlePowerPM.add("55");
	    idlePowerPM.add("65");
	    idlePowerPM.add("75");
	    
	    /**
	     * speed of the processor is counted in MHz meaning 1MHz = 1 cycle per second 
	     * */
	    List<String> processSpeed = new ArrayList<String>();
	    processSpeed.add("1000");
	    processSpeed.add("2000");
	    processSpeed.add("4000");
	    processSpeed.add("6000");
	    
	    /**
	     * speed of bandwidth is measured in bits per seconds 
	     * */
	    
	    List<String> inBW = new ArrayList<String>();
	    inBW.add("100000");
	    inBW.add("200000");
	    inBW.add("300000");
	    inBW.add("400000");
	    
	    List<String> outBW = new ArrayList<String>();
	    outBW.add("100000");
	    outBW.add("200000");
	    outBW.add("300000");
	    outBW.add("400000");
	    
	    List<String> diskBW = new ArrayList<String>();
	    diskBW.add("50000");
	    diskBW.add("100000");
	    diskBW.add("200000");
	    diskBW.add("300000");
	    
	    /** 
	     * End of bandwidth section
	     * */
	    
	    /**
	     * Repository ID = randomly named.
	     * */
	    
	    List<String> resp = new ArrayList<String>();
	    resp.add("hydrogen");
	    resp.add("carbon");
	    resp.add("neon");
	    resp.add("kepler");
	    
	    /**
	     * End of repository array
	     * */
	    
	    /**
	     * This is the final stage of creation of the physical machines
	     * There is use of the Random(); function which is preinstalled into
	     * java. There is then a loop that iterates through the List Arrays
	     * and randomly picks a value from each one and returns the values
	     * In the output there is names for each component. The for loop iterates
	     * through the arrays 1000 times and chooses 1000 random values to generate
	     * 1000 physical machines.
	     * */
	    try (FileWriter file = new FileWriter("PM.txt")) {
	    	  
	    Random random = new Random();

	    for (int x = 0; x < 1000; x++) {
	        
	        String newxml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			+ "<cloud id=\"oxygen\"	scheduler=\"hu.mta.sztaki.lpds.cloud.simulator.iaas.vmscheduling.FirstFitScheduler\" pmcontroller=\"hu.mta.sztaki.lpds.cloud.simulator.iaas.pmscheduling.AlwaysOnMachines\">\n"
			
			+ "<machine id=\"" + (x+1) + "\" cores=\"" + (cores.get(random.nextInt(cores.size()))) 
			+"\" processing=\"" + (processSpeed.get(random.nextInt(processSpeed.size()))) 
			+ "\" memory=\"" + (diskSpace.get(random.nextInt(diskSpace.size()))) + "\">\n"
			
			+ "<powerstates kind=\"host\">\n"
			
			+ "<power	model=\"hu.mta.sztaki.lpds.cloud.simulator.energy.powermodelling.LinearConsumptionModel\" "
			+ "idle=\"" + idlePowerPM.get(random.nextInt(idlePowerPM.size())) 
			+ "\" max=\"" + maxPowerPM.get(random.nextInt(maxPowerPM.size())) 
			+ "\" inState=\"default\" />\n"
			
			+ "<power	model=\"hu.mta.sztaki.lpds.cloud.simulator.energy.powermodelling.ConstantConsumptionModel\" "
			+ "idle=\"" + idlePowerPM.get(random.nextInt(idlePowerPM.size())) 
			+ "\" max=\"" + maxPowerPM.get(random.nextInt(maxPowerPM.size())) + "\" inState=\"OFF\" />\n"
			+ "</powerstates>\n"
			
			+ "<statedelays startup=\"89000\" shutdown=\"29000\" />\n"
			
			+ "<repository id=\"disk\"capacity=\"" + diskSpace.get(random.nextInt(diskSpace.size())) 
			+ "\" inBW=\"" + inBW.get(random.nextInt(inBW.size())) + "\" outBW=\"" 
			+ outBW.get(random.nextInt(outBW.size())) + "\" diskBW=\"" + diskBW.get(random.nextInt(diskBW.size())) + "\">\n"
			
			+ "<powerstates kind=\"storage\">\n"
			+ "<power model=\"hu.mta.sztaki.lpds.cloud.simulator.energy.powermodelling.LinearConsumptionModel\" "
			+ "idle=\"" + idlePowerPM.get(random.nextInt(idlePowerPM.size())) + "\" "
					+ "max=\"" + maxPowerPM.get(random.nextInt(maxPowerPM.size()))  + "\" inState=\"default\" />\n"
			+ "<power model=\"hu.mta.sztaki.lpds.cloud.simulator.energy.powermodelling.ConstantConsumptionModel\" idle=\"0\" max=\"0\" inState=\"OFF\" />\n"
			+ "</powerstates>\n"
			
			+ "<powerstates kind=\"network\">\n"
			+ "<power model=\"hu.mta.sztaki.lpds.cloud.simulator.energy.powermodelling.LinearConsumptionModel\" "
			+ "idle=\"" + idlePowerPM.get(random.nextInt(idlePowerPM.size())) + "\" "
					+ "max=\"" + maxPowerPM.get(random.nextInt(maxPowerPM.size()))  +  "\" inState=\"default\" />\n"
			+ "<power model=\"hu.mta.sztaki.lpds.cloud.simulator.energy.powermodelling.ConstantConsumptionModel\" idle=\"0\" max=\"0\" inState=\"OFF\" />\n"
			+ "</powerstates>\n"
			
			+ "<latency towards=\"repo\" value=\"" + latency.get(random.nextInt(latency.size()))  + "\" />\n"
			+ "</repository>\n"
			+ "</machine>\n"
			
			+ "<repository id=\"repo\" capacity=\"" + diskSpace.get(random.nextInt(diskSpace.size())) 
			+ "\" inBW=\"" + inBW.get(random.nextInt(inBW.size())) + "\" outBW=\"" 
			+ outBW.get(random.nextInt(outBW.size())) + "\" diskBW=\"" + diskBW.get(random.nextInt(diskBW.size())) + "\">\n"
			
			+ "<powerstates kind=\"storage\">\n"
			+ "<power model=\"hu.mta.sztaki.lpds.cloud.simulator.energy.powermodelling.LinearConsumptionModel\" "
			+ "idle=\"" + idlePowerPM.get(random.nextInt(idlePowerPM.size())) + "\" max=\"" 
			+ maxPowerPM.get(random.nextInt(maxPowerPM.size())) 
			+ "\" inState=\"default\" /> \n"
			+ "<power model=\"hu.mta.sztaki.lpds.cloud.simulator.energy.powermodelling.ConstantConsumptionModel\" idle=\"0\" max=\"0\" inState=\"OFF\" />\n"
			+ "</powerstates>\n"
			
			+ "<powerstates kind=\"network\">\n"
			+ "<power model=\"hu.mta.sztaki.lpds.cloud.simulator.energy.powermodelling.LinearConsumptionModel\" "
			+ "idle=\"" + idlePowerPM.get(random.nextInt(idlePowerPM.size())) + "\" max=\"" 
			+ maxPowerPM.get(random.nextInt(maxPowerPM.size())) 
			+ "\" inState=\"default\" /> \n"
			
			+ "<power model=\"hu.mta.sztaki.lpds.cloud.simulator.energy.powermodelling.ConstantConsumptionModel\" idle=\"0\" max=\"0\" inState=\"OFF\" />\n"
			+ "</powerstates>\n" 
			
			+ "<latency towards=\"disk\" value=\"" + latency.get(random.nextInt(latency.size()))  + "\" />\n"
			+ "</repository>\n" + "</cloud>\n\n\n";
	        
	        file.write(newxml);
	        
	        System.out.println(newxml);

	    }
	    
	    }

	    /** End of physical machine creator */
	}
}
