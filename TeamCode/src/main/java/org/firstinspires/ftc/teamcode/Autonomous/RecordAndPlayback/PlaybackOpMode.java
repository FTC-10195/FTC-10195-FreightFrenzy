package org.firstinspires.ftc.teamcode.Autonomous.RecordAndPlayback;

import static org.firstinspires.ftc.teamcode.Robot.Intake.cooldown;

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Robot.Lift;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

@Autonomous(name = "PlaybackOpMode", group = "Record")
public class PlaybackOpMode extends LinearOpMode {

    int flPos, frPos, blPos, brPos;
    double basketPosition, intakePower;
    int liftTargetPos, carouselVelo;
    DcMotorEx fl, fr, bl, br, lift, carousel, intake;
    Servo basket;

    public ArrayList<ArrayList<Double>> valList = new ArrayList<>();
    public int currentIteration = 0;

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

        while (!opModeIsActive() && !gamepad1.a && !isStopRequested()) {
            if (gamepad1.dpad_up && System.currentTimeMillis() - incrementorLastPressed > 500) {
                incrementorLastPressed = System.currentTimeMillis();
                saveLocation++;
            } else if (gamepad1.dpad_down && System.currentTimeMillis() - incrementorLastPressed > cooldown) {
                incrementorLastPressed = System.currentTimeMillis();
                saveLocation--;
            }

            telemetry.addLine("Press dpad up to increment location and dpad down to decrement location");
            telemetry.addLine("Press a to set location");
            telemetry.addData("Current Save Location", saveLocation);
            telemetry.update();
        }

        fileInit();

        telemetry.addData("Save Location", saveLocation);
        telemetry.addLine("Ready to play!");
        telemetry.update();

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            setMotorsAndServos();
            currentIteration++;
            if (currentIteration >= valList.size()) {
                requestOpModeStop();
            }
        }
    }

    public void fileInit() {
        File root = new File(Environment.getExternalStorageDirectory(), "recorder");
        if (!root.exists()) {
            // telemetry.addData("No Playback Data Exists", "");
        }

        File filePath = new File(root, "auto" + saveLocation + ".txt");

        try {
            Scanner sc = new Scanner(filePath);
            while (sc.hasNextLine()) {
                ArrayList<Double> currentVals = new ArrayList<>();
                String line = sc.nextLine();
                String[] vals = line.split(" ");
                for (String val : vals) {
                    currentVals.add(Double.parseDouble(val));
                }
                valList.add(currentVals);
            }
            sc.close();
        } catch (InputMismatchException | FileNotFoundException e) {
            /*telemetry.addData("Error in retrieving playback file", e);
            telemetry.update();*/
        }
    }

    public void fileInit(String path) {
        // TODO: add init stuff
        // Set up the motors and servos
        fl = hardwareMap.get(DcMotorEx.class, "fl");
        fr = hardwareMap.get(DcMotorEx.class, "fr");
        bl = hardwareMap.get(DcMotorEx.class, "bl");
        br = hardwareMap.get(DcMotorEx.class, "br");
        carousel = hardwareMap.get(DcMotorEx.class, "duck");
        lift = hardwareMap.get(DcMotorEx.class, "lift");
        basket = hardwareMap.get(Servo.class, "basket");

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

        File root = new File(Environment.getExternalStorageDirectory(), "recorder");
        if (!root.exists()) {
            // telemetry.addData("No Playback Data Exists", "");
        }

        File filePath = new File(root, path + ".txt");

        try {
            Scanner sc = new Scanner(filePath);
            while (sc.hasNextLine()) {
                ArrayList<Double> currentVals = new ArrayList<>();
                String line = sc.nextLine();
                String[] vals = line.split(" ");
                for (String val : vals) {
                    currentVals.add(Double.parseDouble(val));
                }
                valList.add(currentVals);
            }
            sc.close();
        } catch (InputMismatchException | FileNotFoundException e) {
            /*telemetry.addData("Error in retrieving playback file", e);
            telemetry.update();*/
        }
    }

    public void setMotorsAndServos() {
        flPos = (int) Math.round(valList.get(currentIteration).get(0));
        frPos = (int) Math.round(valList.get(currentIteration).get(1));
        blPos = (int) Math.round(valList.get(currentIteration).get(2));
        brPos = (int) Math.round(valList.get(currentIteration).get(3));
        liftTargetPos = (int) Math.round(valList.get(currentIteration).get(4));
        basketPosition = valList.get(currentIteration).get(5);
        carouselVelo = (int) Math.round(valList.get(currentIteration).get(6));
        intakePower = valList.get(currentIteration).get(7);

        fl.setPower(0.75);
        fr.setPower(0.75);
        bl.setPower(0.75);
        br.setPower(0.75);
        lift.setPower(1);

        fl.setTargetPosition(flPos);
        fr.setTargetPosition(frPos);
        bl.setTargetPosition(blPos);
        br.setTargetPosition(brPos);
        lift.setTargetPosition(liftTargetPos);
        basket.setPosition(basketPosition);
        carousel.setVelocity(carouselVelo);
        intake.setPower(intakePower);

        fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
}
