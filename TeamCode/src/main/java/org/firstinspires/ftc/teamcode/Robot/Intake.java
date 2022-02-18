package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake extends Subsystem {
    public static int cooldown = 250;
    public static double desiredIntakePower = 0.8;

    private boolean intakeOnForward = false;
    private boolean intakeOnBackward = false;
    private long intakeLastPressed = 0;

    private double intakePower;

    private DcMotorEx intakeMotor;
    private FreightDetector freightDetector;

    public Intake(HardwareMap hwMap) {
        intakeMotor = hwMap.get(DcMotorEx.class, "intake");
        intakeMotor.setDirection(DcMotorEx.Direction.FORWARD);
        intakeMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        freightDetector = new FreightDetector(hwMap, null);
    }

    public void drive(boolean intakeForward, boolean intakeBackward) {
        manualIntake(intakeForward, intakeBackward);
    }

    public void manualIntake(boolean intakeForward, boolean intakeBackward) {
        if (intakeForward && System.currentTimeMillis() - intakeLastPressed > cooldown) {
            intakeLastPressed = System.currentTimeMillis();
            intakeOnForward = !intakeOnForward;
            intakeOnBackward = false;
        } else if (intakeBackward && System.currentTimeMillis() - intakeLastPressed > cooldown) {
            intakeLastPressed = System.currentTimeMillis();
            intakeOnBackward = !intakeOnBackward;
            intakeOnForward = false;
        }

        if (intakeOnForward) {
            intakePower = desiredIntakePower;
        } else if (intakeOnBackward) {
            intakePower = -desiredIntakePower / 1.3;
        } else {
            intakePower = 0;
        }

        if (freightDetector.freightDetected()) {
            intakePower = -desiredIntakePower / 1.3;
        }
    }

    public void setPower(double power) {
        intakeMotor.setPower(power);
    }

    @Override
    public void subsystemLoop() {
        intakeMotor.setPower(intakePower);
    }
}
