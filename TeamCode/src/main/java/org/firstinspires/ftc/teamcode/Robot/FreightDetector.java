package org.firstinspires.ftc.teamcode.Robot;

import android.graphics.Color;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

@Config
public class FreightDetector extends Subsystem {
    public static float gain = 2;

    static NormalizedColorSensor freightDetector;

    public FreightDetector(HardwareMap hwMap) {
        freightDetector = hwMap.get(NormalizedColorSensor.class, "freightDetector");
        freightDetector.setGain(gain);
        if (freightDetector instanceof SwitchableLight) {
            ((SwitchableLight) freightDetector).enableLight(true);
        }
    }

    public NormalizedRGBA getValues() {
        return freightDetector.getNormalizedColors();
    }

    public float[] getHSVValues() {
        float[] hsvValues = new float[3];
        Color.colorToHSV(getValues().toColor(), hsvValues);
        return hsvValues;
    }

    public boolean freightDetected() {
        // if some condition is met then return true
        // TODO: add condition
        if (getHSVValues()[0] > 200) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void subsystemLoop() {

    }
}