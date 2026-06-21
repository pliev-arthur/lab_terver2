package lab.terver;

/** Задача 10: Гистограмма с наложенной теоретической плотностью (столбец 5). */
public class Task10 {
    /** Плотность нормального распределения. */
    static double normPdf(double x, double a, double sigma) {
        double z = (x - a) / sigma;
        return Math.exp(-0.5 * z * z) / (sigma * Math.sqrt(2 * Math.PI));
    }

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

        // Гистограмма
        int k = 9;
        double xMin = Double.MAX_VALUE, xMax = Double.MIN_VALUE;
        for (double x : data) {
            if (x < xMin) xMin = x;
            if (x > xMax) xMax = x;
        }
        double h = (xMax - xMin) / k;
        int[] counts = new int[k];
        for (double x : data) {
            int idx = Math.min((int) ((x - xMin) / h), k - 1);
            counts[idx]++;
        }

        System.out.println("Гистограмма и теоретическая плотность:");
        System.out.printf("%-22s %8s %8s %8s%n", "Интервал", "h_гист", "h_теор", "f(серед)");
        System.out.println("-".repeat(46));
        for (int i = 0; i < k; i++) {
            double lo = xMin + i * h;
            double hi = lo + h;
            double mid = (lo + hi) / 2;
            double hHist = counts[i] / (n * h);
            double fTheory = normPdf(mid, xBar, s);
            StringBuilder bar = new StringBuilder();
            for (int j = 0; j < (int) (hHist * 200); j++) bar.append('█');
            System.out.printf("[%7.2f, %7.2f)  %8.4f  %8.4f  %s%n", lo, hi, hHist, fTheory, bar);
        }

        // Сравнение частот: наблюдаемые vs ожидаемые
        System.out.printf("%nСравнение наблюдаемых и ожидаемых частот:%n");
        System.out.printf("%-22s %8s %8s %8s%n", "Интервал", "Obs n_i", "Exp n_i", "Diff");
        for (int i = 0; i < k; i++) {
            double lo = xMin + i * h;
            double hi = lo + h;
            double mid = (lo + hi) / 2;
            double expN = n * h * normPdf(mid, xBar, s);
            System.out.printf("[%7.2f, %7.2f)  %8d  %8.1f  %8.1f%n",
                              lo, hi, counts[i], expN, counts[i] - expN);
        }
    }
}
