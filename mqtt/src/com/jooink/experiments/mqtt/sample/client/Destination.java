package com.jooink.experiments.mqtt.sample.client;



/*
 * a destination, is essentially a string but has a regexp representation useful for filtering
 */
public class Destination {

	private boolean wildcard;
	private String regexp = null;
	private String destination;
	public Destination(String destination) {
		this.destination = destination;
		wildcard = destination.contains("+") || destination.contains("#");
		if(wildcard)
			regexp = destination.replace("$","\\$").replace("#", ".*").replace("+", "[^/]");
	}
	
	public boolean matces(String other) {
		return wildcard ? other.matches(regexp) : destination.equals(other);
	}
	
	public boolean isWildcard() {
		return wildcard;
	}
	
	public boolean canSend() {
		return !wildcard;
	}
	
	public String getDestinationString() {
		return destination;
	}
	
}
