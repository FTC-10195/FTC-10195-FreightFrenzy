package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "Lift Test", group = "Test")
public class LiftTest extends LinearOpMode {
    DcMotorEx liftMotor;

    @Override
    public void runOpMode() throws InterruptedException {
        liftMotor = hardwareMap.get(DcMotorEx.class, "lift");
        liftMotor.setDirection(DcMotorEx.Direction.FORWARD);
        liftMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        waitForStart();
        while (opModeIsActive() && !isStopRequested()) {
            double liftPower = gamepad1.right_trigger - gamepad1.left_trigger;
            liftMotor.setPower(liftPower);
            telemetry.addData("lift height", liftMotor.getCurrentPosition());
            telemetry.update();
        }
    }
}
