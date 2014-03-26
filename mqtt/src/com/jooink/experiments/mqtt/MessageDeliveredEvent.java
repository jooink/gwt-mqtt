package com.jooink.experiments.mqtt;

import com.google.web.bindery.event.shared.Event;
import com.jooink.experiments.mqtt.lowlevel.MqttMessage;


public class MessageDeliveredEvent  extends Event<MessageDeliveredEvent.Handler>{
	public interface Handler {
		public void onMessageDelivered(MessageDeliveredEvent e);
	}

	
	
	public static final Type<Handler> TYPE = new Type<MessageDeliveredEvent.Handler>();

	private MqttMessage message;
	public MessageDeliveredEvent(MqttMessage message) {
		this.message = message;
	}

	public MqttMessage getMEssage() {
		return message;
	}


	
	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onMessageDelivered(this);
	}



	
}
