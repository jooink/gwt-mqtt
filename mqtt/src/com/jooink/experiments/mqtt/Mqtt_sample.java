package com.jooink.experiments.mqtt;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.jooink.experiments.mqtt.lowlevel.ConnectOptions;
import com.jooink.experiments.mqtt.lowlevel.ConnectionHandler;
import com.jooink.experiments.mqtt.lowlevel.ConnectionLostHandler;
import com.jooink.experiments.mqtt.lowlevel.IMqttClient;
import com.jooink.experiments.mqtt.lowlevel.MessageArrivedHandler;
import com.jooink.experiments.mqtt.lowlevel.MessageDeliveredHandler;
import com.jooink.experiments.mqtt.lowlevel.MqttClient;
import com.jooink.experiments.mqtt.lowlevel.MqttMessage;
import com.jooink.experiments.mqtt.lowlevel.SubscribeOptions;
import com.jooink.experiments.mqtt.lowlevel.SubscriptionHandler;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Mqtt_sample implements EntryPoint, ConnectionHandler, ConnectionLostHandler, MessageArrivedHandler, MessageDeliveredHandler, SubscriptionHandler {

	
	protected static final String DESTINATION = "$SYS/#";
	
	public void log(String s) {
		RootPanel.get().add(new HTML(s));
	}
	
	IMqttClient client;
	@Override
	public void onModuleLoad() {

		final TextBox message = new TextBox();
		RootPanel.get().add(message);
		Button b = new Button("Send");
		b.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				log("sending");
				MqttMessage m = MqttMessage.create(message.getValue());
				client.send( m );
				log("sent");
			}
		});
		RootPanel.get().add(b);

		client = MqttClient.createClient("test.mosquitto.org", 80, "clientId");

		client.setConnectionLostHandler(this);
		client.setMessageHandler(this);
		client.setMessageDeliveredHandler(this);
			
		log("connecting");
		client.connect(ConnectOptions.create(this));
		
		
		
		
	}

	//
	//	private static native void alertize() /*-{
	//		client = new Messaging.Client("test.mosquitto.org", 80, "clientId");
	//		client.onConnectionLost = onConnectionLost;
	//		client.onMessageArrived = onMessageArrived;
	//		client.connect({onSuccess:onConnect});
	//
	//	function onConnect() {
	//  		// Once a connection has been made, make a subscription and send a message.
	//  		console.log("onConnect");
	//  		client.subscribe("/World");
	//  		message = new Messaging.Message("Hello");
	//  		message.destinationName = "/World";
	//  		client.send(message); 
	//	};
	//
	//	function onConnectionLost(responseObject) {
	//  		if (responseObject.errorCode !== 0)
	//    	console.log("onConnectionLost:"+responseObject.errorMessage);
	//	};
	//	function onMessageArrived(message) {
	//  		console.log("onMessageArrived:"+message.payloadString);
	//  		client.disconnect(); 
	//	};
	//}-*/;


	@Override
	public void onFailure(int code, String reason) {
		Window.alert(reason);
	}


	@Override
	public void onSuccess() {
		log("connected");
		
		client.subscribe(DESTINATION,SubscribeOptions.create(this));
		log("subscribed");

	}

	@Override
	public void onMessageArrived(MqttMessage m) {
		log("Message arrived on " + m.getDestinationName() + ": " + m.getPayloadString());
	}

	@Override
	public void onConnectionLost(int code, String msg) {
		Window.alert("Context Lost: " + msg + " code: " + code);
	}

	@Override
	public void onMessageDelivered(MqttMessage m) {
		log("Message delivered :" + m.getPayloadString());
	}

	@Override
	public void onSubscriptionFailure(int errorCode, String errorText) {

		log("Subscrioption Failure: " +  errorText);
	}

	@Override
	public void onSubscriptionSuccess() {
		log("Subscrioption DONE");
	}

}