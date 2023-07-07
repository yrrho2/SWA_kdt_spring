package org.prgrms.kdtspringdemo.voucher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VoucherService {

    private VoucherRepository voucherRepository;

    public VoucherService(@Qualifier("Memory") VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    public Voucher getVoucher(UUID voucherId){
        return voucherRepository
                .findById(voucherId)
                .orElseThrow(()-> new RuntimeException("Can not find a Voucher for "+voucherId));
        //.orElseThrow를 사용해서 바우처가 없으면 예외처리 가능.
        // 이게 optional의 메소드인지?
    }

    public void useVoucher(Voucher voucher) {
    }
}
