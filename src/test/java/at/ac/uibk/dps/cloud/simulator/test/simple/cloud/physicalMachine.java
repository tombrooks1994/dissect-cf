
package at.ac.uibk.dps.cloud.simulator.test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

import hu.mta.sztaki.lpds.cloud.simulator.DeferredEvent;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.IaaSService;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.PhysicalMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.VMManager.VMManagementException;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.VirtualMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.constraints.AlterableResourceConstraints;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.constraints.ResourceConstraints;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.constraints.UnalterableConstraintsPropagator;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.pmscheduling.AlwaysOnMachines;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.pmscheduling.PhysicalMachineController;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.pmscheduling.SchedulingDependentMachines;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.resourcemodel.ResourceConsumption;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.vmscheduling.FirstFitScheduler;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.vmscheduling.NonQueueingScheduler;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.vmscheduling.Scheduler;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.vmscheduling.SmallestFirstScheduler;
import hu.mta.sztaki.lpds.cloud.simulator.io.Repository;
import hu.mta.sztaki.lpds.cloud.simulator.io.VirtualAppliance;
import hu.mta.sztaki.lpds.cloud.simulator.util.SeedSyncer;

public class physicalMachine extends IaaSRelatedFoundation {

	// Creation of the physical machine
	
	int requiredCores = 3, reqProcessing = 8, reqmem = 32, 
		reqond = 2 * (int) aSecond, reqoffd = (int) aSecond;
	
	if (requiredCores >= 3) {
		
		System.out.println("Physical Machine has the right amount of cores.")
	
	}	else {

		System.out.println("Please check the cores for correct amount.")
		
	}
	
	requiredDisk = new Repository(128, pmid, 256, 516, 16 
			new HashMap<String, Integer>());
	
	reqDisk = new Repository(123, pmid, 456, 789, 12,
			new HashMap<String, Integer>());
	
	reqDisk = requiredDisk; 
	
	pm = new PhysicalMachine((reqcores, reqProcessing, reqmem, reqDisk,
			reqond, reqoffd, defaultTransitions);
	
	
	
}

public PhysicalMachine(double cores,
		double perCoreProcessing,
		long memory,
		Repository disk, 
		int onD
		int offD
		EnumMap<Phyiscal.Machine.PowerStateKind,EnumMap<PhysicalMachine.State, 
		PowerState>> powerTransitions) {
	
	
}