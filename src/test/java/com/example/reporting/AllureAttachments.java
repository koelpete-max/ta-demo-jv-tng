package com.example.reporting;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class AllureAttachments {

    private AllureAttachments() {}

    @Attachment(value = "{name}", type = "image/png")
    public static byte[] attachPng(byte[] screenshot, String name) {
        return screenshot;
    }

    public static void attachScreenshot(Path path, String name) {
        try {
            byte[] bytes = Files.readAllBytes(path);
            attachPng(bytes, name);
        } catch (Exception ignored) {
        }
    }
}