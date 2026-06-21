package lab.terver;

/** Задача 5.4: Проверка гипотезы о стандартном отклонении (хи-квадрат). */
public class Task5_4 {
    public static void main(String[] args) {
        int n = 30;
        double[] data = new double[n];
        int idx = 0;
        data[idx++] = 10;
        for (int i = 0; i < 2; i++)  data[idx++] = 25;
        for (int i = 0; i < 4; i++)  data[idx++] = 30;
        for (int i = 0; i < 16; i++) data[idx++] = 40;
        for (int i = 0; i < 4; i++)  data[idx++] = 50;
        for (int i = 0; i < 2; i++)  data[idx++] = 55;
        data[idx++] = 70;

        double sigma0 = 10;

        double sum = 0;
        for (double x : data) sum += x;
        double xBar = sum / n;

        double ssq = 0;
        for (double x : data) ssq += (x - xBar) * (x - xBar);
        double s2 = ssq / (n - 1);
        double s = Math.sqrt(s2);

        double chi2Stat = (n - 1) * s2 / (sigma0 * sigma0);

        System.out.printf("Среднее: %.2f%n", xBar);
        System.out.printf("Несмещённая дисперсия s^2: %.4f%n", s2);
        System.out.printf("Выборочное СКО s: %.4f%n", s);
        System.out.printf("Хи-квадрат статистика: %.4f%n", chi2Stat);

        double chi2Crit = 42.557;  // chi2_{0.95}(29)
        System.out.printf("Критическая точка chi2_{0.95}(29) = %.3f%n", chi2Crit);
        System.out.printf("Решение: %s%n",
                          chi2Stat > chi2Crit ? "Отвергаем H0" : "Нет оснований отвергнуть H0");

        int nu = n - 1;
        double z = Math.sqrt(2 * chi2Stat) - Math.sqrt(2 * nu - 1);
        double pValueApprox = 1 - cdfNormal(z);
        System.out.printf("p-value (норм. аппрокс.): %.4f%n", pValueApprox);
    }

    static double cdfNormal(double x) {
        return 0.5 * (1 + erf(x / Math.sqrt(2)));
    }

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
