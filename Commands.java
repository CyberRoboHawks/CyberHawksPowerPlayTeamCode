package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class Commands extends HardwareMapping {

    static final double COUNTS_PER_MOTOR_REV = 960;    // eg: TETRIX Motor Encoder (40:1)
    static final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    static final double AUTONOMOUS_DRIVE_SPEED = .6;
    static final double AUTONOMOUS_TURN_SPEED = .2;

    public enum Direction {
        Clockwise, CounterClockwise
    }

    /**
     * Init of the robot always zero's out the heading
     * This will turn the robot Clockwise direction until
     * the gyro is at this heading within HEADING_ERROR
     * @param power [-1..1]
     * @param heading [0..360] heading based on init of robot
     * @param timeout seconds until abort for Autonomous
     */
    public void rotateClockwiseGyro(double power,double heading,double timeout){
        this.gyroTurn(power,heading,timeout);
    }

    /**
     * Init of the robot always zero's out the heading
     * This will turn the robot Counter Clockwise direction until
     * the gyro is at this heading within HEADING_ERROR
     * @param power [-1..1]
     * @param heading [0..360] heading based on init of robot
     * @param timeout seconds until abort for Autonomous
     */
    public void rotateCounterClockwiseGyro(double power,double heading,double timeout){
        this.gyroTurn(-power,heading,timeout);
    }

//    public void closeClaw() {
//        clawServo.setPosition(1);
//    }
//    public void openClawMidway(){
//        clawServo.setPosition(0.7);
//    }
//    public void openClaw(){
//        clawServo.setPosition(0.4);
//    }

    private void gyroTurn(double speed, double heading, double timeout){
//        runtime.reset();
//
//        while(Math.abs(getError(heading)) >= 5 && (runtime.seconds() < timeout)){
//            leftFrontMotor.setPower(speed);
//            leftRearMotor.setPower(speed);
//            rightFrontMotor.setPower(-speed);
//            rightRearMotor.setPower(-speed);
//        }
//
//        leftFrontMotor.setPower(0);
//        leftRearMotor.setPower(0);
//        rightFrontMotor.setPower(0);
//        rightRearMotor.setPower(0);
    }

    public double getError(double targetAngle) {
        Orientation angles;
        double robotError;

        // calculate error in -179 to +180 range  (
        angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        robotError = targetAngle - angles.firstAngle;
        return robotError;
    }
}