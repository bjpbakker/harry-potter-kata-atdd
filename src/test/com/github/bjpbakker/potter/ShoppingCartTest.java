package com.github.bjpbakker.potter;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ShoppingCartTest {
	@Test
	public void it_calculates_total_for_empty_cart() throws Exception {
		List<Book> noBooks = emptyList();
		ShoppingCart cart = new ShoppingCart(noBooks);
		assertThat(cart.calculateTotal(), is(0L));
	}

	@Test
	public void it_calculates_total_for_one_book() throws Exception {
		List<Book> oneBook = asList(new Book("I", 800L));
		ShoppingCart cart = new ShoppingCart(oneBook);
		assertThat(cart.calculateTotal(), is(800L));
	}

	@Test
	public void it_calculates_total_for_two_books() throws Exception {
		List<Book> books = asList(new Book("I", 800L), new Book("II", 800L));
		ShoppingCart cart = new ShoppingCart(books);
		assertThat(cart.calculateTotal(), is(1520L));
	}

	@Test
	public void it_calculates_total_for_three_books() throws Exception {
		List<Book> books = asList(new Book("I", 800L), new Book("II", 800L),
				new Book("III", 800L));
		ShoppingCart cart = new ShoppingCart(books);
		assertThat(cart.calculateTotal(), is(2160L));
	}

	@Test
	public void it_calculates_total_for_four_books() throws Exception {
		List<Book> books = asList(new Book("I", 800L), new Book("II", 800L),
				new Book("III", 800L), new Book("IV", 800L));
		ShoppingCart cart = new ShoppingCart(books);
		assertThat(cart.calculateTotal(), is(2560L));
	}

	@Test
	public void it_calculates_total_for_five_books() throws Exception {
		List<Book> books = asList(new Book("I", 800L), new Book("II", 800L),
				new Book("III", 800L), new Book("IV", 800L), new Book("V", 800L));
		ShoppingCart cart = new ShoppingCart(books);
		assertThat(cart.calculateTotal(), is(3000L));
	}

	@Test
	public void it_calculates_total_for_two_plus_one_books() throws Exception {
		List<Book> books = asList(new Book("I", 800L), new Book("II", 800L),
				new Book("I", 800L));
		ShoppingCart cart = new ShoppingCart(books);
		assertThat(cart.calculateTotal(), is(2320L));
	}

	@Test
	public void it_calculates_optimal_discount_for_5_plus_3_books() throws Exception {
		List<Book> books = asList(
				new Book("I", 800L), new Book("II", 800L), new Book("III", 800L), new Book("IV", 800L), new Book("V", 800L),
				new Book("I", 800L), new Book("II", 800L), new Book("IV", 800L));
		ShoppingCart cart = new ShoppingCart(books);
		assertThat(cart.calculateTotal(), is(5120L));
	}

	@Test
	public void it_calculates_optimal_discount_for_multiple_sets_of_5_plus_3_books() throws Exception {
		List<Book> books = asList(
				new Book("I", 800L), new Book("II", 800L), new Book("III", 800L), new Book("IV", 800L), new Book("V", 800L),
				new Book("I", 800L), new Book("II", 800L), new Book("III", 800L), new Book("IV", 800L), new Book("V", 800L),
				new Book("I", 800L), new Book("II", 800L), new Book("IV", 800L),
				new Book("I", 800L), new Book("II", 800L), new Book("IV", 800L));
		ShoppingCart cart = new ShoppingCart(books);
		assertThat(cart.calculateTotal(), is(10240L));
	}
}
