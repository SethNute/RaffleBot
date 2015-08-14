# RaffleBot
IRC raffle bot that gives away donuts, uses pircbot as base

<strong>To Install and Use</strong>

`git clone`, resulting in the `RaffleBot` dir being created

Download <a href=http://www.brudvik.org/projects/development/pircbot-with-ssl/>this</a> version of pircbot and extract it into your `RaffleBot` dir. If you `ls` your directory should look like `DonutDan.java LICENSE org README.md`

Open up `DonutDan.java` in whatever editor and fill in lines 29 && 30 with the info of the irc channel you'd like the bot to connect to. Save the file.

Compile using `javac DonutDan.java`

Run using `java DonutDan <Number of donuts to be raffled> <Admin user (the irc name of the guy you wanna be able to give donuts)>`

<strong>Available Commands</strong>

`!donut` - signs the user up for the raffle
`!donutsLeft` - outputs how many donuts are yet to be raffled off
`!giveDonut` - admin only - give the donut to a random person currently entered in the raffle
`!endRaffle` - admin only - give out the rest of the donuts
`!getouttahere` - admin only - dismiss the bot from the channel
