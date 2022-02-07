package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Basket Test", group = "Test")
public class BasketTest extends LinearOpMode {
    Servo basket;

    @Override
    public void runOpMode() throws InterruptedException {
        basket = hardwareMap.get(Servo.class, "basket");
        basket.setPosition(0);
        waitForStart();
        while (opModeIsActive() && !isStopRequested()) {
            double deltaPos = gamepad1.right_trigger - gamepad1.left_trigger;
            deltaPos *= 0.01;
            basket.setPosition(basket.getPosition() + deltaPos);
            telemetry.addData("basket position", basket.getPosition());
            telemetry.update();
        }
    }
}
