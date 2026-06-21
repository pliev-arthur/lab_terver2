package lab.terver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/** Задача 12: Критерий Манна-Уитни однородности двух выборок (столбец 5). */
public class Task12 {

    /** Пара (значение, группа) для совместного ранжирования. */
    private static class Pair {
        final double val;
        final int group;
        Pair(double val, int group) { this.val = val; this.group = group; }
    }

    public static void main(String[] args) throws Exception {
        double[] allData = DataReader.readColumn("data.xlsx", "Лист1", 4);

        // Разбиение: первые 100 vs последние 100
        double[] sample1 = Arrays.copyOfRange(allData, 0, 100);
        double[] sample2 = Arrays.copyOfRange(allData, 100, 200);
        int n1 = sample1.length, n2 = sample2.length;

        // Объединяем и сортируем
        List<Pair> combined = new ArrayList<>();
        for (double x : sample1) combined.add(new Pair(x, 1));
        for (double y : sample2) combined.add(new Pair(y, 2));
        combined.sort(Comparator.comparingDouble(p -> p.val));

        // Ранги с учётом совпадений (mid-rank)
        double[] ranks = new double[combined.size()];
        int i = 0;
        while (i < combined.size()) {
            int j = i;
            while (j < combined.size() && combined.get(j).val == combined.get(i).val)
                j++;
            double avgRank = (i + j + 1) / 2.0;
            for (int t = i; t < j; t++)
                ranks[t] = avgRank;
            i = j;
        }

        // Сумма рангов первой выборки
        double R1 = 0;
        for (int t = 0; t < combined.size(); t++)
            if (combined.get(t).group == 1)
                R1 += ranks[t];

        // Статистика Манна-Уитни
        double U1 = R1 - n1 * (n1 + 1) / 2.0;
        double U2 = (double) n1 * n2 - U1;
        double U = Math.min(U1, U2);

        double EU = n1 * n2 / 2.0;
        double VarU = n1 * n2 * (n1 + n2 + 1) / 12.0;
        double sigmaU = Math.sqrt(VarU);
        double Z = (U - EU) / sigmaU;

        // Описательные статистики выборок
        double mean1 = mean(sample1), mean2 = mean(sample2);
        double med1  = median(sample1), med2 = median(sample2);

        System.out.println("--- Описательные статистики ---");
        System.out.printf("Выборка 1: n=%d, mean=%.4f, median=%.4f%n", n1, mean1, med1);
        System.out.printf("Выборка 2: n=%d, mean=%.4f, median=%.4f%n", n2, mean2, med2);

        System.out.println("\n--- Критерий Манна-Уитни ---");
        System.out.printf("R1 = %.1f%n", R1);
        System.out.printf("U1 = %.1f, U2 = %.1f, U = min(U1,U2) = %.1f%n", U1, U2, U);
        System.out.printf("E(U) = %.1f, sigma = %.4f%n", EU, sigmaU);
        System.out.printf("Z = %.4f%n", Z);
        System.out.printf("Critical |Z| = %.3f (alpha = 0.01)%n", 2.576);
        System.out.printf("Decision: %s%n", Math.abs(Z) > 2.576 ? "REJECT H0" : "Do NOT reject H0");
    }

    static double mean(double[] a) {
        double s = 0;
        for (double x : a) s += x;
        return s / a.length;
    }

    static double median(double[] a) {
        double[] copy = a.clone();
        Arrays.sort(copy);
        int n = copy.length;
        return (copy[n / 2 - 1] + copy[n / 2]) / 2;
    }
}
