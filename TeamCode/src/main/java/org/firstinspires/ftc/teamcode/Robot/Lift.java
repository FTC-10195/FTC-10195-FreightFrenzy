package org.firstinspires.ftc.teamcode.Robot;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Lift extends Subsystem {
    private enum LiftState {
        START,
        LIFT,
        STOP
    }
    private LiftState liftState = LiftState.START;

    public static int allianceHub3;
    public static int sharedHub;

    private double liftPower;

    private DcMotorEx lift;
    public Servo basket;

    public Lift(HardwareMap hwMap) {
        lift = hwMap.get(DcMotorEx.class, "lift");
        basket = hwMap.get(Servo.class, "basket");
        lift.setDirection(DcMotorEx.Direction.FORWARD);
        lift.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        lift.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
    }

    public void drive(boolean liftUp, boolean liftDown, boolean automaticLift) {
        manualLift(liftUp, liftDown);
        automaticLift(automaticLift);
    }

    private void manualLift(boolean liftUp, boolean liftDown) {
        if (liftUp) {
            liftPower = 1;
        } else if (liftDown) {
            liftPower = -1;
        } else {
            liftPower = 0;
        }
    }

    private void automaticLift(boolean automaticLift) {
        switch (liftState) {
            case START:
                if (automaticLift) {

                }
                break;

            case LIFT:
                break;

            case STOP:
                break;
        }
    }

    private void getPosition() {
        lift.getCurrentPosition();
    }

    private void resetPosition() {
        lift.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
    }

    @Override
    public void subsystemLoop() {
        lift.setPower(liftPower);
    }
}