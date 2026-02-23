package com.example.demo.streamer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StreamerAdminController {

    @Autowired
    private StreamerService streamerService;

    @GetMapping("/admin/streamers/sync-youtube-images")
    @ResponseBody
    public String syncYouTubeImages() {
        int updated = streamerService.syncYouTubeProfileImages();
        return "YouTube profile images synced. updated=" + updated;
    }
}
