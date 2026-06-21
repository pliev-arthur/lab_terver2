package lab.terver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Задача 7: Описательные статистики и гистограмма (столбец 5). */
public class Task7 {
    public static void main(String[] args) throws Exception {
        double[] data = DataReader.readColumn("data.xlsx", "Лист1", 4);

        int n = data.length;
        double sum = 0;
        for (double x : data) sum += x;
        double xBar = sum / n;

        double ssq = 0;
        for (double x : data) ssq += (x - xBar) * (x - xBar);
        double s2 = ssq / (n - 1);
        double s = Math.sqrt(s2);

        // Медиана
        List<Double> sorted = new ArrayList<>();
        for (double x : data) sorted.add(x);
        Collections.sort(sorted);
        double Me = (sorted.get(99) + sorted.get(100)) / 2;

        // Центральные моменты
        double m3 = 0, m4 = 0;
        for (double x : data) {
            double d = x - xBar;
            m3 += d * d * d;
            m4 += d * d * d * d;
        }
        m3 /= n;
        m4 /= n;
        double skew = m3 / (s * s * s);
        double kurt = m4 / (s * s * s * s) - 3;

        double xMin = sorted.get(0);
        double xMax = sorted.get(n - 1);

        System.out.printf("n = %d%n", n);
        System.out.printf("min = %.4f, max = %.4f%n", xMin, xMax);
        System.out.printf("Среднее = %.4f%n", xBar);
        System.out.printf("Медиана = %.4f%n", Me);
        System.out.printf("Дисперсия s^2 = %.4f%n", s2);
        System.out.printf("СКО s = %.4f%n", s);
        System.out.printf("Асимметрия = %.4f%n", skew);
        System.out.printf("Эксцесс = %.4f%n", kurt);

        // Гистограмма
        int k = 9;
        double h = (xMax - xMin) / k;
        int[] counts = new int[k];
        for (double x : data) {
            int bin = Math.min((int) ((x - xMin) / h), k - 1);
            counts[bin]++;
        }
        System.out.printf("%nШирина интервала: %.4f%n", h);
        for (int i = 0; i < k; i++) {
            double lo = xMin + i * h;
            double hi = lo + h;
            double rel = (double) counts[i] / n;
            StringBuilder bar = new StringBuilder();
            for (int j = 0; j < (int) (rel * 100); j++) bar.append('█');
            System.out.printf("[%7.2f, %7.2f) n=%3d w=%.3f %s%n", lo, hi, counts[i], rel, bar);
        }
    }
}
