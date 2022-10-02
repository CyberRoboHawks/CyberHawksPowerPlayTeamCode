package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.internal.opmode.TelemetryImpl;

public class Commands extends HardwareMapping {

    static final double COUNTS_PER_MOTOR_REV = 1440;    // eg: TETRIX Motor Encoder (1440 - 60:1; 960 - 40:1)
    static final double DRIVE_GEAR_REDUCTION = .33;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    static final double AUTONOMOUS_DRIVE_SPEED = .6;
    static final double AUTONOMOUS_TURN_SPEED = .2;
    private final ElapsedTime runtime  = new ElapsedTime();

    // Drive forward
    // Drive backward
    // Strafe right
    // Strafe left
    // Rotate right (clockwise)
    // Rotate left (counter-clockwise)
    public void driveForward(double power, double distanceInInches, double timeout){
        encoderDrive(power,distanceInInches,timeout);
    }
    public void driveBackwards(double power, double distanceInInches, double timeout){
        encoderDrive(-power,-distanceInInches,timeout);
    }
    public void spinLeft(double power, double degrees, double timeout){
        gyroTurn(-power, power, degrees, timeout);
    }
    public void spinRight(double power, double degrees, double timeout){
        gyroTurn(power, -power, degrees, timeout);
    }

    private void gyroTurn(double leftMotorPower, double rightMotorPower, double degrees, double timeout){
        runtime.reset();

        while(Math.abs(getRemainingAngle(degrees)) >= 2 && (runtime.seconds() < timeout)){
            leftFrontMotor.setPower(leftMotorPower);
            leftBackMotor.setPower(leftMotorPower);
            rightFrontMotor.setPower(rightMotorPower);
            rightBackMotor.setPower(rightMotorPower);
        }
        stopDrivingMotors();
    }

    private double getRemainingAngle(double targetAngle) {
        // calculate angle in -179 to +180 range  (
        Orientation angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return targetAngle - angles.firstAngle;
    }

    private void encoderDrive(double speed, double distanceInches, double timeoutS) {
        int newLeftRearTarget;
        int newLeftFrontTarget;
        int newRightRearTarget;
        int newRightFrontTarget;

        // Determine new target position, and pass to motor controller
        newLeftRearTarget = leftBackMotor.getCurrentPosition() + (int) (distanceInches * COUNTS_PER_INCH);
        newLeftFrontTarget = leftFrontMotor.getCurrentPosition() + (int) (distanceInches * COUNTS_PER_INCH);
        newRightRearTarget = rightBackMotor.getCurrentPosition() + (int) (distanceInches * COUNTS_PER_INCH);
        newRightFrontTarget = rightFrontMotor.getCurrentPosition() + (int) (distanceInches * COUNTS_PER_INCH);

        leftBackMotor.setTargetPosition(newLeftRearTarget);
        leftFrontMotor.setTargetPosition(newLeftFrontTarget);
        rightBackMotor.setTargetPosition(newRightRearTarget);
        rightFrontMotor.setTargetPosition(newRightFrontTarget);

        // Turn On RUN_TO_POSITION
        setMotorRunMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        runtime.reset();
        leftBackMotor.setPower(Math.abs(speed));
        leftFrontMotor.setPower(Math.abs(speed));
        rightBackMotor.setPower(Math.abs(speed));
        rightFrontMotor.setPower(Math.abs(speed));

        // keep looping while we are still active, and there is time left, and both motors are running.
        // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
        // its target position, the motion will stop.  This is "safer" in the event that the robot will
        // always end the motion as soon as possible.
        // However, if you require that BOTH motors have finished their moves before the robot continues
        // onto the next step, use (isBusy() || isBusy()) in the loop test.
        while (
                (runtime.seconds() < timeoutS) &&
                        (leftBackMotor.isBusy()
                                && leftFrontMotor.isBusy()
                                && rightBackMotor.isBusy()
                                && rightFrontMotor.isBusy())) {

        }

        // Stop all motion;
        stopDrivingMotors();

        // Turn off RUN_TO_POSITION
        setMotorRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void setMotorRunMode(DcMotor.RunMode mode) {
        leftBackMotor.setMode(mode);
        leftFrontMotor.setMode(mode);
        rightBackMotor.setMode(mode);
        rightFrontMotor.setMode(mode);
    }

    private void stopDrivingMotors(){
        leftBackMotor.setPower(0);
        leftFrontMotor.setPower(0);
        rightBackMotor.setPower(0);
        rightFrontMotor.setPower(0);
    }
}