package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Robot;

@Autonomous(name = "Red Full Scrimmage Auto", group = "1", preselectTeleOp = "MecanumTeleOp")
public class RedFullScrimmageAuto extends LinearOpMode {

    private Robot wildWing = new Robot();
    ElapsedTime elapsedTime = new ElapsedTime(ElapsedTime.Resolution.SECONDS);

    @Override
    public void runOpMode() throws InterruptedException {
        initRobot();
        waitForStart();
        if (opModeIsActive() && !isStopRequested()) {
            moveStraight(1, 0.5, wildWing);
            strafe(1, -0.5, wildWing);
            moveStraight(1, -0.25, wildWing);
            elapsedTime.reset();
            wildWing.duckMotor.setVelocity(Robot.duckNormalVelocity);
            sleep(5000);

        }
    }

    private void initRobot() {
        wildWing.init(hardwareMap, true);
    }

    private void moveStraight(double time, double movePower, Robot robot) {
        robot.fl.setPower(movePower);
        robot.fr.setPower(movePower);
        robot.bl.setPower(movePower);
        robot.br.setPower(movePower);

        elapsedTime.reset();

        while (opModeIsActive() && elapsedTime.seconds() < time) {
            telemetry.addData("Path", "Moving Straight: %2.5f S Elapsed", elapsedTime.seconds());
            telemetry.update();
        }

        robot.fl.setPower(0);
        robot.fr.setPower(0);
        robot.bl.setPower(0);
        robot.br.setPower(0);
    }

    /**
     * This function controls the strafing movement of the robot
     * @param time The amount of time (in seconds) that the robot runs
     * @param movePower The power given to the wheels; negative if moving left and positive if moving right
     */
    public void strafe(double time, double movePower, Robot robot) {

        robot.fl.setPower(movePower);
        robot.fr.setPower(-movePower);
        robot.bl.setPower(-movePower);
        robot.br.setPower(movePower);

        elapsedTime.reset();

        while (opModeIsActive() && elapsedTime.seconds() < this.time) {
            telemetry.addData("Path", "Leg 2: %2.5f S Elapsed", elapsedTime.seconds());
            telemetry.update();
        }

        robot.fl.setPower(0);
        robot.fr.setPower(0);
        robot.bl.setPower(0);
        robot.br.setPower(0);
    }

}