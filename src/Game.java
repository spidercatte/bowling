import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Game {

    private static int numberOfPinsPerGame = 10;
    private static final int GAME_FRAME = 10;
    private static Map<Integer, Frame> scoreFrames = new LinkedHashMap();
    private int currentFrame = 0;
    private int remainingPins = 10;

    public Game(int numberOfPins) {
        this.numberOfPinsPerGame = numberOfPins;
    }

    public int score () {
        AtomicReference<Integer> score = new AtomicReference<>(0);
       for (int i=0; i < scoreFrames.size(); i++) {
           Frame frame = scoreFrames.get(i);
           score.set(score.get() + frame.getScore());
        }

        return score.get();
    }

    public void roll (Integer pins) {
        Frame frame = scoreFrames.get(currentFrame);
        if(frame == null) {
            frame = new Frame(currentFrame+1);
            frame.setRoll(pins);
            scoreFrames.put(currentFrame, frame);
            if(scoreFrames.size() > GAME_FRAME) frame.setIsBonusFrame(true);
        } else {
            frame.setRoll(pins);
            Frame prevFrame = scoreFrames.get(currentFrame-1);
            if(prevFrame != null) prevFrame.setNextFrame(frame);
            if(frame.isRollsDone()) {
                currentFrame = currentFrame + 1;
                this.remainingPins = Game.getNumberOfPinsPerGame();
            }
        }
        this.remainingPins = frame.getRemainingPins();
    }

    public boolean isDone() {
        Frame lastFrame = scoreFrames.get(GAME_FRAME - 1);
        if(lastFrame == null) {
            return false;
        } else if(lastFrame.isRollsDone()) {
            if(lastFrame.getBonus_type() == Frame.BONUS_TYPE.STRIKE && scoreFrames.size() < GAME_FRAME + 2) {
                System.out.println("Adding bonus for last set strike");
                return false;
            } else if (lastFrame.getBonus_type() == Frame.BONUS_TYPE.SPARE && scoreFrames.size() < GAME_FRAME + 1) {
                System.out.println("Adding bonus for last set spare");
                return false;
            }
            System.out.println("Bowling is Done");
            return true;
        }

        return false;
    }

    public int getRemainingPins() {
        return this.remainingPins;
    }

    public static int getNumberOfPinsPerGame() {
        return numberOfPinsPerGame;
    }
}
