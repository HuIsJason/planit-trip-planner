package krusty_krab.krusty_krab.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Itinerary {
    private Time startTime;
    private Time endTime;
    private String home;
    private String location;
    private float maxDist;
    private List<String> activities;
    private float budget;
    private List<ItineraryItem> itin = new ArrayList<ItineraryItem>();
    private List<String> visitedEvents = new ArrayList<String>();

    public float minScore = 0.0f;
    public GoogleMaps gm = new GoogleMaps();

    public Itinerary() {
    }

    // Gets the next best event for the user to attend
    public Event getNextBestEvent() throws Exception{
        Time curTime;
        String curLoc = "";
        if(this.getItin().size() == 0){
            curTime = this.getStartTime();
            curLoc = this.getHome();
        }
        else {
            curTime = this.getItin().get(this.getItin().size() - 1).getEndTime();
            ItineraryItem lastItem = this.getItin().get(this.getItin().size() - 1);
            if(lastItem instanceof Event){
                curLoc = ((Event)(lastItem)).getLocation();
            }
        }

        // Gets every event that satisfies the given filters
        List<Event> events = this.gm.getEvents(curTime, this.getEndTime(), curLoc, this.getLocation(), this.getMaxDist(), this.getActivities(), this.getBudget());

        // Gets event with highest score of all events received
        Event bestEvent = events.get(0);
        for(Event e: events){
            if((!this.getVisitedEvents().contains(e.getLocation())) && (e.getScore(curTime, curLoc, this.gm, this.getMaxDist(), this.getBudget()) > bestEvent.getScore(curTime, curLoc, this.gm, this.getMaxDist(), this.getBudget()))){
                bestEvent = e;
            }
        }
        // If no event exists that is not already in the itinerary, exception thrown
        if(this.getVisitedEvents().contains(bestEvent.getLocation())){
            throw new NoSuchElementException();
        }
        return bestEvent;
    }

    // Creates the itinerary
    public void createItinerary() throws Exception{
        // Starts at the specified start time, at the users home
        Time curTime = getStartTime();
        String curLoc = getHome();
        try{
            // Gets first event
            Event nextEvent = getNextBestEvent();

            // Loops until it runs out of events, or all events remaining has a score so low that they shouldn't be on the itinerary
            while(nextEvent.getScore(curTime, curLoc, this.gm, this.getMaxDist(), this.getBudget()) > minScore){
                // Gets transportation object from the next event and the current location of the user
                Transportation transp = this.gm.getTransportation(curLoc, nextEvent.getLocation(), curTime);
                // Transporation object to begin at the current time
                transp.setStartTime(curTime);
                curTime = curTime.add(transp.getExpectedLength());
                transp.setEndTime(curTime);
                //Sets next event to begin after the expected length of the transportation
                nextEvent.setStartTime(curTime);

                // Transporation and event objects are added to the itinerary
                this.itin.add(transp);
                this.itin.add(nextEvent);
                // Event added to list to indicate that it is already in the itinerary
                this.visitedEvents.add(nextEvent.getLocation());

                //Current time updated to after event is over, current location updated to event location
                curLoc = nextEvent.getLocation();
                curTime = curTime.add(nextEvent.getExpectedLength());
                nextEvent.setEndTime(curTime);
                // Gets next event
                nextEvent = getNextBestEvent();
            }
        }
        catch(NoSuchElementException e){}
        // Gets transportation object from last event, back to home, and adds it to the itinerary
        Transportation transp = this.gm.getTransportation(curLoc, getHome(), curTime);
        transp.setStartTime(curTime);
        this.itin.add(transp);
    }
    
    private void deleteEvent() {
	// TODO: delete event from itinerary
	// call joinEvents(startEvent, nextEvent) after to make transportation
    }
    
    private void joinEvents(Event startEvent, Event nextEvent) {
	Transportation transportation = gm.getTransportation(startEvent.getLocation(), nextEvent.getLocation(), startEvent.getEndTime());
	itin.add(transportation);
    }

    public List<ItineraryItem> getItin() {
        return itin;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public String getHome() {
        return home;
    }

    public String getLocation() {
        return location;
    }

    public float getMaxDist() {
        return maxDist;
    }

    public List<String> getActivities() {
        return activities;
    }

    public float getBudget() {
        return budget;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMaxDist(float maxDist) {
        this.maxDist = maxDist;
    }

    public void setActivities(List<String> activities) {
        this.activities = activities;
    }

    public void setBudget(float budget) {
        this.budget = budget;
    }

    public void setItin(List<ItineraryItem> itin) {
        this.itin = itin;
    }

    public List<String> getVisitedEvents() {
        return visitedEvents;
    }

    public float getItinCost() {
        float cost = 0;
        for(ItineraryItem i: getItin()){
            cost+=i.getPrice();
        }
        return cost;
    }
}
