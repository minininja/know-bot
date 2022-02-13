package org.dorkmaster.knowbot.util;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ChannelPicker {

    protected static final String SEPARATOR = "-";
    protected static final String PREFIX = "kb";
    Collection<Collection<String>> channels;

    public ChannelPicker(String prefix, Collection<String> channels) {
        this.channels = channels
                .stream()
                .filter(a -> a.startsWith(prefix))
                .map(a -> toSet(a.split(SEPARATOR)))
                .collect(Collectors.toList());
    }

    public ChannelPicker setChannels(Collection<Collection<String>> channels) {
        this.channels = channels;
        return this;
    }

    protected Set<String> toSet(String[] params) {
        Set<String> result = new LinkedHashSet<>();
        for (String str: params) {
            result.add(str);
        }
        return result;
    }

    protected int matches(String[] sequence, Collection<String> channel) {
        int i = 0;
        for (String part : sequence) {
            if (channel.contains(part)) {
                i++;
            }
        }
        return i;
    }

    class Match {
        protected List<Collection<String>> matches = new LinkedList<>();

        public Match addChannel(Collection<String> match) {
            this.matches.add(match);
            return this;
        }

        public int size() {
            return matches.size();
        }

        public Collection<String> firstMatch() {
            return matches.get(0);
        }
    }

    public String pick(String sequence) {
        List<Match> possible = new LinkedList<>();

        // figure out which is the best match
        String[] seq = sequence.split("\\s");
        for (Collection<String> channel : channels) {
            int matches = matches(seq, channel);
            if (matches > 0) {
                Match match = matches <= possible.size() ? possible.get(matches) : null;
                if (null == match) {
                    match = new Match();
                    while (possible.size() < matches ) {
                        possible.add(new Match());
                    }
                    possible.add(match);
                }
                match.addChannel(channel);
            }
        }

        if (possible.size() > 0 ) {
            Match match = possible.get(possible.size() - 1);
            if (1 == match.size()) {
                // the prefix is already in there
                return String.join(SEPARATOR, match.firstMatch());
            }
        }
        return null;
    }
}
