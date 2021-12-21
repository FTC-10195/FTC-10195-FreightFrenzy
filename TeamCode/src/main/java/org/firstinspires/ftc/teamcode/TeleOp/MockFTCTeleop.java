package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Mock FTC Teleop", group="Exercises")
//@Disabled
public class MockFTCTeleop extends OpMode
{
    DcMotor leftMotor, rightMotor;
    float   leftY, rightY;

    @Override
    public void init() {
        leftMotor = hardwareMap.dcMotor.get("left_motor");
        rightMotor = hardwareMap.dcMotor.get("right_motor");

        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        leftMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rightMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void loop() {
        leftY = gamepad1.left_stick_y * -1;
        rightY = gamepad1.right_stick_y * -1;

        leftMotor.setPower(Range.clip(leftY, -1.0, 1.0));
        rightMotor.setPower(Range.clip(rightY, -1.0, 1.0));

        telemetry.addData("Mode", "running");
        telemetry.addData("sticks", "  left=" + leftY + "  right=" + rightY);
    }
}