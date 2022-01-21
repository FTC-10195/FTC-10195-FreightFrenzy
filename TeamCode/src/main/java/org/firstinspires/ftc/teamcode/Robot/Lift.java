package org.firstinspires.ftc.teamcode.Robot;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.HashMap;

@Config
public class Lift extends Subsystem {
    public static int lowLocation = 100;
    public static int midLocation = 200;
    public static int highLocation = 300;
    public static int sharedLocation = 150;
    public static double basketDeposit = 0.9;
    public static double basketCollect = 0.5;
    public static double depositTime = 0.5;
    public static double desiredLiftPower = 0.25;

    private enum LiftState {
        START,
        LIFT,
        DEPOSIT,
        RETRACT,
        STOP
    }
    private LiftState liftState = LiftState.START;

    private enum DepositLocation {
        LOW,
        MID,
        HIGH,
        SHARED;

        private static DepositLocation[] vals = values();
        private DepositLocation next() {
            return vals[(this.ordinal() + 1) % vals.length];
        }
        private DepositLocation previous() {
            return vals[(this.ordinal() - 1) % vals.length];
        }
    }
    private DepositLocation depositLocation = DepositLocation.HIGH;
    private HashMap<DepositLocation, Integer> depositLocations = new HashMap<>();

    private boolean previousIncrement = false;
    private boolean previousDecrement = false;

    private ElapsedTime liftTimer = new ElapsedTime(ElapsedTime.Resolution.SECONDS);

    private double liftPower;
    private double basketPosition;

    private DcMotorEx lift;
    public Servo basket;

    public Lift(HardwareMap hwMap) {
        lift = hwMap.get(DcMotorEx.class, "lift");
        basket = hwMap.get(Servo.class, "basket");
        lift.setDirection(DcMotorEx.Direction.FORWARD);
        lift.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        lift.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        depositLocations.put(DepositLocation.LOW, lowLocation);
        depositLocations.put(DepositLocation.MID, midLocation);
        depositLocations.put(DepositLocation.HIGH, highLocation);
        depositLocations.put(DepositLocation.SHARED, sharedLocation);
    }

    public void drive(boolean liftUp, boolean liftDown, boolean automaticLift, boolean automaticDeposit,
                      boolean cancelAutomation, boolean incrementLocation, boolean decrementLocation) {
        manualLift(liftUp, liftDown);
        automaticLift(automaticLift, automaticDeposit, cancelAutomation);
        changeDepositLocation(incrementLocation, decrementLocation);
    }

    private void manualLift(boolean liftUp, boolean liftDown) {
        if (liftUp) {
            liftPower = desiredLiftPower;
        } else if (liftDown) {
            liftPower = -desiredLiftPower;
        } else {
            liftPower = 0;
        }
    }

    private void automaticLift(boolean automaticLift, boolean automaticDeposit, boolean cancelAutomation) {
        int depositTicks = depositLocations.getOrDefault(depositLocation, highLocation);
        switch (liftState) {
            case START:
                if (automaticLift) {
                    lift.setTargetPosition(depositTicks);
                    lift.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
                    liftPower = desiredLiftPower;
                    liftState = LiftState.LIFT;
                }
                break;

            case LIFT:
                if (depositTicks - getPosition() < 50) {
                    if (automaticDeposit) {
                        liftTimer.reset();
                        liftState = LiftState.DEPOSIT;
                    }
                }
                break;

            case DEPOSIT:
                basketPosition = basketDeposit;
                if (liftTimer.time() > depositTime) {
                    basketPosition = basketCollect;
                    liftState = LiftState.RETRACT;
                }
                break;

            case RETRACT:
                lift.setTargetPosition(0);
                lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                liftPower = desiredLiftPower;
                if (getPosition() < 50) {
                    liftState = LiftState.STOP;
                }

            case STOP:
                liftPower = 0;
                basketPosition = basketCollect;
                liftState = LiftState.START;
                break;
        }

        if (cancelAutomation) {
            liftState = LiftState.STOP;
        }
    }

    private void changeDepositLocation(boolean increment, boolean decrement) {
        if (increment && !previousIncrement && liftState == LiftState.START) {
            depositLocation = depositLocation.next();
        }

        if (decrement && !previousDecrement && liftState == LiftState.START) {
            depositLocation = depositLocation.previous();
        }

        previousIncrement = increment;
        previousDecrement = decrement;
    }

    private int getPosition() {
        return lift.getCurrentPosition();
    }

    private void resetPosition() {
        lift.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
    }

    @Override
    public void subsystemLoop() {
        lift.setPower(liftPower);
        basket.setPosition(basketPosition);
    }
}