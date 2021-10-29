package com.raminzare.jpodcatcher.model.itunes;

import java.util.ArrayList;
import java.util.List;

public record ItunesChannelData(String author, String image, String explicit, List<String> categories,
								String complete, String type, Integer limit, String countryOfOrigin) {
	public static class ItemChannelDataBuilder {
		private final List<String> categories = new ArrayList<>();
		private String author;
		private String image;
		private String explicit;
		private String complete;
		private String type;
		private Integer limit;
		private String countryOfOrigin;

		public void setAuthor(String author) {
			this.author = author;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public void setExplicit(String explicit) {
			this.explicit = explicit;
		}

		public void addCategory(String category) {
			this.categories.add(category);
		}

		public void setComplete(String complete) {
			this.complete = complete;
		}

		public void setType(String type) {
			this.type = type;
		}

		public void setLimit(Integer limit) {
			this.limit = limit;
		}

		public void setCountryOfOrigin(String countryOfOrigin) {
			this.countryOfOrigin = countryOfOrigin;
		}

		public ItunesChannelData build() {
			return new ItunesChannelData(author, image, explicit, categories, complete, type, limit, countryOfOrigin);
		}
	}
}
