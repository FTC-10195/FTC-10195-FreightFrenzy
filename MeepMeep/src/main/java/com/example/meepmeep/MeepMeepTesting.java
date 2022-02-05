package com.example.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.DriveShim;
import com.noahbres.meepmeep.roadrunner.SampleMecanumDrive;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        DefaultBotBuilder blueBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(30, 30, Math.toRadians(360), Math.toRadians(180), 13)
                .setDimensions(16, 18);

        DefaultBotBuilder redBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(30, 30, Math.toRadians(360), Math.toRadians(360), 13)
                .setDimensions(16, 18);

        RoadRunnerBotEntity blueEntity = blueBot.followTrajectorySequence(carouselBlue(blueBot.build().getDrive()));
        RoadRunnerBotEntity redEntity = redBot.followTrajectorySequence(carouselRed(redBot.build().getDrive()));

        meepMeep.setBackground(MeepMeep.Background.FIELD_FREIGHTFRENZY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(blueEntity)
                .addEntity(redEntity)
                .start();
    }

    private static TrajectorySequence onlyCyclesRed(DriveShim drive) {
        return drive.trajectorySequenceBuilder(new Pose2d(12, -63, Math.toRadians(90)))
                .lineTo(new Vector2d(-12, -42))
                .waitSeconds(0.5)
                .lineToLinearHeading(new Pose2d(12, -65.5, Math.toRadians(180)))
                .lineTo(new Vector2d(60, -65.5))
                .lineTo(new Vector2d(24, -65.5))
                .splineTo(new Vector2d(-12, -42), Math.toRadians(90))
                .waitSeconds(0.5)
                .lineToLinearHeading(new Pose2d(12, -65.5, Math.toRadians(180)))
                .lineTo(new Vector2d(60, -65.5))
                .lineTo(new Vector2d(24, -65.5))
                .splineTo(new Vector2d(-12, -42), Math.toRadians(90))
                .waitSeconds(0.5)
                .lineToLinearHeading(new Pose2d(12, -65.5, Math.toRadians(180)))
                .lineTo(new Vector2d(60, -65.5))
                .lineTo(new Vector2d(24, -65.5))
                .splineTo(new Vector2d(-12, -42), Math.toRadians(90))
                .waitSeconds(0.5)
                .lineToLinearHeading(new Pose2d(12, -65.5, Math.toRadians(180)))
                .lineTo(new Vector2d(60, -65.5))
                .lineTo(new Vector2d(24, -65.5))
                .splineTo(new Vector2d(-12, -42), Math.toRadians(90))
                .waitSeconds(0.5)
                .lineToLinearHeading(new Pose2d(12, -65.5, Math.toRadians(180)))
                .lineToConstantHeading(new Vector2d(42, -65.5))
                .build();
    }

    private static TrajectorySequence everythingRed(DriveShim drive) {
        return drive.trajectorySequenceBuilder(new Pose2d(-36, -63, Math.toRadians(90)))
                .lineToSplineHeading(new Pose2d(-24, -40, Math.toRadians(60)))
                .waitSeconds(0.5)
                .lineToSplineHeading(new Pose2d(-65.5, -54.5, Math.toRadians(90)))
                .waitSeconds(1.5)
                .splineToSplineHeading(new Pose2d(12, -65.5, Math.toRadians(180)), Math.toRadians(0))
                .lineTo(new Vector2d(60, -65.5))
                .lineTo(new Vector2d(24, -65.5))
                .splineTo(new Vector2d(-12, -42), Math.toRadians(90))
                .waitSeconds(0.5)
                .lineToSplineHeading(new Pose2d(12, -65.5, Math.toRadians(180)))
                .lineTo(new Vector2d(60, -65.5))
                .lineTo(new Vector2d(24, -65.5))
                .splineTo(new Vector2d(-12, -42), Math.toRadians(90))
                .waitSeconds(0.5)
                .lineToSplineHeading(new Pose2d(12, -65.5, Math.toRadians(180)))
                .lineTo(new Vector2d(60, -65.5))
                .lineTo(new Vector2d(24, -65.5))
                .splineTo(new Vector2d(-12, -42), Math.toRadians(90))
                .waitSeconds(0.5)
                .lineToSplineHeading(new Pose2d(12, -65.5, Math.toRadians(180)))
                .lineToConstantHeading(new Vector2d(48, -65.5))
                .strafeRight(24)
                .back(12)
                .build();
    }

    private static TrajectorySequence onlyCyclesBlue(DriveShim drive) {
        return drive.trajectorySequenceBuilder(new Pose2d(12, 63, Math.toRadians(-90)))
                .lineToLinearHeading(new Pose2d(0, 38, Math.toRadians(-120)))
                .waitSeconds(0.5)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(30, 65.5, Math.toRadians(180)), Math.toRadians(0))
                .lineTo(new Vector2d(42, 65.5))
                .lineTo(new Vector2d(30, 65.5))
                .splineTo(new Vector2d(0, 38), Math.toRadians(-120))
                .waitSeconds(0.5)
                .splineToSplineHeading(new Pose2d(30, 65.5, Math.toRadians(180)), Math.toRadians(0))
                .lineTo(new Vector2d(44, 65.5))
                .lineTo(new Vector2d(30, 65.5))
                .splineTo(new Vector2d(0, 38), Math.toRadians(-120))
                .waitSeconds(0.5)
                .splineToSplineHeading(new Pose2d(30, 65.5, Math.toRadians(180)), Math.toRadians(0))
                .lineTo(new Vector2d(46, 65.5))
                .lineTo(new Vector2d(30, 65.5))
                .splineTo(new Vector2d(0, 38), Math.toRadians(-120))
                .waitSeconds(0.5)
                .splineToSplineHeading(new Pose2d(30, 65.5, Math.toRadians(180)), Math.toRadians(0))
                .lineTo(new Vector2d(48, 65.5))
                .lineTo(new Vector2d(30, 65.5))
                .splineTo(new Vector2d(0, 38), Math.toRadians(-120))
                .waitSeconds(0.5)
                .splineToSplineHeading(new Pose2d(30, 65.5, Math.toRadians(180)), Math.toRadians(0))
                .lineTo(new Vector2d(50, 65.5))
                .lineTo(new Vector2d(30, 65.5))
                .splineTo(new Vector2d(0, 38), Math.toRadians(-120))
                .waitSeconds(0.5)
                .splineToSplineHeading(new Pose2d(30, 65.5, Math.toRadians(180)), Math.toRadians(0))
                .lineTo(new Vector2d(52, 65.5))
                .lineTo(new Vector2d(30, 65.5))
                .splineTo(new Vector2d(0, 38), Math.toRadians(-120))
                .waitSeconds(0.5)
                .splineToSplineHeading(new Pose2d(30, 65.5, Math.toRadians(180)), Math.toRadians(0))
                .lineTo(new Vector2d(46, 65.5))
                .build();
    }

    private static TrajectorySequence everythingBlue(DriveShim drive) {
        return drive.trajectorySequenceBuilder(new Pose2d(-36, 63, Math.toRadians(-90)))
                .lineToSplineHeading(new Pose2d(-24, 40, Math.toRadians(-60)))
                .waitSeconds(0.5)
                .lineToSplineHeading(new Pose2d(-63, 54.5, Math.toRadians(0)))
                .waitSeconds(1.5)
                .splineToSplineHeading(new Pose2d(12, 65.5, Math.toRadians(180)), Math.toRadians(0))
                .lineTo(new Vector2d(60, 65.5))
                .lineTo(new Vector2d(24, 65.5))
                .splineTo(new Vector2d(-12, 42), Math.toRadians(-90))
                .waitSeconds(0.5)
                .lineToSplineHeading(new Pose2d(12, 65.5, Math.toRadians(180)))
                .lineTo(new Vector2d(60, 65.5))
                .lineTo(new Vector2d(24, 65.5))
                .splineTo(new Vector2d(-12, 42), Math.toRadians(-90))
                .waitSeconds(0.5)
                .lineToSplineHeading(new Pose2d(12, 65.5, Math.toRadians(180)))
                .lineTo(new Vector2d(60, 65.5))
                .lineTo(new Vector2d(24, 65.5))
                .splineTo(new Vector2d(-12, 42), Math.toRadians(-90))
                .waitSeconds(0.5)
                .lineToSplineHeading(new Pose2d(12, 65.5, Math.toRadians(180)))
                .lineToConstantHeading(new Vector2d(48, 65.5))
                .strafeLeft(24)
                .back(12)
                .build();
    }

    private static TrajectorySequence carouselBlue(DriveShim drive) {
        return drive.trajectorySequenceBuilder(new Pose2d(-36, 63, Math.toRadians(-90)))
                .splineToConstantHeading(new Vector2d(-64, 42), Math.toRadians(180))
                .lineToLinearHeading(new Pose2d(-64, 56, Math.toRadians(-90)))
                // turn carousel motor on at 300 velocity
                .waitSeconds(4)
                // turn carousel motor off
                .splineToSplineHeading(new Pose2d(-24, 38, Math.toRadians(135)), Math.toRadians(0))
                .waitSeconds(0.5)
                .lineToLinearHeading(new Pose2d(-56, 55, Math.toRadians(90)))
                // turn intake on
                .lineTo(new Vector2d(-56, 63), SampleMecanumDrive.getVelocityConstraint(2, Math.toRadians(180), 13))
                // turn intake off
                .lineToLinearHeading(new Pose2d(-24, 38, Math.toRadians(135)))
                .lineToLinearHeading(new Pose2d(-60, 36, Math.toRadians(90)))
                .build();
    }

    private static TrajectorySequence carouselRed(DriveShim drive) {
        return drive.trajectorySequenceBuilder(new Pose2d(-36, -63, Math.toRadians(90)))
                .splineToLinearHeading(new Pose2d(-64, -42, Math.toRadians(0)), Math.toRadians(180))
                .lineTo(new Vector2d(-64, -56))
                // turn carousel motor on at 300 velocity
                .waitSeconds(4)
                // turn carousel motor off
                .splineToSplineHeading(new Pose2d(-24, -38, Math.toRadians(-135)), Math.toRadians(0))
                .waitSeconds(0.5)
                .lineToLinearHeading(new Pose2d(-56, -55, Math.toRadians(-90)))
                // turn intake on
                .lineTo(new Vector2d(-56, -63), SampleMecanumDrive.getVelocityConstraint(2, Math.toRadians(180), 13))
                // turn intake off
                .lineToLinearHeading(new Pose2d(-24, -38, Math.toRadians(-135)))
                .lineToLinearHeading(new Pose2d(-60, -36, Math.toRadians(-90)))
                .build();
    }
}