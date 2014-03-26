package com.jooink.experiments.mqtt.sample.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

public class MainPanel extends Composite {

	
	public interface Presenter {

		void connect(String hostname, int port, String clientId,
				String username, String password);
		
	}
	
	Presenter presenter;
	
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	private static MainPanelUiBinder uiBinder = GWT.create(MainPanelUiBinder.class);

	interface MainPanelUiBinder extends UiBinder<Widget, MainPanel> {}

	public MainPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	HasValue<String> hostname;

	@UiField
	HasValue<Integer> port;
	@UiField
	HasValue<String> clientId;
	
	@UiField
	HasValue<String> username;

	@UiField
	HasValue<String> password;

	@UiField
	HasClickHandlers createServerTrigger;
	
	@UiHandler("createServerTrigger")
	void onClick(ClickEvent e) {
		presenter.connect(hostname.getValue(), port.getValue(), clientId.getValue(), username.getValue(), password.getValue());
	}


}
