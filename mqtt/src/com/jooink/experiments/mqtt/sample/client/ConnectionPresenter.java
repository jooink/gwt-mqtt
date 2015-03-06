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
import com.jooink.experiments.mqtt.lowlevel.UnsubscriptionHandler;

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
		
		view.showWidget(subscriptionPanel);
		
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
		System.out.println("diconnection");
		client.disconnect();
	}

	@Override
	public void subscribe(String topicStr) {
	
		Destination topic = new Destination(topicStr);
		

		final ResizeLayoutPanel holder = new ResizeLayoutPanel();
		final HeaderWidget4Stack headerWidget = new HeaderWidget4Stack(topicStr);

		final Subscription subscription = new Subscription(client, topic);
		final SubscriptionPresenter topicSubscriptionPresenter = new SubscriptionPresenter(subscription);

		view.add(holder, headerWidget, 25);
		
		headerWidget.setPresenter(new HeaderWidget4Stack.Presenter() {
			@Override
			public void close() {
				
				topicSubscriptionPresenter.stop();
				subscription.unsubscribe(new UnsubscriptionHandler() {
					
					@Override
					public void onUnsubscriptionSuccess() {
						System.out.println("Unsubscription Done");
					}
					
					@Override
					public void onUnsubscriptionFailure(int errorCode, String errorText) {
						System.err.println("Unsubscription Error");
					}
				});
				
				
				view.remove(holder);
			}
		});

		
		view.showWidget(holder);
		
		
		headerWidget.setStatusText("subscribing");
		subscriptionPanel.setSubscribing(true);

		
		//Retained Messages will arrive immediately so we MUST
		//add the message handler BEFORE calling subscribe 
		//KLUGE: we pass the SubscriptionHandler handler that
		//must have access to headerWidget to the topicSubscriptionPresenter
		//where the message handler can be inserted
		
		//want to be informed about the connection status 
		SubscriptionHandler sh = new SubscriptionHandler() {
			@Override
			public void onSubscriptionSuccess() {		
				//update the header status
				headerWidget.setStatusText("subscribed");
				subscriptionPanel.setSubscribing(false);
			}

			@Override
			public void onSubscriptionFailure(int erroCode, String errorText) {	
				//update the header status
				headerWidget.setStatusText("ERROR " + erroCode + ": " + errorText);  
			}
		};
		
		topicSubscriptionPresenter.go(holder, sh);		


		
		

		
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
