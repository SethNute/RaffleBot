import org.jibble.pircbot.*;
import java.util.ArrayList;
import java.util.Random;

public class DonutDan extends PircBot {

  private ArrayList<String> rafflers = null;
  private boolean isOver = false;
  private int numPrizes;
  private String channel;
  private String password;

  public DonutDan(int numPrizes, String password) {
    this.setName("DonutDan");
    this.numPrizes = numPrizes;
    this.password = password;
    this.rafflers = new ArrayList<>();
  }

  public static void main(String[] args) throws Exception {
    if (args.length != 2) {
      System.out.println("Needs number of prizes and password");
      return;
    }

    DonutDan bot = new DonutDan(Integer.parseInt(args[0]), args[1]);
    bot.connect("myirc.com", port#, new TrustingSSLSocketFactory());
    bot.joinChannel("#channelname");
    bot.setupChannel();
  }

  public void setupChannel() {
    System.out.println("Waiting to connect");
    while (this.getChannels().length == 0) {}
    System.out.println("Connected");

    this.channel = this.getChannels()[0];

    String time = new java.util.Date().toString();
    sendMessage(this.channel, this.numPrizes  + " donut(s) are being given away today!");
    sendMessage(this.channel, "Enter now with command \"!donut\"");
  }

  public void onMessage(String channel, String sender,
                       String login, String hostname, String message) {
    if (message.equalsIgnoreCase("!donut")) {
      this.signUp(sender);
    } else if (message.equalsIgnoreCase("DonutDan endraffle " + this.password)) {
      this.isOver = true;

      this.pickWinners();
      String time = new java.util.Date().toString();
      sendMessage(this.channel, "Today's donut raffle ended at " + time);
    }
  }

  private void signUp(String sender) {
    if (!this.isOver && !this.rafflers.contains(sender)) {
      this.rafflers.add(sender);
      sendMessage(this.channel, sender + ": You have entered the donut raffle!");
    } else {
      sendMessage(this.channel, sender + ": You ain't slick 8-)");
    }
  }

  private void pickWinners() {
    int size = this.rafflers.size();
    String winner;

    if (size <= this.numPrizes) {
      for (int i = 0; i < size; i++) {
        winner = this.rafflers.get(i);
        this.sendMessage(this.channel, winner + ": !!! You get a donut " + winner + " !!!");
      }
    } else if (size > 0) {
      int curSize = size;
      int winnerPos;
      Random rand = new Random();

      for (int i = 0; i < this.numPrizes; i++) {
        winnerPos = rand.nextInt(curSize);
        winner = this.rafflers.get(winnerPos);

        this.sendMessage(this.channel, winner + ": !!! You get a donut " + winner + " !!!");

        rafflers.remove(winnerPos);
        curSize = rafflers.size();
      }
    } else {
      this.sendMessage(this.channel, "Nobody entered the giveaway... :'(");
    }
  }
}
