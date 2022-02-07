package org.firstinspires.ftc.teamcode.Robot;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class Lift extends Subsystem {
    public static int lowLocation = 600;
    public static int midLocation = 1300;
    public static int highLocation = 1900;
    public static int sharedLocation = 150;
    public static double basketDeposit = 0;
    public static double basketCollect = 0.744;
    public static double basketHold = 0.5;
    public static double depositTime = 0.5;
    public static double desiredLiftPower = 1;

    private enum LiftState {
        START,
        LIFT,
        DEPOSIT,
        RETRACT,
        RETRACT_FULLY,
        STOP
    }
    private LiftState liftState = LiftState.START;

    private enum DepositLocation {
        LOW(lowLocation),
        MID(midLocation),
        HIGH(highLocation),
        SHARED(sharedLocation);

        private static final DepositLocation[] states = values();

        private DepositLocation next() {
            return states[(this.ordinal() + 1) % states.length];
        }

        private DepositLocation previous() {
            return states[(this.ordinal() - 1) % states.length];
        }

        public int ticks;

        DepositLocation(int ticks) {
            this.ticks = ticks;
        }
    }
    private DepositLocation depositLocation = DepositLocation.HIGH;

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
        lift.setDirection(DcMotorEx.Direction.REVERSE);
        lift.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        lift.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        basketPosition = basketCollect;
    }

    public void drive(boolean liftUp, boolean liftDown, boolean automaticLift, boolean automaticDeposit,
                      boolean cancelAutomation, boolean incrementLocation, boolean decrementLocation,
                      Telemetry telemetry) {
        manualLift(liftUp, liftDown);
        automaticLift(automaticLift, automaticDeposit, cancelAutomation,
                telemetry);
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

    private void automaticLift(boolean automaticLift, boolean automaticDeposit, boolean cancelAutomation, Telemetry telemetry) {
        int depositTicks = depositLocation.ticks;
        telemetry.addData("Lift State", liftState.name());
        telemetry.addData("Lift Power", liftPower);
        telemetry.update();
        switch (liftState) {
            case START:
                if (automaticLift) {
                    lift.setTargetPosition(depositTicks);
                    lift.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
                    liftPower = desiredLiftPower;
                    basketPosition = basketHold;
                    liftState = LiftState.LIFT;
                }
                break;

            case LIFT:
                liftPower = desiredLiftPower;
                if (depositTicks - getPosition() < 50) {
                    if (automaticDeposit) {
                        liftPower = 0;
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
                liftPower = desiredLiftPower;
                if (getPosition() < 10) {
                    liftTimer.reset();
                    liftState = LiftState.RETRACT_FULLY;
                }
                break;

            case RETRACT_FULLY:
                liftPower = desiredLiftPower / 2;
                if (liftTimer.seconds() > 0.5) {
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