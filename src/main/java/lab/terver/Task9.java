package lab.terver;

/** Задача 9: ММП-оценки параметров нормального распределения. */
public class Task9 {

    public static void main(String[] args) throws Exception {
        double[] data = DataReader.selectColumnForAnalysis("data.xlsx", "Лист1");
        int sampleSize = data.length;

        double sum = 0;
        for (double x : data) sum += x;
        double mleMean = sum / sampleSize;

        double sumSquaredDiffs = 0;
        for (double x : data) {
            sumSquaredDiffs += (x - mleMean) * (x - mleMean);
        }
        double mleVariance = sumSquaredDiffs / sampleSize;
        double mleStdDev = Math.sqrt(mleVariance);
        double unbiasedVariance = sumSquaredDiffs / (sampleSize - 1);

        System.out.printf("n = %d%n", sampleSize);
        System.out.printf("ММП-оценка a: %.4f%n", mleMean);
        System.out.printf("ММП-оценка sigma^2: %.4f%n", mleVariance);
        System.out.printf("ММП-оценка sigma: %.4f%n", mleStdDev);
        System.out.printf("Несмещённая оценка sigma^2: %.4f%n", unbiasedVariance);
        System.out.printf("Смещение ММП-оценки sigma^2: %.4f%n",
                mleVariance - unbiasedVariance);

        // Проверка: E(s2_mle) = (n-1)/n * sigma^2
        System.out.printf("Проверка: s2_mle * n/(n-1) = %.4f%n",
                mleVariance * sampleSize / (sampleSize - 1));
        System.out.printf("          s2_unbiased = %.4f%n", unbiasedVariance);
    }
}
