package org.firstinspires.ftc.teamcode;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

public class ObjectDetection extends HardwareMapping {
    private static final String TFOD_MODEL_ASSET = "PowerPlay.tflite";

    private static final String[] LABELS = {
            "1 Bolt",
            "2 Bulb",
            "3 Panel"
    };

    private static final String VUFORIA_KEY = "AQXDFuj/////AAABmVKd60bbfEvEoe2tGEzNEiB312vgHtihnIKjnUII+7eEo3+nl+Z6JvAbKxlIbWrnIkww+1rCMd7fvsnS5kAcmwlnukJziOiI7AaygpLSIpdTUbMsO3OuvSSx98ZkNfKhsFKU9B9ys6DQSK8PS6I+33IUM5F6Q9bav5OlkbKahByQl9xQlbH/YMJ2Sm0XGk83HnvX630lYQV42rY/91jA8l3uwR/DV4Cj3BTdu7RBJIGNFTS+9WUMkGBI8Sjxd00DIb8gzbTYgGE9NwNA1tOfQsLZwhunC4CobZQRDFGrKOZzjOrMDku4ww2qQQe44x4JiDXmjESw2vFCyIbUYsc+Q8Tc6HusKjDndUryJBWJGX+J";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    public ObjectDetection() {
    }

    public void init(HardwareMap ahwMap) {
        super.init(ahwMap);
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that first.
        initVuforia();
        initTfod();

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null) {
            tfod.activate();
            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // If your target is at distance greater than 50 cm (20") you can increase the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 16/9).
            tfod.setZoom(1.0, 16.0 / 9.0);
        }
    }

    public List<Recognition> readCamera() {
        if (tfod == null) {
            return null;
        }
        // Using getRecognitions because it returns a value every time it's called
        return tfod.getRecognitions();
    }

    public PowerPlayEnums.parkingZone readParkingZone(int attempts, long waitInMs) throws InterruptedException {
        PowerPlayEnums.parkingZone parkingZone = readSignal();
        if (parkingZone == null){
            // try to read the signal for a set number of attempts and wait between each try
            for (int i = 0; i <= attempts; i++) {
                parkingZone = readSignal();
                if (parkingZone != null){
                    break;
                }
                sleep(waitInMs);
            }
        }

        // TODO if parkingZone not found set to Zone2Bulb (default)

        return parkingZone;
    }

    // Initialize the Vuforia localization engine.
    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        //parameters.cameraName = webcamName;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        // reducing confidence to 30% for team signal
        tfodParameters.minResultConfidence = 0.3f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);

        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }

    private PowerPlayEnums.parkingZone readSignal() {
        if (tfod != null) {
            // Using getRecognitions because it returns a value every time it's called
            List<Recognition> recognitions = tfod.getRecognitions();

            if (recognitions != null) {
                for (Recognition recognition : recognitions) {
                    switch (recognition.getLabel()) {
                        case "1 Bolt":
                            return PowerPlayEnums.parkingZone.Zone1Bolt;
                        case "2 Bulb":
                            return PowerPlayEnums.parkingZone.Zone2Bulb;
                        case "3 Panel":
                            return PowerPlayEnums.parkingZone.Zone3Panel;
                    }
                }
            }
        }
        return null;
    }
}
