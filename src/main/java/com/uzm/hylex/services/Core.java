package com.uzm.hylex.services;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Hello world!
 *
 */
public class Core extends JavaPlugin {

	private static Core core;
	private static String CORE_PATH;
	public static PluginLoader loader;

	public void onEnable() {

		long aux = System.currentTimeMillis();

		getServer().getConsoleSender()
				.sendMessage("§b[Hylex Module: Services] §7Plugin §fessencialmente §7carregado com sucesso.");
		getServer().getConsoleSender().sendMessage(
				"§eVersão: §f" + getDescription().getVersion() + " e criado por §f" + getDescription().getAuthors());

		/*
		 * Declations
		 */

		core = this;
		CORE_PATH = getFile().getPath();
		loader = new PluginLoader(this);

		getServer().getConsoleSender()
				.sendMessage("§b[Hylex Module: Services] §7Plugin §fdefinitivamente §7carregado com sucesso (§f"
						+ (System.currentTimeMillis() - aux + " milisegundos§7)"));

		/*
		 * Soon third-party
		 * 
		 * Bukkit.getMessenger().registerOutgoingPluginChannel(this, "FML|HS");
		 * 
		 * Bukkit.getMessenger().registerIncomingPluginChannel(this, "FML|HS", new
		 * ModMessageListener(this));
		 * 
		 * Bukkit.getMessenger().registerIncomingPluginChannel(this, "LABYMOD", new
		 * ModMessageListener(this));
		 * 
		 */

	}

	public void onDisable() {
		getServer().getConsoleSender().sendMessage(
				"§b[Hylex Module: Services] §7Plugin §bdesligado§7, juntamente todos os eventos e comandos também.");

	}

	public static Core getInstance() {
		return core;
	}

	public static String getPath() {
		return CORE_PATH;
	}

	public static PluginLoader getLoader() {
		return loader;
	}

}
