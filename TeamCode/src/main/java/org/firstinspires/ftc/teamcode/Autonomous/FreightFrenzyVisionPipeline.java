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
import java.util.concurrent.CopyOnWriteArrayList;

public class FreightFrenzyVisionPipeline extends OpenCvPipeline {

    /*
     * An enum that defines the positions of the TSE
     */
    public enum ElementPosition {
        LEFT,
        MIDDLE,
        RIGHT
    }

    /*
     * Colour constants
     */
    static final Scalar BLUE = new Scalar(0, 0, 255);

    /*
     * The values for the regions that the program detects in
     * TODO: Tune the positions and sizes of the regions
     */
    public static final Point[] Region1 = {new Point(109, 98), new Point(149, 138)};
    public static final Point[] Region2 = {new Point(181, 98), new Point(221, 138)};
    public static final Point[] Region3 = {new Point(253, 98), new Point(293, 138)};

    /*
     * Working variables
     */

    CopyOnWriteArrayList<Mat> RegionCr = new CopyOnWriteArrayList<>();
    Mat YCrCb = new Mat();
    Mat Cr = new Mat();
    CopyOnWriteArrayList<Integer> averages = new CopyOnWriteArrayList<>();

    // Volatile since accessed by OpMode thread without synchronization
    private volatile ElementPosition position = ElementPosition.LEFT;

    /**
     * Takes the input and converts it into YCrCb colour space, then extracts the Cr channel to the 'Cr' matrix
     * @param input The input frame
     */
    void inputToCr(Mat input)
    {
        Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
        Core.extractChannel(YCrCb, Cr, 1);
    }

    /**
     * Initialises the pipeline and adds all of the submats to the RegionCr ArrayList
     * @param firstFrame First captured frame
     */
    @Override
    public void init(Mat firstFrame)
    {
        inputToCr(firstFrame);

        RegionCr.add(Cr.submat(new Rect(Region1[0], Region1[1])));
        RegionCr.add(Cr.submat(new Rect(Region2[0], Region2[1])));
        RegionCr.add(Cr.submat(new Rect(Region3[0], Region3[1])));
    }

    /**
     * Processes the frame and determines where the TSE is
     * @param input Frame from the camera that is used to determine where the TSE is
     * @return The input image with annotated rectangles to show on the camera preview screen
     */
    @Override
    public Mat processFrame(Mat input)
    {
        inputToCr(input);

        averages.clear();

        /*
         * Computes the average pixel value of each submat region and adds them to the
         * average ArrayList
         */
        averages.add((int) Core.mean(RegionCr.get(0)).val[0]);
        averages.add((int) Core.mean(RegionCr.get(1)).val[0]);
        averages.add((int) Core.mean(RegionCr.get(2)).val[0]);

        /*
         * Draws rectangles on the screen denoting the locations of the sample regions; used for visual
         * aid
         */
        Imgproc.rectangle(
            input, // Buffer to draw on
            Region1[0], // First point which defines the rectangle
            Region1[1], // Second point which defines the rectangle
            BLUE, // The colour the rectangle is drawn in
            2); // Thickness of the rectangle lines
        Imgproc.rectangle(
            input, // Buffer to draw on
            Region2[0], // First point which defines the rectangle
            Region2[1], // Second point which defines the rectangle
            BLUE, // The colour the rectangle is drawn in
            2); // Thickness of the rectangle lines
        Imgproc.rectangle(
            input, // Buffer to draw on
            Region3[0], // First point which defines the rectangle
            Region3[1], // Second point which defines the rectangle
            BLUE, // The colour the rectangle is drawn in
            2); // Thickness of the rectangle lines

        /*
         * Determines the region with the maximum pixel value by finding the maximum value in the averages
         * ArrayList
         */
        int maxRegion = averages.indexOf(Collections.max(averages));

        /*
         * Based on the region that has the minimum Cr value, the position of the TSE is determined
         * accordingly
         *
         * e.g. If the first region (region at index 0) has the minimum Cr value, then the position
         * of the TSE is the left
         */
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

        /*
         * Returns the input buffer to the viewport with the annotations above added
         */
        return input;
    }

    /**
     * Returns the averages of each of the three regions in an ArrayList
     * @return The averages
     */
    public CopyOnWriteArrayList<Integer> getAnalysis()
    {
        return averages;
    }

    /**
     * Returns the average of the specified region
     * @param index Specified region
     * @return The average
     */
    public Integer getAnalysis(int index) {
        return averages.get(index);
    }

    /**
     * Returns the position of the TSE
     * @return The position of the TSE
     */
    public ElementPosition getPosition() {
        return position;
    }
}