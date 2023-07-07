package org.prgrms.kdtspringdemo;

import org.prgrms.kdtspringdemo.voucher.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.*;

public class CommandLineApplication {
    public static void main(String[] args) {
        // 지금당장 구현할 수 없음.
        // Voucher관리 프로그램인데, VoucherRepository와 Service에
        // Voucher를 관리, 저장하는 기능이 없음.
        // 만드려면 만들수있겠지만 나중에 수정을 맞닥드리는것이 확실함.
        // Order를 만들고 이렇게저렇게 하려면... 역시 안됨 Repository에 기능이없음

        Scanner terminal = new Scanner(System.in);
        String line = new String();
        ArrayList<UUID> UL = new ArrayList<UUID>();
        Iterator<UUID> ULI = UL.iterator();
        var applicationContext =  new AnnotationConfigApplicationContext(AppConfiguration.class);
        var voucherService = applicationContext.getBean(MemoryVoucherRepository.class);
        UUID vUUID;
        long amount=0L;
        while(true){
            System.out.println(" ==== Voucher Program ==== ");
            System.out.println("Type exit to exit program.");
            System.out.println("Type create to create a new voucher");
            System.out.println("Type list to list all voucher");
            line=terminal.next();
            if(line.equals("exit") | line.equals("e")){
                System.out.println("EXIT");
                break;
            }else if(line.equals("create") | line.equals("c")){
                System.out.println("Fixed? or Percent?");
                line=terminal.next();
                System.out.println("Amount?");
                amount=terminal.nextLong();
                vUUID = UUID.randomUUID();
                if(line.equals("fixed") | line.equals("f"))
                    voucherService.insert(new FixedAmountVoucher(vUUID, amount));
                else if(line.equals("percent") | line.equals("p"))
                    voucherService.insert(new PercentDiscountVoucher(vUUID, amount));
                UL.add(vUUID);
            }else if(line.equals("list") | line.equals("l")){
                for(UUID u : UL){
                    System.out.println(u.toString());
                }

            }else System.out.println("Wrong");
        }
    }
}