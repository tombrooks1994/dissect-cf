package iot.extension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import hu.mta.sztaki.lpds.cloud.simulator.io.*;
import hu.mta.sztaki.lpds.cloud.simulator.io.NetworkNode.NetworkException;
import hu.mta.sztaki.lpds.cloud.simulator.*;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.IaaSService;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.PhysicalMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.VirtualMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.constraints.AlterableResourceConstraints;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.resourcemodel.*;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.resourcemodel.ResourceConsumption.ConsumptionEvent;

public class Station implements ConsumptionEvent{

	List<StorageObject> so;
	public static Repository repo;
	HashMap<String, Integer> lmap;
	int lat;
	String repoID;
	String toRepoID;
	public static VirtualAppliance va;
	
	
	public Station(Cloud cloud) {
		//va =  new VirtualAppliance("BaseVA", 1000, 0,false, 1073741824);
		va =  new VirtualAppliance("BaseVA", 1000, 0,false, 1000000000);
		cloud.is.machines.get(0).localDisk.registerObject(va);
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
	
	public static void SendToCloud(Station s,Cloud cloud,long t) throws NetworkException{
		long tt=100*1000;
		//long tt=24*60*60*1000;//one day in ms
		while(Timed.getFireCount()<(tt*t)){
			Metering m =new Metering(1);
			s.startCommunicate(cloud.is, Station.repo);
			Timed.simulateUntil(tt*t);
		}
		
	}
	
	

	
	public  void VMallocate(Cloud cloud,VirtualAppliance va,int count) throws Exception{
		AlterableResourceConstraints arc = new AlterableResourceConstraints(8,0.001,16000000000L);
		cloud.is.requestVM(va, arc,cloud.is.machines.get(0).localDisk, count);	
		Repository r = cloud.is.repositories.get(0);
		for(PhysicalMachine pm : cloud.is.machines){
		//	if(!pm.listVMs().isEmpty()){
			for(VirtualMachine vm : pm.listVMs()){
				for(StorageObject so : r.contents()){
					r.requestContentDelivery(so.id, pm.localDisk, this);  
			 	}
				ConsumptionEventAdapter ce = new ConsumptionEventAdapter() {
					@Override
					public void conComplete() {
						System.out.println("VM computeTask has ended");
					}
				};
				
				vm.newComputeTask(10000000, ResourceConsumption.unlimitedProcessing, ce);
				Timed.simulateUntil(Timed.getFireCount()+10000000L);
				
			}		
		}
		

	}
	
	public static void main(String[] args) throws Exception{
		Cloud cloud = new Cloud();
		Station s = new Station(cloud);
		SendToCloud(s,cloud,1);
		s.VMallocate(cloud, Station.va, 7);
		
		Timed.simulateUntilLastEvent();
		System.out.println(cloud.is.repositories.toString()); 
		for(PhysicalMachine p : cloud.is.machines){
			if(p.isHostingVMs()){ System.out.println(p) ;}
		}
	}
}
