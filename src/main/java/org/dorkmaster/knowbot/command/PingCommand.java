package org.dorkmaster.knowbot.command;

import org.javacord.api.DiscordApi;
import org.javacord.api.event.message.MessageCreateEvent;

public class PingCommand  implements Command{
    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public void handleMessage(DiscordApi api, String[] messageParts, MessageCreateEvent event) {
        event.getChannel().sendMessage("Pong!");
    }
}
