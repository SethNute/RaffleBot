import org.jibble.pircbot.*;
import java.util.ArrayList;
import java.util.Random;

public class DonutDan extends PircBot {

  private ArrayList<String> allEntrants = null;
  private ArrayList<String> entrantsWhoHaveNotWon = null;
  private boolean isOver = false;
  private int numPrizes;
  private String channel;
  private String admin;

  public DonutDan(int numPrizes, String admin) {
    this.setName("DonutDan");
    this.numPrizes = numPrizes;
    this.admin = admin;
    this.entrantsWhoHaveNotWon = new ArrayList<>();
    this.allEntrants = new ArrayList<>();
  }

  public static void main(String[] args) throws Exception {
    if (args.length != 2) {
      System.out.println("Needs number of prizes and admin user name");
      return;
    }

    DonutDan bot = new DonutDan(Integer.parseInt(args[0]), args[1]);
    bot.connect("myirc", myport, new TrustingSSLSocketFactory());
    bot.joinChannel("mychannel");
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
    sendMessage(this.admin, "Give a single donut with command \"!giveDonut\" or end the raffle and give out the rest with command \"!endRaffle\"");
  }

  public void onMessage(String channel, String sender,
                       String login, String hostname, String message) {
    String word = this.getFirstWord(message);
    if (word.equalsIgnoreCase("!donut")) {
      this.signUp(sender);
    }
    else if (word.equalsIgnoreCase("!donutsLeft")) {
      sendMessage(this.channel, "There are " + this.numPrizes + " donut(s) left");
    }
    else if (word.equalsIgnoreCase("!giveDonut")) {
      if (sender.equals(this.admin)) {
        this.pickWinner();
      } else {
        this.sendAntiCheatMessage(sender);
      }
    }
    else if (word.equalsIgnoreCase("!endRaffle")) {
      if (sender.equals(this.admin)) {
        this.pickWinners();
      } else {
        this.sendAntiCheatMessage(sender);
      }
    }
    else if (word.equalsIgnoreCase("!getouttahere") && sender.equals(this.admin)) {
      this.disconnect();
    }
  }

  private String getFirstWord(String text) {
    if (text.indexOf(' ') > -1) {
      return text.substring(0, text.indexOf(' '));
    } else {
      return text;
    }
  }

  private void signUp(String sender) {
    if (this.isOver || this.allEntrants.contains(sender)) {
      sendAntiCheatMessage(sender);
    } else {
      this.allEntrants.add(sender);
      this.entrantsWhoHaveNotWon.add(sender);
      sendMessage(this.channel, sender + ": You have entered the donut raffle!");
    }
  }

  private void pickWinner() {
    int size = this.entrantsWhoHaveNotWon.size();
    if (size == 0) {
      this.sendMessage(this.channel, "Nobody is currently entered!");
      return;
    }

    String winner = this.giveDonutToRandomEntrant();
    this.sendMessage(this.channel, winner + ": !!! You get a donut " + winner + " !!!");

    if (this.numPrizes == 0) {
      this.isOver = true;
      this.sendMessage(this.channel, "That was the last donut y'all, thanks for playing");
      this.sendRaffleOverMessage();
    }
  }

  private void pickWinners() {
    this.isOver = true;

    int size = this.entrantsWhoHaveNotWon.size();
    String winner;
    String winners = "";

    if (size <= this.numPrizes) {
      for (int i = 0; i < size; i++) {
        winner = this.entrantsWhoHaveNotWon.get(i);
        this.sendMessage(winner, ": !!! You get a donut " + winner + " !!!");
        winners += winner + ", ";
      }
      this.sendMessage(this.channel, "Winners: [ " + winners + "]");
    } else if (size > 0) {
      int totalPrizes = this.numPrizes;
      for (int i = 0; i < totalPrizes; i++) {
        winner = giveDonutToRandomEntrant();
        winners += winner + ", ";
      }
      this.sendMessage(this.channel, "Winners: [ " + winners + "]");
    } else {
      this.sendMessage(this.channel, "Nobody entered the giveaway... :'(");
    }

    this.sendRaffleOverMessage();
  }

  private String giveDonutToRandomEntrant() {
    int size = this.entrantsWhoHaveNotWon.size();
    Random rand = new Random();
    int winnerPos = rand.nextInt(size);

    String winner = this.entrantsWhoHaveNotWon.get(winnerPos);
    this.sendMessage(winner, ": !!! You get a donut " + winner + " !!!");
    entrantsWhoHaveNotWon.remove(winnerPos);
    this.numPrizes--;

    return winner;
  }

  private void sendRaffleOverMessage() {
    String time = new java.util.Date().toString();
    sendMessage(this.channel, "Today's donut raffle ended at " + time);
  }

  private void sendAntiCheatMessage(String sender) {
    sendMessage(this.channel, sender + ": You ain't slick 8-)");
  }
}
