package org.firstinspires.ftc.teamcode.Autonomous;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FreightFrenzyVisionPipelineContours extends OpenCvPipeline
{
    public enum ElementPosition
    {
        LEFT,
        MIDDLE,
        RIGHT
    }

    // Color constant to use later
    static final Scalar BLUE = new Scalar(0, 0, 255);
    static final Scalar RED = new Scalar(255, 0, 0);

    ArrayList<Mat> RegionGreen;
    Mat greyscale = new Mat();
    ArrayList<Integer> averages;

    // Volatile since accessed by OpMode thread without synchronization
    private volatile ElementPosition position = ElementPosition.LEFT;

    void inputToGreyscale(Mat input) {
        Imgproc.cvtColor(input, greyscale, Imgproc.COLOR_RGB2GRAY);
    }

    @Override
    public void init(Mat firstFrame)
    {
        inputToGreyscale(firstFrame);
    }

    @Override
    public Mat processFrame(Mat input)
    {
        Mat binary = new Mat(input.rows(), input.cols(), input.type(), new Scalar(0));
        Imgproc.threshold(greyscale, binary, 100, 255, Imgproc.THRESH_BINARY_INV);
        //Finding Contours
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(binary, contours, hierarchy, Imgproc.RETR_TREE,
                Imgproc.CHAIN_APPROX_SIMPLE);

        Mat draw = Mat.zeros(input.size(), CvType.CV_8UC3);

        MatOfPoint shippingElement = contours.get(0);

        for (int i = 0; i < contours.size(); i++) {
            //Calculating the area
            double cont_area = Imgproc.contourArea(contours.get(i));
            System.out.println(cont_area);
            if (cont_area > 5000.0) {
                Imgproc.drawContours(draw, contours, i, BLUE, 2,
                        Imgproc.LINE_8, hierarchy, 2, new Point());

            } else {
                Imgproc.drawContours(draw, contours, i, RED, 2, Imgproc.LINE_8,
                        hierarchy, 2, new Point());
            }

            if (cont_area > Imgproc.contourArea(shippingElement)) {
                shippingElement = contours.get(i);
            }
        }
        double centerX = Imgproc.moments(shippingElement).get_m10() / Imgproc.moments(shippingElement).get_m00();
        double centerY = Imgproc.moments(shippingElement).get_m01() / Imgproc.moments(shippingElement).get_m00();



        return input;
    }

    public ArrayList<Integer> getAnalysis()
    {
        return averages;
    }

    public Integer getAnalysis(int index) {
        return averages.get(index);
    }
}