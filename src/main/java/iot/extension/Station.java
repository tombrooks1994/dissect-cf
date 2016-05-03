package iot.extension;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import hu.mta.sztaki.lpds.cloud.simulator.io.*;
import hu.mta.sztaki.lpds.cloud.simulator.io.NetworkNode.NetworkException;
import hu.mta.sztaki.lpds.cloud.simulator.*;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.IaaSService;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.VirtualMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.resourcemodel.*;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.resourcemodel.ResourceConsumption.ConsumptionEvent;

public class Station extends DeferredEvent implements ConsumptionEvent{

	List<StorageObject> so;
	static Repository repo;
	HashMap<String, Integer> lmap;
	int lat;
	String repoID;
	String toRepoID;
	VirtualMachine vm ;
	VirtualAppliance va;

	public Station(long delay) {
		super(delay);
		va =  new VirtualAppliance("BaseVA", 1000, 0);
		vm = new VirtualMachine(va);
		so = new ArrayList<StorageObject>();
		lmap = new HashMap<String, Integer>();
		lat = 11;
		toRepoID="ceph";
		repoID = "StationRepo";
		lmap.put(repoID, lat);
		lmap.put(toRepoID, lat);
		repo = new Repository(600000000L, repoID, 100000L, 100000L, 100000L, lmap);
	}

	// function eventAction generates 1000 StorageObjects
	@Override
	protected void eventAction() {
		for(int i=0;i<1000;i++){
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yy:MM:dd:HH:mm:ss:SS");
			StorageObject so = new StorageObject(sdf.format(cal.getTime())+" ("+i+")", 256, false);
			repo.registerObject(so);
		}
	}
	
	@Override
	public void conComplete() {
		//System.out.println("something happend!");
	}
	
	@Override
	public void conCancelled(ResourceConsumption problematic) {
		System.out.println("conCancelled was called!");
	}
	
	public void startCommunicate(IaaSService cloud,Repository repo) throws NetworkException{
		// 2 repo: forras: repo cel: cloud.repositories.get(0);
		for(StorageObject so : repo.contents()){
		repo.requestContentDelivery(so.id, cloud.repositories.get(0), this);  // returns with boolean if the transfer successful	
		//Timed.simulateUntilLastEvent();
 		}
	}
	
	// iteration
	public static void generateSensorSystemIteration(Cloud cloud) throws NetworkException{
		int i=0;
		long tt=24*60*60*1000;//one day in ms
		Station[] s = new Station[6];
		//tick=ms 
		while(i<1000){
			for(int j=0;j<6;j++){
				s[j] = new Station(1);
				Timed.simulateUntilLastEvent();
				s[j].startCommunicate(cloud.is, s[j].repo);
				Timed.simulateUntilLastEvent();
			}
			i++;
		}
	}
	
	//simulated days
	public static void generateSensorSystemDays(Cloud cloud,long t) throws NetworkException{
		long tt=24*60*60*1000;//one day in ms
		Station[] s = new Station[6];
		//tick=ms 
		while(Timed.getFireCount()<(tt*t)){
			for(int j=0;j<6;j++){
				s[j] = new Station(1);
				Timed.simulateUntilLastEvent();
				s[j].startCommunicate(cloud.is, s[j].repo);
				Timed.simulateUntilLastEvent();
			}
		}
	}
	
	// main method for quick test
	public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, NetworkException{
		Cloud cloud = new Cloud();
		Station.generateSensorSystemDays(cloud,1);
		System.out.println(cloud.is.toString()); 
	}



}
