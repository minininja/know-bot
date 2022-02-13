package org.dorkmaster.knowbot;

import org.dorkmaster.knowbot.command.Command;
import org.dorkmaster.knowbot.command.KnowCommand;
import org.dorkmaster.knowbot.command.PingCommand;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static Map<String, Command> commands = new HashMap<>();
    private static Command[] handlers = new Command[] { new KnowCommand(), new PingCommand()};

    static {
        for (Command command : handlers) {
            commands.put(command.getName(), command);
        }
    }

    public static void main(String[] args) {
        String token = System.getenv("DG_TOKEN");
        if (null == token || token.length() == 0) {
            LOGGER.error("Invalid token - fix it: '" + token + "'");
            System.exit(1);
        } else {
            token = token.trim();
        }
        final String prefix = null == System.getenv("DG_PREFIX") ? "!" : System.getenv("DG_PREFIX");

        DiscordApiBuilder builder = new DiscordApiBuilder().setToken(token);
        CompletableFuture<DiscordApi> future =  builder.login();
        DiscordApi api = future.join();

        api.addMessageCreateListener(event -> {
            String message= event.getMessageContent();
            if (event.getMessageContent().startsWith(prefix)) {
                String[] parts = message.split("\\s");
                Command command = commands.get(parts[0].substring(1));
                if (null != command) {
                    command.handleMessage(api, parts, event);
                }
            }
        });
    }
}
