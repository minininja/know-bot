package org.dorkmaster.knowbot.command;

import org.dorkmaster.knowbot.Main;
import org.dorkmaster.knowbot.util.ChannelPicker;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageSet;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class KnowCommand implements Command {
    private  Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getName() {
        return "kb";
    }
    public static final String SERVER_CHANNEL_PREFIX = "kb";


    protected Collection<ServerChannel> loadChannels(DiscordApi api, Server server) {
        return server.getChannels().stream().filter(a -> a.getName().startsWith(SERVER_CHANNEL_PREFIX)).collect(Collectors.toList());
    }


    @Override
    public void handleMessage(DiscordApi api, String[] messageParts, MessageCreateEvent event)  {
        try {
            TextChannel replyChannel = event.getChannel();
            Optional<Server> server = event.getServer();

            if (server.isPresent()) {
                ChannelPicker picker = new ChannelPicker(
                        "kb",
                        server.get().getChannels().stream().map(a -> a.getName()).collect(Collectors.toList())
                );
                String candidate = picker.pick(event.getMessageContent());
                List<ServerChannel> serverChannel = server.get().getChannelsByName(null == candidate ? "kb" : candidate);
                if (1 == serverChannel.size()) {
                    ServerChannel sourceChannel = serverChannel.get(0);
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
