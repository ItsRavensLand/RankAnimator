package io.github.ItsRavensLand.rankAnimator.animation;

import java.util.List;

public class Animation {

    private final String name;
    private final List<String> frames;
    private final int interval;

    private int currentFrame = 0;
    private int tickCounter = 0;

    public Animation(String name, List<String> frames, int interval) {
        this.name = name;
        this.frames = frames;
        this.interval = interval;
    }

    public boolean tick() {
        if (++tickCounter >= interval) {
            tickCounter = 0;
            currentFrame = (currentFrame + 1) % frames.size();
            return true;
        }
        return false;
    }

    public String getCurrentFrame() {
        return frames.get(currentFrame);
    }

    // independent copy per player
    public Animation copy() {
        return new Animation(name, frames, interval);
    }

    public String getName() { return name; }
    public List<String> getFrames() { return frames; }
    public int getInterval() { return interval; }
}
