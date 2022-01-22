package org.firstinspires.ftc.teamcode.Robot;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.List;

@Config
public class Robot {
    public enum Alliance {
        RED,
        BLUE
    }
    public static Alliance alliance;

    FtcDashboard dashboard;

    List<LynxModule> allHubs;

    TelemetryPacket packet;
    public Drivetrain drivetrain;
    public Carousel carousel;
    public Intake intake;
    // Lift lift;
    // Lights lights;

    private Subsystem[] subsystems;

    public void init(HardwareMap hwMap, Alliance alliance) {
        // Manual bulk caching in order to save time on encoder reads
        allHubs = hwMap.getAll(LynxModule.class);
        for (LynxModule module : allHubs) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        this.dashboard = FtcDashboard.getInstance();
        this.packet = new TelemetryPacket();

        // Set up subsystems
        this.drivetrain = new Drivetrain(hwMap);
        this.carousel = new Carousel(hwMap);
        this.intake = new Intake(hwMap);
        // this.lift = new Lift(hwMap);
        // this.lights = new Lights(hwMap);

        this.subsystems = new Subsystem[] {drivetrain, carousel, intake /*, lights*/};
    }

    public void drive(double leftX, double leftY, double rightX, double slowMode, boolean duckForward,
                      boolean duckBackward, boolean intakeForward, boolean intakeBackward,
                      boolean liftUp, boolean liftDown) {
        drivetrain.drive(leftX, leftY, rightX, slowMode);
        carousel.drive(duckForward, duckBackward);
        intake.drive(intakeForward, intakeBackward);
        // lift.drive(liftUp, liftDown);
        // lights logic
        subsystemLoop();
    }

    private void subsystemLoop() {
        // Clearing bulk cache as required by manual bulk caching
        for (LynxModule module : allHubs) {
            module.clearBulkCache();
        }

        for (Subsystem subsystem : subsystems) {
            subsystem.subsystemLoop();
        }
    }
}