package cn.com.bjjdsy.calc.path;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.bjjdsy.calc.entity.KPath;

public class ParsePath {

	static final Logger logger = LoggerFactory.getLogger(ParsePath.class);

	public List<KPath> parse(KPath path) {
		List<KPath> pathList = new ArrayList<>();
		if ("".equals(path.getTransferStation())) {
			pathList.add(path);
		} else {
			String[] t = path.getTransferStation().split("-");
			String kpath = path.getKpath();
			for (String transfer : t) {
				String[] tStations = transfer.split(";");
				String end = tStations[0];
				String start = tStations[1];
				String subPath = kpath.substring(0, kpath.indexOf(end) + end.length());
				logger.info("{}", kpath);
				kpath = kpath.substring(kpath.indexOf(start));
				logger.info("{}:{}", subPath, kpath);

				KPath sub = this.genKPath(subPath);
				logger.info(sub.getFromStation() + ":" + sub.getToStation());
				pathList.add(sub);
			}
			KPath sub = this.genKPath(kpath);
			logger.info(sub.getFromStation() + ":" + sub.getToStation());
			pathList.add(sub);
		}
		pathList.forEach(p -> {
			logger.info("{}", p.getKpath());
		});
		return pathList;
	}

	private KPath genKPath(String path) {
		String[] subs = path.split("-");
		KPath sub = new KPath();
		sub.setFromStation(subs[0]);
		sub.setToStation(subs[subs.length - 1]);
		sub.setKpath(path);
		sub.setDirect(this.getDirect(sub.getFromStation(), sub.getToStation()));
		return sub;
	}

	private String getDirect(String start, String end) {
		// section
		return "0";
	}
}
