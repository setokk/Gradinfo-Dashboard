package edu.gradinfo.dashboard.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {
	public static final String PROJECT_NAME = "Gradinfo";
	public static final String PROJECT_ROOT = System.getProperty("user.dir") + File.separator + PROJECT_NAME;
	public static final String PROJECT_DB = FileUtils.PROJECT_ROOT + File.separator + "db" + File.separator;
	public static final Path CONFIG_PATH = Paths.get(System.getProperty("user.dir") + File.separator + "config.properties");


	public static List<String> getAllDbFiles() {
		File dbDir = new File(PROJECT_DB);
		return Arrays.stream(dbDir.listFiles())
			.map(File::getName)
			.sorted()
			.collect(Collectors.toList());
	}

	public static String getDbFile(String filename) throws IOException {
		Path filepath = Paths.get(PROJECT_DB + filename);
		if (Files.notExists(filepath))
			return null;

		return Files.readString(filepath);
	}

	public static boolean saveDbFile(String filename, String fileContent) throws IOException {
		Path filepath = Paths.get(PROJECT_DB + filename);
		boolean isExists = exists(filepath);

		Files.writeString(filepath, fileContent);
		return isExists;
	}

	public static boolean exists(String filename) {
		return exists(Paths.get(PROJECT_DB + filename));
	}

	public static boolean exists(Path filepath) {
		return Files.exists(filepath);
	}
}
