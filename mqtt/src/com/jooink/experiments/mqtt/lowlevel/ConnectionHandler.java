package com.jooink.experiments.mqtt.lowlevel;

/* omitting the invocationcontext */
public interface ConnectionHandler {
	public void onSuccess();
	public void onFailure(int errorCode, String errorText);
}
