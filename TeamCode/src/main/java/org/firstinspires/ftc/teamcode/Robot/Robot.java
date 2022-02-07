package org.firstinspires.ftc.teamcode.Robot;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.List;

@Config
public class Robot {
    public enum Alliance {
        RED,
        BLUE
    }
    public static Alliance alliance;

    List<LynxModule> allHubs;

    public Drivetrain drivetrain;
    public Carousel carousel;
    public Intake intake;
    public Lift lift;
    public Lights lights;

    private Subsystem[] subsystems;

    public void init(HardwareMap hwMap, Alliance alliance) {
        // Manual bulk caching in order to save time on encoder reads
        allHubs = hwMap.getAll(LynxModule.class);
        for (LynxModule module : allHubs) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        // Set up subsystems
        this.drivetrain = new Drivetrain(hwMap);
        this.carousel = new Carousel(hwMap);
        this.intake = new Intake(hwMap);
        this.lift = new Lift(hwMap);
        this.lights = new Lights(hwMap);

        this.subsystems = new Subsystem[] {drivetrain, carousel, intake, lift, lights};
    }

    public void drive(Telemetry telemetry, double leftX, double leftY, double rightX, double slowMode, boolean duckForward,
                      boolean duckBackward, boolean intakeForward, boolean intakeBackward,
                      boolean liftUp, boolean liftDown, boolean automaticLift, boolean automaticDeposit,
                      boolean cancelAutomation, boolean incrementLocation, boolean decrementLocation) {
        Subsystem.packet = new TelemetryPacket();
        drivetrain.drive(leftX, leftY, rightX, slowMode);
        carousel.drive(duckForward, duckBackward);
        intake.drive(intakeForward, intakeBackward);
        lift.drive(liftUp, liftDown, automaticLift, automaticDeposit,
                cancelAutomation, incrementLocation, decrementLocation,
                telemetry);
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

        Subsystem.dashboard.sendTelemetryPacket(Subsystem.packet);
    }
}