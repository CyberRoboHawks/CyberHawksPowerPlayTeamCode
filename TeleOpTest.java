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

@TeleOp(name = "TeleOpTest", group = "TeleOp")
@Disabled
public class TeleOpTest extends LinearOpMode {
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
            //commands.liftResetPosition();
            double drivePower = .25;  //1 is 100%, .5 is 50%

            //Driver controller ---------------------
            if (gamepad1 != null) {

                if (gamepad1.left_bumper){
                    commands.liftMoveUp(.5);
                }

                if (gamepad1.b) {
                    commands.liftMoveDown(-.4);
                } else {
                    commands.liftStop();
                }

                if (gamepad1.a) {
                    commands.liftMoveUp(1);
                } else {
                    commands.liftStop();
                }

                if (gamepad1.y) {
                    commands.grabberClose();
                }
                if (gamepad1.x) {
                    commands.grabberOpen();
                }
            }

        }
    }
}