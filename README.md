# RaffleBot
IRC raffle bot that gives away donuts, uses pircbot as base

To Use:
Run <code>ja -classpath pircbot.java:. DonutDan (numberOfPrizes) (passwordToEndRaffle)</code>

Compile <code>jac -class pircbot.java:. DonutDan.java</code>

Once the bot has joined a channel use the command <code>donutdan signup</code> to enter the raffle. Anyone who has the password
can end the raffle via the command <code>donutdan endraffle password</code>
