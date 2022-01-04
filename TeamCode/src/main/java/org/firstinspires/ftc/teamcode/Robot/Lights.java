package org.firstinspires.ftc.teamcode.Robot;

import static com.qualcomm.hardware.rev.RevBlinkinLedDriver.BlinkinPattern;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Lights extends Subsystem {
    RevBlinkinLedDriver lights;
    private BlinkinPattern pattern;

    public final BlinkinPattern freightDetected = BlinkinPattern.RAINBOW_RAINBOW_PALETTE;
    public final BlinkinPattern redAlliance = BlinkinPattern.LIGHT_CHASE_RED;
    public final BlinkinPattern blueAlliance = BlinkinPattern.LIGHT_CHASE_BLUE;

    public Lights(HardwareMap hwMap) {
        lights = hwMap.get(RevBlinkinLedDriver.class, "lights");
    }

    public void setPattern(BlinkinPattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public void runMotorsAndServos() {
        lights.setPattern(pattern);
    }
}