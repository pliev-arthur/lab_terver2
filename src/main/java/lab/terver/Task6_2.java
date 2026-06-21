package lab.terver;

/** Задача 6.2: Критерий хи-квадрат Пирсона для мультиномиальных пропорций. */
public class Task6_2 {
    public static void main(String[] args) {
        int[] nObs = {312, 486, 189};
        double[] pHyp = {0.30, 0.50, 0.20};

        int n = 0;
        for (int ni : nObs) n += ni;

        double[] nExp = new double[3];
        for (int i = 0; i < 3; i++)
            nExp[i] = n * pHyp[i];

        System.out.print("Ожидаемые частоты: ");
        for (int i = 0; i < 3; i++)
            System.out.printf("%.1f ", nExp[i]);
        System.out.println();

        double chi2 = 0;
        for (int i = 0; i < 3; i++) {
            double diff = nObs[i] - nExp[i];
            chi2 += diff * diff / nExp[i];
        }
        System.out.printf("Хи-квадрат Пирсона: %.4f%n", chi2);

        double G = 0;
        for (int i = 0; i < 3; i++)
            G += 2 * nObs[i] * Math.log(nObs[i] / nExp[i]);
        System.out.printf("G-статистика: %.4f%n", G);

        double chi2Crit = 5.991;  // chi2_{0.95}(2)
        System.out.printf("Критическая точка chi2_{0.95}(2) = %.3f%n", chi2Crit);

        double pValue = Math.exp(-chi2 / 2);  // chi2(2) CDF = 1 - e^{-x/2}
        System.out.printf("p-value (точное для nu=2): %.4f%n", pValue);
        System.out.printf("Решение: %s%n",
                          chi2 > chi2Crit ? "Отвергаем H0" : "Нет оснований отвергнуть H0");
    }
}
