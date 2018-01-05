package com.jiexdrop.sanzera.entity;

/**
 * Created by jiexdrop on 04/01/18.
 */

public enum Type {
    GRASS("grass"),
    GRASS_MID("grassMid"),
    GRASS_CENTER("grassCenter"),
    GRASS_LEFT("grassLeft"),
    GRASS_RIGHT("grassRight"),
    CLOUD_1("cloud1", true),
    CLOUD_2("cloud2", true),
    CLOUD_3("cloud3",  true),
    FLAG_BLUE("flagBlue", true),
    SPIKES("spikes", true),
    WATER_MID("liquidWaterTop_mid", true),
    WATER("liquidWater", true),
    TOCH_LIT("tochLit", true);

    public String name;
    boolean col;


    Type(String n) {
        this.name = n;
        this.col = false;
    }

    Type(String n, boolean b) {
        this.name = n;
        this.col = b;
    }

    private static Type[] values = values();
    public Type next()
    {
        return values[(this.ordinal()+1) % values.length];
    }

    public Type previous()
    {
        if(this.ordinal() == 0){
            return values[values.length-1];
        }
        return values[(this.ordinal()-1) % values.length];
    }
}
