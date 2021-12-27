package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot;

@Autonomous(name = "Scrimmage Auto", group = "1", preselectTeleOp = "MecanumTeleOp")
public class ScrimmageAuto extends LinearOpMode {

    private Robot wildWing = new Robot(hardwareMap);

    @Override
    public void runOpMode() throws InterruptedException {
        initRobot();
        waitForStart();
        if (opModeIsActive() && !isStopRequested()) {
            wildWing.setPowers(0.5, 0.5, 0.5, 0.5, 0);
            sleep(1500);
            wildWing.setPowers(-0.5, -0.5, -0.5, -0.5, 0);
            sleep(200);
            wildWing.stopMoving();
        }
    }

    public void initRobot() {
        wildWing.init(hardwareMap, true);
    }
}