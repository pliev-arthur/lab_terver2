package lab.terver;

import java.util.HashMap;
import java.util.Map;

/**
 * Задача 11: Проверка гипотезы о виде распределения.
 * Критерий согласия хи-квадрат Пирсона.
 * Равновероятностные интервалы: m=9, k=2 оцененных параметра,
 * число степеней свободы m-k-1=6, alpha=0.10.
 */
public class Task11 {

    public static void main(String[] args) throws Exception {
        double[] data = DataReader.selectColumnForAnalysis("data.xlsx", "Лист1");
        int sampleSize = data.length;

        double sum = 0;
        for (double x : data) sum += x;
        double sampleMean = sum / sampleSize;

        double sumSquaredDiffs = 0;
        for (double x : data) {
            sumSquaredDiffs += (x - sampleMean) * (x - sampleMean);
        }
        double sampleVariance = sumSquaredDiffs / sampleSize; // смещённая оценка
        double sampleStdDev = Math.sqrt(sampleVariance);

        // Табличные z-квантили
        Map<Double, Double> zQuantileTable = new HashMap<>();
        zQuantileTable.put(0.010, -2.326); zQuantileTable.put(0.025, -1.960);
        zQuantileTable.put(0.050, -1.645); zQuantileTable.put(0.100, -1.282);
        zQuantileTable.put(0.200, -0.842); zQuantileTable.put(0.250, -0.674);
        zQuantileTable.put(0.300, -0.524); zQuantileTable.put(0.333, -0.431);
        zQuantileTable.put(0.400, -0.253); zQuantileTable.put(0.444, -0.141);
        zQuantileTable.put(0.500,  0.000); zQuantileTable.put(0.556,  0.141);
        zQuantileTable.put(0.600,  0.253); zQuantileTable.put(0.667,  0.431);
        zQuantileTable.put(0.700,  0.524); zQuantileTable.put(0.750,  0.674);
        zQuantileTable.put(0.800,  0.842); zQuantileTable.put(0.900,  1.282);
        zQuantileTable.put(0.950,  1.645); zQuantileTable.put(0.975,  1.960);
        zQuantileTable.put(0.990,  2.326);

        double[] sortedProbabilities = zQuantileTable.keySet().stream()
                .mapToDouble(Double::doubleValue).sorted().toArray();

        int m = 9; // число интервалов
        double[] intervalProbabilities = new double[m - 1];
        for (int i = 0; i < m - 1; i++) {
            intervalProbabilities[i] = (i + 1.0) / m;
        }

        double[] boundaries = new double[m + 1];
        boundaries[0] = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < m - 1; i++) {
            boundaries[i + 1] = sampleMean
                    + sampleStdDev * interpolateZ(zQuantileTable, sortedProbabilities,
                            intervalProbabilities[i]);
        }
        boundaries[m] = Double.POSITIVE_INFINITY;

        int[] n_i = new int[m];
        for (double x : data) {
            for (int i = 0; i < m; i++) {
                if (boundaries[i] < x && x <= boundaries[i + 1]) {
                    n_i[i]++;
                    break;
                }
            }
        }

        double np_i = (double) sampleSize / m;
        double chiSquared = 0;
        for (int i = 0; i < m; i++) {
            double diff = np_i - n_i[i];
            chiSquared += diff * diff / np_i;
        }

        int k = 2; // число оцененных параметров
        int degreesOfFreedom = m - k - 1; // 9 - 2 - 1 = 6
        double chiSquaredCritical = 10.645; // chi2_{0.90}(6)

        System.out.println("Границы равновероятностных интервалов:");
        for (int i = 1; i < m; i++) {
            System.out.printf("  p=%.3f z=%.3f x=%.4f%n",
                    intervalProbabilities[i - 1],
                    interpolateZ(zQuantileTable, sortedProbabilities,
                            intervalProbabilities[i - 1]),
                    boundaries[i]);
        }

        System.out.printf("%nm = %d, k = %d, m - k - 1 = %d%n", m, k, degreesOfFreedom);
        System.out.print("Наблюдаемые частоты n_i: ");
        for (int o : n_i) System.out.print(o + " ");
        System.out.printf("%nОжидаемая частота np_i: %.2f%n", np_i);
        System.out.printf("chi2 = %.4f%n", chiSquared);
        System.out.printf("chi2_{0.90}(%d) = %.3f%n",
                degreesOfFreedom, chiSquaredCritical);
        System.out.printf("Решение: %s%n",
                chiSquared > chiSquaredCritical ? "H0 отвергается" : "H0 не отвергается");
    }

    /** Линейная интерполяция z-квантиля по таблице. */
    static double interpolateZ(Map<Double, Double> table, double[] sortedKeys, double p) {
        if (table.containsKey(p)) return table.get(p);
        for (int i = 0; i < sortedKeys.length - 1; i++) {
            if (sortedKeys[i] <= p && p <= sortedKeys[i + 1]) {
                double t = (p - sortedKeys[i]) / (sortedKeys[i + 1] - sortedKeys[i]);
                return table.get(sortedKeys[i])
                        + t * (table.get(sortedKeys[i + 1]) - table.get(sortedKeys[i]));
            }
        }
        return 0.0;
    }
}
