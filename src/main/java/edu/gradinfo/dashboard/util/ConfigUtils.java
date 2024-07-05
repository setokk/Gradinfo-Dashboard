package edu.gradinfo.dashboard.util;

import edu.gradinfo.dashboard.enums.GitStatusEnum;
import edu.gradinfo.dashboard.enums.ThemeEnum;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class ConfigUtils {
	private static Map<String, String> config;

	/*
		Properties that show up in the config file as follows:
		(<property>: <value>)
	*/
	public static final String THEME_PROPERTY = "theme";
	public static final String GIT_PROPERTY   = "gitStatus";


	private ConfigUtils() {}

	public static void createConfig() throws IOException {
		String initialConfig = String.format("%s: %d\n%s: %d\n",
			THEME_PROPERTY, ThemeEnum.LIGHT.ordinal(),
			GIT_PROPERTY, GitStatusEnum.NOT_CLONED.ordinal());

		Files.writeString(FileUtils.CONFIG_PATH, initialConfig);
	}

	public static void loadConfig() throws IOException {
		if (Files.notExists(FileUtils.CONFIG_PATH))
			createConfig();

		List<String> content = Files.readAllLines(FileUtils.CONFIG_PATH);
		config = content.stream()
			.collect(Collectors.toMap(
				line -> {
					String[] splitted = line.split(":\\s*");
					if (splitted.length == 2)
						return splitted[0]; // Property key
					else
						throw new RuntimeException("Problem loading config file. Incorrect syntax.");
					},
				line -> {
					String[] splitted = line.split(":\\s*");
					if (splitted.length == 2)
						return splitted[1]; // Property value
					else
						throw new RuntimeException("Problem loading config file. Incorrect syntax.");
				}
			));
	}

	public static void updateConfig(String property, String value) {
		config.put(property, value);
	}

	public static void saveConfig() throws IOException {
		if (Files.notExists(FileUtils.CONFIG_PATH))
			return;

		StringBuilder configString = new StringBuilder();
		for (var entry : config.entrySet()) {
			configString.append(entry.getKey())
				.append(": ")
				.append(entry.getValue())
				.append("\n");
		}
		Files.writeString(FileUtils.CONFIG_PATH, configString.toString());
	}

	public static Map<String, String> getConfig() {
		return config;
	}
}
