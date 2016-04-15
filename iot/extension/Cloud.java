package iot.extension;

import java.util.HashMap;

import at.ac.uibk.dps.cloud.simulator.test.PMRelatedFoundation;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.IaaSService;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.PhysicalMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.pmscheduling.SchedulingDependentMachines;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.vmscheduling.FirstFitScheduler;
import hu.mta.sztaki.lpds.cloud.simulator.io.Repository;
import hu.mta.sztaki.lpds.cloud.simulator.io.VirtualAppliance;

public class Cloud extends PMRelatedFoundation {

	public static IaaSService createMiniCloud(String cloudName, VirtualAppliance initialVA, long repoNBW, long repoDBW,
			int lat, double cores, long diskNBW, long diskDBW) throws Exception {
		IaaSService iaas = new IaaSService(FirstFitScheduler.class, SchedulingDependentMachines.class);
		HashMap<String, Integer> lmap = new HashMap<String, Integer>();
		String repoID = cloudName + "-Repo", machineID = cloudName + "-Machine";
		lmap.put(repoID, lat);
		lmap.put(machineID, lat);
		Repository repo = new Repository(6000000000000L, repoID, repoNBW, repoNBW, repoDBW, lmap);
		Repository disk = new Repository(6000000000000L, machineID, diskNBW, diskNBW, repoDBW, lmap);
		repo.registerObject(initialVA);
		iaas.registerRepository(repo);
		PhysicalMachine pm = new PhysicalMachine(cores, 1.0, 128000000000L, disk, 89000, 29000, defaultTransitions);
		iaas.registerHost(pm);
		return iaas;
	}
}
