package com.raminzare.jpodcatcher.model.itunes;

import java.util.ArrayList;
import java.util.List;

public record ItunesCategory(String category, List<String> subCategories) {
    public static class Builder {
        private String category;
        private final List<String> subCategories = new ArrayList<>();

        public void setCategory(String category) {
            this.category = category;
        }

        public void addSubCategory(String subCategory) {
            subCategories.add(subCategory);
        }

        public ItunesCategory build() {
            return new ItunesCategory(category, subCategories);
        }
    }
}
