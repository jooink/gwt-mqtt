package com.jooink.experiments.mqtt.sample.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class HeaderWidget extends Composite {

	
	public interface Presenter {
		public void close();
	}
	
	
	private static HeaderWidgetUiBinder uiBinder = GWT.create(HeaderWidgetUiBinder.class);

	interface HeaderWidgetUiBinder extends UiBinder<Widget, HeaderWidget> {
	}


	private Presenter presenter;

	
	public HeaderWidget(String titlestr) {
		initWidget(uiBinder.createAndBindUi(this));
		title.setText(titlestr);
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter; 
	}

	@UiField
	HasClickHandlers trigger;

	@UiField
	HasText title;
	
	@UiField
	HasText status;

	@UiHandler("trigger")
	void onClick(ClickEvent e) {
		presenter.close();
	}

	public void setTitle(String text) {
		title.setText(text);
	}

	public void setStatusText(String string) {
		status.setText(string);
	}


}
