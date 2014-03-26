package com.jooink.experiments.mqtt.lowlevel;

import com.google.gwt.core.client.JavaScriptObject;

public final class ConnectOptions extends JavaScriptObject {
	public static native  ConnectOptions create(ConnectionHandler callback) /*-{
		return {
			onSuccess: function(e) { 
				callback.@com.jooink.experiments.mqtt.lowlevel.ConnectionHandler::onSuccess()();
			},
			onFailure:  function(e) { 
				callback.@com.jooink.experiments.mqtt.lowlevel.ConnectionHandler::onFailure(ILjava/lang/String;)(e.errorCode, e.errorMessage);
			}
		};
	}-*/;

	public native void setUsername(String userName) /*-{
		this.userName = userName;
	}-*/;

	public native void setPassword(String password) /*-{
		this.password = password;
	}-*/;
	
	public native void setTimeout(int timeout) /*-{
		this.timeout = timeout;
	}-*/;
	
	public native void setKeepAliveInterval(int keepAliveInterval) /*-{
		this.keepAliveInterval = keepAliveInterval;
	}-*/;

	public native void setCleanSession(boolean cleanSession) /*-{
		this.cleanSession = cleanSession;
	}-*/;
	
	public native void setWillMessage(MqttMessage testament) /*-{
		this.willMessage = testament;
	}-*/;

	/* {
	 * -timeout:"number",
       -userName:"string", 
       -password:"string", 
       -willMessage:"object", 
       -keepAliveInterval:"number", 
       -cleanSession:"boolean", 
       useSSL:"boolean",
       invocationContext:"object", 
       -onSuccess:"function", 
       -onFailure:"function",
       hosts:"object",
       ports:"object"
       }
	 */

	protected ConnectOptions() {}
}
