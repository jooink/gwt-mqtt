package com.jooink.experiments.mqtt.lowlevel;


//plain wrapper around paho js mqtt library
public interface IMqttClient {

	public void connect(ConnectOptions co);

	public void disconnect();


	public void subscribe(String filter, SubscribeOptions options);
	public void unsubscribe(String filter, UnsubscribeOptions options);


	public void setConnectionLostHandler(ConnectionLostHandler h);
	public void setMessageHandler(MessageArrivedHandler h);
	public void setMessageDeliveredHandler(MessageDeliveredHandler h);


	public void send(MqttMessage m) ;

}