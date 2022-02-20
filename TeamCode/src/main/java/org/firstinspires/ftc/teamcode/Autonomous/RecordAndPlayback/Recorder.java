package org.firstinspires.ftc.teamcode.Autonomous.RecordAndPlayback;

import static org.firstinspires.ftc.teamcode.Robot.Intake.cooldown;
import static java.lang.Math.abs;

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Robot.Carousel;
import org.firstinspires.ftc.teamcode.Robot.FreightDetector;
import org.firstinspires.ftc.teamcode.Robot.Lift;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

@Autonomous(name = "Recorder", group = "Record")
public class Recorder extends LinearOpMode {

    double speedMultiplier = 0.6;

    double flVelo, frVelo, blVelo, brVelo, basketPosition, intakePower;
    int liftTargetPos, carouselVelo;

    int flPos = 0, frPos = 0, blPos = 0, brPos = 0;
    long lastLoopTime;

    DcMotorEx fl, fr, bl, br, lift, carousel, intake;
    FreightDetector freightDetector;
    Servo basket;

    final double TICKS_PER_ROTATION = 537.7;
    final double RPM_19_2 = 312;

    String storedVals = "";

    ArrayList<ArrayList<Double>> valList = new ArrayList<>();

    int saveLocation = 1;
    long incrementorLastPressed = 0;


    @Override
    public void runOpMode() throws InterruptedException {
        // TODO: add init stuff
        // Set up the motors and servos
        fl = hardwareMap.get(DcMotorEx.class, "fl");
        fr = hardwareMap.get(DcMotorEx.class, "fr");
        bl = hardwareMap.get(DcMotorEx.class, "bl");
        br = hardwareMap.get(DcMotorEx.class, "br");
        carousel = hardwareMap.get(DcMotorEx.class, "duck");
        lift = hardwareMap.get(DcMotorEx.class, "lift");
        basket = hardwareMap.get(Servo.class, "basket");
        freightDetector = new FreightDetector(hardwareMap, null);

        // TODO: Find which motors to reverse
        fl.setDirection(DcMotorEx.Direction.REVERSE);
        fr.setDirection(DcMotorEx.Direction.FORWARD);
        bl.setDirection(DcMotorEx.Direction.REVERSE);
        br.setDirection(DcMotorEx.Direction.FORWARD);
        carousel.setDirection(DcMotorEx.Direction.FORWARD);
        lift.setDirection(DcMotorEx.Direction.REVERSE);

        // Set the behaviour of the motors when a power of 0 is passed; brake means it stops in its current state,
        // float means it allows the motor to freely slow down to a stop
        fl.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        carousel.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        lift.setDirection(DcMotorEx.Direction.REVERSE);
        lift.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        lift.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        intake = hardwareMap.get(DcMotorEx.class, "intake");
        intake.setDirection(DcMotorEx.Direction.REVERSE);
        intake.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        basketPosition = Lift.basketCollect;


        while (!opModeIsActive()) {
            if (gamepad1.dpad_up && System.currentTimeMillis() - incrementorLastPressed > 500) {
                incrementorLastPressed = System.currentTimeMillis();
                saveLocation++;
            } else if (gamepad1.dpad_down && System.currentTimeMillis() - incrementorLastPressed > cooldown) {
                incrementorLastPressed = System.currentTimeMillis();
                saveLocation--;
            }

            telemetry.addLine("Press dpad up to increment location and dpad down to decrement location");
            telemetry.addData("Current Save Location", saveLocation);
            telemetry.update();
        }

        lastLoopTime = System.currentTimeMillis();

        while (opModeIsActive() && !isStopRequested()) {
            drivetrain();
            lift();
            carousel();
            intake();
            recordValues();
        }

        save();
    }

