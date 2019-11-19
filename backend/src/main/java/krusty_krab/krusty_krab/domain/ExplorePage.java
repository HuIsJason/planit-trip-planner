package krusty_krab.krusty_krab.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExplorePage {

	private ArrayList<Event> events;
	
	public ExplorePage() {
		events = new ArrayList<Event>();
	}
	
	public void generateExplorePage() {
		Random random = new Random();
		List<Event> allEvents = GoogleMaps.getEventsFromMongo();
		ArrayList<Event> newEvents = new ArrayList<Event>();
		while (newEvents.size() < 10) {
			Event tempEvent = allEvents.get(random.nextInt(allEvents.size()));
			if (!events.contains(tempEvent) && !newEvents.contains(tempEvent)) {
				newEvents.add(tempEvent);
			}
		}
		events = newEvents;
	}

	public ArrayList<Event> getEvents() {
		return events;
	}

	public void setEvents(ArrayList<Event> events) {
		this.events = events;
	}
	
	
}
