package com.uzm.hylex.services.java.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.collect.Maps;
import com.uzm.hylex.services.Core;

public class ConfigurationBuilder {

	public static HashMap<String, ConfigurationBuilder> configs = Maps.newHashMap();

	private String filename;

	private File file;

	private YamlConfiguration yamlConfiguration;

	public ConfigurationBuilder(String filename) {
		this.filename = filename;

		if (!Core.getInstance().getDataFolder().exists()) {
			Core.getInstance().getDataFolder().mkdirs();
		}

		file = new File(Core.getInstance().getDataFolder(), filename + ".yml");

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.err.println("[Hylex] Ocorreu um erro ao criar o arquivo " + filename + ".yml");
			}
		}
		load();
		save();

		configs.put(filename, this);
	}

	public void load() {

		yamlConfiguration = YamlConfiguration.loadConfiguration(file);
	}

	public YamlConfiguration get() {
		return yamlConfiguration;
	}

	public static ConfigurationBuilder find(String name) {
		return configs.get(name.toLowerCase());
	}

	public static ArrayList<ConfigurationBuilder> forEach() {

		return new ArrayList<ConfigurationBuilder>(configs.values());

	}

	public boolean save() {
		try {
			get().save(file);

			return true;
		} catch (IOException e) {
			return false;
		}

	}

	public String getFileName() {
		return filename;
	}

	public File getFile() {
		return file;
	}

}
