package com.prout.traintracker;

/**
 * An arriving train into a station.
 *
 * Contains the trains destination and its estimated time of arrival in seconds.
 */
public class Arrival {
    private String destination;
    private int eta;

    /**
     * Constructor
     * @param destination string of the train
     * @param eta integer in seconds
     */
    public Arrival(String destination, int eta){
        this.destination = destination;
        this.eta = eta;
    }

    public String getDestination() {
        return destination;
    }

    public int getEta() {
        return eta;
    }
}
