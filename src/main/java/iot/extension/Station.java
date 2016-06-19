package iot.extension;

import java.util.ArrayList;
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

/**
 * Station mely reprezental egy, az IoT vilagban megtalalhato allomast, mely
 * adatokat general szenzorjainak segitsegevel
 * 
 * @author Markus Andras - MAAVADT.SZE - Markus.Andras@stud.u-szeged.hu
 *
 */
public class Station implements ConsumptionEvent {
	public static long number = 0;
	private Repository repo;
	private HashMap<String, Integer> lmap;
	private int lat;
	private String repoID;
	private String toRepoID;
	public String name;
	public static ArrayList<Station> stations;

	/**
	 * Konstruktor letrehozza az adott Station repo-jat es egy halozatba
	 * szervezi a cloud-dal
	 * 
	 * @param cloud
	 *            tartalmazza az IaaS felhot
	 * @param name
	 *            egyedi nev a Station-nek
	 */
	public Station(Cloud cloud, String name) {
		this.name = name;
		lmap = new HashMap<String, Integer>();
		lat = 11;
		toRepoID = "ceph";
		repoID = "StationRepo";
		lmap.put(repoID, lat);
		lmap.put(toRepoID, lat);
		repo = new Repository(600000000L, repoID, 100000L, 100000L, 100000L, lmap);
	}

	/**
	 * Getter az adott Station repo-jahoz
	 */
	public Repository getRepo() {
		return this.repo;
	}

	/**
	 * A Station repo-jabol inditott requestContentDelivery altal kuldott SO
	 * sikeres megerkezeset jelzi
	 */
	@Override
	public void conComplete() {
		number--;
	}

	/**
	 * A Station repo-jabol inditott requestContentDelivery altal kuldott SO
	 * sikertelen megerkezeset jelzi
	 */
	@Override
	public void conCancelled(ResourceConsumption problematic) {
		System.out.println("conCancelled was called!");
	}

	/**
	 * Elinditja a kommunikaciot a felho es a repo kozott
	 * 
	 * @param cloud
	 *            tartalmazza az IaaS felhot
	 * @param repo
	 *            a Station helyi repo-ja
	 * @throws NetworkException
	 */
	private void startCommunicate(IaaSService cloud, Repository repo) throws NetworkException {
		for (StorageObject so : repo.contents()) {
			repo.requestContentDelivery(so.id, cloud.repositories.get(0), this);
			number++;
		}
	}

	/**
	 * Statikus aktuator szimulalalasa, mely modositja az adatok gyartasanak
	 * gyakorisagat
	 * 
	 * @param cloud
	 *            tartalmazza az IaaS felhot
	 * @param t
	 *            a szimulalt idotartam tick-ben
	 * @param simulatedTime
	 *            a szimulacio kezdetenek ideje,alapesetben Timed.getFireCount()
	 *            -vel erdemes inditani
	 * @param actuator
	 *            0,-1,1 erteket tartalmazo tomb, mely hatasara no (1) vagy
	 *            csokken (-1) a szenzorok szama
	 * @param filesize
	 *            a Metering altal generalt SO fajl meree
	 * @param sensornumber
	 *            a Station altal mukodtett szenzorok szama
	 * @throws NetworkException
	 */
	public static void SendToCloudWithActuator(Cloud cloud, long t, long simulatedTime, int actuator[], int filesize,
			int sensornumber) throws NetworkException {
		boolean validValue = true;
		long tt = 5000;
		if (actuator != null) {
			for (int i = 0; i < actuator.length; i++) {
				if (actuator[i] != 0 && actuator[i] != 1 && actuator[i] != -1) {
					validValue = false;
					System.out.println("rossz ertek");
				}
			}
		} else {
			validValue = false;
		}
		if (validValue == false) {
			Station.SendToCloud(cloud, t, Timed.getFireCount(), filesize, sensornumber);
		} else {
			if (actuator.length < 2) {
				Station.SendToCloud(cloud, t, Timed.getFireCount(), filesize, sensornumber);
			} else {
				long intervals = (tt * t) / actuator.length;

				for (int i = 0; i < actuator.length; i++) {

					if (actuator[i] == 1) {
						if (sensornumber < 50) {
							sensornumber += 2;
						}
					}
					if (actuator[i] == -1) {
						if (sensornumber > 2) {
							sensornumber -= 2;
						}
					}

					while (Timed.getFireCount() < simulatedTime + intervals * (i + 1)) {
						for (Station s : Scenario.stations) {
							for (int j = 0; j < sensornumber; j++) {
								// Timed.fire();
								new Metering(filesize, s.name, j);
							}
							s.startCommunicate(cloud.is, s.repo);
						}
						do {
							Timed.simulateUntil(Timed.getFireCount() + 1);
						} while (number != 0);
					}
				}
			}
		}
	}

