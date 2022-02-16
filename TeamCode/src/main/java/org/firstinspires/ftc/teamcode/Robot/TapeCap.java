package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class TapeCap extends Subsystem {

    CRServo rho;
    Servo theta, phi;

    public TapeCap(HardwareMap hwMap) {
        rho = hwMap.get(CRServo.class, "rho");
        theta = hwMap.get(Servo.class, "theta");
        phi = hwMap.get(Servo.class, "phi");
    }

    public void drive() {

    }

    @Override
    public void subsystemLoop() {

    }
}
