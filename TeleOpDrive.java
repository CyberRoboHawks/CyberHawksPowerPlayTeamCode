package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@TeleOp(name = "TeleOpDrive", group = "TeleOp")
//@Disabled
public class TeleOpDrive extends LinearOpMode {
    HardwareMapping robot = new HardwareMapping();   // Use our hardware mapping
    Commands commands = new Commands();

    @Override
    public void runOpMode() {
        // Initialize the hardware variables.
        robot.init(hardwareMap);
        commands.init(hardwareMap);

        telemetry.addData("Status:", "Ready");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double drivePower = .25;  //1 is 100%, .5 is 50%

            //Driver controller ---------------------
            if (gamepad1 != null) {
                // Turbo driving
                if (gamepad1.left_bumper) {
                    drivePower = .5;  // set drive power to .5 when the left bumper is pressed
                } else {
                    drivePower = .25; // set drive power to .25 when the left bumper is released
                }

                // Mecanum drive controls
                double power = -gamepad1.left_stick_y;
                double side = gamepad1.left_stick_x;
                double rotate = gamepad1.right_stick_x;

                // Calculate the power for each motor
                double leftFrontPower = (power + side + rotate) * drivePower;
                double rightFrontPower = (power - side - rotate) * drivePower;
                double leftBackPower = (power - side + rotate) * drivePower;
                double rightBackPower = (power + side - rotate) * drivePower;

                telemetry.addData("left front power:", leftFrontPower);
                telemetry.addData("right front power:", rightFrontPower);
                telemetry.addData("left back power:", leftBackPower);
                telemetry.addData("right back power:", rightBackPower);
                telemetry.update();

                // Set the power on each motor
                robot.leftFrontMotor.setPower(leftFrontPower);
                robot.leftBackMotor.setPower(leftBackPower);
                robot.rightFrontMotor.setPower(rightFrontPower);
                robot.rightBackMotor.setPower(rightBackPower);
            }

            //Co-Driver controller ---------------------
            if (gamepad2 != null) {

            }
        }
    }
}