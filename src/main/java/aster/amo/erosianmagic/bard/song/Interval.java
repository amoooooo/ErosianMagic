package aster.amo.erosianmagic.bard.song;

import aster.amo.erosianmagic.ErosianMagic;

public record Interval(float time){
    public static final Interval WHOLE = new Interval(1);
    public static final Interval HALF = new Interval(0.5f);
    public static final Interval QUARTER = new Interval(0.25f);
    public static final Interval EIGHTH = new Interval(0.125f);
    public static final Interval SIXTEENTH = new Interval(0.0625f);
    public static final Interval ZERO = new Interval(0);

    public boolean isInTime(float time, float fudge){
        // if the time is within the interval +- the fudge
        return time <= this.time + fudge;
    }

    public boolean isInPerfectTime(float time, float fudge){
        // if the time is within the interval +- the fudge
        float toBeInTime = 0f;
        if(time > this.time + fudge){
            toBeInTime = -(time - this.time);
        } else if(time < this.time - fudge){
            toBeInTime = this.time - time;
        }
        ErosianMagic.LOGGER.info("Time: " + time + " To be in time: " + toBeInTime);
        return time <= this.time + fudge && time >= this.time - fudge;
    }
}

