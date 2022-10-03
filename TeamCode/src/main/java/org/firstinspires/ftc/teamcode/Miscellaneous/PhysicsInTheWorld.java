package org.firstinspires.ftc.teamcode.Miscellaneous;

import static java.lang.Math.PI;
import static java.lang.Math.abs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "Physics in the World", group = "0")
public class PhysicsInTheWorld extends LinearOpMode {

    // Configuration parameters
    private static final double SLOW_MODE_POWER = 0.35;
    private static final double NORMAL_MODE_POWER = 1;
    private static final double BUTTON_IS_PRESSED_THRESHOLD = 0.10;
    private final double WHEEL_RADIUS = 0.048; // m
    private final double TRACK_WIDTH = 0.34; // m
    private final double TRACK_LENGTH = 0.29; // m
    private final double TICKS_PER_ROTATION = 537.7;

    private DcMotorEx fl, fr, bl, br;
    private double flPower, frPower, blPower, brPower;

    @Override
    public void runOpMode() throws InterruptedException {
        // Set up the motors and servos
        fl = hardwareMap.get(DcMotorEx.class, "fl");
        fr = hardwareMap.get(DcMotorEx.class, "fr");
        bl = hardwareMap.get(DcMotorEx.class, "bl");
        br = hardwareMap.get(DcMotorEx.class, "br");

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

        waitForStart();

        while (opModeIsActive()) {
            while (gamepad1.a && opModeIsActive()) {
                automaticDrive(1, 0, 1);
            }

            while (gamepad1.b && opModeIsActive()) {
                automaticDrive(0.5, 1, 0.5);
            }

            while (gamepad1.y && opModeIsActive()) {
                automaticDrive(2, 0, 0);
            }

            while (gamepad1.x && opModeIsActive()) {
                automaticDrive(0, 2, 0);
            }

            normalDrive();

            fl.setPower(flPower);
            fr.setPower(frPower);
            bl.setPower(blPower);
            br.setPower(brPower);
        }
    }

    private void normalDrive() {
        double y = -gamepad1.left_stick_y; // Reversed
        double x = gamepad1.left_stick_x * (1.1); // Counteract imperfect strafing
        double rx = gamepad1.right_stick_x;

        flPower = (NORMAL_MODE_POWER) * (y + x) + rx;
        frPower = (NORMAL_MODE_POWER) * (y - x) - rx;
        blPower = (NORMAL_MODE_POWER) * (y - x) + rx;
        brPower = (NORMAL_MODE_POWER) * (y + x) - rx;

        // Normalizes all values back to 1
        if (abs(flPower) > 1 || abs(blPower) > 1 || abs(frPower) > 1 || abs(brPower) > 1) {
            // Find the largest power
            double max;
            max = Math.max(abs(flPower), abs(blPower));
            max = Math.max(abs(frPower), max);
            max = Math.max(abs(brPower), max);

            // Divide everything by max (it's positive so we don't need to worry about signs)
            flPower /= max;
            blPower /= max;
            frPower /= max;
            brPower /= max;
        }

        // Slow mode
        if (gamepad1.left_trigger > BUTTON_IS_PRESSED_THRESHOLD) {
            flPower *= SLOW_MODE_POWER;
            frPower *= SLOW_MODE_POWER;
            blPower *= SLOW_MODE_POWER;
            brPower *= SLOW_MODE_POWER;
        }
    }

    private void automaticDrive(double vx, double vy, double wz) {
        // gives wheel speeds in radians per second
        flPower = (1 / WHEEL_RADIUS) * (vx - vy - (TRACK_WIDTH + TRACK_LENGTH) * wz);
        frPower = (1 / WHEEL_RADIUS) * (vx + vy + (TRACK_WIDTH + TRACK_LENGTH) * wz);
        blPower = (1 / WHEEL_RADIUS) * (vx + vy - (TRACK_WIDTH + TRACK_LENGTH) * wz);
        brPower = (1 / WHEEL_RADIUS) * (vx - vy + (TRACK_WIDTH + TRACK_LENGTH) * wz);

        // converts from radians per second to rotations per second
        flPower /= (2 * PI);
        frPower /= (2 * PI);
        blPower /= (2 * PI);
        brPower /= (2 * PI);

        // converts from rotations per second to ticks per second (units used for setVelocity())
        flPower *= TICKS_PER_ROTATION;
        frPower *= TICKS_PER_ROTATION;
        blPower *= TICKS_PER_ROTATION;
        brPower *= TICKS_PER_ROTATION;

        // sets the velocities of all the motors
        fl.setVelocity(flPower);
        fr.setVelocity(frPower);
        bl.setVelocity(blPower);
        br.setVelocity(brPower);
    }
}
