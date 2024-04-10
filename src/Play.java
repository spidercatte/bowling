import java.util.Random;

public class Play {

    public static void main(String[] args) {
        Game bowling = new Game(10);
        while(!bowling.isDone()) {
            Random random = new Random();
            int pins= random.ints(0, bowling.getRemainingPins()+1)
                    .findFirst()
                    .getAsInt();
            bowling.roll(pins);
        }
        System.out.println("The score is: " + bowling.score());
    }
}
