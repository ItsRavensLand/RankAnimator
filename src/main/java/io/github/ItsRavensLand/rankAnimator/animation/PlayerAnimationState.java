package io.github.ItsRavensLand.rankAnimator.animation;

import java.util.UUID;

public class PlayerAnimationState {

    private final UUID uuid;
    private Animation animation;

    public PlayerAnimationState(UUID uuid, Animation animation) {
        this.uuid = uuid;
        this.animation = animation;
    }

    public void tick() {
        animation.tick();
    }

    public String getCurrentPrefix() {
        return animation.getCurrentFrame();
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public UUID getUuid() { return uuid; }
}
