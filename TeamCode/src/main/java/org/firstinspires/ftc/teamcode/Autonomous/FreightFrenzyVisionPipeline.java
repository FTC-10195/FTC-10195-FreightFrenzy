package org.firstinspires.ftc.teamcode.Autonomous;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.Collections;

public class FreightFrenzyVisionPipeline extends OpenCvPipeline {

    public enum ElementPosition {
        LEFT,
        MIDDLE,
        RIGHT
    }

    // Some colour constants
    static final Scalar BLUE = new Scalar(0, 0, 255);

    // The values for the regions that the program detects in
    public static final Point[] Region1 = {new Point(), new Point()};
    public static final Point[] Region2 = {new Point(), new Point()};
    public static final Point[] Region3 = {new Point(), new Point()};

    ArrayList<Mat> RegionGreen;
    Mat green = new Mat();
    ArrayList<Integer> averages;

    // Volatile since accessed by OpMode thread without synchronization
    private volatile ElementPosition position = ElementPosition.LEFT;

    void inputToGreen(Mat input)
    {
        Core.extractChannel(input, green, 2);
    }

    @Override
    public void init(Mat firstFrame)
    {
        inputToGreen(firstFrame);

        RegionGreen.add(green.submat(new Rect(Region1[0], Region1[1])));
        RegionGreen.add(green.submat(new Rect(Region2[0], Region2[1])));
        RegionGreen.add(green.submat(new Rect(Region3[0], Region3[1])));
    }

    @Override
    public Mat processFrame(Mat input)
    {
        averages.add((int) Core.mean(RegionGreen.get(0)).val[0]);
        averages.add((int) Core.mean(RegionGreen.get(1)).val[0]);
        averages.add((int) Core.mean(RegionGreen.get(2)).val[0]);

        Imgproc.rectangle(
            input, // Buffer to draw on
            Region1[0], // First point which defines the rectangle
            Region1[1], // Second point which defines the rectangle
            BLUE, // The color the rectangle is drawn in
            2); // Thickness of the rectangle lines
        Imgproc.rectangle(
            input, // Buffer to draw on
            Region2[0], // First point which defines the rectangle
            Region2[1], // Second point which defines the rectangle
            BLUE, // The color the rectangle is drawn in
            2); // Thickness of the rectangle lines
        Imgproc.rectangle(
            input, // Buffer to draw on
            Region3[0], // First point which defines the rectangle
            Region3[1], // Second point which defines the rectangle
            BLUE, // The color the rectangle is drawn in
            2); // Thickness of the rectangle lines

        int maxRegion = averages.indexOf(Collections.max(averages));
        switch (maxRegion) {
            case 0:
            default:
                position = ElementPosition.LEFT;
            case 1:
                position = ElementPosition.MIDDLE;
                break;
            case 2:
                position = ElementPosition.RIGHT;
                break;
        }
        return input;
    }

    public ArrayList<Integer> getAnalysis()
    {
        return averages;
    }

    public Integer getAnalysis(int index) {
        return averages.get(index);
    }

    public ElementPosition getPosition() {
        return position;
    }
}