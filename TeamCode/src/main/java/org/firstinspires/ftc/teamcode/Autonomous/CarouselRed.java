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
@Autonomous(name = "Carousel Red", group = "1")
public class CarouselRed extends LinearOpMode {
    enum State {
        GO_TO_CAROUSEL,
        SPIN_CAROUSEL,
        GO_TO_HUB,
        DELIVER_FREIGHT,
        GO_TO_DUCK,
        COLLECT_DUCK,
        PARK_IN_STORAGE,
        IDLE
    }

    // Configuration parameters
    public static double depositTime = 0.5;

    // The current cycle that the robot is running
    private boolean duckDeposited = false;

    State currentState = State.GO_TO_CAROUSEL;

    // Define our start pose
    Pose2d startPose = new Pose2d(-36, -63, Math.toRadians(-90));

    @Override
    public void runOpMode() throws InterruptedException {
        AutoDrivetrain drive = new AutoDrivetrain(hardwareMap);
        Robot wildWing = new Robot();
        wildWing.init(hardwareMap, Robot.Alliance.BLUE);

        // Set the initial pose of the robot
        drive.setPoseEstimate(startPose);

        Trajectory carousel = drive.trajectoryBuilder(startPose)
                .splineToLinearHeading(new Pose2d(-64, -42, Math.toRadians(0)), Math.toRadians(180))
                .lineTo(new Vector2d(-64, -56))
                .build();

        Trajectory deliverPreload = drive.trajectoryBuilder(carousel.end())
                .splineToSplineHeading(new Pose2d(-24, -38, Math.toRadians(-135)), Math.toRadians(0))
                .build();

        Trajectory goToDuck = drive.trajectoryBuilder(deliverPreload.end())
                .lineToLinearHeading(new Pose2d(-56, -55, Math.toRadians(-90)))
                .build();

        Trajectory collectDuck = drive.trajectoryBuilder(goToDuck.end())
                .lineTo(new Vector2d(-56, -63)/*, SampleMecanumDrive.getVelocityConstraint(2, Math.toRadians(180), 13)*/)
                .build();

        Trajectory deliverDuck = drive.trajectoryBuilder(collectDuck.end())
                .lineToLinearHeading(new Pose2d(-24, -38, Math.toRadians(-135)))
                .build();

        Trajectory parkInStorage = drive.trajectoryBuilder(deliverDuck.end(), true)
                .lineToLinearHeading(new Pose2d(-60, -36, Math.toRadians(-90)))
                .build();

        ElapsedTime waitTimer = new ElapsedTime();

        waitForStart();

        if (isStopRequested()) return;

        drive.followTrajectoryAsync(carousel);

        while (opModeIsActive() && !isStopRequested()) {
            switch (currentState) {
                case GO_TO_CAROUSEL:
                    if (!drive.isBusy()) {
                        currentState = State.SPIN_CAROUSEL;
                        waitTimer.reset();
                    }
                    break;

                case SPIN_CAROUSEL:
                    if (waitTimer.seconds() > 4) {
                        currentState = State.GO_TO_HUB;
                        drive.followTrajectoryAsync(deliverPreload);
                        // TODO: set lift to correct height
                    }
                    break;

                case GO_TO_HUB:
                    if (!drive.isBusy()) {
                        currentState = State.DELIVER_FREIGHT;
                        // TODO: set basket servo to deposit position
                        waitTimer.reset();
                    }
                    break;

                case DELIVER_FREIGHT:
                    if (waitTimer.seconds() > depositTime) {
                        if (duckDeposited) {
                            currentState = State.PARK_IN_STORAGE;
                            drive.followTrajectoryAsync(parkInStorage);
                        } else {
                            currentState = State.GO_TO_DUCK;
                            drive.followTrajectoryAsync(goToDuck);
                        }
                    }
                    break;

                case GO_TO_DUCK:
                    if (!drive.isBusy()) {
                        currentState = State.COLLECT_DUCK;
                        // TODO: turn on intake
                        drive.followTrajectoryAsync(collectDuck);
                    }
                    break;

                case COLLECT_DUCK:
                    if (!drive.isBusy()) {
                        currentState = State.GO_TO_HUB;
                        drive.followTrajectoryAsync(deliverDuck);
                        duckDeposited = true;
                    }
                    break;

                case PARK_IN_STORAGE:
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