package lab.terver;

/** Задача 4.4: Определение уровня доверия по ширине интервала. */
public class Task4_4 {
    public static void main(String[] args) {
        double mid95 = (94.84 + 105.16) / 2;
        double mid1  = (93.55 + 106.45) / 2;
        double half95 = (105.16 - 94.84) / 2;
        double half1  = (106.45 - 93.55) / 2;

        System.out.printf("Среднее x_bar = %.2f%n", mid95);
        System.out.printf("Полуширина 95%% интервала: %.2f%n", half95);
        System.out.printf("Полуширина искомого интервала: %.2f%n", half1);

        double zRatio = half1 / half95;
        double z1 = 1.96 * zRatio;
        System.out.printf("z-квантиль искомого интервала: %.2f%n", z1);

        double phi = cdfNormal(z1);
        double gamma = 2 * phi - 1;
        System.out.printf("Уровень доверия: %.4f (%.1f%%)%n", gamma, gamma * 100);
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
