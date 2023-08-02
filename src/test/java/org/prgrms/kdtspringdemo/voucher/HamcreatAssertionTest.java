package org.prgrms.kdtspringdemo.voucher;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
public class HamcreatAssertionTest {
    @Test
    @DisplayName("여러 Hamcest mather 테스트")
    void hamCestTest(){
        assertEquals(2,1+1);
        assertThat(1+1, equalTo(2));
        assertThat(1+1, is(2));
        assertThat(1+1, anyOf(is(1),is(2)));

        assertNotEquals(1, 1+1);
        assertThat(1+1, not(1));
        assertThat(1+1, not(equalTo(1)));
    }

    @Test
    @DisplayName("컬렉션에 대한 Matcher 테스트")
    public void hamcestListMatcherTest() {
        var prices = List.of(1,2,3);
        assertThat(prices, hasSize(3));
        assertThat(prices, everyItem(greaterThan(0)));
        assertThat(prices, containsInAnyOrder(2,3,1));
        assertThat(prices, hasItem(1));
        assertThat(prices, hasItem(greaterThanOrEqualTo(2)));

    }
}
