package org.dorkmaster.knowbot.command;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageSet;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class KnowCommand implements Command {

    @Override
    public String getName() {
        return "kb";
    }

    @Override
    public void handleMessage(DiscordApi api, String[] messageParts, MessageCreateEvent event)  {
        try {
            TextChannel replyChannel = event.getChannel();
            Optional<Server> server = event.getServer();

            List<String> parts = new LinkedList(Arrays.asList(messageParts));
            parts.remove(0);
            parts.add(0, "kb");

            if (server.isPresent()) {
                Map<String, ServerChannel> channels = new HashMap();
                for (ServerChannel channel : server.get().getChannels()) {
                    channels.put(channel.getName(), channel);
                }

                ServerChannel sourceChannel = null;
                do {
                    String name = String.join("-", parts);
                    sourceChannel = channels.get(name);
                    if (parts.size() > 0) {
                        parts.remove(parts.size() -1);
                    }
                } while (null == sourceChannel && parts.size() > 0);

                if (null != sourceChannel && sourceChannel instanceof ServerTextChannel) {
                    if (sourceChannel.getId() != event.getChannel().getId()) {
                        CompletableFuture<MessageSet> future = ((ServerTextChannel) sourceChannel).getMessages(100);
                        MessageSet messages = future.get(10, TimeUnit.SECONDS);
                        if (messages.size() > 0) {
                            for (Message message : messages.descendingSet()) {
                                replyChannel.sendMessage(message.getContent());
                            }
                        }
                    }
                }
            }
        } catch(ExecutionException | InterruptedException | TimeoutException e) {
            // eat it
        }
    }
}
