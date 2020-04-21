package com.uzm.hylex.services;

import java.util.HashMap;

import org.bukkit.Bukkit;

import com.google.common.collect.Maps;
import com.uzm.hylex.services.java.util.ConfigurationBuilder;
import com.uzm.hylex.services.lan.WebSocket;

public class PluginLoader {

	private Core core;
	private String spigot_version;
	public HashMap<String, String> permissions = Maps.newHashMap();

	public PluginLoader(Core core) {

		this.core = core;
		spigot_version = Bukkit.getServer().getClass().getPackage().getName()
				.substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf('.') + 1);

		config();
        
		if (ConfigurationBuilder.find("setup").get().getString("type").equalsIgnoreCase("default")) {
			WebSocket socket = WebSocket.create("local_socket",
					ConfigurationBuilder.find("setup").get().getString("websockets.local_socket.address"));

			socket.addQueryParam(
					"?server=" + ConfigurationBuilder.find("setup").get().getString("websockets.local_socket.client_name"));

			socket.build();

			socket.sendHeaders("Authorization", "00f1ff268656703e14faf2d05");

			socket.connect().setup();			
		}


	}

	public void config() {
		new ConfigurationBuilder("setup").load();
	}

	public String getSpigotVersion() {
		return spigot_version;
	}

	public HashMap<String, String> getPermissions() {
		return permissions;
	}

	public Core getCore() {
		return core;
	}
}
