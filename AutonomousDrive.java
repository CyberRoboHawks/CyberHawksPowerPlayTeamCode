package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Autonomous", group = "Pushbot")
public class AutonomousDrive extends AutonomousBase {

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize the hardware variables.
        startupInit();
        final double DRIVE_SPEED = 0.2;
        PowerPlayEnums.startingSide startingSide = PowerPlayEnums.startingSide.Right;

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            // read the signal for parking zone
            PowerPlayEnums.parkingZone parkingZone = objectDetection.readParkingZone(5, 500);

            // close grabber on the cone
            commands.grabberClose();

            //raise lift to drive height
            commands.liftMoveToPosition(PowerPlayEnums.liftPosition.Drive);

            // drive towards the high junction pole
            commands.driveForward(DRIVE_SPEED, 54, 5);

            // TODO raise lift to the high position

            // turn towards the junction pole
            switch (startingSide){
                case Left:
                    commands.spinRight(DRIVE_SPEED, -45, 3);
                    break;
                case Right:
                    commands.spinLeft(DRIVE_SPEED, 45, 3);
                    break;
            }
            //TODO may need to move slightly forward to place cone

            // TODO open grabber and drop cone

            // turn back towards the init/starting heading
            switch (startingSide){
                case Left:
                    commands.spinLeft(DRIVE_SPEED, 0, 3);
                    break;
                case Right:
                    commands.spinRight(DRIVE_SPEED, 0, 3);
                    break;
            }

            // lower lift to drive height
            commands.liftMoveToPosition(PowerPlayEnums.liftPosition.Drive);

            // navigate to parking location
            switch (parkingZone) {
                case Zone1Bolt:
                    commands.spinLeft(DRIVE_SPEED, 90, 3);
                    commands.driveForward(DRIVE_SPEED, 24, 3);
                    break;
                case Zone2Bulb:
                    // already in zone 2 - stop
                    break;
                case Zone3Panel:
                    commands.spinRight(DRIVE_SPEED, -90, 3);
                    commands.driveForward(DRIVE_SPEED, 24, 5);
                    break;
            }

           sleep(30000);
        }
    }
}

//            Orientation angles = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
//            telemetry.addData("Roll (x)", "%4.2f", angles.thirdAngle);
//            telemetry.addData("Pitch (y)", "%4.2f", angles.secondAngle);
//            telemetry.addData("Yaw (z)", "%4.2f",  angles.firstAngle);
//            telemetry.update();