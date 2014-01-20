package com.github.bjpbakker.potter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class Series {
	private final Map<String, List<Book>> byTitle;

	public Series(List<Book> books) {
		byTitle = books.stream().collect(Collectors.groupingBy(book -> book.title));
	}

	public Set<Book> take() {
		Set<Book> series = new HashSet<>();
		byTitle.forEach((title, books) -> series.add(books.get(0)));
		return series;
	}

	public boolean nonEmpty() {
		return !byTitle.isEmpty();
	}

	public Set<Book> takeLimited(int limit) {
		return take().stream().sorted(
				(lhs, rhs) -> Integer.compare(numberOfBooks(lhs.title), numberOfBooks(rhs.title)) * -1
		).limit(limit).collect(toSet());
	}

	public Series drop(Set<Book> series) {
		List<String> titles = pluckTitles(series);
		List<Book> retain = new LinkedList<>();
		byTitle.forEach((title, books) ->
				retain.addAll(titles.contains(title) ? drop(books) : books));
		return new Series(retain);
	}

	private List<String> pluckTitles(Set<Book> series) {
		return series.stream().map(book -> book.title).collect(toList());
	}

	private int numberOfBooks(String title) {
		return byTitle.get(title).size();
	}

	private List<Book> drop(List<Book> list) {
		return list.stream().skip(1).collect(toList());
	}
}