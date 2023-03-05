package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class GridController {
    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private Grid grid;

    @MessageMapping("/mark")
    @SendTo("/topic/grid")
    public Grid mark(MarkMessage markMessage) {
        return grid.mark(markMessage);
    }

    @SubscribeMapping("/grid")
    public Grid sendInitialGrid() {
        return grid;
    }
}
