package org.dorkmaster.knowbot.command;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.dorkmaster.knowbot.util.ChannelMatcher;
import org.dorkmaster.knowbot.util.Metrics;
import org.dorkmaster.knowbot.util.Pair;
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

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class KnowCommand implements Command {
    private  Logger logger = LoggerFactory.getLogger(this.getClass());
    protected static final String CACHE_NAME = "kbChannelCache";
    private static Cache CACHE = CacheManager.getInstance().getCache(CACHE_NAME);

    static {
        CACHE = CacheManager.getInstance().getCache(CACHE_NAME);
        if (null == CACHE) {
            CacheManager.getInstance().addCache(CACHE_NAME);
            CACHE = CacheManager.getInstance().getCache(CACHE_NAME);
        }
    }

    @Override
    public String getName() {
        return "kb";
    }
    public static final String SERVER_CHANNEL_PREFIX = "kb";

    protected Collection<ServerChannel> loadChannels(DiscordApi api, Server server) {
        return server.getChannels().stream().filter(a -> a.getName().startsWith(SERVER_CHANNEL_PREFIX)).collect(Collectors.toList());
    }

    protected boolean parrot(DiscordApi api, MessageCreateEvent event, String channel) throws ExecutionException, InterruptedException, TimeoutException {
        List<ServerChannel> sources = event.getServer().get().getChannelsByName(channel);
        if (sources.size() > 0) {
            ServerChannel source = sources.get(0);
            if (source.getId() != event.getChannel().getId()) {
                CompletableFuture<MessageSet> future = ((ServerTextChannel) source).getMessages(100);
                MessageSet messages = future.get(10, TimeUnit.SECONDS);
                if (messages.size() > 0) {
                    for (Message message : messages.descendingSet()) {
                        event.getChannel().sendMessage(message.getContent());
                    }
                    return true;
                }
            }
        }
        return false;
    }

    protected String cleanup(String input) {
        return input.replaceAll("!kb ", "")
                .replaceAll("[^\\w\\s]", "")
                .replaceAll("\\s\\s", " ");
    }

    @Override
    public void handleMessage(DiscordApi api, String[] messageParts, MessageCreateEvent event)  {
        Metrics.mark("Requests");
        try {
            TextChannel replyChannel = event.getChannel();
            Optional<Server> server = event.getServer();

            if (server.isPresent()) {
                Metrics.mark("Server-" + server.get().getId());

                ChannelMatcher cm = null;
                Element e = CACHE.get(server.get().getId());
                if (null == e || e.isExpired()) {
                    cm = new ChannelMatcher(server.get().getChannels().stream()
                            .map(a -> a.getName())
                            .filter(a-> a.startsWith("kb"))
                            .collect(Collectors.toList())
                    );
                    CACHE.put(new Element(server.get().getId(), cm));
                } else {
                    cm = (ChannelMatcher) e.getObjectValue();
                }

                String cleaned = cleanup(event.getMessageContent());
                Collection<Pair<Double, String>> matches = cm.matches(cleaned);
                logger.debug("Term '{}' matches '{}'", cleaned, matches);
                if (0 != matches.size()) {
                    Pair<Double, String> match = matches.iterator().next();
                    logger.info("Term '{}' Match '{}' Weight {}", event.getMessageContent(), match.getSecond(), match.getFirst());
                    if (!parrot(api, event, match.getSecond())) {
                        logger.debug("Channel not found, falling back to default");
                        parrot(api, event, "kb");
                    }
                } else {
                    parrot(api, event, "kb");
                }
            }
        } catch(ExecutionException | InterruptedException | TimeoutException e) {
            // eat it
        }
    }
}
