package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Carousel extends Subsystem {
    public enum CarouselState {
        SETUP,
        START,
        SPEED
    }

    private CarouselState carouselState = CarouselState.SETUP;

    public static int carouselStartTime = 1300;
    public static int carouselSpeedTime = 600;
    public static int carouselNormalVelocity = 600;
    public static int carouselSpeedVelocity = 4000;

    private double carouselVelocity;

    ElapsedTime carouselMotorTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    private DcMotorEx carouselMotor;

    public Carousel(HardwareMap hwMap) {
        carouselMotor = hwMap.get(DcMotorEx.class, "duck");
        carouselMotor.setDirection(DcMotorEx.Direction.FORWARD);
        carouselMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
    }

    public void determineCarouselPower(boolean carouselForward, boolean carouselBackward) {
        switch (carouselState) {
            case SETUP:
                if (carouselForward) {
                    carouselMotorTimer.reset();
                    carouselMotor.setDirection(DcMotorEx.Direction.FORWARD);
                    carouselState = CarouselState.START;
                } else if (carouselBackward) {
                    carouselMotorTimer.reset();
                    carouselMotor.setDirection(DcMotorEx.Direction.REVERSE);
                    carouselState = CarouselState.START;
                }
                break;
            case START:
                carouselVelocity = carouselNormalVelocity;
                if (carouselMotorTimer.time() > carouselStartTime) {
                    carouselMotorTimer.reset();
                    carouselState = CarouselState.SPEED;
                }
                break;
            case SPEED:
                carouselVelocity = carouselSpeedVelocity;
                if (carouselMotorTimer.time() > carouselSpeedTime) {
                    carouselMotorTimer.reset();
                    carouselVelocity = 0;
                    carouselState = CarouselState.SETUP;
                }
                break;
        }
    }

    @Override
    public void runMotorsAndServos() {
        carouselMotor.setVelocity(carouselVelocity);
    }
}