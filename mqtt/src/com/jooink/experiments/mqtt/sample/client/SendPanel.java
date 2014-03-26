package com.jooink.experiments.mqtt.sample.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class SendPanel extends Composite {

	
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

	interface PanelUiBinder extends UiBinder<Widget, SendPanel> {}

	public SendPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	


	
	@UiField
	HasValue<String> topicSnd;
	
	@UiField
	HasValue<String> msgSnd;


	@UiField
	ListBox qosSnd;

	@UiField
	CheckBox retainSnd;
	
	@UiField
	HasClickHandlers sendTrigger;
	
	@UiHandler("sendTrigger")
	void onSendClick(ClickEvent e) {
		
		
		
		presenter.send(topicSnd.getValue(),msgSnd.getValue(), qosSnd.getSelectedIndex(), retainSnd.getValue());
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
