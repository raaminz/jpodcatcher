package com.raminzare.jpodcatcher.model;

public record Enclosure(String url, Long length, String type) {

    public static class Builder {
        private String url;
        private Long length;
        private String type;

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setLength(Long length) {
            this.length = length;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Enclosure build() {
            return new Enclosure(url, length, type);
        }
    }
}
