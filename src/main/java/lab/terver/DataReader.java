package lab.terver;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataReader {

    public static double[] readColumn(String resourcePath, String sheetName, int columnIndex)
            throws Exception {
        List<Double> values = new ArrayList<>();
        InputStream inputStream = DataReader.class.getResourceAsStream("/" + resourcePath);
        if (inputStream == null) {
            throw new IllegalArgumentException("Resource not found: " + resourcePath);
        }
        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheet(sheetName);
            for (Row row : sheet) {
                Cell cell = row.getCell(columnIndex);
                if (cell != null) {
                    values.add(cell.getNumericCellValue());
                }
            }
        }
        return values.stream().mapToDouble(Double::doubleValue).toArray();
    }

    /**
     * Автоматически выбирает столбец для анализа (задачи 7–12):
     *  1. Загружает все 6 столбцов из xlsx
     *  2. Отбрасывает дискретные (число уникальных значений < 100)
     *  3. Среди оставшихся непрерывных выбирает с |асимметрией|,
     *     наиболее близкой к 0 (похож на нормальное распределение)
     *
     * @param resourcePath путь к xlsx (classpath)
     * @param sheetName    имя листа
     * @return массив double[] выбранного столбца
     */
    public static double[] selectColumnForAnalysis(String resourcePath, String sheetName)
            throws Exception {
        final int columnCount = 6;
        List<double[]> continuousColumns = new ArrayList<>();
        List<Integer> continuousIndices = new ArrayList<>();

        // Шаг 1: загрузка всех столбцов, отсев дискретных
        for (int col = 0; col < columnCount; col++) {
            double[] data = readColumn(resourcePath, sheetName, col);
            long uniqueCount = Arrays.stream(data).distinct().count();
            if (uniqueCount >= 100) {
                continuousColumns.add(data);
                continuousIndices.add(col + 1); // 1-based номер столбца
            }
        }

        if (continuousColumns.isEmpty()) {
            throw new IllegalStateException("Не найдено непрерывных столбцов в файле данных");
        }

        // Шаг 2: выбор столбца с асимметрией, наиболее близкой к 0
        int bestIndex = 0;
        double bestAbsSkewness = Double.MAX_VALUE;
        double[] bestSkewnessValues = new double[continuousColumns.size()];

        for (int i = 0; i < continuousColumns.size(); i++) {
            double[] data = continuousColumns.get(i);
            double mean = mean(data);
            double stdDev = Math.sqrt(variance(data, mean, data.length));
            double thirdMoment = 0;
            for (double x : data) {
                thirdMoment += Math.pow((x - mean) / stdDev, 3);
            }
            double skewness = thirdMoment / data.length;
            bestSkewnessValues[i] = skewness;
            if (Math.abs(skewness) < bestAbsSkewness) {
                bestAbsSkewness = Math.abs(skewness);
                bestIndex = i;
            }
        }

        System.out.println("=== Автоматический выбор столбца ===");
        for (int i = 0; i < continuousColumns.size(); i++) {
            String marker = (i == bestIndex) ? " <-- выбран" : "";
            System.out.printf("  Столбец %d (n=%d): асимметрия = %+.4f%s%n",
                    continuousIndices.get(i), continuousColumns.get(i).length,
                    bestSkewnessValues[i], marker);
        }
        if (continuousColumns.size() == 1) {
            System.out.println("  (единственный непрерывный столбец, выбран автоматически)");
        }
        System.out.println();

        return continuousColumns.get(bestIndex);
    }

    private static double mean(double[] data) {
        double sum = 0;
        for (double x : data) sum += x;
        return sum / data.length;
    }

    private static double variance(double[] data, double mean, int n) {
        double sumSq = 0;
        for (double x : data) sumSq += (x - mean) * (x - mean);
        return sumSq / n;
    }
}
