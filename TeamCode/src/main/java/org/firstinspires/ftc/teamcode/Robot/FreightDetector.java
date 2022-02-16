package org.firstinspires.ftc.teamcode.Robot;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Config
public class FreightDetector extends Subsystem {
    public ColorSensor colorSensor;

    public FreightDetector(HardwareMap hwMap, FtcDashboard dashboard) {
        colorSensor = hwMap.get(ColorSensor.class, "freightDetector");
        colorSensor.enableLed(true);
    }

    public boolean freightDetected() {
        // if some condition is met then return true
        // TODO: add condition
        if (colorSensor.blue() > 200) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void subsystemLoop() {

    }
}