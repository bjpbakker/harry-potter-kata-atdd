package com.github.bjpbakker.potter;

public class Book {
	public final String title;
	public final Long price;

	public Book(String title, Long price) {
		this.title = title;
		this.price = price;
	}

	@Override
	public String toString() {
		return String.format("%s => %d", title, price);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Book) {
			Book rhs = (Book) obj;
			return this.title.equals(rhs.title);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return 37 * 7 + this.title.hashCode();
	}
}