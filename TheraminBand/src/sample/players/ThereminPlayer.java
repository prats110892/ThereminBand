package sample.players;

import ddf.minim.AudioOutput;
import ddf.minim.Minim;
import ddf.minim.ugens.Oscil;
import ddf.minim.ugens.Waves;

/**
 * A Player that creates a sinusoidal wave between the C1 and C8 frequencies and plays them through an audio output
 * Created by batman on 19/10/16.
 */

public class ThereminPlayer {
    private static final float MIN_FREQUENCY = 261.63f;
    private static final float MAX_FREQUENCY = 523.25f;

    private Minim mMinimInstance;
    private AudioOutput mAudioOutput;
    private Oscil mSineWaveOscilator;

    private boolean mIsPlaying = false;

    public void initialize() {
        mMinimInstance = new Minim(this);
        mAudioOutput = mMinimInstance.getLineOut();
        mSineWaveOscilator = new Oscil(MIN_FREQUENCY, 0f, Waves.SINE);
    }

    public void pause() {
        if (mIsPlaying) {
            mSineWaveOscilator.unpatch(mAudioOutput);
            mIsPlaying = false;
        }
    }

    public void play() {
        if (!mIsPlaying) {
            mSineWaveOscilator.patch(mAudioOutput);
            mIsPlaying = true;
        }
    }

    public void destroy() {
        pause();
        mAudioOutput.close();
        mMinimInstance.stop();
    }

    public void updateFrequencyBy(float changeInFrequency) {
        mSineWaveOscilator.setFrequency((changeInFrequency * (MAX_FREQUENCY - MIN_FREQUENCY)) + MIN_FREQUENCY);
    }

    public void updateAmplitude(float newAmplitude) {
        mSineWaveOscilator.setAmplitude(newAmplitude);
    }
}
