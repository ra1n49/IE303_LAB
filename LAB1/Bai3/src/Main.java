import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static int crossProduct(int[] p, int[] q, int[] r) {
        return (q[0] - p[0]) * (r[1] - p[1]) - (q[1] - p[1]) * (r[0] - p[0]);
    }

    public static List<int[]> convexHull(List<int[]> points) {
        points.sort((a, b) -> a[0] == b[0] ? a[1] - b[1] : a[0] - b[0]);

        List<int[]> lower = new ArrayList<>();
        for (int[] p : points) {
            while (lower.size() >= 2 && crossProduct(lower.get(lower.size() - 2), lower.get(lower.size() - 1), p) <= 0) {
                lower.remove(lower.size() - 1);
            }
            lower.add(p);
        }

        List<int[]> upper = new ArrayList<>();
        for (int i = points.size() - 1; i >= 0; i--) {
            int[] p = points.get(i);
            while (upper.size() >= 2 && crossProduct(upper.get(upper.size() - 2), upper.get(upper.size() - 1), p) <= 0) {
                upper.remove(upper.size() - 1);
            }
            upper.add(p);
        }

        lower.remove(lower.size() - 1);
        upper.remove(upper.size() - 1);

        lower.addAll(upper);
        return lower;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        List<int[]> points = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            points.add(new int[]{x, y});
        }

        List<int[]> result = convexHull(points);

        for (int[] p : result) {
            System.out.println(p[0] + " " + p[1]);
        }
    }
}