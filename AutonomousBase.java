package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public abstract class AutonomousBase extends LinearOpMode {
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
}