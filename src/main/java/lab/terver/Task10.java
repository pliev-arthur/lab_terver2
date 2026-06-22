package lab.terver;

/** Задача 10: Гистограмма с наложенной теоретической плотностью. */
public class Task10 {

    /** Плотность нормального распределения. */
    static double normalPdf(double x, double mean, double stdDev) {
        double z = (x - mean) / stdDev;
        return Math.exp(-0.5 * z * z) / (stdDev * Math.sqrt(2 * Math.PI));
    }

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
        double sampleVariance = sumSquaredDiffs / (sampleSize - 1);
        double sampleStdDev = Math.sqrt(sampleVariance);

        // Гистограмма
        int binCount = 9;
        double minValue = Double.MAX_VALUE;
        double maxValue = Double.MIN_VALUE;
        for (double x : data) {
            if (x < minValue) minValue = x;
            if (x > maxValue) maxValue = x;
        }
        double binWidth = (maxValue - minValue) / binCount;
        int[] frequencies = new int[binCount];
        for (double x : data) {
            int binIndex = Math.min((int) ((x - minValue) / binWidth), binCount - 1);
            frequencies[binIndex]++;
        }

        System.out.println("Гистограмма и теоретическая плотность:");
        System.out.printf("%-22s %8s %8s %8s%n",
                "Интервал", "h_гист", "h_теор", "f(серед)");
        System.out.println("-".repeat(46));
        for (int i = 0; i < binCount; i++) {
            double lowerBound = minValue + i * binWidth;
            double upperBound = lowerBound + binWidth;
            double midpoint = (lowerBound + upperBound) / 2;
            double histogramDensity = frequencies[i] / (sampleSize * binWidth);
            double theoreticalDensity = normalPdf(midpoint, sampleMean, sampleStdDev);
            StringBuilder bar = new StringBuilder();
            for (int j = 0; j < (int) (histogramDensity * 200); j++) {
                bar.append('█');
            }
            System.out.printf("[%7.2f, %7.2f)  %8.4f  %8.4f  %s%n",
                    lowerBound, upperBound, histogramDensity, theoreticalDensity, bar);
        }

        // Сравнение частот: наблюдаемые vs ожидаемые
        System.out.printf("%nСравнение наблюдаемых и ожидаемых частот:%n");
        System.out.printf("%-22s %8s %8s %8s%n",
                "Интервал", "Obs n_i", "Exp n_i", "Diff");
        for (int i = 0; i < binCount; i++) {
            double lowerBound = minValue + i * binWidth;
            double upperBound = lowerBound + binWidth;
            double midpoint = (lowerBound + upperBound) / 2;
            double expectedCount = sampleSize * binWidth
                    * normalPdf(midpoint, sampleMean, sampleStdDev);
            System.out.printf("[%7.2f, %7.2f)  %8d  %8.1f  %8.1f%n",
                    lowerBound, upperBound, frequencies[i], expectedCount,
                    frequencies[i] - expectedCount);
        }
    }
}
