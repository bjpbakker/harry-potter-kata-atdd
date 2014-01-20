package com.github.bjpbakker.potter;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ShoppingCart {
	private List<Book> cart;

	public ShoppingCart(List<Book> items) {
		this.cart = items;
	}

	public Long calculateTotal() {
		Long total = 0L;
		Series series = new Series(cart);
		while (series.nonEmpty()) {
			Set<Book> books = takeOptimalDiscount(series);
			float discount = discount(books.size());
			Long subtotal = books.stream()
					.map(book -> book.price)
					.map(price -> applyDiscount(discount, price))
					.reduce(0L, (acc, price) -> acc + price);
			total += subtotal;
			series = series.drop(books);
		}
		return total;
	}

	private Long applyDiscount(float discount, Long price) {
		Float value = discount * price;
		return value.longValue();
	}

	private Set<Book> takeOptimalDiscount(Series series) {
		boolean canOptimizeDiscount = countSeriesOfSize(series, 5) == countSeriesOfSize(series, 3);
		if (canOptimizeDiscount) {
			return series.takeLimited(4);
		} else {
			return series.take();
		}
	}

	private long countSeriesOfSize(Series series, int size) {
		return collect(series).stream().filter(s -> s.size() == size).count();
	}

	private List<Set<Book>> collect(Series series) {
		List<Set<Book>> all = new LinkedList<>();
		while (series.nonEmpty()) {
			Set<Book> taken = series.take();
			all.add(taken);
			series = series.drop(taken);
		}
		return all;
	}

	private float discount(int distinctBooks) {
		switch (distinctBooks) {
			case 1:
				return 1f;
			case 2:
				return 0.95f;
			case 3:
				return 0.9f;
			case 4:
				return 0.8f;
			case 5:
				return 0.75f;
			default:
				return Float.NaN;
		}
	}
}