    public void drivetrain() {
        double y = -(Math.signum(gamepad1.left_stick_y) * Math.pow(gamepad1.left_stick_y, 2)); // Reversed
        double x = Math.signum(gamepad1.left_stick_x) * Math.pow(gamepad1.left_stick_x, 2) * (1.41); // Counteract imperfect strafing
        double rx = Math.signum(gamepad1.right_stick_x) * Math.pow(gamepad1.right_stick_x, 2);

        flVelo = speedMultiplier * (y + x + rx);
        frVelo = speedMultiplier * (y - x - rx);
        blVelo = speedMultiplier * (y - x + rx);
        brVelo = speedMultiplier * (y + x - rx);

        // Normalizes all values back to 1
        if (abs(flVelo) > 1 || abs(blVelo) > 1 || abs(frVelo) > 1 || abs(brVelo) > 1) {
            // Find the largest power
            double max;
            max = Math.max(abs(flVelo), abs(blVelo));
            max = Math.max(abs(frVelo), max);
            max = Math.max(abs(brVelo), max);

            // Divide everything by max (it's positive so we don't need to worry about signs)
            flVelo /= max;
            blVelo /= max;
            frVelo /= max;
            brVelo /= max;
        }

        flVelo *= (RPM_19_2 / 60) * TICKS_PER_ROTATION;
        frVelo *= (RPM_19_2 / 60) * TICKS_PER_ROTATION;
        blVelo *= (RPM_19_2 / 60) * TICKS_PER_ROTATION;
        brVelo *= (RPM_19_2 / 60) * TICKS_PER_ROTATION;

        fl.setVelocity(flVelo);
        fr.setVelocity(frVelo);
        bl.setVelocity(blVelo);
        br.setVelocity(brVelo);

        flPos = fl.getCurrentPosition();
        frPos = fr.getCurrentPosition();
        blPos = bl.getCurrentPosition();
        brPos = br.getCurrentPosition();
    }

    public void lift() {
        if (gamepad1.dpad_up) {
            liftTargetPos = Lift.highLocation;
            basketPosition = Lift.basketHold;
        } else if (gamepad1.dpad_down) {
            liftTargetPos = 0;
        }

        if (gamepad1.a) {
            basketPosition = Lift.basketDeposit;
        } else if (gamepad1.b) {
            basketPosition = Lift.basketCollect;
        }

        lift.setPower(1);
        lift.setTargetPosition(liftTargetPos);

        basket.setPosition(basketPosition);

        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void carousel() {
        if (gamepad1.right_bumper) {
            carouselVelo = Carousel.carouselNormalVelocity / 2;
        } else if (gamepad1.left_bumper) {
            carouselVelo = -Carousel.carouselNormalVelocity / 2;
        } else {
            carouselVelo = 0;
        }

        carousel.setVelocity(carouselVelo);
    }

    public void intake() {
        intakePower = gamepad1.right_trigger - gamepad1.left_trigger;

        /*if (freightDetector.freightDetected()) {
            intakePower = -Intake.desiredIntakePower / 1.3;
        }*/

        intake.setPower(intakePower);
    }

    public void recordValues() {
        ArrayList<Double> valLog = new ArrayList<>();
        valLog.add((double) flPos);
        valLog.add((double) frPos);
        valLog.add((double) blPos);
        valLog.add((double) brPos);
        valLog.add((double) liftTargetPos);
        valLog.add(basketPosition);
        valLog.add((double) carouselVelo);
        valLog.add(intakePower);
        valList.add(valLog);
    }

    public void save(){
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "recorder");

            if (!root.exists()){
                root.mkdir();
            }

            String fileName = "auto" + saveLocation + ".txt";
            File filepath = new File(root, fileName);

            if (filepath.delete()) {
                telemetry.addData("File successfully deleted", "");
            } else {
                telemetry.addData("File not deleted", filepath.getAbsolutePath());
            }

            if (filepath.createNewFile()) {
                telemetry.addData("File successfully created", filepath.getAbsolutePath());
            } else {
                telemetry.addData("File not created", "");
            }

            FileWriter writer = new FileWriter(filepath);
            for (ArrayList<Double> vals : valList) {
                for (double val : vals) {
                    writer.write(val + " ");
                }
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
