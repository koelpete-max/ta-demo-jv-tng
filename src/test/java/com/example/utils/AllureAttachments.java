package com.example.utils;

import com.microsoft.playwright.Page;
import io.qameta.allure.Attachment;

public class AllureAttachments {

    @Attachment(value = "{name}", type = "image/png")
    public static byte[] attachScreenshot(String name, Page page) {
        if (page == null) {
            return new byte[0];
        }
        return page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
    }
}
