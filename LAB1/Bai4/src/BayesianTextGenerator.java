import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class BayesianTextGenerator {
    private Map<String, Map<String, Double>> prob;
    private Random r;
    private Set<String> vocab;

    public BayesianTextGenerator() {
        prob = new HashMap<>();
        vocab = new HashSet<>();
        r = new Random();
    }

    public void trainModel(String path) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {
            String line;
            Map<String, Map<String, Integer>> count = new HashMap<>();
            Map<String, Integer> total = new HashMap<>();

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] w = line.trim().split("\\s+");
                for (int i = 0; i < w.length - 1; i++) {
                    String cur = w[i].toLowerCase().trim();
                    String next = w[i + 1].toLowerCase().trim();

                    vocab.add(cur);
                    vocab.add(next);

                    count.putIfAbsent(cur, new HashMap<>());
                    count.get(cur).merge(next, 1, Integer::sum);
                    total.merge(cur, 1, Integer::sum);
                }
            }

            for (String cur : count.keySet()) {
                prob.put(cur, new HashMap<>());
                int t = total.get(cur);

                for (Map.Entry<String, Integer> e : count.get(cur).entrySet()) {
                    double p = (e.getValue() + 1.0) / (t + count.get(cur).size());
                    prob.get(cur).put(e.getKey(), p);
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public String generateText(String start, int max) {
        start = start.toLowerCase().trim();

        if (!vocab.contains(start)) return "Tu " + start + " khong ton tai";

        List<String> text = new ArrayList<>();
        text.add(start);
        String cur = start;

        while (text.size() < max) {
            Map<String, Double> next = prob.get(cur);

            if (next == null || next.isEmpty()) {
                break;
            }

            String word = selectNext(next);
            text.add(word);
            cur = word;
        }

        return String.join(" ", text);
    }

    private String selectNext(Map<String, Double> p) {
        double rand = r.nextDouble();
        double cum = 0.0;

        for (Map.Entry<String, Double> e : p.entrySet()) {
            cum += e.getValue();
            if (rand <= cum) {
                return e.getKey();
            }
        }
        return new ArrayList<>(p.keySet()).get(0);
    }

    public static void main(String[] args) {
        BayesianTextGenerator gen = new BayesianTextGenerator();
        String path = "UIT-ViOCD/UIT-ViOCD.txt";
        gen.trainModel(path);

        Scanner sc = new Scanner(System.in);
        String s = sc.next();

        System.out.println(gen.generateText(s, 5));
    }
}