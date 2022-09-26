package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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
    HardwareMapping robot = new HardwareMapping();   // Use our hardware mapping

    @Override
    public void runOpMode() {
        // Initialize the hardware variables.
        robot.init(hardwareMap);
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double drivePower = .5;  //1 is 100%, .5 is 50%

            //Driver controller ---------------------
            if (gamepad1 != null) {
                // TODO add button mapping for "slow" drive

                // Mecanum drive controls
                double power = -gamepad1.left_stick_y;
                double side = gamepad1.left_stick_x;
                double rotate = gamepad1.right_stick_x;

                double leftFrontPower  = (power + side + rotate) * drivePower;
                double rightFrontPower = (power - side - rotate) * drivePower;
                double leftBackPower   = (power - side + rotate) * drivePower;
                double rightBackPower  = (power + side - rotate) * drivePower;

                robot.leftFrontMotor.setPower(leftFrontPower);
                robot.leftBackMotor.setPower(leftBackPower);
                robot.rightFrontMotor.setPower(rightFrontPower);
                robot.rightBackMotor.setPower(rightBackPower);
            }
        }
    }
}
