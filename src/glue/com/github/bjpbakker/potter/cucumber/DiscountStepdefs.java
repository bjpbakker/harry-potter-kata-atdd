package com.github.bjpbakker.potter.cucumber;

import com.github.bjpbakker.potter.Book;
import com.github.bjpbakker.potter.ShoppingCart;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.stream.IntStream.range;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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
		Book book = bookByTitle(title).orElseThrow(() -> new UnknownTitle(title));
		range(0, numberOfCopies).forEach(n -> this.cart.add(book));
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
