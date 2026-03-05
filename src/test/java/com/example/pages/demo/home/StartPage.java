package com.example.pages.demo.home;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

@Slf4j
public class StartPage {

    private final Page page;
    @Getter
    private final String expectedPageTitle = "Home | E-Finance | PostFinance (Demo)";
    private final StartPageLocators homePageLocators;

    @Inject
    public StartPage(Page page) {
        this.page = page;
        homePageLocators = new StartPageLocators(page);
    }

    public StartPage navigateTo(String url) {
        log.info("Navigating to URL: '{}'", url);
        page.navigate(url);
        try {
            var companyBrandImgLocator = homePageLocators.getCompanyBrandImgLocator();
            companyBrandImgLocator.waitFor(
                    new Locator.WaitForOptions().setTimeout(45000)
            );
            log.info("==> Company Brand Image is visible {}", companyBrandImgLocator.isVisible());
            return companyBrandImgLocator.isVisible() ? this : null;
        } catch (Exception e) {
            log.error("Failed to wait for company brand image, URL='{}'", page.url(), e);
            // Nur im CI: Screenshot machen und als Artifact sammeln
            try {
                page.screenshot(new Page.ScreenshotOptions()
                        .setPath(java.nio.file.Paths.get("artifacts/startpage-timeout.png"))
                        .setFullPage(true));
            } catch (Exception ignored) {}
            throw e;
        }
    }

}
