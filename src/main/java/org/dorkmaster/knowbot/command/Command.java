package org.dorkmaster.knowbot.command;

import org.javacord.api.DiscordApi;
import org.javacord.api.event.message.MessageCreateEvent;

public interface Command {
    String getName();
    void handleMessage(DiscordApi api, String[] messageParts, MessageCreateEvent event);
}

