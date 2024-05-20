package com.prout.traintracker;

/**
 * Represents a Tube line and it's running status
 */
public class TubeLine {
    private String name;
    private String status;

    /**
     * Constructor.
     *
     * @param name name of the line
     * @param status running status of the line (good service, minor delay etc)
     */
    public TubeLine(String name, String status){
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }
}
