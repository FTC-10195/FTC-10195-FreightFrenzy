package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot.Robot;

@TeleOp(name = "Blue TeleOp", group = "1")
public class BlueTeleOp extends LinearOpMode {
    MecanumTeleOp teleOp = new MecanumTeleOp();

    @Override
    public void runOpMode() throws InterruptedException {
        teleOp.initRobot(teleOp.wildWing, Robot.Alliance.BLUE);
        waitForStart();
        while (opModeIsActive() && !isStopRequested()) {
            teleOp.mainLoop(teleOp.wildWing);
        }
    }
}
