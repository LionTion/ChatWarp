# ChatWarp
An external Minecraft Plugin for the 20w14infinite Snapshot Server migrating your Twitch Chat

### How to use:

**1.** Download the Release<br/>
**2.** Execute run.sh/.bat<br/>
**3.** Open settings.properties and fill in the information required. Use a trusted service or website to get the access code.<br/>
**4.** Make sure your minecraft server version *20w14infinite* is online and has RCON enabled.<br/>
You can enable RCON by opening server.properties and changing the following lines:<br/>
`enable-rcon=false` -> `enable-rcon=true`<br/>
**and**<br/>
`rcon.password=`-> `rcon.password=YourDesiredPassword`<br/>
**5.** Execute run.sh/.bat again.<br/>
All necessary files will be created.<br/>
You can change **bannedWords.list** by adding or removing lines of words.<br/>
**6.** Do what you want :)<br/>
You can add **currentseed.txt** and **timeout.txt** to OBS Studio or attach them to any other program to display the info or use it for other purposes.<br/>
<br/>
Use **!warp <seed>** in your twitch chat to warp all players in the server to a new dimension.<br/>
