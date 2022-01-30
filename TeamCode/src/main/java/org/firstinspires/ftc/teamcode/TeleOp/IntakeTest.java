package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "Intake Test", group = "1")
public class IntakeTest extends LinearOpMode {
    DcMotorEx intakeMotor;

    @Override
    public void runOpMode() throws InterruptedException {
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");
        intakeMotor.setDirection(DcMotorEx.Direction.FORWARD);
        intakeMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        waitForStart();
        while (opModeIsActive() && !isStopRequested()) {
            double intakePower = gamepad1.right_trigger;
            intakeMotor.setPower(intakePower);
            telemetry.addData("intake power", intakePower);
            telemetry.update();
        }
    }
}
