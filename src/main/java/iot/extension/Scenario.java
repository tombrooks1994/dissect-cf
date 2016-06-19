package iot.extension;

import java.util.ArrayList;
import hu.mta.sztaki.lpds.cloud.simulator.Timed;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.PhysicalMachine;
import hu.mta.sztaki.lpds.cloud.simulator.io.VirtualAppliance;

public class Scenario {
	public static ArrayList<Station> stations = null;

	/**
	 * Konstruktorral indithato szimulacio
	 * 
	 * @param va
	 *            VirtualApplience, ha null akkor a Cloud default VA-jat
	 *            hasznalja
	 * @param delay
	 *            Metering kesleltese
	 * @param filesize
	 *            Metering altal letrehozott fajl merete
	 * @param datafile
	 *            A Station-oket tartalmazo XML fajl helye a rendszerben
	 * @param actuators
	 *            Statikus aktuator betoltse, null, ha nincs
	 * @param VMnumber
	 *            a VM szama
	 * @param t
	 *            a szimulalt ido
	 * @param sensornumber
	 *            a Station szenzorainak a szama
	 * @throws Exception
	 */
	public Scenario(VirtualAppliance va, int filesize, String datafile, int[] actuators, int VMnumber, int t,
			int sensornumber) throws Exception {
		Cloud cloud;
		boolean isReady = true; // bool valtozo, hogy ha sikeres a scenario
								// letrehozasa elinditja a szimulaciot
		if (va == null) {
			cloud = new Cloud(Cloud.v); // IaaS letrehozasa defaulta VA-val
		} else {
			cloud = new Cloud(va);
		}
		if (datafile.isEmpty()) {
			isReady = false;
			System.out.println("Datafile nem lehet null");
		} else {
			stations = ReadXML.ReadFromXML(cloud, datafile);
		}

		if (filesize <= 0) {
			System.out.println("Filesize nem lehet 0-nal kisebb");
			isReady = false;
		}

		
		if (VMnumber <= 0) {
			System.out.println("VMnumber nem lehet 0-nal kisebb");
			isReady = false;
		}
		if (sensornumber <= 0) {
			System.out.println("sensornumber nem lehet 0-nal kisebb");
			isReady = false;
		}

		if (isReady) {

			Station.SendToCloudWithActuator(cloud, t, Timed.getFireCount(),actuators, filesize, sensornumber);
			Station.VMallocate(cloud, cloud.getVa(), VMnumber);
			Timed.simulateUntilLastEvent();

			System.out.println("~~~~~~~~~~~~");
			System.out.println(cloud.is.repositories.toString());
			System.out.println("~~~~~~~~~~~~");
			for (PhysicalMachine p : cloud.is.machines) {
				if (p.isHostingVMs()) {
					System.out.println(p);
				}
			}
			System.out.println("~~~~~~~~~~~~");
		} else {
			System.out.println("Scenario-t nem lehet vegrehajtani!");
		}
	}

}
