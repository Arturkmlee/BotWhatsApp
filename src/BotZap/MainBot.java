package BotZap;

public class MainBot {
    public static void main(String[] args) {
        Thread bot = new Thread(new Bot());
        bot.start();
    }
}
