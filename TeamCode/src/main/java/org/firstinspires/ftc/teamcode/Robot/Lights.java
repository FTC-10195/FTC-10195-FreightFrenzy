package org.firstinspires.ftc.teamcode.Robot;

import static com.qualcomm.hardware.rev.RevBlinkinLedDriver.BlinkinPattern;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Lights extends Subsystem {
    RevBlinkinLedDriver lights;

    public final BlinkinPattern FREIGHT_DETECTED = BlinkinPattern.ORANGE;
    public final BlinkinPattern STANDARD_PATTERN = BlinkinPattern.BLUE_VIOLET;

    private BlinkinPattern pattern = STANDARD_PATTERN;

    FreightDetector freightDetector;

    static Telemetry telemetry;

    public Lights(HardwareMap hwMap) {
        lights = hwMap.get(RevBlinkinLedDriver.class, "lights");
        freightDetector = new FreightDetector(hwMap, dashboard);
    }

    public void checkBasket() {
        if (freightDetector.freightDetected()) {
            pattern = FREIGHT_DETECTED;
        } else {
            pattern = STANDARD_PATTERN;
        }
    }

    @Override
    public void subsystemLoop() {
        lights.setPattern(pattern);
    }
}