package org.dorkmaster.knowbot.util;


import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

public class ChannelMatcherTests {

    @Test
    public void testMatching() {
        List<String> channels = List.of("one-two-three", "three-two-one");
        ChannelMatcher cm = new ChannelMatcher(channels);

        Collection<Pair<Double, String>> matches = cm.matches("two");
        Assert.assertEquals(channels.size(), matches.size());
    }

    @Test
    public void testFirstMatch() {
        List<String> channels = List.of("one-two-three", "three-two-one");
        ChannelMatcher cm = new ChannelMatcher(channels);

        Collection<Pair<Double, String>> matches = cm.matches("one");
        Assert.assertEquals(channels.size(), matches.size());
        Assert.assertEquals(channels.get(0), matches.iterator().next().getSecond());
    }

    @Test
    public void testEqualMatchSecondaryOrdering() {
        List<String> channels = List.of("one-two-three-four", "one-two-three");
        ChannelMatcher cm = new ChannelMatcher(channels);

        Collection<Pair<Double, String>> matches = cm.matches("one");
        Assert.assertEquals(channels.size(), matches.size());
        Assert.assertEquals(channels.get(1), matches.iterator().next().getSecond());
    }

    @Test
    public void testNonMatches() {
        List<String> channels = List.of("one-two-three-four", "one-two-three");
        ChannelMatcher cm = new ChannelMatcher(channels);

        Collection<Pair<Double, String>> matches = cm.matches("four");
        Assert.assertEquals(channels.size() - 1, matches.size());
        Assert.assertEquals(channels.get(0), matches.iterator().next().getSecond());
    }

    @Test
    public void testWeighting() {
        List<String> channels = List.of("one-two-three-four", "one-two-three");
        ChannelMatcher cm = new ChannelMatcher(channels);

        Collection<Pair<Double, String>> matches = cm.matches("one four");
        Assert.assertEquals(channels.size(), matches.size());
        Assert.assertEquals(channels.get(0), matches.iterator().next().getSecond());
    }

    @Test
    public void testWeightingOutOfOrder() {
        List<String> channels = List.of("one-two-three-four", "one-two-three");
        ChannelMatcher cm = new ChannelMatcher(channels);

        Collection<Pair<Double, String>> matches = cm.matches("four one");
        Assert.assertEquals(channels.size(), matches.size());
        Assert.assertEquals(channels.get(0), matches.iterator().next().getSecond());
    }

    @Test
    public void testRegex() {
        String input = "!kb test 43143 $#!$#@$ fdsafsd";
        System.out.println(input.replaceAll("!kb ", "").replaceAll("[^\\w\\s]", ""));
    }

}
