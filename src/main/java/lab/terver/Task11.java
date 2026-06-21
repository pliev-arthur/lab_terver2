package lab.terver;

import java.util.HashMap;
import java.util.Map;

/**
 * Задача 11: Проверка нормальности критерием хи-квадрат Пирсона (столбец 5).
 * Используются равновероятностные интервалы (k=9, nu=6, alpha=0.10).
 */
public class Task11 {
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

        // Табличные z-квантили для вероятностей p
        Map<Double, Double> zTable = new HashMap<>();
        zTable.put(0.010, -2.326); zTable.put(0.025, -1.960); zTable.put(0.050, -1.645);
        zTable.put(0.100, -1.282); zTable.put(0.200, -0.842); zTable.put(0.250, -0.674);
        zTable.put(0.300, -0.524); zTable.put(0.333, -0.431); zTable.put(0.400, -0.253);
        zTable.put(0.444, -0.141); zTable.put(0.500,  0.000); zTable.put(0.556,  0.141);
        zTable.put(0.600,  0.253); zTable.put(0.667,  0.431); zTable.put(0.700,  0.524);
        zTable.put(0.750,  0.674); zTable.put(0.800,  0.842); zTable.put(0.900,  1.282);
        zTable.put(0.950,  1.645); zTable.put(0.975,  1.960); zTable.put(0.990,  2.326);

        double[] sortedP = zTable.keySet().stream().mapToDouble(Double::doubleValue)
                                  .sorted().toArray();

        int k = 9;
        double[] probs = new double[k - 1];
        for (int i = 0; i < k - 1; i++) probs[i] = (i + 1.0) / k;

        double[] boundaries = new double[k + 1];
        boundaries[0] = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < k - 1; i++)
            boundaries[i + 1] = xBar + s * getZ(zTable, sortedP, probs[i]);
        boundaries[k] = Double.POSITIVE_INFINITY;

        int[] observed = new int[k];
        for (double x : data) {
            for (int i = 0; i < k; i++) {
                if (boundaries[i] < x && x <= boundaries[i + 1]) {
                    observed[i]++;
                    break;
                }
            }
        }

        double expected = (double) n / k;
        double chi2 = 0;
        for (int i = 0; i < k; i++)
            chi2 += (observed[i] - expected) * (observed[i] - expected) / expected;

        int nu = k - 1 - 2;  // 9 - 1 - 2 = 6
        double chi2Crit = 10.645;  // chi2_{0.90}(6)

        System.out.println("Границы равновероятностных интервалов:");
        for (int i = 1; i < k; i++)
            System.out.printf("  p=%.3f z=%.3f x=%.4f%n", probs[i - 1],
                              getZ(zTable, sortedP, probs[i - 1]), boundaries[i]);

        System.out.printf("%nk = %d, nu = %d%n", k, nu);
        System.out.printf("Наблюдаемые частоты: ");
        for (int o : observed) System.out.print(o + " ");
        System.out.printf("%nОжидаемая частота: %.2f%n", expected);
        System.out.printf("Chi2 = %.4f%n", chi2);
        System.out.printf("Critical chi2_0.90(%d) = %.3f%n", nu, chi2Crit);
        System.out.printf("Decision: %s%n", chi2 > chi2Crit ? "REJECT H0" : "Do NOT reject H0");
    }

    /** Линейная интерполяция z-квантиля по таблице. */
    static double getZ(Map<Double, Double> table, double[] sortedP, double p) {
        if (table.containsKey(p)) return table.get(p);
        for (int i = 0; i < sortedP.length - 1; i++) {
            if (sortedP[i] <= p && p <= sortedP[i + 1]) {
                double t = (p - sortedP[i]) / (sortedP[i + 1] - sortedP[i]);
                return table.get(sortedP[i]) + t * (table.get(sortedP[i + 1]) - table.get(sortedP[i]));
            }
        }
        return 0.0;
    }
}
