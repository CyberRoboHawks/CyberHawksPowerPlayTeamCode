package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

public class HardwareMapping {
    public DcMotor leftFrontMotor = null;
    public DcMotor leftBackMotor = null;
    public DcMotor rightFrontMotor = null;
    public DcMotor rightBackMotor = null;
    public TouchSensor liftMaxSensor = null;
    public TouchSensor liftMinSensor = null;
    public Servo grabberServo = null;

    public DcMotor liftMotorLeft = null;
    public DcMotor liftMotorRight = null;

    public BNO055IMU imu = null;

    HardwareMap hardwareMap = null;
    public ElapsedTime runtime = new ElapsedTime();

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap hardware) {
        // Save reference to Hardware map
        hardwareMap = hardware;

        // Define and Initialize Motors
        leftFrontMotor = setupMotor("leftFrontMotor", DcMotor.Direction.FORWARD, 0, true,true);
        leftBackMotor = setupMotor("leftBackMotor", DcMotor.Direction.FORWARD, 0, true,true);
        rightFrontMotor = setupMotor("rightFrontMotor", DcMotor.Direction.REVERSE, 0, true,true);
        rightBackMotor = setupMotor("rightBackMotor", DcMotor.Direction.REVERSE, 0, true,true);

        //TODO setup lift and grabber motors
        grabberServo = setupServo("grabberServo", 0.05);
        liftMotorLeft = setupMotor("liftMotorLeft", DcMotor.Direction.FORWARD, 0, false, true);
        liftMotorRight = setupMotor("liftMotorRight", DcMotor.Direction.REVERSE, 0, false, true);
        liftMinSensor = hardwareMap.get(TouchSensor.class, "liftMin");
        liftMaxSensor = hardwareMap.get(TouchSensor.class, "liftMax");

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
    }

    /* Init Motor, set direction, initial power and encoder runmode (if applicable)
     * @return the configured DcMotor or null if the motor is not found
     */
    private DcMotor setupMotor(String name, DcMotorSimple.Direction direction, int initialPower, boolean useEncoder, boolean brakeMode) {
        try {

            DcMotor motor = hardwareMap.get(DcMotor.class, name);
            motor.setDirection(direction);
            motor.setPower(initialPower);

            if (useEncoder) {
                motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }

            if (brakeMode) {
                motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            } else {
                motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            }
            return motor;
        } catch (Exception e) {
            return null;
        }
    }

    /* Init CRServo and set initial power
     * @return the configured CRServo or null if the servo is not found
     */
    private CRServo setupCRServo(String name, double initialPower) {
        try {
            CRServo servo = hardwareMap.get(CRServo.class, name);
            servo.setPower(initialPower);
            return servo;
        } catch (Exception e) {
            return null;
        }
    }

    /* Init Servo and set initial position
     * @return the configured Servo or null if the servo is not found
     */
    private Servo setupServo(String name, double initialPosition) {
        try {
            Servo servo = hardwareMap.get(Servo.class, name);
            servo.setPosition(initialPosition);
            return servo;
        } catch (Exception e) {
            return null;
        }
    }

    /* Init WebcamName
     * @return the configured WebcamName or null if the webcam is not found
     */
    private WebcamName setupWebcam(String name) {
        try {
            WebcamName webcamName = hardwareMap.get(WebcamName.class, name);
            return webcamName;
        } catch (Exception e) {
            return null;
        }
    }
}