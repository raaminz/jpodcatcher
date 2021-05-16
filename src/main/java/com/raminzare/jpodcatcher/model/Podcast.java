package com.raminzare.jpodcatcher.model;

public record Podcast (String title) {


    public static class PodcastBuilder{
        private String title;

        public void setTitle(String title) {
            this.title = title;
        }

        public Podcast build() {
            return new Podcast(title);
        }
    }
}
