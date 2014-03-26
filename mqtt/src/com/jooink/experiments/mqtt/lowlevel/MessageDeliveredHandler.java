package com.jooink.experiments.mqtt.lowlevel;


public interface MessageDeliveredHandler {
	public void onMessageDelivered(MqttMessage m);
}
