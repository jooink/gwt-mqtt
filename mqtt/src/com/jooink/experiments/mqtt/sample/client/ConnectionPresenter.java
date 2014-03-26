package com.jooink.experiments.mqtt.sample.client;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.jooink.experiments.mqtt.Client;
import com.jooink.experiments.mqtt.Subscription;
import com.jooink.experiments.mqtt.lowlevel.MessageDeliveredHandler;
import com.jooink.experiments.mqtt.lowlevel.MqttMessage;
import com.jooink.experiments.mqtt.lowlevel.SubscriptionHandler;

public class ConnectionPresenter implements SubscriptionPanel.Presenter,SendPanel.Presenter, MessageDeliveredHandler {


	private Client client;
	private StackLayoutPanel view;

	
	
	public ConnectionPresenter(Client client) {
		this.client = client;
		
		//hook the client bus
		//nothing :l
	}

	private SubscriptionPanel subscriptionPanel;
	private SendPanel sendPanel;
	public void go(HasWidgets.ForIsWidget holder) {
		//crea la sua view
		view  = new StackLayoutPanel(Unit.PX);
		

		//a 'page' for sending
		sendPanel = new SendPanel();
		sendPanel.setPresenter(this);
		view.add(sendPanel,new HTML("Send"),25);

		
		//a 'page' asking for filters and a subscription trigger
		subscriptionPanel = new SubscriptionPanel();
		subscriptionPanel.setPresenter(this);
		view.add(subscriptionPanel,new HTML("Subscribe"),25);
		
		
		//insert the view
		holder.clear();
		holder.add(view);
	
		
		//this is not useful cause the connection is done in the calling presenter
		//		client.addConnectionHandler(new ConnectionEvent.Handler() {
		//		@Override
		//		public void onSuccess(ConnectionEvent e) {
		//			//update the view
		//			headerWidget.setStatusText("connected");
		//			//directly do something BUT should change
		//			//more or less this subscribe is the creation and execution of a new presenter
		//			subscribe(client,SYS_TOPIC,subscriptionsWidget);
		//			subscribe(client, DESTINATION, subscriptionsWidget);
		//		}
		//		@Override
		//		public void onFailure(ConnectionEvent e) {
		//
		//			//update the view
		//			headerWidget.setStatusText("ERROR");
		//			subscriptionsWidget.add(new HTML(e.getErrorText() + " (code: "+e.getErrorCode()+")"));
		//		}
		//		});



	}

	public void stop() { 
		//nothing to do XXX (dunno)
	}

	@Override
	public void subscribe(String topicStr) {
	
		Destination topic = new Destination(topicStr);
		

		final ResizeLayoutPanel holder = new ResizeLayoutPanel();
		final HeaderWidget headerWidget = new HeaderWidget(topicStr);

		final Subscription subscription = new Subscription(client, topic);
		final SubscriptionPresenter topicSubscriptionPresenter = new SubscriptionPresenter(subscription);

		view.add(holder, headerWidget, 25);
		
		headerWidget.setPresenter(new HeaderWidget.Presenter() {
			@Override
			public void close() {
				topicSubscriptionPresenter.stop();
				view.remove(holder);
			}
		});

		
		view.showWidget(holder);
		
		
		headerWidget.setStatusText("subscribing");
		subscriptionPanel.setSubscribing(true);
		//want to be informed about the connection status 
		subscription.subscribe( new SubscriptionHandler() {

			@Override
			public void onSubscriptionSuccess() {		
				//update the header status
				headerWidget.setStatusText("subscribed");
				subscriptionPanel.setSubscribing(false);

				//can go ... it is subscribed
				topicSubscriptionPresenter.go(holder);		

			}

			@Override
			public void onSubscriptionFailure(int erroCode, String errorText) {	
				//update the header status
				headerWidget.setStatusText("ERROR " + erroCode + ": " + errorText);  
			}
		});

		
		

		
	}

	@Override
	public void send(String topic, String message, int qos, boolean retain) {
		sendPanel.setDelivering(true);	
		MqttMessage m = MqttMessage.create(message);
		m.setDestinationName(topic);
		m.setQos(qos);
		m.setRetained(retain);
		client.send(m, this);
	}

	@Override
	public void onMessageDelivered(MqttMessage m) {
		sendPanel.setDelivering(false);
	}

}
