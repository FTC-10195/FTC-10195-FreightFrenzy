package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Autonomous.RecordAndPlayback.Playback;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@Autonomous(name = "Red Autonomous", group = "", preselectTeleOp = "MecanumTeleOp")
public class RedAutonomous extends LinearOpMode {
    String zone = "right";

    FreightFrenzyVisionPipeline pipeline;
    OpenCvInternalCamera phoneCam;

    private void OpenCVSetup() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(
                OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        pipeline = new FreightFrenzyVisionPipeline();
        phoneCam.setPipeline(pipeline);

        // Optimized so the preview isn't messed up
        phoneCam.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);

        phoneCam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                phoneCam.startStreaming(800,448, OpenCvCameraRotation.SIDEWAYS_LEFT);
            }

            @Override
            public void onError(int errorCode) {
                telemetry.addData("FAILED; Error Code", errorCode);
                telemetry.update();
            }
        });
    }


    @Override
    public void runOpMode() throws InterruptedException {
        Playback playbackLow = new Playback(hardwareMap);
        Playback playbackMiddle = new Playback(hardwareMap);
        Playback playbackHigh = new Playback(hardwareMap);
        playbackLow.fileInit("redLow");
        playbackMiddle.fileInit("redMiddle");
        playbackHigh.fileInit("redHigh");
        OpenCVSetup();
        while (!opModeIsActive() && !isStopRequested()) {
            setZone();
            telemetry.addData("Analysis", pipeline.getAnalysis());
            telemetry.addData("Rings", pipeline.getPosition());
            telemetry.addData("Zone", zone);
            telemetry.update();
        }

        while (opModeIsActive()) {
            switch (zone) {
                case "left": default:
                    playbackLow.setMotorsAndServos();
                    break;

                case "middle":
                    playbackMiddle.setMotorsAndServos();
                    break;

                case "right":
                    playbackHigh.setMotorsAndServos();
                    break;
            }
        }
    }

    private void setZone() {
        switch (pipeline.getPosition()) {
            case LEFT:
                zone = "left";
                break;
            case MIDDLE:
                zone = "middle";
                break;
            case RIGHT:
                zone = "right";
                break;
        }
    }
}
