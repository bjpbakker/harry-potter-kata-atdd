Feature: Harry Potter book sale

  Scenario Outline: buy discounted Harry Potter books

    When I buy <I> copies of "Harry Potter I"
    And I buy <II> copies of "Harry Potter II"
    And I buy <III> copies of "Harry Potter III"
    And I buy <IV> copies of "Harry Potter IV"
    And I buy <V> copies of "Harry Potter V"
    Then I must pay $<Total>

  Examples:
    | I | II | III | IV | V |  Total |
    | 1 | 0  | 0   | 0  | 0 |   8.00 |
    | 1 | 1  | 0   | 0  | 0 |  15.20 |
    | 1 | 1  | 1   | 0  | 0 |  21.60 |
    | 1 | 1  | 1   | 1  | 0 |  25.60 |
    | 1 | 1  | 1   | 1  | 1 |  30.00 |
    | 2 | 0  | 0   | 0  | 0 |  16.00 |
    | 2 | 1  | 0   | 0  | 0 |  23.20 |
    | 2 | 2  | 2   | 1  | 1 |  51.20 |
    | 4 | 4  | 4   | 2  | 2 | 102.40 |
