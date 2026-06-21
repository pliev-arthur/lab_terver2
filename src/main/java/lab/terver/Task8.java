package lab.terver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Задача 8: Проверка случайности — критерий серий и критерий инверсий (столбец 5). */
public class Task8 {
    public static void main(String[] args) throws Exception {
        double[] data = DataReader.readColumn("data.xlsx", "Лист1", 4);

        int n = data.length;

        // --- Критерий серий (медиана) ---
        List<Double> sorted = new ArrayList<>();
        for (double x : data) sorted.add(x);
        Collections.sort(sorted);
        double med = (sorted.get(n / 2 - 1) + sorted.get(n / 2)) / 2;

        char[] binary = new char[n];
        for (int i = 0; i < n; i++)
            binary[i] = data[i] > med ? 'A' : 'B';

        int R = 1;
        for (int i = 1; i < n; i++)
            if (binary[i] != binary[i - 1]) R++;

        int nA = 0, nB = 0;
        for (char c : binary)
            if (c == 'A') nA++; else nB++;

        double ER = 2.0 * nA * nB / (nA + nB) + 1;
        double VarR = 2.0 * nA * nB * (2.0 * nA * nB - nA - nB)
                     / ((nA + nB) * (nA + nB) * (nA + nB - 1));
        double sigmaR = Math.sqrt(VarR);
        double Z_R = (R - ER) / sigmaR;
        double p_R = 2 * (1 - cdfNormal(Math.abs(Z_R)));

        System.out.println("=== Критерий серий ===");
        System.out.printf("Медиана: %.4f%n", med);
        System.out.printf("n_A = %d, n_B = %d%n", nA, nB);
        System.out.printf("R = %d%n", R);
        System.out.printf("E(R) = %.2f%n", ER);
        System.out.printf("Var(R) = %.4f, sigma = %.4f%n", VarR, sigmaR);
        System.out.printf("Z = %.4f%n", Z_R);
        System.out.printf("p-value = %.4f%n", p_R);

        // --- Критерий инверсий ---
        long I = 0;
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++)
                if (data[i] > data[j]) I++;

        double EI = n * (n - 1) / 4.0;
        double VarI = n * (n - 1.0) * (2.0 * n + 5) / 72.0;
        double sigmaI = Math.sqrt(VarI);
        double Z_I = (I - EI) / sigmaI;
        double p_I = 2 * (1 - cdfNormal(Math.abs(Z_I)));

        System.out.println("\n=== Критерий инверсий ===");
        System.out.printf("I = %d%n", I);
        System.out.printf("E(I) = %.0f%n", EI);
        System.out.printf("Var(I) = %.2f%n", VarI);
        System.out.printf("sigma = %.4f%n", sigmaI);
        System.out.printf("Z = %.4f%n", Z_I);
        System.out.printf("p-value = %.4f%n", p_I);
    }

    /** CDF стандартного нормального распределения. */
    static double cdfNormal(double x) {
        return 0.5 * (1 + erf(x / Math.sqrt(2)));
    }

    /** Аппроксимация функции ошибок (Abramowitz and Stegun, формула 7.1.26). */
    static double erf(double x) {
        double t = 1.0 / (1.0 + 0.3275911 * Math.abs(x));
        double tau = t * (0.254829592
                        + t * (-0.284496736
                        + t * (1.421413741
                        + t * (-1.453152027
                        + t * 1.061405429))));
        double y = 1 - tau * Math.exp(-x * x);
        return x >= 0 ? y : -y;
    }
}
