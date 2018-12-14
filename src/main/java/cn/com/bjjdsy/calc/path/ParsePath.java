package cn.com.bjjdsy.calc.path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.bjjdsy.calc.entity.KPath;
import cn.com.bjjdsy.calc.entity.TransferStation;

public class ParsePath {

	static final Logger logger = LoggerFactory.getLogger(ParsePath.class);

	String kpath = "";

	public List<KPath> parse(KPath path) {
		List<KPath> pathList = new ArrayList<>();
		if ("".equals(path.getTransferStation())) {
			// todo add direct

			pathList.add(path);
		} else {
			String[] ts = path.getTransferStation().split("-");
			kpath = path.getKpath();
			for (String tStation : ts) {
				TransferStation transfer = this.getTransferStation(tStation);
				String subPath = this.getSubPath(transfer, path);
				logger.info("kpath:{}", kpath);
				KPath sub = this.genKPath(subPath, path);
				logger.info(sub.getFromStation() + ":" + sub.getToStation());
				pathList.add(sub);
			}
			KPath sub = this.genKPath(kpath, path);
			logger.info(sub.getFromStation() + ":" + sub.getToStation());
			pathList.add(sub);
		}
		path.setSubPaths(pathList);

		// get transfer direct
		List<String> stationCodes = path.getStationCodes();
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
		pathList.forEach(p -> {
			logger.info("{}", p.getKpath());
		});
		path.getTransferStations().forEach(t -> {
			logger.info("{}:{}", t.getFromStation(), t.getToStation());
		});

		return pathList;
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

		if (path.getTransferStations() == null) {
			path.setTransferStations(new ArrayList<>());
		}
		path.getTransferStations().add(transfer);
		return subPath;
	}

	private KPath genKPath(String path, KPath kpath) {
		String[] subs = path.split("-");
		KPath sub = new KPath();
		sub.setFromStation(subs[0]);
		sub.setToStation(subs[subs.length - 1]);
		sub.setKpath(path);
		sub.setDirect(this.getDirect(subs[0], subs[1]));
		kpath.getStationCodes().addAll(Arrays.asList(subs));
		return sub;
	}

	private int getDirect(String start, String end) {
		// todo
		// section
		return 1;
	}
}
