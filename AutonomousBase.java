package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public abstract class AutonomousBase extends LinearOpMode {
    Commands commands = new Commands();
    HardwareMapping robot = new HardwareMapping();   // Use our hardware mapping
    //ObjectDetection objectDetection = new ObjectDetection();

    public void startupInit() {
        commands.init(hardwareMap);
        robot.init(hardwareMap);
//        objectDetection.init(hardwareMap);
        telemetry.addData("webcam", "ready");
        telemetry.update();
    }

//    public int readBarcodeFloor() {
//        telemetry.addData("Reading barcode", "...");
//        telemetry.update();
//        int floor = objectDetection.getBarcodeFloor(objectDetection);
//        if (floor == 0) {
//            floor = 1;
//            telemetry.addData("no barcode found using floor ", floor);
//        }
//        telemetry.addData("floor ", floor);
//        telemetry.update();
//        return floor;
//    }
}