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

public class SubscriptionPanel extends Composite {

	
	public interface Presenter {
		void subscribe(String topic);		
	}
	
	Presenter presenter;
	
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	
	public interface Style extends CssResource {
		public String subscribingStyle();
	}
	
	@UiField
	Style style;
	
	private static PanelUiBinder uiBinder = GWT.create(PanelUiBinder.class);

	interface PanelUiBinder extends UiBinder<Widget, SubscriptionPanel> {}

	public SubscriptionPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	
	

	@UiField
	HasValue<String> topic;
		
	
	@UiField
	HasClickHandlers subscribeTrigger;
	@UiHandler("subscribeTrigger")
	void onClick(ClickEvent e) {
		presenter.subscribe(topic.getValue());
	}

	
	
	@UiField 
	HTMLPanel subscribePanel;
	
	public void setSubscribing(boolean delivering) {
		if(delivering) {
			subscribePanel.addStyleName(style.subscribingStyle());
		} else {
			subscribePanel.removeStyleName(style.subscribingStyle());
		}
	}
	

}
