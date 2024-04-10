public class Frame {

    private Integer frameId;
    private int roll1;
    private int roll2;
    private ROLL_STATUS rollStatus = ROLL_STATUS.NONE;
    private BONUS_TYPE bonus_type = BONUS_TYPE.NONE;
    private Frame nextFrame;
    private boolean isBonusFrame;

    public enum BONUS_TYPE {
        SPARE,
        STRIKE,
        NONE
    }

    public enum ROLL_STATUS {
        NONE,
        ROLL1_DONE,
        ROLL2_DONE,
    }

    public Frame(Integer frameId) {
        this.frameId = frameId;
    }

    public int getScore() {
        return this.roll1 + this.roll2 + this.getBonus();
    }

    public void setRoll1(int pins) {
        System.out.println("Adding "+ pins +" pins as roll 1 for frame " + frameId);
        this.roll1 = pins;
        this.rollStatus = ROLL_STATUS.ROLL1_DONE;
        if(pins == Game.getNumberOfPinsPerGame()) {
            System.out.println("It's a strike");
            this.bonus_type = BONUS_TYPE.STRIKE;
            this.rollStatus = ROLL_STATUS.ROLL2_DONE;
        }
    }

    public void setRoll2(int pins) {
        System.out.println("Adding "+ pins +" pins as roll 2 for frame " + frameId);
        this.roll2 = pins;
        if((roll1 + pins)==Game.getNumberOfPinsPerGame()) {
            System.out.println("It's a spare");
            this.bonus_type  = BONUS_TYPE.SPARE;
        }

        this.rollStatus = ROLL_STATUS.ROLL2_DONE;
    }

    public BONUS_TYPE getBonus_type() {
        return bonus_type;
    }

    public Integer getRoll1() {
        return this.roll1;
    }

    public Integer getRoll2() {
        return this.roll2;
    }

    public boolean isRollsDone() {
        return this.rollStatus == ROLL_STATUS.ROLL2_DONE;
    }

    public void setRoll(int pins) {
        if(isRollsDone()) {
            return;
        } else if(this.rollStatus == ROLL_STATUS.ROLL1_DONE && !this.isBonusFrame) {
            this.setRoll2(pins);
        } else if(this.rollStatus == ROLL_STATUS.NONE) {
            this.setRoll1(pins);
        }
    }

    private Frame getNextFrame() {
        return this.nextFrame;
    }

    public void setNextFrame(Frame nextFrame) {
        this.nextFrame = nextFrame;
    }

    private int getBonus() {
        Frame.BONUS_TYPE bonus_type = this.getBonus_type();
        Frame nextFrame = this.getNextFrame();

        if(nextFrame == null) return 0;

        if(bonus_type == Frame.BONUS_TYPE.STRIKE) {
            return getBonusForStrike(nextFrame);
        } else if(bonus_type == Frame.BONUS_TYPE.SPARE) {
            return nextFrame.getRoll1();
        }

        return 0;
    }

    private int getBonusForStrike(Frame nextFrame) {
        if (nextFrame.getBonus_type() != Frame.BONUS_TYPE.STRIKE) {
            return nextFrame.getRoll1() + nextFrame.getRoll2();
        } else {
            Frame nextnextFrame = nextFrame.getNextFrame();
            return nextFrame.getRoll1() + nextnextFrame.getRoll1();
        }
    }

    public int getRemainingPins() {
        return Game.getNumberOfPinsPerGame() - this.roll1 - this.roll2;
    }

    public void setIsBonusFrame(boolean isBonusFrame){
        this.isBonusFrame = isBonusFrame;
    }
}
