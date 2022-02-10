package org.firstinspires.ftc.teamcode.Autonomous.RecordAndPlayback;

import static java.lang.Math.abs;

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Recorder extends LinearOpMode {

    double speedMultiplier = 0.6;

    double flVelo, frVelo, blVelo, brVelo;
    DcMotorEx fl, fr, bl, br;

    final double TICKS_PER_ROTATION = 537.7;
    final double RPM_19_2 = 312;

    String storedVals;

    ArrayList<ArrayList<Double>> valList = new ArrayList<>();


    @Override
    public void runOpMode() throws InterruptedException {
        // TODO: add init stuff
        // Set up the motors and servos
        fl = hardwareMap.get(DcMotorEx.class, "fl");
        fr = hardwareMap.get(DcMotorEx.class, "fr");
        bl = hardwareMap.get(DcMotorEx.class, "bl");
        br = hardwareMap.get(DcMotorEx.class, "br");

        // TODO: Find which motors to reverse
        fl.setDirection(DcMotorEx.Direction.REVERSE);
        fr.setDirection(DcMotorEx.Direction.FORWARD);
        bl.setDirection(DcMotorEx.Direction.REVERSE);
        br.setDirection(DcMotorEx.Direction.FORWARD);

        // Set the behaviour of the motors when a power of 0 is passed; brake means it stops in its current state,
        // float means it allows the motor to freely slow down to a stop
        fl.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        while (opModeIsActive() && !isStopRequested()) {
            drivetrain();
            recordValues();
        }

        convertToString();
        save();
    }

    public void drivetrain() {
        double y = -gamepad1.left_stick_y; // Reversed
        double x = gamepad1.left_stick_x * (1.41); // Counteract imperfect strafing
        double rx = gamepad1.right_stick_x;

        flVelo = (y + x + rx);
        frVelo = (y - x - rx);
        blVelo = (y - x + rx);
        brVelo = (y + x - rx);

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

        flVelo *= speedMultiplier;
        blVelo *= speedMultiplier;
        frVelo *= speedMultiplier;
        brVelo *= speedMultiplier;

        flVelo *= (RPM_19_2 / 60) * TICKS_PER_ROTATION;
        frVelo *= (RPM_19_2 / 60) * TICKS_PER_ROTATION;
        blVelo *= (RPM_19_2 / 60) * TICKS_PER_ROTATION;
        brVelo *= (RPM_19_2 / 60) * TICKS_PER_ROTATION;

        fl.setVelocity(flVelo);
        fr.setVelocity(frVelo);
        bl.setVelocity(blVelo);
        br.setVelocity(brVelo);
    }

    public void recordValues() {
        ArrayList<Double> valLog = new ArrayList<>();
        valLog.add(flVelo);
        valLog.add(frVelo);
        valLog.add(blVelo);
        valLog.add(brVelo);
        valList.add(valLog);
    }

    public void convertToString() {
        for (ArrayList<Double> vals : valList) {
            for (double val : vals) {
                storedVals += (val + " ");
            }
            storedVals += "\n";
        }
    }

    public void save(){
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "recorder");

            if (!root.exists()){
                root.mkdir();
            }

            String fileName = "testAuto.txt";
            File filepath = new File(root, fileName);
            FileWriter writer = new FileWriter(filepath);
            writer.append(storedVals);
            writer.flush();
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
