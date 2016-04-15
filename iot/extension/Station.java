package iot.extension;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.junit.Assert;

import at.ac.uibk.dps.cloud.simulator.test.ConsumptionEventAssert;
import hu.mta.sztaki.lpds.cloud.simulator.Timed;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.IaaSService;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.resourcemodel.ResourceConsumption;
import hu.mta.sztaki.lpds.cloud.simulator.io.NetworkNode;
import hu.mta.sztaki.lpds.cloud.simulator.io.Repository;
import hu.mta.sztaki.lpds.cloud.simulator.io.StorageObject;
import hu.mta.sztaki.lpds.cloud.simulator.io.VirtualAppliance;

public class Station extends Timer {
	private HashMap<String, Integer> lmap;
	private String repoID;
	private int lat;
	//private Sensor[] sensors;
	public Repository repo;
	protected List<StorageObject> so_list;
	
	public Station(){
		 lmap = new HashMap<String, Integer>();
		 repoID = "Repo";
		 lat=11;
		 lmap.put(repoID, lat);
		 //sensors = new Sensor[6];
		 repo = new Repository(600000000L, repoID, 100000L, 100000L, 100000L, lmap);
		 so_list= new ArrayList<StorageObject>();
	}
	

	public void task(long minute) throws InterruptedException {
		long time = minute * 60 * 1000;
		Calendar cal = Calendar.getInstance();
		long startTime = cal.getTimeInMillis();
		long endTime = cal.getTimeInMillis();

		while ((endTime - startTime) < time) {
			cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yy:MM:dd:HH:mm:ss:SS");
			StorageObject so = new StorageObject(" " + sdf.format(cal.getTime()), 256, false);
			so_list.add(so);
			System.out.println(so.toString() + " : " + so_list.size());
			Thread.sleep(5);
			endTime = cal.getTimeInMillis();
		}
	}
	public List<StorageObject> getSo_list() {
		return so_list;
	}
	public void setSo_list(List<StorageObject> so_list) {
		this.so_list = so_list;
	}
	public  void mete() {
		Timer t = new Timer();

		t.schedule(new TimerTask() {

			public void run() {
				Calendar cal = Calendar.getInstance();

				SimpleDateFormat sdf = new SimpleDateFormat("yy:MM:dd:HH:mm:ss");
				StorageObject so = new StorageObject(" " + sdf.format(cal.getTime()), 256, false);
				so_list.add(so);
				System.out.println(so.toString());
				System.out.println(so_list.size());
			}

		}, 0, 2000);
	}

	public static void main(String[] args) throws Exception{
		Station st =  new Station();
		//Cloud cloud = new Cloud();
		VirtualAppliance va = new VirtualAppliance("a", 1000, 0);
	//	IaaSService iaas = Cloud.createMiniCloud("TestCloud", va, 250000L , 100000L, 11, 48.0, 125000L, 50000L);
	//	Repository cel_repo = iaas.repositories.get(0); //cel repo
		 int targetlat = 3; // ticks
		 int sourcelat = 2; // ticks
		 String sourceName = "Source";
		 String targetName = "Target";
		 HashMap<String, Integer> lm = new HashMap<String, Integer>();
			lm.put(sourceName, sourcelat);
			lm.put(targetName, targetlat);
		//general so-kat
		st.task(1);
		
		//regisztralja az objektet a repoba
		for(StorageObject so : st.getSo_list()){
			st.repo.registerObject(so);
		}
		System.out.println(st.repo.toString());
	
		// atvitel
	/*	for (StorageObject so : st.repo.contents()) {
			st.repo.requestContentDelivery(so.id, cel_repo, new ConsumptionEventAssert() {
				public void conComplete() {
					super.conComplete();
				}
			});


			NetworkNode source = new NetworkNode(sourceName, 100000, 100000, 5000, lm);
			NetworkNode target = new NetworkNode(targetName, 100000, 100000, 5000, lm);
			
			NetworkNode.initTransfer(so.size, ResourceConsumption.unlimitedProcessing, source, target, new ConsumptionEventAssert(Timed.getFireCount()));
			//NetworkNode asd = new NetworkNode.SingleTransfer();
			
		}
		
		*/
	}

	
	
	
}
