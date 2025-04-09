package org.appium.framework.reports;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.testng.ITestResult;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DashboardLogger {

    private static final String JSON_DIR = System.getProperty("user.dir") + "/reports/json/";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final List<Map<String, Object>> results = new ArrayList<>();

    public static void log(ITestResult result, String platformName, String base64Screenshot) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("testName", result.getMethod().getMethodName());
        data.put("className", result.getTestClass().getName());
        data.put("platform", platformName);
        data.put("status", getStatus(result.getStatus()));
        data.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        data.put("duration", (result.getEndMillis() - result.getStartMillis()) + "ms");
        data.put("reportFileName", ExtentReport.getReportFileName());

        if (result.getThrowable() != null) {
            data.put("error", result.getThrowable().toString());
        }

        if (base64Screenshot != null) {
            data.put("screenshot", base64Screenshot);
        }

        results.add(data);
    }

//    public static void saveToFile() {
//        try {
//            Files.createDirectories(Paths.get(JSON_DIR));
//            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
//            String fileName = "run_" + timestamp + ".json";
//            Path filePath = Paths.get(JSON_DIR + fileName);
//
//            try (FileWriter writer = new FileWriter(filePath.toFile())) {
//                gson.toJson(results, writer);
//            }
//
//            // Update index.json with list of all JSON files
//            try (Stream<Path> files = Files.list(Paths.get(JSON_DIR))) {
//                List<String> jsonFiles = files
//                        .filter(f -> f.toString().endsWith(".json"))
//                        .map(Path::getFileName)
//                        .map(Path::toString)
//                        .collect(Collectors.toList());
//
//                try (FileWriter indexWriter = new FileWriter(JSON_DIR + "index.json")) {
//                    gson.toJson(jsonFiles, indexWriter);
//                }
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void saveToFile() {
        try {
            Files.createDirectories(Paths.get(JSON_DIR));
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String fileName = "run_" + timestamp + ".js";
            Path filePath = Paths.get(JSON_DIR + fileName);

            // Convert results to JS variable format
            String jsVariable = "var testResults = " + gson.toJson(results) + ";";

            try (FileWriter writer = new FileWriter(filePath.toFile())) {
                writer.write(jsVariable);
            }

            // Optional: update index.js with list of JS files
            try (Stream<Path> files = Files.list(Paths.get(JSON_DIR))) {
                List<String> jsFiles = files
                        .filter(f -> f.toString().endsWith(".js"))
                        .map(Path::getFileName)
                        .map(Path::toString)
                        .collect(Collectors.toList());

                String indexJs = "var availableRuns = " + gson.toJson(jsFiles) + ";";
                try (FileWriter indexWriter = new FileWriter(JSON_DIR + "index.js")) {
                    indexWriter.write(indexJs);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String getStatus(int status) {
        switch(status) {
            case ITestResult.SUCCESS:
                return "PASS";
            case ITestResult.FAILURE:
                return "FAIL";
            case ITestResult.SKIP:
                return "SKIPPED";
            default:
                return "UNKNOWN";
        }
    }
}
