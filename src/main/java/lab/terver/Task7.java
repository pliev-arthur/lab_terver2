package lab.terver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Задача 7: Описательные статистики и гистограмма. */
public class Task7 {

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
        double sampleVariance = sumSquaredDiffs / sampleSize;
        double sampleStdDev = Math.sqrt(sampleVariance);

        // Медиана
        List<Double> sorted = new ArrayList<>();
        for (double x : data) sorted.add(x);
        Collections.sort(sorted);
        double median = (sorted.get(99) + sorted.get(100)) / 2;

        // Центральные моменты
        double thirdCentralMoment = 0;
        double fourthCentralMoment = 0;
        for (double x : data) {
            double deviation = x - sampleMean;
            thirdCentralMoment += deviation * deviation * deviation;
            fourthCentralMoment += deviation * deviation * deviation * deviation;
        }
        thirdCentralMoment /= sampleSize;
        fourthCentralMoment /= sampleSize;
        double skewness = thirdCentralMoment
                / (sampleStdDev * sampleStdDev * sampleStdDev);
        double excessKurtosis =
                fourthCentralMoment
                        / (sampleStdDev * sampleStdDev * sampleStdDev * sampleStdDev) - 3;

        double minValue = sorted.get(0);
        double maxValue = sorted.get(sampleSize - 1);

        System.out.printf("n = %d%n", sampleSize);
        System.out.printf("min = %.4f, max = %.4f%n", minValue, maxValue);
        System.out.printf("Среднее = %.4f%n", sampleMean);
        System.out.printf("Медиана = %.4f%n", median);
        System.out.printf("Дисперсия s^2 = %.4f%n", sampleVariance);
        System.out.printf("СКО s = %.4f%n", sampleStdDev);
        System.out.printf("Асимметрия = %.4f%n", skewness);
        System.out.printf("Эксцесс = %.4f%n", excessKurtosis);

        // Гистограмма
        int binCount = 9;
        double binWidth = (maxValue - minValue) / binCount;
        int[] frequencies = new int[binCount];
        for (double x : data) {
            int bin = Math.min((int) ((x - minValue) / binWidth), binCount - 1);
            frequencies[bin]++;
        }
        System.out.printf("%nШирина интервала: %.4f%n", binWidth);
        for (int i = 0; i < binCount; i++) {
            double lowerBound = minValue + i * binWidth;
            double upperBound = lowerBound + binWidth;
            double relativeFrequency = (double) frequencies[i] / sampleSize;
            StringBuilder bar = new StringBuilder();
            for (int j = 0; j < (int) (relativeFrequency * 100); j++) {
                bar.append('█');
            }
            System.out.printf("[%7.2f, %7.2f) n=%3d w=%.3f %s%n",
                    lowerBound, upperBound, frequencies[i], relativeFrequency, bar);
        }
    }
}
