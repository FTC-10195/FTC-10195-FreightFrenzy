package org.firstinspires.ftc.teamcode.Robot;

import static com.qualcomm.hardware.rev.RevBlinkinLedDriver.BlinkinPattern;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Lights extends Subsystem {
    RevBlinkinLedDriver lights;
    private BlinkinPattern pattern;

    public final BlinkinPattern FREIGHT_DETECTED = BlinkinPattern.RAINBOW_RAINBOW_PALETTE;
    public final BlinkinPattern STANDARD_PATTERN = BlinkinPattern.CP1_2_END_TO_END_BLEND;

    FreightDetector freightDetector;

    public Lights(HardwareMap hwMap) {
        lights = hwMap.get(RevBlinkinLedDriver.class, "lights");
        freightDetector = new FreightDetector(hwMap, dashboard);
    }

    public void setPattern(BlinkinPattern pattern) {
        this.pattern = pattern;
    }

    public void freightDetected() {
        if (freightDetector.freightDetected()) {
            lights.setPattern(FREIGHT_DETECTED);
        } else {
            lights.setPattern(STANDARD_PATTERN);
        }
    }

    @Override
    public void subsystemLoop() {
        lights.setPattern(pattern);
    }
}