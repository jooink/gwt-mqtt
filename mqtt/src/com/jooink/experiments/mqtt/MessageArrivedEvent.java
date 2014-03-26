package com.jooink.experiments.mqtt;

import com.google.web.bindery.event.shared.Event;
import com.jooink.experiments.mqtt.lowlevel.MqttMessage;


public class MessageArrivedEvent  extends Event<MessageArrivedEvent.Handler>{
	public interface Handler {
		public void onMessageArrived(MessageArrivedEvent e);
	}

	
	
	public static final Type<Handler> TYPE = new Type<MessageArrivedEvent.Handler>();

	private MqttMessage message;
	public MessageArrivedEvent(MqttMessage message) {
		this.message = message;
	}

	public MqttMessage getMessage() {
		return message;
	}


	
	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onMessageArrived(this);
	}



	
}
