package iot.extension;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import hu.mta.sztaki.lpds.cloud.simulator.io.*;
import hu.mta.sztaki.lpds.cloud.simulator.io.NetworkNode.NetworkException;
import hu.mta.sztaki.lpds.cloud.simulator.util.PowerTransitionGenerator;
import hu.mta.sztaki.lpds.cloud.simulator.*;
import hu.mta.sztaki.lpds.cloud.simulator.energy.powermodelling.PowerState;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.PhysicalMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.resourcemodel.*;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.resourcemodel.ResourceConsumption.ConsumptionEvent;

public class Station extends Timed implements ConsumptionEvent {
	private Repository repo;
	private HashMap<String, Integer> lmap;
	private int lat;
	private String repoID;
	private String toRepoID;
	private String name;
	private PhysicalMachine pm;
	private boolean isWorking = true;
	public static ArrayList<Station> stations=new ArrayList<Station>();
	public static long SOnumber=0;
	public static final double minpower = 20;
	public static final double idlepower = 200;
	public static final double maxpower = 300;
	public static final double diskDivider = 10;
	public static final double netDivider = 20;

	public static EnumMap<PhysicalMachine.PowerStateKind, EnumMap<PhysicalMachine.State, PowerState>> defaultTransitions;
	static {
		try {
			defaultTransitions = PowerTransitionGenerator.generateTransitions(
					minpower, idlepower, maxpower, diskDivider, netDivider);
		} catch (Exception e) {
			throw new IllegalStateException(
					"Cannot initialize the default transitions");
		}
	}
	public Station(String name,final long freq) {
		this.startMeter(freq);
		this.name = name;
		lmap = new HashMap<String, Integer>();
		lat = 11;
		toRepoID = "ceph";
		repoID = "StationRepo"+" - "+this.name;
		lmap.put(repoID, lat);
		lmap.put(toRepoID, lat);
		repo = new Repository(600000000L, repoID, 100000L, 100000L, 100000L, lmap);
		pm = new PhysicalMachine(8, 0.00155875, 8000000000L, repo, 89000, 29000, defaultTransitions);
		stations.add(this);
	}
	
	public String getName() {
		return name;
	}
	public  Repository getRepo() {
		return this.repo;
	}

	/**
	 * A Station repo-jabol inditott requestContentDelivery altal kuldott SO
	 * sikeres megerkezeset jelzi
	 * @throws NetworkException 
	 */
	@Override
	public void conComplete()   {	
		System.out.println("conComplete has been called!");
		
	}

	/**
	 * A Station repo-jabol inditott requestContentDelivery altal kuldott SO
	 * sikertelen megerkezeset jelzi
	 */
	@Override
	public void conCancelled(ResourceConsumption problematic) {
		System.out.println("conCancelled has been called!");
	}

	private void startCommunicate() throws NetworkException {
		for (StorageObject so : repo.contents()) {
			repo.requestContentDelivery(so.id, Cloud.iaas.repositories.get(0), this);	
		}
	}

	public void startMeter(final long interval) {
		if(isWorking){
			subscribe(interval);
		}
	}
	
	public void stopMeter() {
		unsubscribe();
	}
	
	@Override
	public void tick(long fires) throws NetworkException {
		if(Timed.getFireCount()>5000L){
			this.stopMeter();
		}
		new Metering(this.name);
		this.startCommunicate();
	}
	
public static void main(String[] args) throws Exception{
		new Cloud(Cloud.v);
		Station s = new Station("stat-1",10);
		
		
		Timed.simulateUntilLastEvent();
		System.out.println(s.repo);
		System.out.println("~~~~~~~~~~~~");
		System.out.println(Cloud.iaas.repositories.toString());
		
	}
}
