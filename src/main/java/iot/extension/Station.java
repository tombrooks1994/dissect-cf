package iot.extension;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


import javax.xml.parsers.ParserConfigurationException;
import iot.extension.*;
import org.xml.sax.SAXException;

import hu.mta.sztaki.lpds.cloud.simulator.io.*;
import hu.mta.sztaki.lpds.cloud.simulator.io.NetworkNode.NetworkException;
import hu.mta.sztaki.lpds.cloud.simulator.*;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.IaaSService;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.VirtualMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.resourcemodel.*;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.resourcemodel.ResourceConsumption.ConsumptionEvent;

public class Station implements ConsumptionEvent{

	List<StorageObject> so;
	public static Repository repo;
	HashMap<String, Integer> lmap;
	int lat;
	String repoID;
	String toRepoID;
	VirtualMachine vm ;
	VirtualAppliance va;

	public Station() {
		
		//va =  new VirtualAppliance("BaseVA", 1000, 0);
		//vm = new VirtualMachine(va);
		so = new ArrayList<StorageObject>();
		lmap = new HashMap<String, Integer>();
		lat = 11;
		toRepoID="ceph";
		repoID = "StationRepo";
		lmap.put(repoID, lat);
		lmap.put(toRepoID, lat);
		repo = new Repository(600000000L, repoID, 100000L, 100000L, 100000L, lmap);
	}

	@Override
	public void conComplete() {
		//System.out.println("something happened!");
	}
	
	@Override
	public void conCancelled(ResourceConsumption problematic) {
		System.out.println("conCancelled was called!");
	}
	
	public void startCommunicate(IaaSService cloud,Repository repo) throws NetworkException{
		for(StorageObject so : repo.contents()){
		repo.requestContentDelivery(so.id, cloud.repositories.get(0), this);  
 		}
	}
	
	public static void Send(Station s,Cloud cloud,long t) throws NetworkException{
		long tt=100*1000;
		//long tt=24*60*60*1000;//one day in ms
		
		while(Timed.getFireCount()<(tt*t)){
			Metering m =new Metering(1);
			s.startCommunicate(cloud.is, Station.repo);
			Timed.simulateUntil(tt);
			//System.out.println(Timed.getFireCount());		
		}
		
	}

	public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, NetworkException{
		Cloud cloud = new Cloud();
		Station s = new Station();
		Send(s,cloud,1);
		System.out.println(cloud.is.toString()); 
	}



}
