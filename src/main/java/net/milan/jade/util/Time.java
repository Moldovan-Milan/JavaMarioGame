package net.milan.jade.util;

public class Time {
    public static float timeStarted = System.nanoTime();

    public static float getTime(){
        return (float) ((System.nanoTime() - timeStarted) * 1E-9); // Convert nanoseconds to seconds
    }
}
