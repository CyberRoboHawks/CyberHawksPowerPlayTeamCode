package org.firstinspires.ftc.teamcode;
import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.internal.camera.delegating.DelegatingCaptureSequence;

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

    public ObjectDetection(){
    }

    public void init(HardwareMap ahwMap) {
        super.init(ahwMap);
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
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
            tfod.setZoom(1.0, 16.0/9.0);
        }
    }

    /**
     * Returns the duck position if it's found on during detection
     * @return the X position of the duck {-1 = Not found, value between 0-600}
     */
    public float getDuckPosition(){
        float duck = -1.0f;
        if (tfod != null) {
            // Using getRecognitions because it returns a value every time it's called
            List<Recognition> updatedRecognitions = tfod.getRecognitions();

            if (updatedRecognitions != null) {
                // step through the list of recognitions and get Duck position
                for (Recognition recognition : updatedRecognitions) {
                     if(recognition.getLabel().equals("Duck")){
                         // find the center of the duck recognition box
                         duck = (recognition.getLeft() + recognition.getRight()) / 2;
                    }
                }
            }
        }
        return duck;
    }

    /**
     * Returns an assessment of the object detection based on duck position
     * @return the determined state {1 = level 1 (Default), 2= level 2, 3 = level 3}
     */
    public int getDuckState(float duckPosition){
        // Split the viewable screen (~600 wide) into 3 zones and if the duck is found in that zone return that zone number
        if (duckPosition < 0)
            return 0;
        if (duckPosition < 200)
            return 1;
        else if(duckPosition < 400)
            return 2;
        else
            return 3;
    }

    /**
     * Initialize the Vuforia localization engine.
     */
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
        // reducing confidence to 50%
        tfodParameters.minResultConfidence = 0.3f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);

        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }
}
