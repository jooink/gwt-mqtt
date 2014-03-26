package com.jooink.experiments.mqtt.sample.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellWidget;

public class DestinationDisplay extends CellWidget<Destination> {

	private static final Cell<Destination> cell = new AbstractCell<Destination>() {

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context, Destination value, SafeHtmlBuilder sb) {
			sb.appendHtmlConstant(value.getDestinationString());
		}

	};
	
	public DestinationDisplay(Destination initialValue) {
		super(cell, initialValue);
	}
	
}
