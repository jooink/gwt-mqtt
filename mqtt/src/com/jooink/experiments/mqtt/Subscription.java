package com.jooink.experiments.mqtt;

import java.util.ArrayList;

import com.google.web.bindery.event.shared.HandlerRegistration;
import com.jooink.experiments.mqtt.MessageArrivedEvent.Handler;
import com.jooink.experiments.mqtt.lowlevel.MqttMessage;
import com.jooink.experiments.mqtt.lowlevel.SubscribeOptions;
import com.jooink.experiments.mqtt.lowlevel.SubscriptionHandler;
import com.jooink.experiments.mqtt.lowlevel.UnsubscribeOptions;
import com.jooink.experiments.mqtt.lowlevel.UnsubscriptionHandler;
import com.jooink.experiments.mqtt.sample.client.Destination;

public class Subscription {

	private Destination filter;
	private Client client;
	public Subscription(Client client,Destination filter) {
		this.client = client;
		this.filter = filter;
	}	

	public void subscribe(SubscriptionHandler handler) {
		client.getMqttClient().subscribe(filter.getDestinationString(), SubscribeOptions.create(handler));
	}

	//handlers to messages in this registration
	//note: this is less efficient than listening the client so 
	//
	private ArrayList<MessageArrivedEvent.Handler> messageHandlers = new ArrayList<MessageArrivedEvent.Handler>();
	private HandlerRegistration theRealHandlerToTheBus = null;
	private Handler deliverToHandlersPerSubscription = new Handler() {

		@Override
		public void onMessageArrived(MessageArrivedEvent e) {
			MqttMessage m = e.getMessage(); 
			if(filter.matces(m.getDestinationName())) {
				for(MessageArrivedEvent.Handler h : messageHandlers)
					h.onMessageArrived(e);
			}
		}
	};
	public HandlerRegistration addMessageArrivedHandler(final MessageArrivedEvent.Handler handler) {
		if(theRealHandlerToTheBus == null)			
			theRealHandlerToTheBus =  client.addMessageArrivedHandler(deliverToHandlersPerSubscription);
		messageHandlers.add(handler);
		return new HandlerRegistration() {
			@Override
			public void removeHandler() {
				messageHandlers.remove(handler);
				if(messageHandlers.isEmpty()) {
					theRealHandlerToTheBus.removeHandler();
					theRealHandlerToTheBus = null;
				}

			}
		};
	}


	public void unsubscribe(final UnsubscriptionHandler handler) {
		client.getMqttClient().unsubscribe(filter.getDestinationString(), UnsubscribeOptions.create(new UnsubscriptionHandler() {

			@Override
			public void onUnsubscriptionSuccess() {
				theRealHandlerToTheBus.removeHandler();
				theRealHandlerToTheBus = null;
				messageHandlers.clear();
				handler.onUnsubscriptionSuccess();
			}

			@Override
			public void onUnsubscriptionFailure(int errorCode, String errorText) {
				//even when we fail we should cleanup ?!?!?!?! //XXX
				theRealHandlerToTheBus.removeHandler();
				theRealHandlerToTheBus = null;
				messageHandlers.clear();
				handler.onUnsubscriptionFailure(errorCode, errorText);
			}
		}));
	}

	public Destination getFilter() {
		return filter;
	}		
}