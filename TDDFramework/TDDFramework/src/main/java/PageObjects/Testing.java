package PageObjects;

public class Testing {
    public static void main(String[] args) {
        String pre = "$128,00.00";
        int premium = Integer.parseInt(pre.substring(1,pre.indexOf(".")).replaceAll(",",""));
        System.out.println(premium);
    }
}
