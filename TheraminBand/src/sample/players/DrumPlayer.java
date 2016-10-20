package sample.players;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

/**
 * Class to play snare and cymbal sounds controlled using serial events
 * Created by batman on 19/10/16.
 */
public class DrumPlayer {
    private MediaPlayer mSnarePlayer, mCymbalPlayer;

    public void initialize() {
        Media snareMedia = new Media(new File("assets/snare.mp3").toURI().toString());
        Media cymbalMedia = new Media(new File("assets/cymbal.mp3").toURI().toString());
        mSnarePlayer = new MediaPlayer(snareMedia);
        mCymbalPlayer = new MediaPlayer(cymbalMedia);
    }

    public void destroy() {
        mSnarePlayer.stop();
        mCymbalPlayer.stop();
    }

    public void playSnare() {
        mSnarePlayer.stop();
        mSnarePlayer.play();
    }

    public void playCymbal() {
        mCymbalPlayer.stop();
        mCymbalPlayer.play();
    }
}