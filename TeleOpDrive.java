package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@TeleOp(name = "TeleOpDrive", group = "TeleOp")
//@Disabled
public class TeleOpDrive extends LinearOpMode {
    HardwareMapping robot = new HardwareMapping();   // Use our hardware mapping
    Commands commands = new Commands();
    static final double STANDARD_DRIVE_SPEED = .3;
    static final double TURBO_DRIVE_SPEED = .5;
    static final double ROTATE_SPEED = .3;
    static final double LIFT_MAX_UP_POWER = .5;
    static final double LIFT_MAX_DOWN_POWER = .15;
    static final double LIFT_HOLD_POWER = .2;

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize the hardware variables.
        robot.init(hardwareMap);
        commands.init(hardwareMap);

        boolean fieldCentric = false;
        double strafePower;
        double forwardPower;
        double rotatePower;
        double offsetHeading = 0;

        boolean isGrabberOpen = true;
        double liftPower = 0;

        telemetry.addData("Status:", "Ready");
        telemetry.addData("Driving Mode:", "Robot Centric");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        //robot.imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double drivePower = STANDARD_DRIVE_SPEED;  //1 is 100%, .5 is 50%

            if (fieldCentric) {
                telemetry.addData("Driving Mode:", "Field Centric");
            } else {
                telemetry.addData("Driving Mode:", "Robot Centric");
            }

            //Driver controller ---------------------
            if (gamepad1 != null) {

                // Field centric toggle
                if (gamepad1.b && gamepad1.right_bumper) {
                    fieldCentric = !fieldCentric;
                    // Pause to let the user release the button
                    sleep(250);
                }

                // Turbo driving
                if (gamepad1.left_bumper) {
                    drivePower = TURBO_DRIVE_SPEED;  // turbo speed when the left bumper is pressed
                }

                // mecanum driving
                strafePower = gamepad1.left_stick_x;
                forwardPower = -gamepad1.left_stick_y;
                rotatePower = gamepad1.right_stick_x;

                offsetHeading = 0;
                if (fieldCentric) {
                    offsetHeading = getAngle();
                }

                driveFieldCentric(strafePower, forwardPower, rotatePower, drivePower, offsetHeading);
            }

            //Co-Driver controller ---------------------
            if (gamepad2 != null) {
                if (gamepad2.b) {
                    if (isGrabberOpen) {
                        commands.grabberClose();
                    } else {
                        commands.grabberOpen();
                    }
                    isGrabberOpen = !isGrabberOpen;
                    sleep(300);
                }

                if (gamepad2.left_bumper){
                    // Hold the lift in place
                    commands.liftMoveUp(LIFT_HOLD_POWER);
                }

                if (gamepad2.a){
                    // move the lift off of field level
                    commands.liftMoveToPosition(PowerPlayEnums.liftPosition.Drive, 3);
                    // hold the lift at the drive position
                    commands.liftMoveUp(LIFT_HOLD_POWER);
                }

                liftPower = -gamepad2.left_stick_y;
                if (abs(liftPower) != 0) {
                    if (liftPower > 0) {
                        if (robot.liftMaxSensor.isPressed()){
                            // hold the lift at the top with minimal power
                            liftPower = LIFT_HOLD_POWER;
                        }
                        else {
                            liftPower = liftPower * LIFT_MAX_UP_POWER;
                        }

                        commands.liftMoveUp(liftPower);
                        //telemetry.addData("lift move up at:", liftPower);
                    } else {
                        if (robot.liftMinSensor.isPressed()){
                            // at the bottom no power needed
                            liftPower = 0;
                        }
                        else {
                            liftPower = liftPower * LIFT_MAX_DOWN_POWER;
                        }

                        commands.liftMoveDown(liftPower);
                        //telemetry.addData("lift move down at:", liftPower );
                    }
                } else {
                    commands.liftStop();
                }
            }
            //telemetry.addData("robot angle:", getAngle());
            telemetry.update();
        }
    }

    private void driveFieldCentric(double strafeSpeed, double forwardSpeed, double rotate, double drivePower, double heading) {
        //finds just how much power to give the robot based on how much x and y given by gamepad
        //range.clip helps us keep our power within positive 1
        // also helps set maximum possible value of 1/sqrt(2) for x and y controls if at a 45 degree angle (which yields greatest possible value for y+x)
        double power = Range.clip(Math.hypot(strafeSpeed, forwardSpeed), 0, 1);

        //the inverse tangent of opposite/adjacent gives us our gamepad degree
        double theta = Math.atan2(forwardSpeed, strafeSpeed);
        double movementDegree = theta - Math.toRadians(heading);

        // Calculate the adjusted degrees for x and y movement
        double yOffset = Math.sin(movementDegree - Math.PI / 4);
        double xOffset = Math.cos(movementDegree - Math.PI / 4);

        double maxOffset = Math.max(abs(yOffset), abs(xOffset));

        // normalize the x/y offset and apply control stick power
        yOffset = power * yOffset / maxOffset;
        xOffset = power * xOffset / maxOffset;

        double leftFrontPower = (xOffset + rotate) * drivePower;
        double leftBackPower = (yOffset + rotate) * drivePower;
        double rightFrontPower = (yOffset - rotate) * drivePower;
        double rightBackPower = (xOffset - rotate) * drivePower;

        // Set the power on each motor
        robot.leftFrontMotor.setPower(leftFrontPower);
        robot.leftBackMotor.setPower(leftBackPower);
        robot.rightFrontMotor.setPower(rightFrontPower);
        robot.rightBackMotor.setPower(rightBackPower);
    }

    //allows us to quickly get our z angle
    private double getAngle() {
        return robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
    }
}