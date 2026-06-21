package lab.terver;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Вспомогательный класс для чтения числовых данных из xlsx-файла.
 * Использует Apache POI.
 */
public class DataReader {

    /**
     * Читает числовые значения из указанного столбца листа xlsx-файла.
     * Файл ищется в classpath (src/main/resources/).
     *
     * @param resourcePath путь к ресурсу (например "data.xlsx")
     * @param sheetName    имя листа
     * @param colIndex     индекс столбца (0 = A, 1 = B, ..., 5 = F)
     * @return массив прочитанных значений
     */
    public static double[] readColumn(String resourcePath, String sheetName, int colIndex)
            throws Exception {
        List<Double> list = new ArrayList<>();
        InputStream is = DataReader.class.getResourceAsStream("/" + resourcePath);
        if (is == null) {
            throw new IllegalArgumentException("Resource not found: " + resourcePath);
        }
        try (var wb = new XSSFWorkbook(is)) {
            Sheet sheet = wb.getSheet(sheetName);
            for (Row row : sheet) {
                Cell cell = row.getCell(colIndex);
                if (cell != null) {
                    list.add(cell.getNumericCellValue());
                }
            }
        }
        return list.stream().mapToDouble(Double::doubleValue).toArray();
    }
}
