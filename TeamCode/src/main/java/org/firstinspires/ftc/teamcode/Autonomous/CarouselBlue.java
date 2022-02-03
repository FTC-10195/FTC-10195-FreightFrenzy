/*
package org.firstinspires.ftc.teamcode.Autonomous;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RoadRunner.drive.AutoDrivetrain;
import org.firstinspires.ftc.teamcode.Robot.Robot;

@Config
@Autonomous(name = "Carousel Blue", group = "1")
public class CarouselBlue extends LinearOpMode {
    enum State {
        CAROUSEL,
        DELIVER_FREIGHT,
        COLLECT_DUCK,
        PARK_IN_STORAGE,
        IDLE
    }

    // Configuration parameters
    public static int numCycles = 4;
    public static double depositTime = 0.5;

    // The current cycle that the robot is running
    private int currentCycle = 0;

    State currentState = State.CAROUSEL;

    // Define our start pose
    Pose2d startPose = new Pose2d(12, 63, Math.toRadians(-90));

    @Override
    public void runOpMode() throws InterruptedException {
        AutoDrivetrain drive = new AutoDrivetrain(hardwareMap);
        Robot wildWing = new Robot();
        wildWing.init(hardwareMap, Robot.Alliance.BLUE);

        // Set the initial pose of the robot
        drive.setPoseEstimate(startPose);

        Trajectory carousel = drive.trajectoryBuilder(startPose)
                .lineToLinearHeading(new Pose2d(0, 38, Math.toRadians(-120)))
                .build();

        Trajectory deliverFreight = drive.trajectoryBuilder(carousel.end(), true)
                .lineToLinearHeading(new Pose2d(0, 38, Math.toRadians(-120)))
                .build();

        Trajectory parkInWarehouse = drive.trajectoryBuilder(deliverFreight.end(), true)
                .splineToSplineHeading(new Pose2d(30, 65.5, Math.toRadians(180)), Math.toRadians(0))
                .lineTo(new Vector2d(42, 65.5))
                .build();

        ElapsedTime waitTimer = new ElapsedTime();

        waitForStart();

        if (isStopRequested()) return;

        drive.followTrajectoryAsync(placePreloadedBlock);

        while (opModeIsActive() && !isStopRequested()) {
            switch (currentState) {
                case PLACE_PRELOADED_BLOCK:
                    // TODO: put lift to correct height
                    if (!drive.isBusy()) {
                        // TODO: set servo to deposit position
                        currentState = State.DELIVER_CARGO;
                        waitTimer.reset();
                    }
                    break;

                case CYCLE:
                    // TODO: set intake power to 1
                    // TODO: if there's a block inside the basket, reverse intake

                    if (false) {
                        wildWing.intake.setPower(-1);
                    } else {
                        wildWing.intake.setPower(1);
                    }

                    if (!drive.isBusy()) {
                        // TODO: set servo to deposit position
                        currentState = State.DELIVER_CARGO;
                        waitTimer.reset();
                    }
                    break;

                case DELIVER_CARGO:
                    if (waitTimer.seconds() > depositTime) {
                        currentCycle++;
                        // TODO: set the basket servo to the collection position
                        // TODO: set intake power to 1
                        if (currentCycle <= numCycles) {
                            currentState = State.CYCLE;
                            drive.followTrajectoryAsync(cycle);
                        } else {
                            currentState = State.PARK_IN_WAREHOUSE;
                            drive.followTrajectoryAsync(parkInWarehouse);
                        }
                    }
                    break;

                case PARK_IN_WAREHOUSE:
                    if (!drive.isBusy()) {
                        currentState = State.IDLE;
                    }
                    break;

                case IDLE:
                    requestOpModeStop();
                    break;
            }

            drive.update();

            Pose2d poseEstimate = drive.getPoseEstimate();

            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", poseEstimate.getHeading());
            telemetry.update();
        }
    }
}
 */