package com.jooink.experiments.mqtt.lowlevel;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public final class MqttClient extends JavaScriptObject implements IMqttClient  {
	
	
	protected MqttClient() {}


	interface Script extends ClientBundle {

		public static Script INSTANCE = GWT.create(Script.class);

		@Source("mqttws31.js")
		TextResource mwttwsjs();
	}


	private static boolean injected = false;

	private static void ensureInjected() {
		if(!injected) {
			ScriptInjector.fromString(Script.INSTANCE.mwttwsjs().getText()).inject();
			injected = true;
		}
	}


	public static IMqttClient createClient(String serverUri, String clientId) {
		return createClient(serverUri, -1, clientId);
	}

	
	public static IMqttClient createClient(String server, int port, String clientId) {
		ensureInjected();
		return _createClient(server,port,clientId);
	}


	private static native MqttClient _createClient(String server, int port, String clientId) /*-{
		return new Messaging.Client(server, port, clientId);
	}-*/;


	@Override
	public native void connect(ConnectOptions co) /*-{		
		this.connect(co);
	}-*/;

	@Override
	public native void disconnect() /*-{		
		this.disconnect();
	}-*/;

	@Override
	public native void subscribe(String filter, SubscribeOptions options) /*-{
		this.subscribe(filter,options);
	}-*/;

	
	@Override
	public native void unsubscribe(String filter, UnsubscribeOptions options) /*-{
		this.unsubscribe(filter,options);
	}-*/;


	@Override
	public native void setConnectionLostHandler(ConnectionLostHandler h) /*-{
		this.onConnectionLost = function(e) { 
				h.@com.jooink.experiments.mqtt.lowlevel.ConnectionLostHandler::onConnectionLost(ILjava/lang/String;)(e.errorCode, e.errorMessage);
		}
	}-*/;

	@Override
	public native void setMessageHandler(MessageArrivedHandler h) /*-{
		this.onMessageArrived = function(m) { 
			h.@com.jooink.experiments.mqtt.lowlevel.MessageArrivedHandler::onMessageArrived(Lcom/jooink/experiments/mqtt/lowlevel/MqttMessage;)(m);
		}
	}-*/;

	@Override
	public native void setMessageDeliveredHandler(MessageDeliveredHandler h) /*-{
		this.onMessageDelivered = function(m) { 
			h.@com.jooink.experiments.mqtt.lowlevel.MessageDeliveredHandler::onMessageDelivered(Lcom/jooink/experiments/mqtt/lowlevel/MqttMessage;)(m);
		}
	}-*/;


	@Override
	public native void send(MqttMessage m) /*-{
  		  this.send(m);
	}-*/;


}
