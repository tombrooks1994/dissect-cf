package iot.extension;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.spi.ResourceBundleControlProvider;

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
		lat = 5;
		toRepoID="ceph";
		repoID = "StationRepo";
		lmap.put(repoID, lat);
		lmap.put(toRepoID, lat);
		repo = new Repository(600000000L, repoID, 100000L, 100000L, 100000L, lmap);
	}

	@Override
	protected void eventAction() {
		
		for(int i=0;i<1000;i++){
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yy:MM:dd:HH:mm:ss:SS");
			StorageObject so = new StorageObject(sdf.format(cal.getTime())+" ("+i+")", 256, false);
			repo.registerObject(so);
		
			//System.out.println(so.toString() + "  .:. "+ repo.toString());
			
		}
	}
	
	@Override
	public void conComplete() {
		System.out.println("something happend!");
		
	}

	@Override
	public void conCancelled(ResourceConsumption problematic) {
		System.out.println("conCancelled was called!");
		
	}
	
	public void startCommunicate(IaaSService cloud,Repository repo) throws NetworkException{
		// 2 repo: forras: repo cel: cloud.repositories.get(0);
			
			for(StorageObject so : repo.contents()){
			repo.requestContentDelivery(so.id, cloud.repositories.get(0), this);  // returns with boolean if the transfer successful
 		}
		
		
		    
		          
	}
	
	public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, NetworkException{
		Station s = new Station(1);
		Timed.simulateUntilLastEvent();
		Cloud cloud = new Cloud();
		s.startCommunicate(cloud.is, s.repo);	
		Timed.simulateUntilLastEvent();
	}



}
