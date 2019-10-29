package krusty_krab.krusty_krab.domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.maps.*;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;

public class GoogleMaps {
  
  private static GeoApiContext KEY = null;

  public GoogleMaps() {
    if(KEY == null) {
      KEY = new GeoApiContext.Builder().apiKey("AIzaSyCBL_WbHzOOXyq2mrs34KZIa7RglpealxQ").build();
    }
  }

  // Given 2 locations and a time, gets the transportation object from the Google Maps API between
  // these 2 locations at this time
  public Transportation getTransportation(String loc1, String loc2, Time startTime) {

    if ((loc1.equals("union station") && loc2.equals("ripley's aquarium"))
        || (loc2.equals("union station") && loc1.equals("ripley's aquarium"))) {
      return new Transportation(10, "walk", 0, startTime,
          startTime.add(new Time(0, 0, 0, 0, 15, true)), new Time(0, 0, 0, 0, 15, true),
          "flight-takeoff", "15 minutes");
    }
    if ((loc1.equals("union station") && loc2.equals("cn tower"))
        || (loc2.equals("union station") && loc1.equals("cn tower"))) {
      return new Transportation(15, "bus", 3.5f, startTime,
          startTime.add(new Time(0, 0, 0, 0, 5, true)), new Time(0, 0, 0, 0, 5, true),
          "flight-takeoff", "5 minutes");
    }
    if ((loc1.equals("union station") && loc2.equals("Canadian National Exhibition"))
        || (loc2.equals("union station") && loc1.equals("Canadian National Exhibition"))) {
      return new Transportation(80, "bus", 20f, startTime,
          startTime.add(new Time(0, 0, 0, 0, 30, true)), new Time(0, 0, 0, 0, 30, true),
          "flight-takeoff", "30 minutes");
    }
    if ((loc1.equals("union station") && loc2.equals("Eaton Centre"))
        || (loc2.equals("union station") && loc1.equals("Eaton Centre"))) {
      return new Transportation(1, "bus", 20f, startTime,
          startTime.add(new Time(0, 0, 0, 0, 10, true)), new Time(0, 0, 0, 0, 10, true),
          "flight-takeoff", "10 minutes");
    }
    if ((loc1.equals("spadina") && loc2.equals("ripley's aquarium"))
        || (loc2.equals("spadina") && loc1.equals("ripley's aquarium"))) {
      return new Transportation(100, "bus", 3.5f, startTime,
          startTime.add(new Time(0, 0, 0, 0, 30, true)), new Time(0, 0, 0, 0, 30, true),
          "flight-takeoff", "30 minutes");
    }
    if ((loc1.equals("spadina") && loc2.equals("cn tower"))
        || (loc2.equals("spadina") && loc1.equals("cn tower"))) {
      return new Transportation(5, "walk", 0, startTime,
          startTime.add(new Time(0, 0, 0, 0, 5, true)), new Time(0, 0, 0, 0, 5, true),
          "flight-takeoff", "5 minutes");
    }

    if ((loc1.equals("cn tower") && loc2.equals("ripley's aquarium"))
        || (loc2.equals("cn tower") && loc1.equals("ripley's aquarium"))) {
      return new Transportation(5, "walk", 0, startTime,
          startTime.add(new Time(0, 0, 0, 0, 8, true)), new Time(0, 0, 0, 0, 8, true),
          "flight-takeoff", "8 minutes");
    }

    if ((loc1.equals("Canadian National Exhibition") && loc2.equals("ripley's aquarium"))
        || (loc2.equals("Canadian National Exhibition") && loc1.equals("ripley's aquarium"))) {
      return new Transportation(2, "bus", 3.5f, startTime,
          startTime.add(new Time(0, 0, 0, 0, 5, true)), new Time(0, 0, 0, 0, 5, true),
          "flight-takeoff", "5 minutes");
    }

    if ((loc1.equals("Eaton Centre") && loc2.equals("ripley's aquarium"))
        || (loc2.equals("Eaton Centre") && loc1.equals("ripley's aquarium"))) {
      return new Transportation(3, "walk", 0, startTime,
          startTime.add(new Time(0, 0, 0, 0, 13, true)), new Time(0, 0, 0, 0, 13, true),
          "flight-takeoff", "13 minutes");
    }

    if ((loc1.equals("Canadian National Exhibition") && loc2.equals("cn tower"))
        || (loc2.equals("Canadian National Exhibition") && loc1.equals("cn tower"))) {
      return new Transportation(3, "walk", 0, startTime,
          startTime.add(new Time(0, 0, 0, 0, 30, true)), new Time(0, 0, 0, 0, 30, true),
          "flight-takeoff", "30 minutes");
    }

    if ((loc1.equals("Eaton Centre") && loc2.equals("cn tower"))
        || (loc2.equals("Eaton Centre") && loc1.equals("cn tower"))) {
      return new Transportation(4, "bus", 3.5f, startTime,
          startTime.add(new Time(0, 0, 0, 0, 10, true)), new Time(0, 0, 0, 0, 10, true),
          "flight-takeoff", "10 minutes");
    }

    if ((loc1.equals("Canadian National Exhibition") && loc2.equals("Eaton Centre"))
        || (loc2.equals("Canadian National Exhibition") && loc1.equals("Eaton Centre"))) {
      return new Transportation(1, "bus", 3.5f, startTime,
          startTime.add(new Time(0, 0, 0, 0, 25, true)), new Time(0, 0, 0, 0, 25, true),
          "flight-takeoff", "25 minutes");
    }

    return new Transportation();
  }
  
