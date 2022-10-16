package org.firstinspires.ftc.teamcode;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.List;

@Autonomous(name = "Vision Test", group = "Pushbot")
//@Disabled
public class VisionTest extends LinearOpMode {
    ObjectDetection objectDetection = new ObjectDetection();

    @Override
    public void runOpMode() throws InterruptedException {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        objectDetection.init(hardwareMap);

        telemetry.addData("webcam", "ready");
        telemetry.update();
        // Trying to call objectDetection will not work before game start

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of Autonomous
        while (opModeIsActive()) {

           // getAllRecognitions();

            PowerPlayEnums.parkingZone parkingZone = objectDetection.readParkingZone(5, 500);
            telemetry.addData("parking location found", parkingZone);
            telemetry.update();

            sleep(30000);
        }
    }


    private void getAllRecognitions() {
        List<Recognition> objects;
        for (int i = 0; i <= 600; i++) {
            objects = objectDetection.readCamera();
            telemetry.addData("count", objects.size());
            if (objects != null || !objects.isEmpty()) {
                for (Recognition recognition : objects) {
                    telemetry.addData("label", recognition.getLabel());
                    telemetry.addData("confidence", recognition.getConfidence());
                }
            }
            telemetry.update();
            sleep(500);
        }
    }
}