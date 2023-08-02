package org.prgrms.kdtspringdemo.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.kdtspringdemo.voucher.FixedAmountVoucher;
import org.prgrms.kdtspringdemo.voucher.MemoryVoucherRepository;
import org.prgrms.kdtspringdemo.voucher.VoucherService;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {
    class OrderRepositoryStub implements OrderRepositry {
        @Override
        public Order insert(Order order) {
            return null;
        }
    }
    @Test
    @DisplayName("오더가 생성되어야한다. (stub)")
    void createOrder(){
//      Given
        var voucherRepository = new MemoryVoucherRepository();
        var fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 100);
        voucherRepository.insert(fixedAmountVoucher);
        var sut = new OrderService(new VoucherService(voucherRepository), new MemoryOrderRepositry());

//      When
        var order = sut.createOrder(UUID.randomUUID(), List.of(new OrderItem(UUID.randomUUID(), 200, 1)), fixedAmountVoucher.getVoucherId());

//      Then
        assertThat(order.totalAmount(), is(100L));
        assertThat(order.getVoucher().isEmpty(), is(false));
        assertThat(order.getVoucher().get().getVoucherId(), is(fixedAmountVoucher.getVoucherId()));
        assertThat(order.getOrderStatus(), is(OrderStatus.ACCEPTED));

    }

    @Test
    @DisplayName("오더가 생성되어야한다. (mock)")
    void createOrderByMock(){
//      Given
        mock();
        var voucherServiceMock = mock(VoucherService.class);
        var oderderRepositoryMock = mock(OrderRepositry.class);
        var fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 100);
        when(voucherServiceMock.getVoucher(fixedAmountVoucher.getVoucherId())).thenReturn(fixedAmountVoucher);
        var sut = new OrderService(voucherServiceMock, oderderRepositoryMock);

//      When
        var order = sut.createOrder(
            UUID.randomUUID(),
            List.of(new OrderItem(UUID.randomUUID(), 200, 1)),
            fixedAmountVoucher.getVoucherId());

//      Then
        assertThat(order.totalAmount(), is(100L));
        assertThat(order.getVoucher().isEmpty(), is(false));
        var inOrder = inOrder(voucherServiceMock, oderderRepositoryMock);

        // 작동 순서는 이렇다. 각각의 mock들이 어떤순서로 진행되는지 테스트가능
        inOrder.verify(voucherServiceMock).getVoucher(fixedAmountVoucher.getVoucherId());
        inOrder.verify(oderderRepositoryMock).insert(order);
        inOrder.verify(voucherServiceMock).useVoucher(fixedAmountVoucher);


    }
}
