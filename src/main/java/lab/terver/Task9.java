package lab.terver;

/** Задача 9: ММП-оценки параметров нормального распределения (столбец 5). */
public class Task9 {
    public static void main(String[] args) throws Exception {
        double[] data = DataReader.readColumn("data.xlsx", "Лист1", 4);

        int n = data.length;

        double sum = 0;
        for (double x : data) sum += x;
        double aHat = sum / n;

        double ssq = 0;
        for (double x : data) ssq += (x - aHat) * (x - aHat);
        double sigma2HatMle = ssq / n;
        double sigmaHatMle = Math.sqrt(sigma2HatMle);

        double sigma2Unbiased = ssq / (n - 1);

        System.out.printf("n = %d%n", n);
        System.out.printf("ММП-оценка a: %.4f%n", aHat);
        System.out.printf("ММП-оценка sigma^2: %.4f%n", sigma2HatMle);
        System.out.printf("ММП-оценка sigma: %.4f%n", sigmaHatMle);
        System.out.printf("Несмещённая оценка sigma^2: %.4f%n", sigma2Unbiased);
        System.out.printf("Смещение ММП-оценки sigma^2: %.4f%n", sigma2HatMle - sigma2Unbiased);

        // Проверка: E(s2_hat) должно быть (n-1)/n * sigma^2
        System.out.printf("Проверка: s2_mle * n/(n-1) = %.4f%n", sigma2HatMle * n / (n - 1));
        System.out.printf("          s2_unbiased = %.4f%n", sigma2Unbiased);
    }
}
