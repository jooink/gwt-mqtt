package com.jooink.experiments.mqtt.lowlevel;

/* omitting the invocationcontext */
public interface SubscriptionHandler {
	public void onSubscriptionSuccess();
	public void onSubscriptionFailure(int errorCode, String errorText);
}
