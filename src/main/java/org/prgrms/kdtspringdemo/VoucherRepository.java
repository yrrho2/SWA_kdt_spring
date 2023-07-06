package org.prgrms.kdtspringdemo;

import java.util.Optional;
import java.util.UUID;

public interface VoucherRepository {

    Optional<Voucher> findById(UUID voucherId);
        // voucher가 없을수도 있다는걸 Optional로 알려줌
    Voucher insert(Voucher voucher);

}
