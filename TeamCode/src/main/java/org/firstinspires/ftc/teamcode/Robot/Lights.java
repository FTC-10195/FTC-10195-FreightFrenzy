package org.firstinspires.ftc.teamcode.Robot;

import static com.qualcomm.hardware.rev.RevBlinkinLedDriver.BlinkinPattern;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Lights extends Subsystem {
    RevBlinkinLedDriver lights;
    private BlinkinPattern pattern;

    public final BlinkinPattern FREIGHT_DETECTED = BlinkinPattern.RAINBOW_RAINBOW_PALETTE;
    public final BlinkinPattern RED_ALLIANCE = BlinkinPattern.RAINBOW_LAVA_PALETTE;
    public final BlinkinPattern BLUE_ALLIANCE = BlinkinPattern.RAINBOW_OCEAN_PALETTE;

    public Lights(HardwareMap hwMap) {
        lights = hwMap.get(RevBlinkinLedDriver.class, "lights");
    }

    public void setPattern(BlinkinPattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public void subsystemLoop() {
        lights.setPattern(pattern);
    }
}