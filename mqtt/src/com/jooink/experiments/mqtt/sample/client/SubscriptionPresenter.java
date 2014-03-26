package com.jooink.experiments.mqtt.sample.client;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.CustomScrollPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.jooink.experiments.mqtt.MessageArrivedEvent;
import com.jooink.experiments.mqtt.Subscription;
import com.jooink.experiments.mqtt.lowlevel.MessageDeliveredHandler;
import com.jooink.experiments.mqtt.lowlevel.MqttMessage;
import com.jooink.experiments.mqtt.lowlevel.UnsubscriptionHandler;

public class SubscriptionPresenter implements UnsubscriptionHandler, SendShortPanel.Presenter, MessageDeliveredHandler {

	private Subscription subscription;
	//private Client client;


	public SubscriptionPresenter(Subscription subscription) {
		this.subscription = subscription;
	}

	public void stop() {		
		if(subscription != null) {
			subscription.unsubscribe(this);
		}	
	}


	SendShortPanel shortSend;
	
	
	public void go(HasWidgets.ForIsWidget holder) {


		final LayoutPanel outer = new LayoutPanel();
		final FlowPanel view = new FlowPanel();
		subscription.addMessageArrivedHandler(new MessageArrivedEvent.Handler() {

			@Override
			public void onMessageArrived(MessageArrivedEvent e) {
				if( subscription.getFilter().isWildcard() ) 
					view.insert(new HTML("<b>"+e.getMessage().getDestinationName()+"</b> <pre>" + e.getMessage().getPayloadString()+"</pre>"),0);
				else
					view.insert(new HTML("<pre>" + e.getMessage().getPayloadString()+"</pre>"),0);

			}
		});

		holder.clear();
		outer.add(new CustomScrollPanel(view));
		if(! subscription.getFilter().isWildcard() ) {
			shortSend = new SendShortPanel(subscription.getFilter().getDestinationString());
			shortSend.setPresenter(this);
			outer.add(shortSend);
			outer.setWidgetBottomHeight(shortSend, 0, Unit.PX, 100, Unit.PX);
		}
		holder.add(outer);	
	}

	@Override
	public void onUnsubscriptionSuccess() {
		System.out.println("unSubscription Success");
	}

	@Override
	public void onUnsubscriptionFailure(int errorCode, String errorText) {
		System.err.println("unSubscription ERROR");

	}
	
	
	
	@Override
	public void send(String topic, String message, int qos, boolean retain) {
		shortSend.setDelivering(true);	
		MqttMessage m = MqttMessage.create(message);
		m.setDestinationName(topic);
		m.setQos(qos);
		m.setRetained(retain);
		subscription.getClient().send(m, this);
	}

	@Override
	public void onMessageDelivered(MqttMessage m) {
		shortSend.setDelivering(false);
	}


}
