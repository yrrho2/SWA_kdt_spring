package org.prgrms.kdtspringdemo;

import org.prgrms.kdtspringdemo.voucher.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.*;
import java.util.*;


public class CommandLineApplication {
    public static void main(String[] args) throws IOException {
        Scanner terminal = new Scanner(System.in);
        String line;
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

            }else if(line.equals("list") | line.equals("l")){
                file.RefreshList();
                Iterator iterator = file.getVoucher().iterator();
                while(iterator.hasNext()){
                    UUID u = UUID.fromString(iterator.next().toString());
                    System.out.println(u);
                }

            }else System.out.println("Wrong");
        }
    }
}


class FileListSample {
    private String dirPath = "C:\\Users\\user\\dev-prgms\\kdt-spring-demo\\src\\main\\java\\org\\prgrms\\kdtspringdemo\\voucherFiles";
    private File dir = new File(dirPath);
    private File[] fileString = dir.listFiles();

    public void RefreshList(){
        fileString = dir.listFiles();
    }

    public File[] getFiles() {
        return fileString;
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

    public ArrayList<UUID> getVoucher(){
        ArrayList<UUID> FileUUID = new ArrayList<>();
        try{
            for(File file : fileString ){
                BufferedReader br = new BufferedReader(new FileReader(dir+"//"+file.getName()));
                String StringUUID = br.readLine();
                FileUUID.add(UUID.fromString(StringUUID));
                br.close();
            }
        } catch (IOException e){
            System.out.println(e.getMessage());
            e.getStackTrace();
        }
        return FileUUID;
    }

}