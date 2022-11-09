package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public abstract class AutonomousBase extends LinearOpMode {
    static final float DRIVE_SPEED = 0.2f;

    Commands commands = new Commands();
    HardwareMapping robot = new HardwareMapping();   // Use our hardware mapping
    ObjectDetection objectDetection = new ObjectDetection();

    public void startupInit() {
        commands.init(hardwareMap);
        robot.init(hardwareMap);
        objectDetection.init(hardwareMap);
        telemetry.addData("Status:", "ready");
        telemetry.update();
    }

    protected void executeOperations(PowerPlayEnums.startingSide startingSide) throws InterruptedException {
        // close grabber on the cone
        commands.grabberClose();
        sleep(200);

        // read the signal for parking zone
        PowerPlayEnums.parkingZone parkingZone = objectDetection.readParkingZone(5, 500);
        telemetry.addData("ParkingZone :", parkingZone);
        telemetry.update();

        // drive forward to move off the wall
        commands.driveForward(DRIVE_SPEED, 5, 2);

        //raise lift to drive height
        commands.liftMoveToPosition(PowerPlayEnums.liftPosition.Drive, 3);
        // hold the lift at the drive position
        commands.liftMoveUp(.1);

        // drive towards the high junction pole and push signal out of the way
        commands.driveForward(DRIVE_SPEED, 55, 5);

        // back up
        commands.driveBackwards(DRIVE_SPEED, 8, 3);

        //  raise lift to the high position
        commands.liftMoveToPosition(PowerPlayEnums.liftPosition.HighJunction, 5);

        // hold the lift at the high position
        commands.liftMoveUp(.1);

        // turn towards the junction pole
        switch (startingSide) {
            case Left:
                commands.spinRight(DRIVE_SPEED, -45, 3);
                break;
            case Right:
                commands.spinLeft(DRIVE_SPEED, 45, 3);
                break;
        }

        // move slightly forward to place cone
        commands.driveForward(DRIVE_SPEED, 8.5, 3);
        sleep(1000);

        commands.liftMoveDown(.2);
        sleep(400);

        commands.liftStop();

        // hold the lift to drop the cone
        commands.liftMoveUp(.1);
        sleep(500);

        //  open grabber and drop cone
        commands.grabberOpen();

//        // hold the lift at the high position
//        commands.liftMoveUp(.1);

        // move backwards away from the high junction
        commands.driveBackwards(DRIVE_SPEED, 8, 2);

        // lower lift
        commands.liftMoveToPosition(PowerPlayEnums.liftPosition.Floor, 5);

        // navigate to parking location
        switch (parkingZone) {
            case Zone1Bolt:
                commands.spinLeft(DRIVE_SPEED, 90, 3);
                commands.driveForward(DRIVE_SPEED, 20, 3);
                break;
            case Zone2Bulb:
                if (startingSide == PowerPlayEnums.startingSide.Left){
                    commands.spinLeft(DRIVE_SPEED, 0, 3);
                }else{
                    commands.spinRight(DRIVE_SPEED, 0, 3);
                }
                // already in zone 2 - stop
                break;
            case Zone3Panel:
                commands.spinRight(DRIVE_SPEED, -90, 3);
                commands.driveForward(DRIVE_SPEED, 22, 5);
                break;
        }
    }
}