package cn.com.bjjdsy.calc.path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.bjjdsy.calc.entity.KPath;
import cn.com.bjjdsy.calc.entity.SubPath;
import cn.com.bjjdsy.calc.entity.TransferStation;
import cn.com.bjjdsy.common.CalcConstant;

public class ParsePath {

	static final Logger logger = LoggerFactory.getLogger(ParsePath.class);

	String kpath = "";

	public List<SubPath> parse(KPath path) {
		List<SubPath> subPathList = new ArrayList<>();
		if ("".equals(path.getTransferStation())) {
			SubPath sub = this.genSubPath(path.getKpath(), path);
			subPathList.add(sub);
		} else {
			String[] ts = path.getTransferStation().split("-");
			kpath = path.getKpath();
			for (String tStation : ts) {
				TransferStation transfer = this.getTransferStation(tStation);
				String subPath = this.getSubPath(transfer, path);
//				logger.info("kpath:{}", kpath);
				SubPath sub = this.genSubPath(subPath, path);
//				logger.info(sub.getFromStation() + ":" + sub.getToStation());
				subPathList.add(sub);
			}
			SubPath sub = this.genSubPath(kpath, path);
//			logger.info(sub.getFromStation() + ":" + sub.getToStation());
			subPathList.add(sub);
		}
		path.setSubPaths(subPathList);

		// get transfer direct
		List<String> stationCodes = path.getStationCodes();
//		logger.info("transfer size:{}", path.getTransferStations().size());

		for (TransferStation transfer : path.getTransferStations()) {
			String fromStation = transfer.getFromStation();
			String prevFromStation = stationCodes.get(stationCodes.indexOf(fromStation) - 1);
			int fromDirect = this.getDirect(prevFromStation, fromStation);
			String toStation = transfer.getToStation();
			String nextToStation = stationCodes.get(stationCodes.indexOf(toStation) + 1);
			int toDirect = this.getDirect(toStation, nextToStation);
			transfer.setFromDirect(fromDirect);
			transfer.setToDirect(toDirect);
			path.getTransferStationMap().put(toStation + ":" + toDirect, transfer);
		}
//		logger.info("transfer map size:{}", path.getTransferStationMap().size());
		path.getTransferStationMap().forEach((k, v) -> {
//			logger.info("{}:{}", k, v);
		});

		subPathList.forEach(p -> {
			logger.info("sub path:{}", p.getPath());
		});
		path.getTransferStations().forEach(t -> {
			logger.info("transfer:{}->{}", t.getFromStation(), t.getToStation());
		});

		return subPathList;
	}

	private TransferStation getTransferStation(String transfer) {
		String[] tStations = transfer.split(";");
		String prevEnd = tStations[0];
		String nextStart = tStations[1];
		TransferStation tStation = new TransferStation();
		tStation.setFromStation(prevEnd);
		tStation.setToStation(nextStart);
		return tStation;
	}

	private String getSubPath(TransferStation transfer, KPath path) {
		String subPath = kpath.substring(0,
				kpath.indexOf(transfer.getFromStation()) + transfer.getFromStation().length());
//		logger.info("{}", kpath);
		kpath = kpath.substring(kpath.indexOf(transfer.getToStation()));
//		logger.info("{},{}", subPath, kpath);
		path.getTransferStations().add(transfer);
		return subPath;
	}

	private SubPath genSubPath(String path, KPath kpath) {
		String[] subs = path.split("-");
		SubPath sub = new SubPath();
		sub.setFromStation(subs[0]);
		sub.setToStation(subs[subs.length - 1]);
		sub.setPath(path);
		sub.setDirect(this.getDirect(subs[0], subs[1]));
		kpath.getStationCodes().addAll(Arrays.asList(subs));
		return sub;
	}

	private int getDirect(String start, String stop) {
		int direct = CalcConstant.sectionDict.get(start + ":" + stop);
		logger.info("{}->{} {}", start, stop, direct);
		return direct;
	}

	public static void main(String[] args) {
		String s = "adb|efg";
		String[] ss = s.split("\\|");
		System.out.println(Arrays.toString(ss));
	}
}
