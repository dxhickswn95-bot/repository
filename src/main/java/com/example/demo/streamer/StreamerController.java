package com.example.demo.streamer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class StreamerController {
	@Autowired
    private final StreamerService streamerService;

    public StreamerController(StreamerService streamerService) {
        this.streamerService = streamerService;
    }

    
}
