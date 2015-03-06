package com.jooink.experiments.mqtt.lowlevel;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.typedarrays.shared.ArrayBuffer;

public final class MqttMessage extends JavaScriptObject {
	protected MqttMessage() {}
	
	public static native MqttMessage create(String stringPayload) /*-{
		return new Paho.MQTT.Message(stringPayload);
	}-*/;
	
	public static native MqttMessage create(ArrayBuffer arrayPayload) /*-{
		return new Paho.MQTT.Message(arrayPayload);
	}-*/;

	public native String getPayloadString() /*-{
		return this.payloadString;
	}-*/;
	
	public native String getDestinationName() /*-{
		return this.destinationName;
	}-*/;

	public native int getQos() /*-{
		return this.qos;
	}-*/;

	public native boolean isDuplicate() /*-{
		return this.duplicate;
	}-*/;

	public native boolean isRetained() /*-{
		return this.retained;
	}-*/;
	
	
	
	public native MqttMessage setDestinationName(String destinationName) /*-{
		this.destinationName = destinationName;
		return this;
	}-*/;
	
	
	public native MqttMessage setQos(int qos) /*-{
		this.qos = qos;
		return this;
	}-*/;
	

	public native MqttMessage setRetained(boolean retain) /*-{
		this.retained = retain;
		return this;
	}-*/;

}
