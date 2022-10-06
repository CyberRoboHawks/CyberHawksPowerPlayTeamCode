package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@Autonomous(name = "Autonomous", group = "Pushbot")
public class AutonomousDrive extends AutonomousBase {

    @Override
    public void runOpMode() {
        // Initialize the hardware variables.
        startupInit();
        final double DRIVE_SPEED = 0.2;

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            // TODO add Autonomous actions here

//            commands.driveForward(0.2,10,2);
//            sleep(450);
//            commands.driveBackwards(0.2,10,2);
//            sleep(499);
//            commands.spinLeft(.2, 90, 3);
//            sleep(500);
//            commands.spinRight(.2, -90, 3);
//            sleep(500);
//            commands.spinLeft(.2, 0, 3);
//            sleep(500);

            // Drive in a square
//            commands.driveForward(DRIVE_SPEED, 20, 2);
//            sleep(500);
//            commands.spinLeft(DRIVE_SPEED, 90, 3);
//            sleep(500);
//            commands.driveForward(DRIVE_SPEED, 20, 2);
//            sleep(500);
//            commands.spinLeft(DRIVE_SPEED, 180, 3);
//            sleep(500);
//            commands.driveForward(DRIVE_SPEED, 20, 2);
//            sleep(500);
//            commands.spinLeft(DRIVE_SPEED, -90, 3);
//            sleep(500);
//            commands.driveForward(DRIVE_SPEED, 20, 2);
//            sleep(500);
//            commands.spinLeft(DRIVE_SPEED, 0, 3);
//            sleep(500);
            commands.strafeRight(DRIVE_SPEED, 20, 2);
            commands.strafeLeft(DRIVE_SPEED, 30, 2);
            sleep(30000);
        }
    }
}


//            Orientation angles = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
//            telemetry.addData("Roll (x)", "%4.2f", angles.thirdAngle);
//            telemetry.addData("Pitch (y)", "%4.2f", angles.secondAngle);
//            telemetry.addData("Yaw (z)", "%4.2f",  angles.firstAngle);
//            telemetry.update();