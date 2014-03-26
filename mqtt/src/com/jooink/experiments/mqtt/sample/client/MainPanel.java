package com.jooink.experiments.mqtt.sample.client;

import java.util.Random;

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
				String username, String password, int keepAlive);
		
	}
	
	Presenter presenter;
	
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	private static MainPanelUiBinder uiBinder = GWT.create(MainPanelUiBinder.class);

	interface MainPanelUiBinder extends UiBinder<Widget, MainPanel> {}

	public MainPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		
		
		//generate a random clientid for testing purposes
		StringBuilder sb = new StringBuilder();
		Random random = new Random();

		for (int i=0;i<5;i++) {
		    sb.append('a'+random.nextInt(26));
		}
		String code = sb.toString();
		clientId.setValue(code);
		
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
	HasValue<Integer> keepAlive;
	
	@UiField
	HasClickHandlers createServerTrigger;
	
	@UiHandler("createServerTrigger")
	void onClick(ClickEvent e) {
		presenter.connect(hostname.getValue(), port.getValue(), clientId.getValue(), username.getValue(), password.getValue(), keepAlive.getValue());
	}


}
