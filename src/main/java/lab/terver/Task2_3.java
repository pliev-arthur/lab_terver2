package lab.terver;

/** Задача 2.3: 80%-ный доверительный интервал для среднеквадратического отклонения. */
public class Task2_3 {
    public static void main(String[] args) {
        int n = 26;
        double s = 6;
        int nu = n - 1;  // 25

        // Квантили хи-квадрат из таблиц
        double chi2Lower = 16.473;  // chi2_{0.10}(25)
        double chi2Upper = 34.382;  // chi2_{0.90}(25)

        double sigmaLower = s * Math.sqrt(nu / chi2Upper);
        double sigmaUpper = s * Math.sqrt(nu / chi2Lower);

        System.out.printf("chi2_{0.10}(25) = %.3f%n", chi2Lower);
        System.out.printf("chi2_{0.90}(25) = %.3f%n", chi2Upper);
        System.out.printf("80%%-ный ДИ для sigma: (%.3f; %.3f)%n", sigmaLower, sigmaUpper);
        System.out.printf("80%%-ный ДИ для sigma^2: (%.2f; %.2f)%n",
                          sigmaLower * sigmaLower, sigmaUpper * sigmaUpper);
    }
}
