package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot;

@TeleOp(name = "Mecanum TeleOp", group = "1")
public class MecanumTeleOp extends LinearOpMode {

    private Robot wildWing = new Robot(hardwareMap);

    @Override
    public void runOpMode() throws InterruptedException {
        initRobot(wildWing);
        waitForStart();
        while (opModeIsActive() && !isStopRequested()) {
            mainLoop(wildWing);
        }
    }

    public void initRobot(Robot robot) {
        robot.init(hardwareMap, true);
    }

    public void mainLoop(Robot robot) {
        robot.drive(
                gamepad1.left_stick_x,
                gamepad1.left_stick_y,
                gamepad1.right_stick_x,
                gamepad1.left_trigger,
                gamepad1.right_bumper,
                gamepad1.left_bumper
        );
    }
}