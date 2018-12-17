package cn.com.bjjdsy.calc.entity;

import java.util.Date;

public class UDInfo {

	private String oStationCode;
	private String dStationCode;
	private String entryTime;
	private String outTime;
	private int fromDirect;
	private int toDirect;
	private String pathSn;

	public String getPathSn() {
		return pathSn;
	}

	public void setPathSn(String pathSn) {
		this.pathSn = pathSn;
	}

	public int getFromDirect() {
		return fromDirect;
	}

	public void setFromDirect(int fromDirect) {
		this.fromDirect = fromDirect;
	}

	public int getToDirect() {
		return toDirect;
	}

	public void setToDirect(int toDirect) {
		this.toDirect = toDirect;
	}

	public String getoStationCode() {
		return oStationCode;
	}

	public void setoStationCode(String oStationCode) {
		this.oStationCode = oStationCode;
	}

	public String getdStationCode() {
		return dStationCode;
	}

	public void setdStationCode(String dStationCode) {
		this.dStationCode = dStationCode;
	}

	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	public String getOutTime() {
		return outTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}

}
