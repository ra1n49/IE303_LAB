import java.util.Scanner;

public class Main {
    public static double sHinhTron(double r) {
        return 4 * Math.atan(1) * r * r;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double r = sc.nextDouble();
        double dientich = sHinhTron(r);
        System.out.println(dientich);
    }
}