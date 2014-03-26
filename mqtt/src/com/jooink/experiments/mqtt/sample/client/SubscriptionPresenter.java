package com.jooink.experiments.mqtt.sample.client;

import com.google.gwt.user.client.ui.CustomScrollPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.jooink.experiments.mqtt.MessageArrivedEvent;
import com.jooink.experiments.mqtt.Subscription;
import com.jooink.experiments.mqtt.lowlevel.UnsubscriptionHandler;

public class SubscriptionPresenter implements UnsubscriptionHandler {

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


	public void go(HasWidgets.ForIsWidget holder) {
		final FlowPanel view = new FlowPanel();

		subscription.addMessageArrivedHandler(new MessageArrivedEvent.Handler() {

			@Override
			public void onMessageArrived(MessageArrivedEvent e) {
					view.insert(new HTML("<b>"+e.getMessage().getDestinationName()+"</b> <pre>" + e.getMessage().getPayloadString()+"</pre>"),0);
			}
		});
		
		holder.clear();
		holder.add(new CustomScrollPanel(view));	
	}

	@Override
	public void onUnsubscriptionSuccess() {
		System.out.println("unSubscription Success");
	}

	@Override
	public void onUnsubscriptionFailure(int errorCode, String errorText) {
		System.err.println("unSubscription ERROR");

	}

}
