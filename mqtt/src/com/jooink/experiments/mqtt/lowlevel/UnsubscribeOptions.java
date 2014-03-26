package com.jooink.experiments.mqtt.lowlevel;

import com.google.gwt.core.client.JavaScriptObject;

public final class UnsubscribeOptions extends JavaScriptObject  {
	
	protected UnsubscribeOptions() {}
	
	public static native  UnsubscribeOptions create(UnsubscriptionHandler callback) /*-{
		return {
			onSuccess: function(e) { 
				callback.@com.jooink.experiments.mqtt.lowlevel.UnsubscriptionHandler::onUnsubscriptionSuccess()();
			},
			onFailure:  function(e) { 
				callback.@com.jooink.experiments.mqtt.lowlevel.UnsubscriptionHandler::onUnsubscriptionFailure(ILjava/lang/String;)(e.errorCode, e.errorMessage);
			}
		};
	}-*/;
	
	public native void setTimeout(int timeout) /*-{
		this.timeout = timeout;
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
