package org.prgrms.kdtspringdemo;

import java.util.Optional;
import java.util.UUID;

public class OrderContext {
    //개별 클래스에 대한 책임을 가지고있음
    public VoucherRepository voucherRepository(){
        return new VoucherRepository() {
            @Override
            public Optional<Voucher> findById(UUID voucherId) {
                return Optional.empty();
            }
        };
    }
    public OrderRepositry orderRepositry(){
        return new OrderRepositry() {
            @Override
            public void insert(Order order) {

            }
        };
    }
    public VoucherService voucherService(){
        return new VoucherService(voucherRepository());
    }
    public OrderService orderService(){
        return new OrderService(voucherService(),orderRepositry());
    }
    // OrderService가 어떤 voucerService와 어떤 orderRepository를 쓰는지
    // 관계에 대한 책임도 가지고있다.
}
/*
 OrderContext가 여러 객체들간의 관계를 맺게 해줌.
 객체가 자신이 사용할 객체를 직접 생성하지 않게 됨.

 OrderService가 사용할 객체들을 OrderContext가 생성하게 되었음.
 이것이 IoC가 이루어 졌다고 보고, 생성과 관계에 대한 책임, 권한이 있는것을
 IoC컨테이너라고 부름. 여기서는 OrderContext가 그럼.

 프레임워크나, Context, 콘테이너들에게 객체 생성이나 관계 대한 권한을 위임해버리면
 단단한 결합도가 생성되지 않음.


 */
