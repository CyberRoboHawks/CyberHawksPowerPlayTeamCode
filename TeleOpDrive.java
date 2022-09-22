package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@TeleOp(name="TeleOpDrive", group="TeleOp")
//@Disabled
public class TeleOpDrive extends LinearOpMode {
    static final double DRIVE_SPEED = 0.7;

    /* Declare OpMode members. */
    HardwareMapping robot = new HardwareMapping();   // Use our hardware mapping
    Commands commands = new Commands();
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        // Initialize the hardware variables.
        robot.init(hardwareMap);
        commands.init(hardwareMap);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            //Driver controller ---------------------
            if (gamepad1 != null) {
                // Mecanum drive controls
                double speed = -gamepad1.left_stick_y; // Note: pushing stick forward gives negative value
                double turn = gamepad1.right_stick_x;
                double strafe = gamepad1.left_stick_x;

                // Combine the joystick requests for each axis-motion to determine each wheel's power.
                // Set up a variable for each drive wheel to save the power level for telemetry.
                double leftFrontPower  = speed + turn + strafe;
                double rightFrontPower = speed - turn - strafe;
                double leftRearPower   = speed - turn + strafe;
                double rightRearPower  = speed + turn - strafe;

//                // This ensures that the robot maintains the desired motion.
//                double max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
//                max = Math.max(max, Math.abs(leftRearPower));
//                max = Math.max(max, Math.abs(rightRearPower));
//
//                if (max > 1.0) {
//                    leftFrontPower  /= max;
//                    rightFrontPower /= max;
//                    leftRearPower   /= max;
//                    rightRearPower  /= max;
//                }

                // Send calculated power to wheels
//                robot.leftFrontMotor.setPower(leftFrontPower);
//                robot.rightFrontMotor.setPower(rightFrontPower);
//                robot.leftRearMotor.setPower(leftRearPower);
//                robot.leftRearMotor.setPower(rightRearPower);

                // Show the elapsed game time and wheel power.
                telemetry.addData("Status", "Run Time: " + runtime.seconds());
                telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
                telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftRearPower, rightRearPower);
                telemetry.update();
            }

            // Pace this loop so jaw action is reasonable speed.
            sleep(40);
            idle();
        }
    }
}
