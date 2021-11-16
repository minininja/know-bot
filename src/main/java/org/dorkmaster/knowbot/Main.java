package org.dorkmaster.knowbot;

import org.dorkmaster.knowbot.command.Command;
import org.dorkmaster.knowbot.command.KnowCommand;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static Map<String, Command> commands = new HashMap<>();
    private static Command[] handlers = new Command[] { new KnowCommand() };

    static {
        for (Command command : handlers) {
            commands.put(command.getName(), command);
        }
    }

    public static void main(String[] args) {
        String token = System.getenv("DG_TOKEN");
        if (null == token || token.length() == 0) {
            System.out.println("Invalid token - fix it: '" + token + "'");
            System.exit(1);
        }
        System.out.println("Using '" + token + "'");
        final String prefix = null == System.getenv("DG_PREFIX") ? "!" : System.getenv("DG_PREFIX");

        DiscordApi api = new DiscordApiBuilder().setToken(System.getenv("DG_TOKEN")).login().join();
        api.addMessageCreateListener(event -> {
            String message= event.getMessageContent();
            if (event.getMessageContent().startsWith(prefix)) {
                String[] parts = message.split("\\s");
                Command command = commands.get(parts[0].substring(1));
                if (null != command) {
                    command.handleMessage(api, parts, event);
                }
            }
            if (event.getMessageContent().equalsIgnoreCase("!ping")) {
                event.getChannel().sendMessage("Pong!");
            }
        });
    }
}
