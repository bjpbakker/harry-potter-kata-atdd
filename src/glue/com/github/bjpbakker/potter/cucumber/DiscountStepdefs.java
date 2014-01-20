package com.github.bjpbakker.potter.cucumber;

import java.util.List;
import java.util.LinkedList;
import java.util.Optional;

import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;

import com.github.bjpbakker.potter.Book;
import com.github.bjpbakker.potter.ShoppingCart;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class DiscountStepdefs {
	private static final List<Book> series = asList(
			new Book("Harry Potter I", 800L),
			new Book("Harry Potter II", 800L),
			new Book("Harry Potter III", 800L),
			new Book("Harry Potter IV", 800L),
			new Book("Harry Potter V", 800L)
	);

	private List<Book> cart = new LinkedList<>();

	@When("^I buy (\\d+) cop(?:y|ies) of \"([^\"]*)\"$")
	public void I_buy_copy_of(int numberOfCopies, String title) throws Throwable {
		for (int i = 0; i < numberOfCopies; i++) {
			Book book = bookByTitle(title).orElseThrow(() -> new UnknownTitle(title));
			this.cart.add(book);
		}
	}

	@Then("^I must pay \\$(\\d+.?\\d*)$")
	public void I_must_pay_$(float amount) throws Throwable {
		Long expected = Float.valueOf(amount * 100).longValue();
		ShoppingCart shoppingCart = new ShoppingCart(this.cart);
		Long total = shoppingCart.calculateTotal();
		assertThat(total, is(expected));
	}

	private Optional<Book> bookByTitle(String title) {
		return series.stream()
				.filter(book -> title.equals(book.title))
				.findFirst();
	}

	private static class UnknownTitle extends RuntimeException {
		public UnknownTitle(String title) {
			super(title);
		}
	}
}
