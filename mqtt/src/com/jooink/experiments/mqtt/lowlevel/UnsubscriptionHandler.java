package com.jooink.experiments.mqtt.lowlevel;

/* omitting the invocationcontext */
public interface UnsubscriptionHandler {
	public void onUnsubscriptionSuccess();
	public void onUnsubscriptionFailure(int errorCode, String errorText);
}
