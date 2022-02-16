package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Robot.Lift;

@TeleOp(name = "Lift Test", group = "Test")
public class LiftTest extends LinearOpMode {
    DcMotorEx liftMotor;
    Servo basket;
    double basketPos = Lift.basketCollect, liftPower = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        liftMotor = hardwareMap.get(DcMotorEx.class, "lift");
        liftMotor.setDirection(DcMotorEx.Direction.REVERSE);
        liftMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        basket = hardwareMap.get(Servo.class, "basket");
        basket.setPosition(basketPos);

        waitForStart();
        while (opModeIsActive() && !isStopRequested()) {
            liftPower = gamepad1.right_trigger - gamepad1.left_trigger;
            liftMotor.setPower(liftPower);

            if (gamepad1.a) {
                basketPos = Lift.basketDeposit;
            } else if (gamepad1.b) {
                basketPos = Lift.basketHold;
            } else if (gamepad1.y) {
                basketPos = Lift.basketCollect;
            }
            basket.setPosition(basketPos);

            telemetry.addData("lift height", liftMotor.getCurrentPosition());
            telemetry.addData("basket pos", basket.getPosition());
            telemetry.update();
        }
    }
}
