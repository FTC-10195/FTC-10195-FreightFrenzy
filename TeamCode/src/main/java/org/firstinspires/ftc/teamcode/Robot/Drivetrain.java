package org.firstinspires.ftc.teamcode.Robot;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Drivetrain extends Subsystem {
    // Configuration parameters
    public static double slowModePower = 0.35;
    public static double normalModePower = 1;
    public static double buttonIsPressedThreshold = 0.10;

    private DcMotorEx fl, fr, bl, br;
    private double flPower, frPower, blPower, brPower;

    public Drivetrain(HardwareMap hwMap) {
        // Set up the motors and servos
        fl = hwMap.get(DcMotorEx.class, "fl");
        fr = hwMap.get(DcMotorEx.class, "fr");
        bl = hwMap.get(DcMotorEx.class, "bl");
        br = hwMap.get(DcMotorEx.class, "br");

        // TODO: Find which motors to reverse
        fl.setDirection(DcMotorEx.Direction.REVERSE);
        fr.setDirection(DcMotorEx.Direction.FORWARD);
        bl.setDirection(DcMotorEx.Direction.REVERSE);
        br.setDirection(DcMotorEx.Direction.FORWARD);

        // Set the behaviour of the motors when a power of 0 is passed; brake means it stops in its current state,
        // float means it allows the motor to freely slow down to a stop
        fl.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
    }

    public void drive(double strafe, double straight, double turn, double slowMode) {
        teleopDrive(strafe, straight, turn, slowMode);
    }

    public void teleopDrive(double strafe, double straight, double turn, double slowMode) {
        /*
        Left joystick to move forward/backward/left/right, right joystick to turn
         */

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
    }

    @Override
    public void subsystemLoop() {
        fl.setPower(flPower);
        fr.setPower(frPower);
        bl.setPower(blPower);
        br.setPower(brPower);
    }
}
