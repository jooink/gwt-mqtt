package com.jooink.experiments.mqtt;

import com.google.web.bindery.event.shared.Event;


public class ConnectionLostEvent  extends Event<ConnectionLostEvent.Handler>{
	public interface Handler {
		public void onConnectionLost(ConnectionLostEvent e);
	}

	
	public static final Type<Handler> TYPE = new Type<ConnectionLostEvent.Handler>();
	private int errorCode;
	private String errorText;
	
	public ConnectionLostEvent(int errorCode, String errorText) {
		this.errorCode = errorCode;
		this.errorText = errorText;
	}
	

	public int getErrorCode() {
		return errorCode;
	}

	public String getErrorText() {
		return errorText;
	}


	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onConnectionLost(this);
	}

	
}
