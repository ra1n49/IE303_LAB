import java.util.Random;

public class Main {
    public static double tinhPi(int n) {
        Random random = new Random();
        int check = 0;

        for (int i = 0; i < n; i++) {
            double x = random.nextDouble();
            double y = random.nextDouble();

            if (x * x + y * y <= 1) {
                check++;
            }
        }
        return 4.0 * check / n;
    }

    public static void main(String[] args) {
        int i = 1000000;
        double pi = tinhPi(i);
        System.out.println(pi);
    }
}