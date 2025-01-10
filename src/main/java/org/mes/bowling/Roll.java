package org.mes.bowling;

public class Roll {
    private int frame;
    private int rollInFrame;
    private int pins;  // number of pins toppled on roll
    private boolean strike;
    private boolean spare;

    public Roll(int frame, int rollInFrame, int pins, boolean strike, boolean spare, boolean extraRoll) {
        this.frame = frame;
        this.rollInFrame = rollInFrame;
        this.pins = pins;
        this.strike = strike;
        this.spare = spare;
    }

    public int getFrame() { return frame; }

    public int getRollInFrame() { return rollInFrame; }

    public int getPins() { return pins; }

    public boolean isStrike() { return strike; }

    public boolean isSpare() { return spare; }


    @Override
    public String toString() {
        return "Roll{" +
                "frame=" + frame +
                ", rollInFrame=" + rollInFrame +
                ", pins=" + pins +
                ", strike=" + strike +
                ", spare=" + spare +
                '}';
    }
}
