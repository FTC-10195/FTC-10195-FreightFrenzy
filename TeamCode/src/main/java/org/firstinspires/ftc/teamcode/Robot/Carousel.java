package org.firstinspires.ftc.teamcode.Robot;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
public class Carousel extends Subsystem {
    private enum CarouselState {
        START,
        NORMAL,
        SPEED
    }

    private CarouselState carouselState = CarouselState.START;

    public static int carouselStartTime = 1100;
    public static int carouselSpeedTime = 500;
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

    public void drive(boolean carouselForward, boolean carouselBackward) {
        determineCarouselPower(carouselForward, carouselBackward);
    }

    public void determineCarouselPower(boolean carouselForward, boolean carouselBackward) {
        switch (carouselState) {
            case START:
                if (carouselForward) {
                    carouselMotorTimer.reset();
                    carouselMotor.setDirection(DcMotorEx.Direction.FORWARD);
                    carouselState = CarouselState.NORMAL;
                } else if (carouselBackward) {
                    carouselMotorTimer.reset();
                    carouselMotor.setDirection(DcMotorEx.Direction.REVERSE);
                    carouselState = CarouselState.NORMAL;
                }
                break;
            case NORMAL:
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
                    carouselState = CarouselState.START;
                }
                break;
        }
    }

    @Override
    public void subsystemLoop() {
        carouselMotor.setVelocity(carouselVelocity);
    }
}