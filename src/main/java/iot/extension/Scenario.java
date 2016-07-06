package iot.extension;

import java.util.ArrayList;
import hu.mta.sztaki.lpds.cloud.simulator.Timed;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.PhysicalMachine;
import hu.mta.sztaki.lpds.cloud.simulator.io.VirtualAppliance;

public class Scenario {
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
	public Scenario(VirtualAppliance va, int filesize, String datafile) throws Exception {
		
		boolean isReady = true;
		
		if (va == null) {
			 new Cloud(Cloud.v); // IaaS letrehozasa defaulta VA-val
		} else {
			new Cloud(va);
		}
		if (datafile.isEmpty()) {
			isReady = false;
			System.out.println("Datafile nem lehet null");
		} else {
			ReadXML.ReadFromXML( datafile,filesize);
		}

		if (filesize <= 0) {
			System.out.println("Filesize nem lehet 0-nal kisebb");
			isReady = false;
		}

		if (isReady) {
			
			
			Timed.simulateUntilLastEvent();

			System.out.println("~~~~~~~~~~~~");
			System.out.println(Cloud.iaas.repositories.toString());
			System.out.println("~~~~~~~~~~~~");
			for (PhysicalMachine p : Cloud.iaas.machines) {
				if (!p.isHostingVMs()) {
					System.out.println(p);
				}
			}
			System.out.println("~~~~~~~~~~~~");
			for(Station s : Station.stations){
				System.out.println(s.toString());
			}
		} else {
			System.out.println("Scenario-t nem lehet vegrehajtani!");
		}
	}

}
