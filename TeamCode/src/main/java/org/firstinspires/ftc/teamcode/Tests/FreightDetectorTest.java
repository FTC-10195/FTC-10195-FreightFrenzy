package org.firstinspires.ftc.teamcode.Tests;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot.FreightDetector;

@TeleOp(name = "Freight Detector Test", group = "Test")
public class FreightDetectorTest extends LinearOpMode {
    FreightDetector freightDetector;
    float[] hsvValues = {0f, 0f, 0f};

    @Override
    public void runOpMode() throws InterruptedException {
        freightDetector = new FreightDetector(hardwareMap, null);
        waitForStart();
        while (opModeIsActive() && !isStopRequested()) {

            Color.RGBToHSV(
                    freightDetector.colorSensor.red() * 8,
                    freightDetector.colorSensor.green() * 8,
                    freightDetector.colorSensor.blue() * 8,
                    hsvValues
            );

            telemetry.addData("Clear", freightDetector.colorSensor.alpha());
            telemetry.addData("Red  ", freightDetector.colorSensor.red());
            telemetry.addData("Green", freightDetector.colorSensor.green());
            telemetry.addData("Blue ", freightDetector.colorSensor.blue());
            telemetry.addData("Hue  ", hsvValues[0]);
            telemetry.update();
        }
    }
}
