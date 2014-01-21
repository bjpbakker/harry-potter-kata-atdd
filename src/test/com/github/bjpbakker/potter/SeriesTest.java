package com.github.bjpbakker.potter;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SeriesTest {
	@Test
	public void take_gets_empty_series_for_no_books() throws Exception {
		List<Book> books = emptyList();
		Set<Book> taken = new Series(books).take();
		assertThat(taken, sorted(is(books)));
	}

	@Test
	public void take_gets_all_books_for_books_all_are_distinct() throws Exception {
		List<Book> books = asList(new Book("I", 0L), new Book("II", 0L));
		Set<Book> taken = new Series(books).take();
		assertThat(taken, sorted(is(books)));
	}

	@Test
	public void take_gets_one_of_each_title() throws Exception {
		List<Book> books = asList(new Book("I", 0L), new Book("I", 0L), new Book("II", 0L));
		Set<Book> taken = new Series(books).take();
		assertThat(taken, sorted(is(asList(new Book("I", 0L), new Book("II", 0L)))));
	}

	@Test
	public void takeLimit_gets_books_limited_by_size() throws Exception {
		List<Book> books = asList(new Book("I", 0L), new Book("I", 0L), new Book("II", 0L));
		Set<Book> limitedTaken = new Series(books).takeLimited(1);
		assertThat(limitedTaken, is(asSet(new Book("I", 0L))));
	}

	@Test
	public void drop_binds_reduced_book_list_to_series() throws Exception {
		List<Book> books = asList(new Book("I", 0L), new Book("II", 0L));
		Series series = new Series(books).drop(asSet(new Book("I", 0L)));
		Set<Book> taken = series.take();
		assertThat(taken, sorted(is(asList(new Book("II", 0L)))));
	}

	@Test
	public void drop_binds_rest_series() throws Exception {
		List<Book> books = asList(new Book("I", 0L), new Book("II", 0L), new Book("I", 0L));
		Series series = new Series(books);
		series = series.drop(series.take());
		assertThat(series.take(), is(asSet(new Book("I", 0L))));
	}

	@Test
	public void series_of_empty_book_list_should_be_empty() throws Exception {
		Series emptySeries = new Series(emptyList());
		assertThat("#isEmpty should be true", emptySeries.isEmpty());
	}

	@Test
	public void series_with_books_should_be_non_empty() throws Exception {
		Series series = new Series(asList(new Book("I", 0L)));
		assertThat("#isEmpty should be false", !series.isEmpty());
	}

	private Set<Book> asSet(Book... books) {
		return new HashSet<>(asList(books));
	}

	private Matcher<Collection<Book>> sorted(Matcher<List<Book>> matches) {
		return new FeatureMatcher<Collection<Book>, List<Book>>(matches, "sorted", "sorted") {
			@Override
			protected List<Book> featureValueOf(Collection<Book> ts) {
				List<Book> sorted = new ArrayList<>(ts);
				Collections.sort(sorted, (lhs, rhs) -> lhs.title.compareTo(rhs.title));
				return sorted;
			}
		};
	}
}
