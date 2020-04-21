package com.uzm.hylex.services.lan;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.json.JSONObject;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.emitter.Emitter.Listener;
import io.socket.engineio.client.Transport;

public class WebSocket {

	public static HashMap<String, WebSocket> sockets = Maps.newHashMap();

	private String query = "";

	private HashMap<String, Listener> events = Maps.newHashMap();

	private Socket socket;

	private String address;

	private String name;

	private boolean connected = true;

	public WebSocket(String address) {
		this.address = address;

	}

	public Socket build() {
		try {
			this.socket = IO.socket(address + getQueryParams());
			return this.socket;
		} catch (URISyntaxException e) {
			Bukkit.getServer().getConsoleSender().sendMessage(
					"§b[Hylex - Module: Services] §e[WebSocket - Socket.io] §c§lERROR §7Socket with address '"
							+ getAddress() + getQueryParams() + "' §7cannot be loaded.");
			return null;
		}

	}

	public WebSocket connect() {
		try {

			socket.connect();

			Bukkit.getServer().getConsoleSender()
					.sendMessage("§b[Socket.io] §7O servidor está tentanto logar no NodeJS como o cliente §f" + getQueryParams());
			registerListeners();
			Thread.sleep(300);
			if (connected) {
				System.out.println("");
				Bukkit.getServer().getConsoleSender().sendMessage("§b[Socket.io] §7Sucesso §a" + "Code 500" + ": §f"
						+ "O cliente consegiu se conectar ao servidor como " + getQueryParams());
				System.out.println("");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return this;
	}

	public WebSocket setup() {
		socket.on("disconnect-f", new Listener() {
			public void call(Object... args) {

				JsonObject response = new JsonParser().parse(((JSONObject) args[0]).toString()).getAsJsonObject();
				System.out.println("");
				Bukkit.getServer().getConsoleSender()
						.sendMessage("§b[Hylex - Module: Services] §e[WebSocket - Socket.io] §7Error §c"
								+ response.get("error").getAsInt() + " > §f" + response.get("message").getAsString());
				System.out.println("");

				connected = false;

			}
		});
		return this;
	}

	public String getAddress() {
		return address;
	}

	public WebSocket addQueryParam(String param) {
		query += param;

		return this;
	}

	public Socket getSocket() {
		return socket;
	}

	public String getQueryParams() {
		return query;
	}

	public boolean isConnected() {
		return connected;
	}

	public String getName() {
		return name;
	}

	public WebSocket sendHeaders(final String key, final String value) {
		socket.io().on(Manager.EVENT_TRANSPORT, new Listener() {
			public void call(Object... args) {
				Transport transport = (Transport) args[0];
				transport.on(Transport.EVENT_REQUEST_HEADERS, new Emitter.Listener() {
					public void call(Object... args) {
						@SuppressWarnings("unchecked")
						Map<String, List<String>> headers = (Map<String, List<String>>) args[0];

						headers.put(key, Arrays.asList(value));
					}
				});
			}
		});
		return this;
	}

	public WebSocket addListener(final Listener listener, String eventname) {
		events.put(eventname, listener);
		return this;
	}

	public WebSocket registerListeners() {
		for (Entry<String, Listener> r : events.entrySet()) {
			if (socket != null) {
				if (!socket.hasListeners(r.getKey())) {
					socket.on(r.getKey(), r.getValue());
				}

			}
		}
		return this;
	}

	public static WebSocket get(String name) {
		if (sockets.containsKey(name)) {
			return sockets.get(name);
		} else {
			return null;
		}
	}

	public static WebSocket create(String name, String address) {
		if (!sockets.containsKey(name)) {
			sockets.put(name, new WebSocket(address));
			return sockets.get(name);
		} else {
			return sockets.get(name);
		}
	}

}
