package com.jooink.experiments.mqtt;

import java.util.HashMap;

import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.jooink.experiments.mqtt.lowlevel.ConnectOptions;
import com.jooink.experiments.mqtt.lowlevel.ConnectionHandler;
import com.jooink.experiments.mqtt.lowlevel.ConnectionLostHandler;
import com.jooink.experiments.mqtt.lowlevel.IMqttClient;
import com.jooink.experiments.mqtt.lowlevel.MessageArrivedHandler;
import com.jooink.experiments.mqtt.lowlevel.MessageDeliveredHandler;
import com.jooink.experiments.mqtt.lowlevel.MqttClient;
import com.jooink.experiments.mqtt.lowlevel.MqttMessage;

public class Client {

	private IMqttClient client;


	private void setHandlers() {
		this.client.setConnectionLostHandler(connectionLostHandler);
		this.client.setMessageHandler(messageArrivedHandler);
		this.client.setMessageDeliveredHandler(messageDeliveredHandler);				
	}

	public Client(String server, int port, String clientId) {
		this.client = MqttClient.createClient(server, port, clientId);
		setHandlers();
	}

	public Client(String serverUri, String clientId) {
		this.client = MqttClient.createClient(serverUri,clientId);
		setHandlers();
	}

	protected boolean connected;
	protected boolean connecting;
	protected boolean requestedDisconnection = false;
	
	public boolean isConnected() {
		return connected;
	}

	protected EventBus bus = new SimpleEventBus();

	protected MessageArrivedHandler messageArrivedHandler = new MessageArrivedHandler() {
		@Override
		public void onMessageArrived(MqttMessage m) {
			bus.fireEvent(new MessageArrivedEvent(m));
		}
	};

	
	private HashMap<MqttMessage, MessageDeliveredHandler> perMessageHandlers = new HashMap<MqttMessage, MessageDeliveredHandler>();
	protected MessageDeliveredHandler messageDeliveredHandler = new MessageDeliveredHandler() {

		@Override
		public void onMessageDelivered(MqttMessage m) {
			
			//SHORT CIRCUTING FOR per MESSAGE DELIVERING notifications
			//that are callbacks more than eventhandlers
			MessageDeliveredHandler mh = perMessageHandlers.remove(m);
			if(mh != null)
				mh.onMessageDelivered(m);

			bus.fireEvent(new MessageDeliveredEvent(m));
		}
	};


	class ClientConnectionHandler implements ConnectionHandler {
		private ConnectionHandler userHandler;

		public ClientConnectionHandler(ConnectionHandler userHandler) {
			this. userHandler = userHandler;
		}

		@Override
		public void onSuccess() {
			connected = true;
			connecting = false;
			userHandler.onSuccess();
		}

		@Override
		public void onFailure(int errorCode, String errorText) {
			connecting = false;
			connected = false;
			userHandler.onFailure(errorCode, errorText);
		}
	};


	protected ConnectionLostHandler connectionLostHandler = new ConnectionLostHandler() {

		@Override
		public void onConnectionLost(int code, String msg) {
			
			connected = false;
			
			//this is a bug: the js calls the handler even when disconnection is requested
			//this is just a workaround
			//XXX
			if(requestedDisconnection) {
				requestedDisconnection = false;
				return;
			}
			bus.fireEvent(new ConnectionLostEvent(code, msg));
		}
	};


	public void connect(ConnectionHandler handler) {
		connecting = true;
		client.connect(ConnectOptions.create(new ClientConnectionHandler(handler)));
	}

	public void connect(ConnectionHandler handler, String userName, String password, int keepalive) {
		connecting = true;
		ConnectOptions co = ConnectOptions.create(new ClientConnectionHandler(handler));
	
		if(userName!= null && userName.length()>0)
			co.setUsername(userName);
		if(password!= null && password.length()>0)
			co.setPassword(password);
		if(keepalive >0)
			co.setKeepAliveInterval(keepalive);
		
		client.connect(co);
	}


	public void disconnect() {
		
		if(!connected)
			return;
		//XXX
		//this seems a bug in the js: the context lost handler is called
		//XXX
		requestedDisconnection = true;
		
		client.disconnect();
	}
	





	//private Map<String,Subscription> filters = new HashMap<String,Subscription>();


	//public Subscription;

	public void send(MqttMessage m) {
		client.send(m);
	}

	public MqttMessage send(MqttMessage m, MessageDeliveredHandler mh) {
		perMessageHandlers.put(m, mh);
		client.send(m);
		return m;
	}

	public  HandlerRegistration addMessageArrivedHandler( MessageArrivedEvent.Handler handler) {
		return bus.addHandler(MessageArrivedEvent.TYPE, handler);
	}

	public HandlerRegistration addConnectionLostHandler(ConnectionLostEvent.Handler handler) {
		return bus.addHandler(ConnectionLostEvent.TYPE, handler);
	}

	//package private
	 IMqttClient getMqttClient() {
		 return client;
	}


}
