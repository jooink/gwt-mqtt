package com.jooink.experiments.mqtt.lowlevel;

import java.util.HashSet;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Timer;

public class FakeMqttClient implements IMqttClient {

	private String server;
	private int port;
	private String clientId;
	private ConnectionLostHandler connetionLostHandler;

	private MessageArrivedHandler messageArrivedHandler;
	private MessageDeliveredHandler messageDeliveredHandler;

	public FakeMqttClient(String server, int port, String clientId) {

		this.server = server;
		this.port = port;
		this.clientId = clientId;
	}

	@Override
	public void setConnectionLostHandler(ConnectionLostHandler h) {
		this.connetionLostHandler = h;
	}

	@Override
	public void setMessageHandler(MessageArrivedHandler h) {
		this.messageArrivedHandler = h;
	}

	@Override
	public void setMessageDeliveredHandler(MessageDeliveredHandler h) {
		this.messageDeliveredHandler = h;
	}


	private int num;
	private Timer timer = new Timer() {

		private int last = 0;
		@Override
		public void run() {
			if(messageArrivedHandler == null)
				return;

			if(subscribedFilters.isEmpty())
				return;

			if(last >= subscribedFilters.size()) {
				last = 0;
			}

			MqttMessage m = MqttMessage.create(" Fake Message " + (num++) );
			m.setDestinationName((String)subscribedFilters.toArray()[last]);
			messageArrivedHandler.onMessageArrived(m);
			last++;
		}

	};


	private static native void call_onSuccess(ConnectOptions o) /*-{
	    o.onSuccess();
	}-*/;

	@Override
	public void connect(final ConnectOptions co) {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				call_onSuccess(co);
				timer.scheduleRepeating(1000);
			}
		});
	}

	@Override
	public void disconnect() {
		timer.cancel();
		//:)
	}

	HashSet<String> subscribedFilters = new HashSet<String>();
	private static native void call_onSubscriptionSuccess(SubscribeOptions o) /*-{
    	o.onSubscriptionSuccess();
	}-*/;
	@Override
	public void subscribe(String filter,final SubscribeOptions options) {
		subscribedFilters.add(filter);
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				call_onSubscriptionSuccess(options);
			}
		});
	}


	private static native void call_onUnsubscriptionSuccess(UnsubscribeOptions o) /*-{
		o.onUnsubscriptionSuccess();
	}-*/;

	@Override
	public void unsubscribe(String filter, final UnsubscribeOptions options) {
		subscribedFilters.remove(filter);
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				call_onUnsubscriptionSuccess(options);
			}
		});
	}

	@Override
	public void send(final MqttMessage m) {

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {

				if(messageDeliveredHandler != null) 
					messageDeliveredHandler.onMessageDelivered(m);

				//me
				//XXX actually we should provide a real match for wildcard topics
				if(subscribedFilters.contains(m.getDestinationName()) && messageArrivedHandler != null) {
					messageArrivedHandler.onMessageArrived(m);
				}

			}
		});


	}

}
