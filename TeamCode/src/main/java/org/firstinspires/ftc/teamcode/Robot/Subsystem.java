package org.firstinspires.ftc.teamcode.Robot;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

public abstract class Subsystem {
    public static FtcDashboard dashboard = FtcDashboard.getInstance();
    public static TelemetryPacket packet = new TelemetryPacket();
    public abstract void subsystemLoop();
}
