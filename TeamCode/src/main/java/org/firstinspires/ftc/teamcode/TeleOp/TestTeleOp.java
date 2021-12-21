package org.firstinspires.ftc.teamcode.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

@Config
@TeleOp(name = "TestTeleOp", group = "a")
public class TestTeleOp extends OpMode {

    // Configuration parameters
    public static double slowModePower = 0.35;
    public static double normalModePower = 0.8;
    public static double buttonIsPressedThreshold = 0.10;

    // State variables
    DcMotorEx fl, fr, bl, br;
    double flPower, frPower, blPower, brPower;

    // FTC Dashboard helps edit variables on the fly and graph telemetry values
    FtcDashboard dashboard = FtcDashboard.getInstance();
    TelemetryPacket packet = new TelemetryPacket();

    @Override
    public void init() {
        // Set up the motors and servos
        fl = hardwareMap.get(DcMotorEx.class, "fl");
        fr = hardwareMap.get(DcMotorEx.class, "fr");
        bl = hardwareMap.get(DcMotorEx.class, "bl");
        br = hardwareMap.get(DcMotorEx.class, "br");

        // TODO: Find which motors to reverse
        fl.setDirection(DcMotorEx.Direction.FORWARD);
        fr.setDirection(DcMotorEx.Direction.REVERSE);
        bl.setDirection(DcMotorEx.Direction.FORWARD);
        br.setDirection(DcMotorEx.Direction.REVERSE);

        // Set the motors to stay in place when a power of 0 is passed
        fl.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void loop() {
        /*
        The left joystick to move forward/backward/left/right, right joystick to turn
        gamepad 1 controls movement and wobble goal
        gamepad 2 controls the shooter and intake
         */

        // region Gamepad 1

        double y = gamepad1.left_stick_y * -1; // Reversed
        double x = gamepad1.left_stick_x * (sqrt(2)); // Counteract imperfect strafing
        double rx = gamepad1.right_stick_x;

        flPower = (normalModePower) * (y + x) - rx;
        frPower = (normalModePower) * (y - x) + rx;
        blPower = (normalModePower) * (y - x) - rx;
        brPower = (normalModePower) * (y + x) + rx;

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
        if (gamepad1.left_trigger > buttonIsPressedThreshold) {
            flPower *= slowModePower;
            frPower *= slowModePower;
            blPower *= slowModePower;
            brPower *= slowModePower;
        }

        //endregion

        // Sets all powers and servo positions
        fl.setPower(flPower);
        fr.setPower(frPower);
        bl.setPower(blPower);
        br.setPower(brPower);

        packet.put("FL Power", flPower);
        packet.put("FR Power", frPower);
        packet.put("BL Power", blPower);
        packet.put("BR Power", brPower);

        dashboard.sendTelemetryPacket(packet);
    }
}