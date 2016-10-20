package sample;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import sample.listeners.*;
import sample.players.*;

public class Main extends Application {

    private static final float VOLUME_MIN_RADIUS = 20.0f;
    private static final float VOLUME_MAX_RADIUS = 120.0f;

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final int MARGIN = 100;

    private final DoubleProperty centerX = new SimpleDoubleProperty(0);
    private final DoubleProperty radius = new SimpleDoubleProperty(VOLUME_MIN_RADIUS);

    private com.leapmotion.leap.Controller mLeapController;
    private LeapEventListener mLeapEventListener;
    private ThereminPlayer mThereminPlayer;
    private DrumPlayer mDrumPlayer;
    private ArduinoSerialPortListener mArduinoSerialPortListener;

    @Override
    public void start(Stage primaryStage) throws Exception {
        initializeLeapMotion();
        initializeTheraminPlayer();
        initializeDrumPlayer();
        initializeArduinoSerialPortListener();
        initializeUIAndGetRoot(primaryStage);
    }



    @Override
    public void stop() throws Exception {
        destroyLeapMotion();
        destroyTheraminPlayer();
        destroyDrumPlayer();
        destroyArduinoSerialPortListener();
        super.stop();
    }

    private DoubleProperty centerX() {
        return centerX;
    }

    private DoubleProperty radius() {
        return radius;
    }

    private void initializeLeapMotion() {
        mLeapController = new com.leapmotion.leap.Controller();
        mLeapEventListener = new LeapEventListener(this);
        mLeapController.addListener(mLeapEventListener);

    }

    private void destroyLeapMotion() {
        mLeapController.removeListener(mLeapEventListener);
        mLeapController = null;
        mLeapEventListener = null;
    }

    private void initializeTheraminPlayer() {
        mThereminPlayer = new ThereminPlayer();
        mThereminPlayer.initialize();
    }


    private void destroyTheraminPlayer() {
        mThereminPlayer.destroy();
        mThereminPlayer = null;
    }


    private void initializeDrumPlayer() {
        mDrumPlayer = new DrumPlayer();
        mDrumPlayer.initialize();
    }

    private void destroyDrumPlayer() {
        mDrumPlayer.destroy();
        mDrumPlayer = null;
    }


    private void initializeArduinoSerialPortListener() {
        mArduinoSerialPortListener = new ArduinoSerialPortListener(this);
        while (!mArduinoSerialPortListener.initialize()) {
            System.out.println("Please plug in your arduino device...");
        }
    }


    private void destroyArduinoSerialPortListener() {
        mArduinoSerialPortListener.destroy();
        mArduinoSerialPortListener = null;
    }

    private void initializeUIAndGetRoot(Stage primaryStage) {
        Circle volumeCircle = new Circle(VOLUME_MIN_RADIUS);
        volumeCircle.setFill(Color.GREEN);
        volumeCircle.radiusProperty().bind(radius);
        StackPane.setAlignment(volumeCircle, Pos.CENTER_LEFT);

        Circle pitchCircle = new Circle(VOLUME_MIN_RADIUS);
        pitchCircle.setFill(Color.BLUE);
        pitchCircle.translateXProperty().bind(centerX);
        StackPane.setAlignment(pitchCircle, Pos.CENTER);

        StackPane root = new StackPane(pitchCircle, volumeCircle);
        root.setPadding(new Insets(MARGIN, MARGIN, MARGIN, 2 * MARGIN));
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void play() {
        mThereminPlayer.play();
    }

    private void pause() {
        mThereminPlayer.pause();
    }

    public void updatePitchBy(float changeInPitch) {
        int halfScreenWidth = (WIDTH - (2 * MARGIN)) / 2;
        double centerX = halfScreenWidth * changeInPitch;
        centerX().set(centerX);
        mThereminPlayer.updateFrequencyBy(changeInPitch);
    }

    public void updateVolume(float newVolume) {
        radius().set(VOLUME_MIN_RADIUS + (newVolume * (VOLUME_MAX_RADIUS - VOLUME_MIN_RADIUS)));
        mThereminPlayer.updateAmplitude(newVolume);
    }

    public void notifyConnect() {}

    public void notifyDisconnect() {
        pause();
    }

    public void notifyNoHandsAvailable() {
        pause();
    }

    public void notifyHandsAvailable() {
        play();
    }

    public void notifySerialInstructionReceived(String instruction) {
        if (instruction.contains("C") || instruction.contains("c")) {
            mDrumPlayer.playCymbal();
        } else if (instruction.contains("D") || instruction.contains("d")) {
            mDrumPlayer.playSnare();
        }
    }



    public static void main(String[] args) {
        launch(args);
    }
}
