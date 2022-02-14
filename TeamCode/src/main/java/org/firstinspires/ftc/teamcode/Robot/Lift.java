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
    public static int highLocation = 2000;
    public static int sharedLocation = 600;
    public static double basketDeposit = 0.08;
    public static double basketCollect = 0.744;
    public static double basketHold = 0.5;
    public static double depositTime = 1.0;
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
    int depositTicks;

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

    public void drive(boolean liftUp, boolean liftDown, boolean automaticLiftLow,
                      boolean automaticLiftMiddle, boolean automaticLiftHigh,
                      boolean automaticDeposit, boolean cancelAutomation,
                      Telemetry telemetry) {
        manualLift(liftUp, liftDown);
        automaticLift(automaticLiftLow, automaticLiftMiddle,
                automaticLiftHigh,
                automaticDeposit, cancelAutomation,
                telemetry);
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

    private void automaticLift(boolean automaticLiftLow, boolean automaticLiftMiddle,
                               boolean automaticLiftHigh,
                               boolean automaticDeposit, boolean cancelAutomation,
                               Telemetry telemetry) {
        telemetry.addData("Lift State", liftState.name());
        telemetry.addData("Lift Power", liftPower);
        telemetry.update();
        switch (liftState) {
            case START:
                if (automaticLiftLow) {
                    depositLocation = DepositLocation.LOW;
                    depositTicks = depositLocation.ticks;
                    lift.setTargetPosition(depositTicks);

                    lift.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
                    liftPower = desiredLiftPower;
                    basketPosition = basketHold;
                    liftState = LiftState.LIFT;
                } else if (automaticLiftMiddle) {
                    depositLocation = DepositLocation.MID;
                    depositTicks = depositLocation.ticks;
                    lift.setTargetPosition(depositTicks);

                    lift.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
                    liftPower = desiredLiftPower;
                    basketPosition = basketHold;
                    liftState = LiftState.LIFT;
                } else if (automaticLiftHigh) {
                    depositLocation = DepositLocation.HIGH;
                    depositTicks = depositLocation.ticks;
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
                break;

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