package com.company;

public class Visitor extends User {

    public static final int rate = 15;
    public static final String TYPE = "Visitor";

    public Visitor(){
        super();
        setType(TYPE);
    }
}
