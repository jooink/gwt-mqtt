package com.jooink.experiments.mqtt.sample.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

public class SendShortPanel extends Composite {

	
	public interface Presenter {
		void send(String topic, String message, int qos, boolean retain);		
	}
	
	Presenter presenter;
	
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	
	public interface Style extends CssResource {
		public String deliveringStyle();
	}
	
	@UiField
	Style style;
	
	private static PanelUiBinder uiBinder = GWT.create(PanelUiBinder.class);

	interface PanelUiBinder extends UiBinder<Widget, SendShortPanel> {}

	
	String topic;
	public SendShortPanel(String topic) {
		this.topic = topic;
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	


	
	@UiField
	HasValue<String> msgSnd;

	@UiField
	HasClickHandlers sendTrigger;
	
	@UiHandler("sendTrigger")
	void onSendClick(ClickEvent e) {

		presenter.send(topic,msgSnd.getValue(), 2, false);
	}
	
	
	@UiField 
	HTMLPanel sendPanel;
	
	public void setDelivering(boolean delivering) {
		if(delivering) {
			sendPanel.addStyleName(style.deliveringStyle());
		} else {
			sendPanel.removeStyleName(style.deliveringStyle());
		}
	}
	

}
