package org.firstinspires.ftc.teamcode.Autonomous;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RoadRunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.Robot;

/**
 * This opmode explains how you follow multiple trajectories in succession, asynchronously. This
 * allows you to run your own logic beside the drive.update() command. This enables one to run
 * their own loops in the background such as a PID controller for a lift. We can also continuously
 * write our pose to PoseStorage.
 * <p>
 * The use of a State enum and a currentState field constitutes a "finite state machine."
 * You should understand the basics of what a state machine is prior to reading this opmode. A good
 * explanation can be found here:
 * https://www.youtube.com/watch?v=Pu7PMN5NGkQ (A finite state machine introduction tailored to FTC)
 * or here:
 * https://gm0.org/en/stable/docs/software/finite-state-machines.html (gm0's article on FSM's)
 * <p>
 * You can expand upon the FSM concept and take advantage of command based programming, subsystems,
 * state charts (for cyclical and strongly enforced states), etc. There is still a lot to do
 * to supercharge your code. This can be much cleaner by abstracting many of these things. This
 * opmode only serves as an initial starting point.
 */
@Config
@Autonomous(group = "advanced")
public class TestRRAutonomous extends LinearOpMode {

    // This enum defines our "state"
    // This is essentially just defines the possible steps our program will take
    enum State {
        PLACE_PRELOADED_BLOCK,
        CYCLE,
        DELIVER_CARGO,
        PARK_IN_WAREHOUSE,
        IDLE
    }

    public static int numCycles = 4;
    public static double waitTime = 0.5;
    private int currentCycle = 0;

    State currentState = State.PLACE_PRELOADED_BLOCK;

    // Define our start pose
    // This assumes we start at x: 15, y: 10, heading: 180 degrees
    Pose2d startPose = new Pose2d(12, 63, Math.toRadians(-90));

    @Override
    public void runOpMode() throws InterruptedException {
        Lift lift = new Lift(hardwareMap);
        Robot wildWing = new Robot(hardwareMap);

        // Set the initial pose of the robot
        wildWing.setPoseEstimate(startPose);

        Trajectory placePreloadedBlock = wildWing.trajectoryBuilder(startPose)
                .lineToLinearHeading(new Pose2d(0, 38, Math.toRadians(-120)))
                .build();

        Trajectory cycle = wildWing.trajectoryBuilder(placePreloadedBlock.end(), true)
                .splineToSplineHeading(new Pose2d(30, 65.5, Math.toRadians(180)), Math.toRadians(0))
                .lineTo(new Vector2d(42, 65.5))
                .lineTo(new Vector2d(30, 65.5))
                .splineTo(new Vector2d(0, 38), Math.toRadians(-120))
                .build();

        Trajectory parkInWarehouse = wildWing.trajectoryBuilder(cycle.end(), true)
                .lineToLinearHeading(new Pose2d(12, 65.5, Math.toRadians(180)))
                .lineToConstantHeading(new Vector2d(42, 65.5))
                .build();

        ElapsedTime waitTimer = new ElapsedTime();

        waitForStart();

        if (isStopRequested()) return;

        wildWing.followTrajectoryAsync(placePreloadedBlock);
        // TODO: put lift to correct height

        while (opModeIsActive() && !isStopRequested()) {
            switch (currentState) {
                case PLACE_PRELOADED_BLOCK:
                    // TODO: put lift to correct height
                    if (!wildWing.isBusy()) {
                        currentState = State.DELIVER_CARGO;
                        waitTimer.reset();
                    }
                    break;

                case CYCLE:
                    // TODO: set intake power to 1
                    // TODO: if there's a block inside the basket, reverse intake
                    if (!wildWing.isBusy()) {
                        currentState = State.DELIVER_CARGO;
                    }
                    break;

                case DELIVER_CARGO:
                    if (waitTimer.seconds() > waitTime) {
                        currentCycle++;
                        // TODO: set the basket servo to the collection position
                        if (currentCycle <= numCycles) {
                            currentState = State.CYCLE;
                            wildWing.followTrajectoryAsync(cycle);
                        } else {
                            currentState = State.PARK_IN_WAREHOUSE;
                            wildWing.followTrajectoryAsync(parkInWarehouse);
                        }
                    }
                    break;

                case PARK_IN_WAREHOUSE:
                    if (!wildWing.isBusy()) {
                        currentState = State.IDLE;
                    }
                    break;

                case IDLE:
                    break;
            }

            wildWing.update();
            lift.update();

            Pose2d poseEstimate = wildWing.getPoseEstimate();

            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", poseEstimate.getHeading());
            telemetry.update();
        }
    }

    // Assume we have a hardware class called lift
    // Lift uses a PID controller to maintain its height
    // Thus, update() must be called in a loop
    class Lift {
        public Lift(HardwareMap hardwareMap) {
            // Beep boop this is the the constructor for the lift
            // Assume this sets up the lift hardware
        }

        public void update() {
            // Beep boop this is the lift update function
            // Assume this runs some PID controller for the lift
        }
    }
}