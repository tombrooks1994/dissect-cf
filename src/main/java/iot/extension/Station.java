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
	private long time;
	private int filesize;
	private int sensornumber;
	private PhysicalMachine pm;
	private boolean isWorking = true;
	public static ArrayList<Station> stations=new ArrayList<Station>();
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
	/**
	 * Konstruktor meroallomas szimulalasahoz
	 * @param name allomasnev, egyedinek kell lennie
	 * @param freq allomas frekvenciaja
	 * @param time allomas mukodesi ideje
	 * @param sensornumber allomas szenzorainak a szama
	 * @param filesize szenzor altal mert adat merete
	 */
	public Station(String name,final long freq,long time,int sensornumber,int filesize,long maxinbw,long maxoutbw,long diskbw,String torepo) {
		this.startMeter(freq);
		this.name = name;
		this.filesize=filesize;
		this.time=time;
		this.sensornumber=sensornumber;
		lmap = new HashMap<String, Integer>();
		lat = 11;
		//toRepoID = "ceph";
		toRepoID = torepo;
		repoID = "StationRepo"+" - "+this.name;
		lmap.put(repoID, lat);
		lmap.put(toRepoID, lat);
		repo = new Repository(600000000L, repoID, maxinbw, maxoutbw, diskbw, lmap);
		pm = new PhysicalMachine(8, 0.00155875, 8000000000L, repo, 89000, 29000, defaultTransitions);
		
	}
	/*
	 *  Getterek&Setterek
	 */
	public long getTime() {
		return time;
	}
	public int getSensornumber() {
		return sensornumber;
	}
	public String getName() {
		return name;
	}
	public  Repository getRepo() {
		return this.repo;
	}

	/**
	 *  A Station repo-jabol inditott requestContentDelivery altal kuldott SO
	 * 	sikeres megerkezeset jelzi
	 */
	@Override
	public void conComplete()   {	
		//System.out.println("conComplete has been called!");
	}

	/**
	 * A Station repo-jabol inditott requestContentDelivery altal kuldott SO
	 * sikertelen megerkezeset jelzi
	 */
	@Override
	public void conCancelled(ResourceConsumption problematic) {
		System.out.println("conCancelled has been called!");
	}

	/**
	 * Ideiglenes! Kesobb: Parametere a cel repo
	 * @throws NetworkException
	 */
	private void startCommunicate(Repository r) throws NetworkException {
		for (StorageObject so : repo.contents()) {
			repo.requestContentDelivery(so.id, r, this);	
		}
	}

	/**
	 * Elinditja a Station mukodeset
	 * @param interval frekvencia
	 */
	private void startMeter(final long interval) {
		if(isWorking){
			subscribe(interval);
		}
	}
	/**
	 * leallitja a Station mukodeset
	 */
	private void stopMeter() {
		unsubscribe();
	}
	/**
	 * megkeresi a cel repository-t az iaas felhoben
	 * @param torepo a cel repo azonositoja
	 * @return
	 */
	private Repository findRepo(String torepo){
		Repository r=null;
		
		for(Repository tmp : Cloud.iaas.repositories){
			if(tmp.getName().equals(torepo)){
				r=tmp;
			}
			else{
				for(PhysicalMachine pm :Cloud.iaas.machines){
					if(pm.localDisk.getName().equals(torepo)){
						r=pm.localDisk;
					}
				}
			}
		}
		return r;
	}
	/**
	 *  Ha letelt a Station mukodesenek ideje, leallitja azt
	 *  Adatot gyujt & elkuldi a reponak
	 */
	@Override
	public void tick(long fires)  {
		if(Timed.getFireCount()>time){
			this.stopMeter();
		}
		for(int i=0;i<sensornumber;i++){
			new Metering(this.name,i,filesize);
		}
		try {
			Repository r = findRepo(this.toRepoID);
			if(r!=null){
				this.startCommunicate(r);
			}else{
				System.out.println("Nincs kapcsolat a repo-k kozott!");
			}
			
		} catch (NetworkException e) {
			e.printStackTrace();
		}
	}
	@Override
	public String toString() {
		return "Station [toRepoID=" + toRepoID + ", name=" + name + ", time=" + time + ", filesize=" + filesize
				+ ", sensornumber=" + sensornumber + "]";
	}

	
}
