package lab.terver;

import java.util.Arrays;

/**
 * Задача 12: Проверка гипотезы однородности двух выборок
 * с помощью критерия Манна-Уитни.
 *
 * Статистика U вычисляется через парные сравнения:
 * U = sum I{X_i < Y_j} + (1/2) sum I{X_i = Y_j}.
 */
public class Task12 {

    public static void main(String[] args) throws Exception {
        double[] allData = DataReader.selectColumnForAnalysis("data.xlsx", "Лист1");

        // Разбиение: выборка X (первые 100), выборка Y (последние 100)
        double[] sampleX = Arrays.copyOfRange(allData, 0, 100);
        double[] sampleY = Arrays.copyOfRange(allData, 100, 200);
        int m = sampleX.length;
        int n = sampleY.length;

        // Вычисление U через парные сравнения
        double U = 0;
        for (double x : sampleX) {
            for (double y : sampleY) {
                if (x < y) {
                    U += 1;
                } else if (x == y) {
                    U += 0.5;
                }
            }
        }

        double expectedU = m * n / 2.0;
        double varianceU = m * n * (m + n + 1) / 12.0;
        double stdDevU = Math.sqrt(varianceU);
        double stat = Math.abs(U - expectedU) / stdDevU;

        double zCrit = 2.576; // x_{0.995}[N(0,1)]

        // Описательные статистики
        double meanX = mean(sampleX);
        double meanY = mean(sampleY);
        double medianX = median(sampleX);
        double medianY = median(sampleY);

        System.out.println("--- Описательные статистики ---");
        System.out.printf("Выборка X: m=%d, среднее=%.4f, медиана=%.4f%n",
                m, meanX, medianX);
        System.out.printf("Выборка Y: n=%d, среднее=%.4f, медиана=%.4f%n",
                n, meanY, medianY);

        System.out.println("\n--- Критерий Манна-Уитни ---");
        System.out.printf("Число парных сравнений: m*n = %d%n", m * n);
        System.out.printf("U = %.1f%n", U);
        System.out.printf("M[U] = mn/2 = %.1f%n", expectedU);
        System.out.printf("D[U] = mn(m+n+1)/12 = %.1f, sigma = %.4f%n",
                varianceU, stdDevU);
        System.out.printf("|U - mn/2| / sqrt(mn(m+n+1)/12) = %.4f%n", stat);
        System.out.printf("x_{1-alpha/2}[N(0,1)] = %.3f (alpha = 0.01)%n", zCrit);
        System.out.printf("Решение: %s%n",
                stat > zCrit ? "H0 отвергается" : "H0 не отвергается");
    }

    static double mean(double[] array) {
        double s = 0;
        for (double x : array) s += x;
        return s / array.length;
    }

    static double median(double[] array) {
        double[] copy = array.clone();
        Arrays.sort(copy);
        int n = copy.length;
        return (copy[n / 2 - 1] + copy[n / 2]) / 2;
    }
}
