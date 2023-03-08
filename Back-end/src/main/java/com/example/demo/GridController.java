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

    @MessageMapping("/restart")
    @SendTo("/topic/grid")
    public Grid restart() {
        System.out.println("RESTARTING");
        ConnectionHandler.userCount = 0;
        ConnectionHandler.user1 = null;
        ConnectionHandler.user2 = null;
        grid = new Grid();
        return grid;
    }

    @SubscribeMapping("/grid")
    public Grid sendInitialGrid() {
        return grid;
    }
}
