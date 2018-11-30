package cn.com.bjjdsy.calc.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ReadDataFile {

	static final Logger logger = LoggerFactory.getLogger(ReadDataFile.class);

	public void read(String filename) {
		File dataFile = new File(this.getClass().getResource(filename).getFile());
		try (BufferedReader reader = new BufferedReader(new FileReader(dataFile));) {
			reader.readLine();
			reader.readLine();
			while (true) {
				String line = reader.readLine();
				if (line == null || line.length() == 0) {
					break;
				}
				// parse the data
				String[] data = line.split(",");
				this.parseData(data);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public abstract void parseData(String[] data);
}
