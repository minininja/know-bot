package org.dorkmaster.knowbot.util;


import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ChannelPickerTests {

    @Test
    public void testMatching() {
        String input = "one two three";
        ChannelPicker channelPicker = new ChannelPicker("kb", List.of(
                "kb-one-two-three", "kb-two-three-four", "kb-three-four-five", "one-two-three"
        ));

        System.out.println("Sending 'one two three'");
        String channel = channelPicker.pick("one two three");
        System.out.println("got back " + channel);

        Assert.assertEquals("kb-" + input.replaceAll("\\s", "-"), channel);
    }
}
