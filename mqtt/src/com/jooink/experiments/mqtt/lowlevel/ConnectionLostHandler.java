package com.jooink.experiments.mqtt.lowlevel;

public interface ConnectionLostHandler {
	
	public void onConnectionLost(int code, String msg);

}
