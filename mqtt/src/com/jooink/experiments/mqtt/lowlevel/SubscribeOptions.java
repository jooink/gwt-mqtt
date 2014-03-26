package com.jooink.experiments.mqtt.lowlevel;

import com.google.gwt.core.client.JavaScriptObject;

public final class SubscribeOptions extends JavaScriptObject  {
	
	protected SubscribeOptions() {}
	
	public static native  SubscribeOptions create(SubscriptionHandler callback) /*-{
		return {
			onSuccess: function(e) { 
				callback.@com.jooink.experiments.mqtt.lowlevel.SubscriptionHandler::onSubscriptionSuccess()();
			},
			onFailure:  function(e) { 
				callback.@com.jooink.experiments.mqtt.lowlevel.SubscriptionHandler::onSubscriptionFailure(ILjava/lang/String;)(e.errorCode, e.errorMessage);
			}
		};
	}-*/;

	public native void setTimeout(int timeout) /*-{
		this.timeout = timeout;
	}-*/;
	
	public native void setQos(int qos) /*-{
		this.qos = qos;
	}-*/;
	
//	validate(subscribeOptions,  {qos:"number", 
//        invocationContext:"object", 
//        onSuccess:"function", 
//        onFailure:"function",
//        timeout:"number"
//       });
//	if (subscribeOptions.timeout && !subscribeOptions.onFailure)
//		throw new Error("subscribeOptions.timeout specified with no onFailure callback.");
//	if (typeof subscribeOptions.qos !== "undefined" 
//			&& !(subscribeOptions.qos === 0 || subscribeOptions.qos === 1 || subscribeOptions.qos === 2 ))
//		throw new Error(format(ERROR.INVALID_ARGUMENT, [subscribeOptions.qos, "subscribeOptions.qos"]));
//	client.subscribe(filter, subscribeOptions);

}
