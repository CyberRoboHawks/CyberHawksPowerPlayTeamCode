package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@Autonomous(name="Autonomous", group="Pushbot")
public class AutonomousDrive extends AutonomousBase {

    @Override
    public void runOpMode() {
        // Initialize the hardware variables.
        startupInit();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            Orientation angles = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            telemetry.addData("Roll (x)", "%4.2f", angles.thirdAngle);
            telemetry.addData("Pitch (y)", "%4.2f", angles.secondAngle);
            telemetry.addData("Yaw (z)", "%4.2f",  angles.firstAngle);

            telemetry.update();

            //sleep(30000);
        }
    }
}