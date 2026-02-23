package com.example.demo;

public class DummyVideo {
    private int videoId;
    private String streamerName;
    private int viewCount;

    public DummyVideo(int videoId, String streamerName, int viewCount) {
        this.videoId = videoId;
        this.streamerName = streamerName;
        this.viewCount = viewCount;
    }

    public int getVideoId() {
        return videoId;
    }

    public String getStreamerName() {
        return streamerName;
    }

    public int getViewCount() {
        return viewCount;
    }
}
