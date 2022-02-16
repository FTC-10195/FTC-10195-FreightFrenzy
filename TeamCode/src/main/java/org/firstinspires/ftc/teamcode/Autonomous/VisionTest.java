package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@Autonomous(name = "Vision Test", group = "1")
public class VisionTest extends LinearOpMode {
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
        OpenCVSetup();
        while (!opModeIsActive() && !isStopRequested()) {
            setZone();
            telemetry.addData("Analysis", pipeline.getAnalysis());
            telemetry.addData("Rings", pipeline.getPosition());
            telemetry.addData("Zone", zone);
            telemetry.update();
        }
        // do stuff
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
