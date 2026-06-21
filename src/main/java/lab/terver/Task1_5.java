package lab.terver;

/** Задача 1.5: 90%-ный левосторонний доверительный интервал для средней доходности. */
public class Task1_5 {
    public static void main(String[] args) {
        int n = 16;
        double xBar = 8;
        double s = 4;

        // t_{0.90}(15) = 1.341
        double tGamma = 1.341;
        double se = s / Math.sqrt(n);
        double lower = xBar - tGamma * se;

        System.out.printf("Стандартная ошибка: %.4f%n", se);
        System.out.printf("t_{0.90}(15) = %.3f%n", tGamma);
        System.out.printf("Нижняя граница: %.4f%n", lower);
        System.out.printf("90%%-ный левосторонний ДИ: (%.4f; +inf)%n", lower);
    }
}
