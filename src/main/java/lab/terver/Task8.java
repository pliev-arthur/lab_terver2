package lab.terver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Задача 8: Проверка случайности — критерий серий и критерий инверсий. */
public class Task8 {

    public static void main(String[] args) throws Exception {
        double[] data = DataReader.selectColumnForAnalysis("data.xlsx", "Лист1");
        int n = data.length;
        double alpha = 0.05;
        double zCrit = 1.96; // x_{0.975}[N(0;1)]

        // ==================== Критерий серий ====================
        // Находим выборочную медиану
        List<Double> sorted = new ArrayList<>();
        for (double x : data) sorted.add(x);
        Collections.sort(sorted);
        double median = (sorted.get(n / 2 - 1) + sorted.get(n / 2)) / 2;

        // Строим последовательность знаков: '+' если x_i > Me, '-' если x_i < Me
        char[] signs = new char[n];
        for (int i = 0; i < n; i++) {
            signs[i] = data[i] > median ? '+' : '-';
        }

        // Подсчитываем число серий KS
        int ks = 1;
        for (int i = 1; i < n; i++) {
            if (signs[i] != signs[i - 1]) ks++;
        }

        // Число плюсов и минусов
        int n1 = 0, n2 = 0;
        for (char c : signs) {
            if (c == '+') n1++; else n2++;
        }

        // Статистика Z (с поправкой на непрерывность −1/2)
        double eRuns = 2.0 * n1 * n2 / (n1 + n2) + 1;
        double varRuns = 2.0 * n1 * n2 * (2.0 * n1 * n2 - n1 - n2)
                / ((n1 + n2) * (n1 + n2) * (n1 + n2 - 1));
        double sigmaRuns = Math.sqrt(varRuns);
        double zRuns = (ks - eRuns - 0.5) / sigmaRuns;

        System.out.println("=== Критерий серий ===");
        System.out.printf("Медиана: %.4f%n", median);
        System.out.printf("n1 (+) = %d, n2 (−) = %d%n", n1, n2);
        System.out.printf("KS (число серий) = %d%n", ks);
        System.out.printf("E(KS) = 2·n1·n2/(n1+n2) + 1 = %.2f%n", eRuns);
        System.out.printf("Var(KS) = %.4f, σ = %.4f%n", varRuns, sigmaRuns);
        System.out.printf("Z = (KS − E(KS) − 1/2) / σ = %.4f%n", zRuns);
        System.out.printf("|Z| = %.4f, z_крит = %.4f%n", Math.abs(zRuns), zCrit);
        System.out.printf("Решение: %s%n%n",
                Math.abs(zRuns) < zCrit ? "H0 принимается" : "H0 отвергается");

        // ==================== Критерий инверсий ====================
        long t = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (data[i] > data[j]) t++;
            }
        }

        // Асимптотическая проверка:
        // |T_n − n(n−1)/4| · 6/n^{3/2}  <  x_{1−α/2}[N(0;1)]
        double statInversions = Math.abs(t - n * (n - 1.0) / 4.0) * 6.0 / Math.pow(n, 1.5);
        double eInversions = n * (n - 1.0) / 4.0;

        System.out.println("=== Критерий инверсий ===");
        System.out.printf("T_n (число инверсий) = %d%n", t);
        System.out.printf("E(T_n) = n(n−1)/4 = %.0f%n", eInversions);
        System.out.printf("|T_n − E(T_n)| · 6 / n^{3/2} = %.4f%n", statInversions);
        System.out.printf("z_крит = %.4f%n", zCrit);
        System.out.printf("Решение: %s%n",
                statInversions < zCrit ? "H0 принимается" : "H0 отвергается");
    }
}
