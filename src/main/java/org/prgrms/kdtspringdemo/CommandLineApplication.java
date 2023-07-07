package org.prgrms.kdtspringdemo;

import org.prgrms.kdtspringdemo.voucher.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.*;
import java.nio.file.Files;
import java.util.*;


public class CommandLineApplication {
    public static void main(String[] args) throws IOException {
        Scanner terminal = new Scanner(System.in);
        String line;
        ArrayList<UUID> UL = new ArrayList<UUID>();
        UUID vUUID;
        long amount;

        FileListSample file = new FileListSample();

        var applicationContext =  new AnnotationConfigApplicationContext(AppConfiguration.class);
        var voucherService = applicationContext.getBean(MemoryVoucherRepository.class);
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
                vUUID=UUID.randomUUID();
                if(line.equals("fixed") | line.equals("f"))
                    voucherService.insert(file.saveVoucher(new FixedAmountVoucher(vUUID, amount), amount));
                else if(line.equals("percent") | line.equals("p"))
                    voucherService.insert(file.saveVoucher(new PercentDiscountVoucher(vUUID, amount), amount));
                UL.add(vUUID);

            }else if(line.equals("list") | line.equals("l")){
                for(UUID u : UL){
                    System.out.println(u.toString());
                }

            }else System.out.println("Wrong");
        }
    }
}


class FileListSample {
    private String dirPath = "C:\\Users\\user\\dev-prgms\\kdt-spring-demo\\src\\main\\java\\org\\prgrms\\kdtspringdemo\\voucherFiles";
    private File dir = new File(dirPath);
    private File[] files;

    public FileListSample() {
        this.files = dir.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                if(file.isFile() && file.getName().toUpperCase().startsWith("")) {
                    return true;
                }else {
                    return false;
                }
            }
        });
    }

    public File[] getFiles() {
        return files;
    }
    public void setFiles(String name) throws IOException {
        FileOutputStream output = new FileOutputStream(dirPath+"\\"+name+".txt");
        output.close();
    }
    public Voucher saveVoucher(Voucher voucher, long amount) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(dirPath+"\\"+voucher.getVoucherId()+".txt");
        pw.println(voucher.getVoucherId());
        pw.print(amount);
        pw.close();
        return voucher;
    }
    // 출처 https://docko.tistory.com/621
}