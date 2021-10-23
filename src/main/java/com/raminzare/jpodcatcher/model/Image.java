package com.raminzare.jpodcatcher.model;

public record Image(String url, String title, String link) {

    public static class ImageBuilder {
        private String url;
        private String title;
        private String link;

        public ImageBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public ImageBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public ImageBuilder setLink(String link) {
            this.link = link;
            return this;
        }

        public Image build() {
            return new Image(url, title, link);
        }
    }
}
