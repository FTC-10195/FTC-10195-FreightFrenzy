package org.firstinspires.ftc.teamcode.Robot;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Config
public class Robot {
    FtcDashboard dashboard;
    TelemetryPacket packet;
    Drivetrain drivetrain;
    Carousel carousel;
    Intake intake;
    Lift lift;
    // Lights lights

    private Subsystem[] subsystems;

    public void init(HardwareMap hwMap) {
        this.dashboard = FtcDashboard.getInstance();
        this.packet = new TelemetryPacket();

        // Set up subsystems
        this.drivetrain = new Drivetrain(hwMap);
        this.carousel = new Carousel(hwMap);
        this.intake = new Intake(hwMap);

        // this.lights = new Lights(hwMap);

        this.subsystems = new Subsystem[] {drivetrain, carousel, intake /*, lights*/};
    }

    public void drive(double leftX, double leftY, double rightX, double slowMode, boolean duckForward,
                      boolean duckBackward, boolean intakeForward, boolean intakeBackward,
                      boolean liftUp, boolean liftDown) {
        drivetrain.drive(leftX, leftY, rightX, slowMode);
        carousel.drive(duckForward, duckBackward);
        intake.drive(intakeForward, intakeBackward);
        lift.drive(liftUp, liftDown);
        // lights logic
        runMotorsAndServos();
    }

    private void runMotorsAndServos() {
        for (Subsystem subsystem : subsystems) {
            subsystem.runMotorsAndServos();
        }
    }
}