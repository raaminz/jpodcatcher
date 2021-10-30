package com.raminzare.jpodcatcher.model.itunes;

public record ItunesItemData(String title, String episodeType, String episode, String season, String duration,
                             String explicit, String image, String block) {
    public static class Builder {
        private String title;
        private String episodeType;
        private String episode;
        private String season;
        private String duration;
        private String explicit;
        private String image;
        private String block;

        public void setTitle(String title) {
            this.title = title;
        }

        public void setEpisodeType(String episodeType) {
            this.episodeType = episodeType;
        }

        public void setEpisode(String episode) {
            this.episode = episode;
        }

        public void setSeason(String season) {
            this.season = season;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public void setExplicit(String explicit) {
            this.explicit = explicit;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public void setBlock(String block) {
            this.block = block;
        }

        public ItunesItemData build() {
            return new ItunesItemData(title, episodeType, episode, season, duration, explicit, image, block);
        }
    }
}
