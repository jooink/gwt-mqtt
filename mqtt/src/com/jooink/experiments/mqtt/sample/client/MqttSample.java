package com.jooink.experiments.mqtt.sample.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.jooink.experiments.mqtt.Client;
import com.jooink.experiments.mqtt.ConnectionLostEvent;
import com.jooink.experiments.mqtt.ConnectionLostEvent.Handler;
import com.jooink.experiments.mqtt.lowlevel.ConnectionHandler;

public class MqttSample implements EntryPoint, MainPanel.Presenter, Handler {

	//	private static final String server = "test.mosquitto.org";		
	//	private static final int port = 80;
	//	private static final String clientId = "gwtMqTT";

	protected static final String SYS_TOPIC = "$SYS/#";
	//protected static final String DESTINATION = "/gwtMqtt";
	//protected static final String DESTINATION = "$SYS/broker/clients/total";
	protected static final String DESTINATION = "$SYS/broker/messages/stored";



	//	private ConnectionEvent.Handler connectionEventHandler = new ConnectionEvent.Handler() {
	//		
	//		@Override
	//		public void onSuccess(ConnectionEvent e) {
	//			Window.alert("Connection done");
	//		}
	//		
	//		@Override
	//		public void onFailure(ConnectionEvent e) {
	//			Window.alert("Connection Error: " + e.getErrorText());			
	//		}
	//	};
	//	private SubscriptionEvent.Handler subscriptionHandler = new SubscriptionEvent.Handler() {
	//		
	//		@Override
	//		public void onSuccess(SubscriptionEvent e) {
	//			Window.alert("Subscription done on " + e.getFilter());
	//
	//		}
	//		
	//		@Override
	//		public void onFailure(SubscriptionEvent e) {
	//			Window.alert("Subscription Error: " + e.getErrorText());
	//		}
	//	};
	//
	//	



	private TabLayoutPanel servers = new TabLayoutPanel(30,Unit.PX);

	@Override
	public void onModuleLoad() {

		//RootLayoutPanel.get().add(new DestinationDisplay( new Destination("$SYS/#") ));


		RootLayoutPanel.get().add(servers);

		MainPanel mp = new MainPanel();

		mp.setPresenter(this);
		servers.add(mp,new HTML("MqTT"));		

	}


	@Override
	public void connect(final String hostname, int port, String clientId, String username, String password) {

		//using the FAKE Client
		//final Client client = new Client(MQTTUtils.createFAKEClient(hostname, port, clientId));
		final Client client = new Client(hostname, port, clientId);


		//create the header (part of this view, not the connextion presenter)
		final ResizeLayoutPanel holder = new ResizeLayoutPanel();
		final HeaderWidget headerWidget = new HeaderWidget("ws://" + hostname + (80==port?"":port) + " " + clientId);


		final ConnectionPresenter serverConnectionPresenter = new ConnectionPresenter(client);

		servers.add(holder, headerWidget);

		headerWidget.setPresenter(new HeaderWidget.Presenter() {
			@Override
			public void close() {
				serverConnectionPresenter.stop();
				servers.remove(holder);
			}
		});

		servers.selectTab(holder);


		client.addConnectionLostHandler(this);

		headerWidget.setStatusText("connecting");


		ConnectionHandler ca = new ConnectionHandler() {

			@Override
			public void onSuccess() {		
				//update the header status
				headerWidget.setStatusText("connected");
				//can go ... it is connected
				serverConnectionPresenter.go(holder);		
			}


			@Override
			public void onFailure(int errorCode, String errorText) {	
				//update the header status
				headerWidget.setStatusText("ERROR " + errorCode + ": " + errorText);

			}
		};

		if(username != null && username.length()>0)
			client.connect(ca,username,password);
		else
			client.connect(	ca);


	}


	@Override
	public void onConnectionLost(ConnectionLostEvent e) {
		Window.alert("Connection Lost ... be warned");
	}

}
