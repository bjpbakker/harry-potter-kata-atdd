package com.github.bjpbakker.potter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;

public class ShoppingCart {
	private List<Book> cart;

	public ShoppingCart(List<Book> items) {
		this.cart = items;
	}

	public Long calculateTotal() {
		return total(new Series(cart));
	}

	private Long total(Series series) {
		if (series.isEmpty()) {
			return 0L;
		} else {
			Set<Book> books = takeOptimalDiscount(series);
			Long subtotal = subtotal(books, discount(books.size()));
			return subtotal + total(series.drop(books));
		}
	}

	private Long subtotal(Set<Book> books, float discount) {
		return books.stream()
					.map(book -> book.price)
					.map(price -> applyDiscount(discount, price))
					.reduce(0L, (acc, price) -> acc + price);
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
		if (series.isEmpty()) {
			return emptyList();
		} else {
			Set<Book> taken = series.take();
			return cons(taken, collect(series.drop(taken)));
		}
	}

	private List<Set<Book>> cons(Set<Book> x, List<Set<Book>> coll) {
		List<Set<Book>> list = new ArrayList<>(coll.size() + 1);
		list.add(x);
		list.addAll(coll);
		return list;
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
