# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project overview

Russian-language mathematical statistics coursework. Solutions implemented in Java with Apache POI for data access. Each task (`docs/1_5.md`–`docs/12.md`) contains mathematical analysis with embedded Java code.

## Project structure (Maven)

```
lab_terver2/
├── pom.xml
├── docs/                          # Решения .md
│   ├── 1_5.md .. 12.md
├── src/main/java/lab/terver/      # Исходный код
│   ├── DataReader.java
│   └── Task*.java
├── src/main/resources/
│   └── data.xlsx
└── src/test/java/lab/terver/      # Тесты (пусто)
```

## Data file

`src/main/resources/data.xlsx` — 200 rows x 6 columns on sheet `Лист1`. Each column is an independent dataset drawn from one of four continuous distributions: normal, uniform on [-1, 2θ], gamma-like (θ² x e^{-θx}), or quadratic on [0, θ]. Tasks reference selecting one of the 6 columns for analysis.

## Task structure (from tasks.md)

**Section 1 — Interval Estimation** (tasks 1.5, 2.3, 3.4, 4.4): Confidence intervals for mean (normal, left-sided), variance (normal), proportion (Bernoulli), and interpreting confidence levels.

**Section 2 — Hypothesis Testing** (tasks 5.4, 6.2): Chi-squared test for variance (normal, one-sided) and chi-squared goodness-of-fit for multinomial proportions.

**Section 3 — Full Data Analysis** (tasks 7-12): For a chosen continuous dataset:
7. Descriptive statistics + histogram → guess distribution family
8. Test for randomness (runs test or inversions test, α=0.05)
9. Estimate parameters via MLE
10. Overlay fitted density on histogram
11. Pearson chi-squared goodness-of-fit test (α=0.1)
12. Split sample, Mann-Whitney homogeneity test (α=0.01)

## Key conventions for solutions

- Each task must state notation, the main formula, and the assumptions under which it's valid
- Sections 1 and 2 require explicit hypotheses (H₀, H₁) and test selection rationale
- Section 3 requires all intermediate calculations shown

## Common libraries (Java)

- Apache POI (`org.apache.poi:poi-ooxml:5.2.5`) — чтение `data.xlsx`
- `java.lang.Math` — математические функции
- `DataReader.java` — утилита для загрузки данных из xlsx (classpath)

## Build

```bash
mvn compile
mvn exec:java -Dexec.mainClass="lab.terver.Task7"
```

## DataReader usage

```java
// Чтение из classpath (src/main/resources/data.xlsx)
double[] data = DataReader.readColumn("data.xlsx", "Лист1", 4); // столбец E
```

## Solution formatting rules

- **Каждая задача — в отдельный файл**: `1_5.md`, `2_3.md`, `3_4.md`, `4_4.md`, `5_4.md`, `6_2.md`, `7.md`, `8.md`, `9.md`, `10.md`, `11.md`, `12.md`
- **Максимально подробные объяснения**: каждый шаг решения должен быть пояснён — почему выбрана та или иная формула, почему применим тот или иной статистический тест, как интерпретируется результат
- **Альтернативные решения**: если для задачи существует альтернативный метод решения (например, точный тест вместо асимптотического, другой тип доверительного интервала), привести его с пояснением различий
- **Структура каждого файла**: обозначения → предположения → основные формулы → расчёты → вывод
- **Проверка вычислений**: для задач, решаемых с помощью Java, приводить и аналитический вывод, и код
