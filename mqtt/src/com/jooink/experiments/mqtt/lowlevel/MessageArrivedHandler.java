package com.jooink.experiments.mqtt.lowlevel;


public interface MessageArrivedHandler {
	public void onMessageArrived(MqttMessage m);
}
