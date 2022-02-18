package org.firstinspires.ftc.teamcode.Robot;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Config
public class FreightDetector extends Subsystem {
    public static ColorSensor colorSensor;

    public FreightDetector(HardwareMap hwMap, FtcDashboard dashboard) {
        colorSensor = hwMap.get(ColorSensor.class, "freightDetector");
        colorSensor.enableLed(true);
    }

    public boolean freightDetected() {
        // if some condition is met then return true
        // TODO: add condition
        return colorSensor.red() > 200 || colorSensor.green() > 200 ||
                colorSensor.blue() > 200 || colorSensor.alpha() > 200;
    }

    @Override
    public void subsystemLoop() {

    }
}