	/**
	 * Statikus metodus, az osszes Stationt adatgeneralasra kenyszeriti
	 * 
	 * @param cloud
	 *            tartalmazza az IaaS felhot
	 * @param t
	 *            a szimulalt idotartam tick-ben
	 * @param simulatedTime
	 *            a szimulacio kezdetenek ideje,alapesetben Timed.getFireCount()
	 *            -vel erdemes inditani
	 * @param delay
	 *            Metering esemeny kesleltese
	 * @param filesize
	 *            a Metering altal generalt SO fajl meree
	 * @param sensornumber
	 *            a Station altal mukodtett szenzorok szama
	 * @throws NetworkException
	 */
	public static void SendToCloud(Cloud cloud, long t, long simulatedTime, int filesize, int sensornumber)
			throws NetworkException {
		long tt = 5000;
		// long tt=24*60*60*1000;//one day in ms
		while (Timed.getFireCount() < simulatedTime + (tt * t)) {
			for (Station s : Scenario.stations) {
				for (int i = 0; i < sensornumber; i++) {
					// Timed.fire();
					new Metering(filesize, s.name, i);
				}
				s.startCommunicate(cloud.is, s.repo);
			}
			// System.out.println(Timed.getFireCount());
			do {
				Timed.simulateUntil(Timed.getFireCount() + 1);
			} while (number != 0);
		}
	}

	/**
	 * Statikus metodus, mely letrehozza a VM-eket, azok szamara szamitasi
	 * feladatot indit a beerkezett adatok meretenek alapjan
	 * 
	 * @param cloud
	 *            az IaaS felhot tartalmazza
	 * @param va
	 *            alapesetben a Cloud (default) VA-jat erdemes atadni
	 * @param count
	 *            a VM-ek szama
	 * @throws Exception
	 */
	public static void VMallocate(Cloud cloud, VirtualAppliance va, int count) throws Exception {
		AlterableResourceConstraints arc = new AlterableResourceConstraints(8, 0.001, 16000000000L);
		VirtualMachine[] ArrayVM = new VirtualMachine[count];
		ArrayVM = cloud.is.requestVM(va, arc, cloud.is.machines.get(0).localDisk, count);
		Repository r = cloud.is.repositories.get(0);

		for (VirtualMachine vm : ArrayVM) {
			while (!vm.getState().equals(State.RUNNING)) {
				Timed.simulateUntil(Timed.getFireCount() + 1);
			}
			ConsumptionEventAdapter ce = new ConsumptionEventAdapter() {
				@Override
				public void conComplete() {
					System.out.println("VM computeTask has ended");
				}
			};
			vm.newComputeTask((r.getMaxStorageCapacity() - r.getFreeStorageCapacity()) / 1024 * 10000000,
					ResourceConsumption.unlimitedProcessing, ce);
		}
		for (PhysicalMachine pm : cloud.is.machines) {
			if (pm.isHostingVMs()) {
				ConsumptionEventAdapter e = new ConsumptionEventAdapter() {
					@Override
					public void conComplete() {
						System.out.println("readToMemory has ended");
					}
				};
				pm.localDisk.readToMemory(r.getMaxStorageCapacity() - r.getFreeStorageCapacity(), 1000, false, e);
			}

		}
	}
}
