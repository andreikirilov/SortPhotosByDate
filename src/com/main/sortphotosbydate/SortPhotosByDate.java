package com.main.sortphotosbydate;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class SortPhotosByDate {

    private static String mainPath;
    public static boolean subfolder = true;

    public static void startSortPhotoByDate(String appPath) {
        mainPath = appPath + "\\";
        Interface.jTextArea.setText("");
        Interface.jProgressBar.setValue(0);
        Interface.jTextArea.append("Сортировка по дате съемки запущена в папке: \"" + appPath + "\"\n");
        sortPhotoByDate(appPath + "\\");
        Interface.jProgressBar.setValue(100);
        Interface.jTextArea.append("Сортировка по дате съемки завершена!");
    }

    private static void sortPhotoByDate(String folderPath) {
        int current = 0;
        File[] listOfFiles = new File(folderPath).listFiles();
        for (int i = 0; i < (listOfFiles != null ? listOfFiles.length : 0); i++) {
            current = current + 100 / (listOfFiles.length);
            Interface.jProgressBar.setValue(current);
            File currentFile = listOfFiles[i];
            String fileName = currentFile.getName();
            if (getFileExtension(fileName).toLowerCase().equals("jpg") || getFileExtension(fileName).toLowerCase().equals("jpeg")) {
                File file = new File(folderPath + fileName);
                File destFolder;
                try {
                    Metadata metadata = ImageMetadataReader.readMetadata(file);
                    Directory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
                    try {
                        destFolder = new File(mainPath + new SimpleDateFormat("MM.dd.yy").format(directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)));
                    } catch (NullPointerException ignored) {
                        destFolder = new File(mainPath + "Бездатные");
                    }
                    makeDirectory(destFolder);
                    if (!file.renameTo(new File(destFolder, fileName))) {
                        destFolder = new File(mainPath + "Копии");
                        makeDirectory(destFolder);
                        while (!file.renameTo(new File(destFolder, fileName))) {
                            destFolder = new File(destFolder + "\\Копии");
                            makeDirectory(destFolder);
                        }
                        Interface.jTextArea.append("Фото \"" + fileName + "\" перемещено в папку: \"" + destFolder + "\"\n");
                    } else {
                        Interface.jTextArea.append("Фото \"" + fileName + "\" перемещено в папку: \"" + destFolder + "\"\n");
                    }
                } catch (ImageProcessingException | IOException ignored) {
                    destFolder = new File(mainPath + "Поврежденные");
                    makeDirectory(destFolder);
                    while (!file.renameTo(new File(destFolder, fileName))) {
                        destFolder = new File(destFolder + "\\Копии");
                        makeDirectory(destFolder);
                        Interface.jTextArea.append("Поврежденный файл \"" + fileName + "\" перемещен в папку: \"" + destFolder + "\"\n");
                    }
                }
            } else if (subfolder && (currentFile.isDirectory())) {
                Interface.jTextArea.append("Сортировка продолжится в папке: \"" + currentFile + "\"\n");
                sortPhotoByDate(currentFile.getPath() + "\\");
                try {
                    if (Objects.requireNonNull(currentFile.list()).length == 0) {
                        Files.delete(currentFile.toPath());
                        Interface.jTextArea.append("Удалена пустая папка: \"" + currentFile + "\"\n");
                    }
                } catch (Exception ignored) {
                    Interface.jTextArea.append("Возникла непредвиденная ошибка!\n");
                }
            }
        }
    }

    private static String getFileExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        return index == -1 ? "" : fileName.substring(index + 1);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void makeDirectory(File destFolder) {
        if (!destFolder.exists()) {
            destFolder.mkdir();
        }
    }
}