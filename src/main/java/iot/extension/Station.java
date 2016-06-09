package iot.extension;

import java.util.HashMap;
import hu.mta.sztaki.lpds.cloud.simulator.io.*;
import hu.mta.sztaki.lpds.cloud.simulator.io.NetworkNode.NetworkException;
import hu.mta.sztaki.lpds.cloud.simulator.*;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.IaaSService;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.PhysicalMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.VirtualMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.VirtualMachine.State;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.constraints.AlterableResourceConstraints;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.resourcemodel.*;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.resourcemodel.ResourceConsumption.ConsumptionEvent;

public class Station implements ConsumptionEvent {
	private static Repository repo;
	private HashMap<String, Integer> lmap;
	private int lat;
	private String repoID;
	private String toRepoID;

	/*
	 * Konstruktor letrehozza a repo-t, egy halozatba szervezi a 'cloud'
	 * IaaS-szel Station ~= Szenzor
	 */
	public Station(Cloud cloud) {
		lmap = new HashMap<String, Integer>();
		lat = 11;
		toRepoID = "ceph";
		repoID = "StationRepo";
		lmap.put(repoID, lat);
		lmap.put(toRepoID, lat);
		repo = new Repository(600000000L, repoID, 100000L, 100000L, 100000L, lmap);
	}

	public static Repository getRepo() {
		return repo;
	}

	@Override
	public void conComplete() {
		// System.out.println("something happened!");
	}

	@Override
	public void conCancelled(ResourceConsumption problematic) {
		System.out.println("conCancelled was called!");
	}

	/*
	 * startCommunicate metodus, Station 'repo'-ja kozott a meres eredmenyeit
	 * elkuldi a 'cloud' IaaS repo-nak
	 */
	private void startCommunicate(IaaSService cloud, Repository repo) throws NetworkException {
		for (StorageObject so : repo.contents()) {
			repo.requestContentDelivery(so.id, cloud.repositories.get(0), this);
			repo.deregisterObject(so);
		}
	}

	/*
	 * SendToCloud metodus, 't' -szeres ideig mereseket general
	 */
	public void SendToCloud(Station s, Cloud cloud, long t, long d) throws NetworkException {
		long tt = 100 * 1000;
		// long tt=24*60*60*1000;//one day in ms
		while (Timed.getFireCount() < d + (tt * t)) {
			new Metering(1, 1024);
			s.startCommunicate(cloud.is, Station.repo);
			Timed.simulateUntil(d + tt * t);
		}

	}

	/*
	 * VMallocate statikus metodus, mely 'count' db VM-t hoz letre, majd
	 * szimulalja a transzfert a VM fele, es szamitasi feladatot ad a
	 * letrehozott VM-eknek
	 * 
	 */
	public static void VMallocate(Cloud cloud, VirtualAppliance va, int count) throws Exception {
		AlterableResourceConstraints arc = new AlterableResourceConstraints(8, 0.001, 16000000000L);
		cloud.is.requestVM(va, arc, cloud.is.machines.get(0).localDisk, count);
		Repository r = cloud.is.repositories.get(0);

		for (PhysicalMachine pm : cloud.is.machines) {
			for (VirtualMachine vm : pm.listVMs()) {
				while (!vm.getState().equals(State.RUNNING)) {
					Timed.simulateUntil(Timed.getFireCount() + 1);
				}

				ConsumptionEventAdapter e = new ConsumptionEventAdapter() {
					@Override
					public void conComplete() {
						System.out.println("readToMemory has ended");
					}
				};
				pm.localDisk.readToMemory(r.getMaxStorageCapacity() - r.getFreeStorageCapacity(), 1000, false, e);

				ConsumptionEventAdapter ce = new ConsumptionEventAdapter() {
					@Override
					public void conComplete() {
						System.out.println("VM computeTask has ended");
					}
				};
				vm.newComputeTask(10000000, ResourceConsumption.unlimitedProcessing, ce);
			}
		}
	}

	public static void main(String[] args) throws Exception {

		VirtualAppliance va = new VirtualAppliance("BaseVA", 1000, 0, false, 1000000000);
		Cloud cloud = new Cloud(va);

		for (int i = 0; i < 2; i++) {
			Station s = new Station(cloud);
			s.SendToCloud(s, cloud, 1, Timed.getFireCount());
		}
		VMallocate(cloud, cloud.getVa(), 6);
		Timed.simulateUntilLastEvent();

		System.out.println(cloud.is.repositories.toString());
		for (PhysicalMachine p : cloud.is.machines) {
			if (p.isHostingVMs()) {
				System.out.println(p);
			}
		}
	}
}
