package lab.terver;

/** Задача 3.4: 95%-ный доверительный интервал для доли (Бернулли). */
public class Task3_4 {
    public static void main(String[] args) {
        int n = 300;
        double pHat = 0.32;
        double z = 1.96;  // z_{0.975}

        // Метод 1: Асимптотический интервал
        double se = Math.sqrt(pHat * (1 - pHat) / n);
        double delta = z * se;
        System.out.printf("Асимптотический: (%.4f; %.4f)%n", pHat - delta, pHat + delta);

        // Метод 2: Интервал Вильсона
        double z2 = z * z;
        double denom = 1 + z2 / n;
        double center = (pHat + z2 / (2 * n)) / denom;
        double halfWidth = z * Math.sqrt(pHat * (1 - pHat) / n + z2 / (4.0 * n * n)) / denom;
        System.out.printf("Вильсон:       (%.4f; %.4f)%n", center - halfWidth, center + halfWidth);

        // Метод 3: Агрести-Коул
        int nTilde = n + 4;
        double pTilde = (n * pHat + 2) / nTilde;
        double seTilde = Math.sqrt(pTilde * (1 - pTilde) / nTilde);
        double deltaTilde = z * seTilde;
        System.out.printf("Агрести-Коул:  (%.4f; %.4f)%n", pTilde - deltaTilde, pTilde + deltaTilde);
    }
}
