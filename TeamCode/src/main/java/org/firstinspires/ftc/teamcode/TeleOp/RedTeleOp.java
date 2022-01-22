package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot.Robot;

@TeleOp(name = "Red TeleOp", group = "1")
public class RedTeleOp extends LinearOpMode {
    MecanumTeleOp teleOp = new MecanumTeleOp();

    @Override
    public void runOpMode() throws InterruptedException {
        teleOp.initRobot(teleOp.wildWing, Robot.Alliance.RED);
        waitForStart();
        while (opModeIsActive() && !isStopRequested()) {
            teleOp.mainLoop(teleOp.wildWing);
        }
    }
}
