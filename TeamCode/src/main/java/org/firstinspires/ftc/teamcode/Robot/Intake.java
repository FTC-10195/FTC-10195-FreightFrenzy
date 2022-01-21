package org.firstinspires.ftc.teamcode.Robot;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

public class Intake extends Subsystem {
    public static int cooldown = 250;

    private boolean intakeOnForward = false;
    private boolean intakeOnBackward = false;
    private long intakeLastPressed = 0;

    private double intakePower;

    private DcMotorEx intakeMotor;

    FreightDetector freightDetector;

    public Intake(HardwareMap hwMap) {
        freightDetector = new FreightDetector(hwMap);

        intakeMotor = hwMap.get(DcMotorEx.class, "intake");
        intakeMotor.setDirection(DcMotorEx.Direction.FORWARD);
        intakeMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
    }

    public void drive(boolean intakeForward, boolean intakeBackward) {
        manualIntake(intakeForward, intakeBackward);
        reverseIntake();
    }

    private void manualIntake(boolean intakeForward, boolean intakeBackward) {
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
            intakePower = 1;
        } else if (intakeOnBackward) {
            intakePower = -1;
        } else {
            intakePower = 0;
        }
    }

    private void reverseIntake() {
        if (freightDetector.freightDetected()) {
            intakePower = -1;
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

class FreightDetector {
    public static float gain = 2;

    NormalizedColorSensor freightDetector;

    public FreightDetector(HardwareMap hwMap) {
        freightDetector = hwMap.get(NormalizedColorSensor.class, "freightDetector");
        freightDetector.setGain(gain);
        if (freightDetector instanceof SwitchableLight) {
            ((SwitchableLight) freightDetector).enableLight(true);
        }
    }

    public NormalizedRGBA getValues() {
        return freightDetector.getNormalizedColors();
    }

    public float[] getHSVValues() {
        float[] hsvValues = new float[3];
        Color.colorToHSV(getValues().toColor(), hsvValues);
        return hsvValues;
    }

    public boolean freightDetected() {
        // if some condition is met then return true
        // TODO: add condition
        return getHSVValues()[0] > 200;
    }
}