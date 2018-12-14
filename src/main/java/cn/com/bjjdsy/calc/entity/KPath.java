package cn.com.bjjdsy.calc.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KPath {

	private String fromStation;
	private String toStation;
	private String ksn;
	private String kpath;
	private String transferStation;
	private Map<String, TransferStation> transferStationMap;// key:
	private List<KPath> subPaths;

	// for sub path
	private int direct;
	private List<TransferStation> transferStations;
	private List<String> stationCodes;
	private int time;
	private int oTime;
	private int dTime;
	private int tTime;

	public KPath() {
		this.transferStations = new ArrayList<>();
		this.stationCodes = new ArrayList<>();
		this.transferStationMap = new HashMap<>();
	}

	public List<TransferStation> getTransferStations() {
		return transferStations;
	}

	public void setTransferStations(List<TransferStation> transferStations) {
		this.transferStations = transferStations;
	}

	public int getDirect() {
		return direct;
	}

	public void setDirect(int direct) {
		this.direct = direct;
	}

	public String getFromStation() {
		return fromStation;
	}

	public void setFromStation(String fromStation) {
		this.fromStation = fromStation;
	}

	public String getToStation() {
		return toStation;
	}

	public void setToStation(String toStation) {
		this.toStation = toStation;
	}

	public String getKsn() {
		return ksn;
	}

	public void setKsn(String ksn) {
		this.ksn = ksn;
	}

	public String getKpath() {
		return kpath;
	}

	public void setKpath(String kpath) {
		this.kpath = kpath;
	}

	public String getTransferStation() {
		return transferStation;
	}

	public void setTransferStation(String transferStation) {
		this.transferStation = transferStation;
	}

	public List<KPath> getSubPaths() {
		return subPaths;
	}

	public void setSubPaths(List<KPath> subPaths) {
		this.subPaths = subPaths;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getoTime() {
		return oTime;
	}

	public void setoTime(int oTime) {
		this.oTime = oTime;
	}

	public int getdTime() {
		return dTime;
	}

	public void setdTime(int dTime) {
		this.dTime = dTime;
	}

	public int gettTime() {
		return tTime;
	}

	public void settTime(int tTime) {
		this.tTime = tTime;
	}

	public void addTime(int time) {
		this.time += time;
	}

	public void addTTime(int tTime) {
		this.tTime += tTime;
	}

	public List<String> getStationCodes() {
		return stationCodes;
	}

	public void setStationCodes(List<String> stationCodes) {
		this.stationCodes = stationCodes;
	}

	public Map<String, TransferStation> getTransferStationMap() {
		return transferStationMap;
	}

	public void setTransferStationMap(Map<String, TransferStation> transferStationMap) {
		this.transferStationMap = transferStationMap;
	}

}
