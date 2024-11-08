import java.io.IOException;

public class GoogleOnWebApplication {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide a search keyword as a command-line argument.");
            return;
        }
        
        String keyword = args[0];  // 接收命令列參數
        
        try {
            System.out.println(new GoogleQuery(keyword).query());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
