package com.example.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedLight;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.DriveShim;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        DefaultBotBuilder myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(70, 70, Math.toRadians(360), Math.toRadians(180), 10)
                .setDimensions(13, 18);

        RoadRunnerBotEntity entity = myBot.followTrajectorySequence(onlyCyclesBlue(myBot.build().getDrive()));

        meepMeep.setBackground(MeepMeep.Background.FIELD_FREIGHTFRENZY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(entity)
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
                //.lineToLinearHeading(new Pose2d(12, 65.5, Math.toRadians(180)))
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
}