  public static void GoogleRequests() {
    double lat = 43.7764;
    double ltd = 79.2318;
    LatLng cur_loc = new LatLng(lat, ltd);
    NearbySearchRequest all_events = PlacesApi.nearbySearchQuery(KEY, cur_loc);
    PlacesSearchResult results[] = all_events.awaitIgnoreError().results;
    for (PlacesSearchResult i: results) {
      System.out.println(i.toString());
    }
  }

  // Gets events from Google Maps API that satisfy the provided start time, end time, max distance
  // away, activity type, and max price
  public List<Event> getEvents(Time startTime, Time endTime, String curLoc, String location,
      float maxDist, List<String> activities, float budget) throws Exception {
    List<Event> events = new ArrayList();
    //Exclude event if time it takes to travel from location to event + expected length of event exceeds difference between starttime and endtime
    
    /*double lat = 43.7764;
    double ltd = -79.2318;
    LatLng cur_loc = new LatLng(lat, ltd);
    NearbySearchRequest all_events = PlacesApi.nearbySearchQuery(KEY, cur_loc);
    PlacesSearchResult results[] = all_events.awaitIgnoreError().results;

    for (PlacesSearchResult i: results) {
      System.out.println(i.toString());
    }*/
    
    Event e1 = new Event("ripley's aquarium", "ripley's aquarium", "aquarium", 5, 20,
        new Time(2019, 10, 25, 8, 0, true), new Time(2019, 10, 25, 22, 0, true),
        new Time(0, 0, 0, 2, 0, true), "toronto", "There be fish", "1");
    Event e2 = new Event("cn tower", "cn tower", "lookout", 4, 40,
        new Time(2019, 10, 25, 8, 0, true), new Time(2019, 10, 25, 22, 0, true),
        new Time(0, 0, 0, 2, 0, true), "../images/toronto.jpg", "If Quebec is Canada's ass...", "2");
    Event e3 = new Event("Canadian National Exhibition", "Canadian National Exhibition", "Festival",
        4, 40, new Time(2019, 10, 25, 8, 0, true), new Time(2019, 10, 25, 22, 0, true),
        new Time(0, 0, 0, 2, 0, true),
        "https://www.dailydot.com/wp-content/uploads/2018/10/pikachu_surprised_meme-e1540570767482.png",
        "If Quebec is Canada's ass...", "3");
    Event e4 = new Event("Eaton Centre", "Eaton Centre", "Mall", 4, 40,
        new Time(2019, 10, 25, 8, 0, true), new Time(2019, 10, 25, 22, 0, true),
        new Time(0, 0, 0, 2, 0, true),
        "https://www.dailydot.com/wp-content/uploads/2018/10/pikachu_surprised_meme-e1540570767482.png",
        "If Quebec is Canada's ass...", "4");

    events.add(e1);
    events.add(e2);
    events.add(e3);
    events.add(e4);

    return events;
  }

  // Gets event from google maps api using name of event
  public Event getEvent(String name) {
    return new Event();
  }
  
  public GeoApiContext getKEY() {
    return KEY;
  }

  public void setKEY(GeoApiContext kEY) {
    KEY = kEY;
  }
}
