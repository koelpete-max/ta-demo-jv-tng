package com.example.tests;

import com.example.base.BaseTest;
import com.example.pages.demo.home.TopbarPanelText;
import com.example.pages.demo.home.TopbarPanelUrl;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MerchantPortalPageBasicTests extends BaseTest {
    private boolean isMerchantPortalPageVisible = false;

    @BeforeMethod
    public void navigateToHomeEFinancePage() {
        navigateToHomePage(BASE_URL);

        isMerchantPortalPageVisible = topbarPanel().clickOnPageItem(
                TopbarPanelText.MERCHANTPORTAL,
                TopbarPanelUrl.getUrl.get(TopbarPanelText.MERCHANTPORTAL)
        );
    }

    @Test
    public void verifyMerchantPortalPageTest() {
        testLog().step("Merchant Portal Page Test");

        testLog().step("Asserting that Portal page is loaded");
        Assert.assertTrue(
                isMerchantPortalPageVisible,
                "Failed to load Merchant Portal Page '" + BASE_URL + "'"
        );

        testLog().step("Merchant Portal Page successfully loaded");
    }
}
