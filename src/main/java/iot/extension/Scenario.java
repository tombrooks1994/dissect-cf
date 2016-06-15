package iot.extension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import hu.mta.sztaki.lpds.cloud.simulator.Timed;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.PhysicalMachine;
import hu.mta.sztaki.lpds.cloud.simulator.io.VirtualAppliance;

public class Scenario {
	private ArrayList<Station> stations;
	private int delay;
	private int filesize;
	private int[] actuators;
	
	public Scenario(VirtualAppliance va,int delay,int filesize,String datafile,int[] actuators,int VMnumber,int t) throws Exception{
		Cloud cloud;
		boolean izReady=true; // bool valtozo, hogy ha sikeres a scenario letrehozasa elinditja a szimulaciot
		if(va==null){
			cloud = new Cloud(Cloud.v); // IaaS letrehozasa defaulta VA-val
		}
		else{
			 cloud = new Cloud(va);
		}
		if(datafile.isEmpty()){
			izReady=false;
			System.out.println("Datafile nem lehet null");
		}
		else{
			stations=ReadXML.ReadFromXML(cloud,datafile);
		}
		this.filesize=filesize;
		if(filesize<=0){
			System.out.println("Filesize nem lehet 0-nal kisebb");
			izReady=false;
		}
		this.delay=delay;
		if(delay<=0){
			System.out.println("Delay nem lehet 0-nal kisebb");
			izReady=false;
		}
		if(VMnumber<=0){
			System.out.println("VMnumber nem lehet 0-nal kisebb");
			izReady=false;
		}
		this.actuators=actuators;
		
		if(izReady){
			for(Station s : stations){
				
				s.SendToCloudWithActuator(s, cloud, t, Timed.getFireCount(),actuators,delay,filesize);
				System.out.println(Timed.getFireCount());
				
				
				System.out.println("in progress: "+s.name);
			}
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
		}
		else{
			System.out.println("Scenario-t nem lehet vegrehajtani!");
		}
	}
}
