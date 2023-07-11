package org.prgrms.kdtspringdemo;

import org.prgrms.kdtspringdemo.voucher.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.*;


public class CommandLineApplication {
    public static void main(String[] args) throws IOException {
        Scanner terminal = new Scanner(System.in);
        String line;
        UUID vUUID;
        long amount;

        VoucherFileControl file = new VoucherFileControl();

        var applicationContext =  new AnnotationConfigApplicationContext(AppConfiguration.class);
        applicationContext.register(AppConfiguration.class);
        var environment = applicationContext.getEnvironment();
        environment.setActiveProfiles("local");
        var voucherService = applicationContext.getBean(VoucherRepository.class);
        // applicationContext.refresh();
        // 이거쓰면 refresh() 두번이상 한다고 혼냄.
        // 디버깅 해보니까 AnnotationConfigApplicationContext만들때 한번 해서 그런듯?



        while(true){
            System.out.println(" ==== Voucher Program ==== ");
            System.out.println("Type exit to exit program.");
            System.out.println("Type create to create a new voucher");
            System.out.println("Type list to list all voucher");
            System.out.println("Type black to list black list");
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

            }else if(line.equals("black") | line.equals("b")){
                BlackList BL = new BlackList(applicationContext);
                System.out.println(BL.getData().stream().reduce("",(a,b)->a+"\n"+b));

            }else if(line.equals("dev") | line.equals("d")){
                System.out.println(MessageFormat.format("is Jdbc Repo -> {0}", voucherService instanceof JdbcVoucherRepository));
                System.out.println(MessageFormat.format("is Jdbc Repo -> {0}", voucherService.getClass().getCanonicalName()));

            }else System.out.println("Wrong");
        }
    }
}
class BlackList {
    private final Resource resource;
    public BlackList(ApplicationContext applicationContext) {
        this.resource = applicationContext.getResource("file:kdt_files/black_list.csv");
    }
    public ArrayList getData() throws IOException {
        File file = this.resource.getFile();
        ArrayList strings = (ArrayList) Files.readAllLines(file.toPath());
        return strings;
    }
    // 그리고 Yaml Propertie를 만든 후, 메모리 바우처 레포지토리를 개발 프로파일에서만 작동하도록

}


class VoucherFileControl {
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