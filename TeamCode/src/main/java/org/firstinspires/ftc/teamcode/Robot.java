package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RoadRunner.drive.SampleMecanumDrive;

@Config
public class Robot extends SampleMecanumDrive {
    public Robot(HardwareMap hardwareMap) {
        super(hardwareMap);
    }

    public enum DuckState {
        SETUP,
        START,
        SPEED,
        WAIT
    }
    private DuckState duckState = DuckState.SETUP;

    public DcMotorEx fl, fr, bl, br, duckMotor, intakeMotor;

    // Configuration parameters
    public static double slowModePower = 0.35;
    public static double normalModePower = 0.8;
    public static double buttonIsPressedThreshold = 0.10;

    private double flPower, frPower, blPower, brPower, intakePower;
    private double duckVelocity;

    ElapsedTime duckMotorTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    public static int duckStartTime = 1300;
    public static int duckSpeedTime = 600;

    public static int duckNormalVelocity = 600;
    public static int duckSpeedVelocity = 4000;

    private boolean firstFrame = true;

    boolean usingDashboard;
    FtcDashboard dashboard;
    TelemetryPacket packet;

    public void init(HardwareMap hwMap, boolean dashboard) {
        // Set up FTC dashboard if desired
        usingDashboard = dashboard;
        if (usingDashboard) {
            this.dashboard = FtcDashboard.getInstance();
            this.packet = new TelemetryPacket();
        }

        // Set up the motors and servos
        fl = hwMap.get(DcMotorEx.class, "fl");
        fr = hwMap.get(DcMotorEx.class, "fr");
        bl = hwMap.get(DcMotorEx.class, "bl");
        br = hwMap.get(DcMotorEx.class, "br");
        duckMotor = hwMap.get(DcMotorEx.class, "duck");
        intakeMotor = hwMap.get(DcMotorEx.class, "intake");

        // TODO: Find which motors to reverse
        fl.setDirection(DcMotorEx.Direction.REVERSE);
        fr.setDirection(DcMotorEx.Direction.FORWARD);
        bl.setDirection(DcMotorEx.Direction.REVERSE);
        br.setDirection(DcMotorEx.Direction.FORWARD);
        duckMotor.setDirection(DcMotorEx.Direction.FORWARD);
        intakeMotor.setDirection(DcMotorEx.Direction.FORWARD);

        // Set the behaviour of the motors when a power of 0 is passed; brake means it stops in its current state,
        // float means it allows the motor to freely slow down to a stop
        fl.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        duckMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        intakeMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
    }

    public void drive(double leftX, double leftY, double rightX, double slowMode, boolean duckForward,
                      boolean duckBackward, boolean intakeForward, boolean intakeBackward) {
        drivetrain(leftX, leftY, rightX, slowMode);
        duck(duckForward, duckBackward);
        intake(intakeForward, intakeBackward);
        setPowers();
    }

    public void drivetrain (double strafe, double straight, double turn, double slowMode) {
        /*
        The left joystick to move forward/backward/left/right, right joystick to turn
        gamepad 1 controls movement and wobble goal
        gamepad 2 controls the shooter and intake
         */

        // region Gamepad 1

        double y = -straight; // Reversed
        double x = strafe * (1.41); // Counteract imperfect strafing
        double rx = turn;

        flPower = (normalModePower) * (y + x) + rx;
        frPower = (normalModePower) * (y - x) - rx;
        blPower = (normalModePower) * (y - x) + rx;
        brPower = (normalModePower) * (y + x) - rx;

        // Normalizes all values back to 1
        if (abs(flPower) > 1 || abs(blPower) > 1 || abs(frPower) > 1 || abs(brPower) > 1) {
            // Find the largest power
            double max;
            max = Math.max(abs(flPower), abs(blPower));
            max = Math.max(abs(frPower), max);
            max = Math.max(abs(brPower), max);

            max = abs(max);

            // Divide everything by max (it's positive so we don't need to worry about signs)
            flPower /= max;
            blPower /= max;
            frPower /= max;
            brPower /= max;
        }

        // Slow mode
        if (slowMode > buttonIsPressedThreshold) {
            flPower *= slowModePower;
            frPower *= slowModePower;
            blPower *= slowModePower;
            brPower *= slowModePower;
        }

        //endregion

        if (usingDashboard) {
            packet.put("FL Power", flPower);
            packet.put("FR Power", frPower);
            packet.put("BL Power", blPower);
            packet.put("BR Power", brPower);

            dashboard.sendTelemetryPacket(packet);
        }
    }

    private void duck(boolean duckForward, boolean duckBackward) {

        switch (duckState) {
            case SETUP:
                if (duckForward) {
                    duckMotorTimer.reset();
                    duckMotor.setDirection(DcMotorEx.Direction.FORWARD);
                    duckState = DuckState.START;
                } else if (duckBackward) {
                    duckMotorTimer.reset();
                    duckMotor.setDirection(DcMotorEx.Direction.REVERSE);
                    duckState = DuckState.START;
                }
                break;
            case START:
                duckVelocity = duckNormalVelocity;
                if (duckMotorTimer.time() > duckStartTime) {
                    duckMotorTimer.reset();
                    duckState = DuckState.SPEED;
                }
                break;
            case SPEED:
                duckVelocity = duckSpeedVelocity;
                if (duckMotorTimer.time() > duckSpeedTime) {
                    duckMotorTimer.reset();
                    duckVelocity = 0;
                    duckState = DuckState.SETUP;
                }
                break;
        }
    }

    private void intake(boolean intakeForward, boolean intakeBackward) {
        if (intakeForward) {
            intakePower = 1;
            packet.put("Test", "Test");
            dashboard.sendTelemetryPacket(packet);
        } else if (intakeBackward) {
            intakePower = -1;
        } else {
            intakePower = 0;
        }
    }

    public void setPowers() {
        fl.setPower(flPower);
        fr.setPower(frPower);
        bl.setPower(blPower);
        br.setPower(brPower);
        duckMotor.setVelocity(duckVelocity);
        intakeMotor.setPower(intakePower);
    }

    public void setPowers(double flPower, double frPower, double blPower, double brPower, double duckVelocity,
                          double intakePower) {
        fl.setPower(flPower);
        fr.setPower(frPower);
        bl.setPower(blPower);
        br.setPower(brPower);
        duckMotor.setVelocity(duckVelocity);
        intakeMotor.setPower(intakePower);
    }

    public void stopMoving() {
        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);
        duckMotor.setPower(0);
        intakeMotor.setPower(0);
    }
}
