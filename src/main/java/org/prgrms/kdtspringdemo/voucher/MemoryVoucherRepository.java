package org.prgrms.kdtspringdemo.voucher;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Qualifier("Memory")
public class MemoryVoucherRepository implements VoucherRepository {
    // VoucherRepository가 많아서 VoucherService의 생성자에서 어떤 class를 가져올지 정할때
    //  Primary를 붙여주면 가장 우선시됨

    private final Map<UUID, Voucher> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<Voucher> findById(UUID voucherId) {
        return Optional.ofNullable(storage.get(voucherId));
    }
    @Override
    public Voucher insert(Voucher voucher) {
        storage.put(voucher.getVoucherId(), voucher);
        return voucher;
    }
}
