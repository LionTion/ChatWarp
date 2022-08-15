# ChatWarp
An external Minecraft Plugin for the 20w14infinite Snapshot Server migrating your Twitch Chat

### How to use:

**1.** Download the Release
**2.** Execute run.sh/.bat
**3.** Open settings.properties and fill in the information required. Use a trusted service or website to get the access code.
**4.** Make sure your minecraft server version *20w14infinite* is online and has RCON enabled.
You can enable RCON by opening server.properties and changing the following lines:
`enable-rcon=false` -> `enable-rcon=true`
**and**
`rcon.password=`-> `rcon.password=YourDesiredPassword`
**5.** Execute run.sh/.bat again.
All necessary files will be created.
You can change **bannedWords.list** by adding or removing lines of words.
**6.** Do what you want :)
You can add **currentseed.txt** and **timeout.txt** to OBS Studio or attach them to any other program to display the info or use it for other purposes.

Use **!warp <seed>** in your twitch chat to warp all players in the server to a new dimension.
