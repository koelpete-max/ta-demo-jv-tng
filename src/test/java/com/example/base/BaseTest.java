package com.example.base;

import com.aventstack.extentreports.ExtentTest;
import com.example.core.TestContext;
import com.example.core.config.TestConfig;
import com.example.core.i18n.Language;
import com.example.di.DaggerTestComponent;
import com.example.di.TestComponent;
import com.example.pages.demo.efinance.EFinanceTopNavigationBar;
import com.example.pages.demo.efinance.transactionoverview.EFinanceTransactionOverviewFrame;
import com.example.pages.demo.home.StartPage;
import com.example.pages.demo.home.TopbarPanel;
import com.example.reporting.ExtentManager;
import com.example.reporting.ReportManager;
import com.example.reporting.TestLogger;
import com.example.utils.EnvConfig;
import com.example.utils.ScreenShotUtil;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

@Slf4j
@Listeners({ com.example.reporting.ExtentTestNgListener.class })
public abstract class BaseTest {

    public Language language;

    protected static final String BASE_URL = EnvConfig.resolveBaseUrl();

    // Thread-lokale Container
    private static final ThreadLocal<TestComponent> TL_DI = new ThreadLocal<>();
    private static final ThreadLocal<TestContext>   TL_CTX = new ThreadLocal<>();
    private static final ThreadLocal<Page>          TL_PAGE = new ThreadLocal<>();

    private static final ThreadLocal<StartPage> TL_HOME = new ThreadLocal<>();
    private static final ThreadLocal<EFinanceTopNavigationBar> TL_NAV = new ThreadLocal<>();
    private static final ThreadLocal<TopbarPanel> TL_TOPBAR = new ThreadLocal<>();
    private static final ThreadLocal<EFinanceTransactionOverviewFrame> TL_FRAME = new ThreadLocal<>();

    private static final ThreadLocal<TestLogger> TL_LOGGER = new ThreadLocal<>();

    protected TestLogger testLog() {
        return TL_LOGGER.get();
    }

    protected TestComponent di()            { return TL_DI.get(); }
    protected TestContext ctx()             { return TL_CTX.get(); }
    protected Page page()                   { return TL_PAGE.get(); }
    protected StartPage homePage()          { return TL_HOME.get(); }
    protected EFinanceTopNavigationBar eFinanceTopNavigationBar() { return TL_NAV.get(); }
    protected TopbarPanel topbarPanel()     { return TL_TOPBAR.get(); }
    protected EFinanceTransactionOverviewFrame eFinanceTransactionOverviewFrame() { return TL_FRAME.get(); }

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        language = TestConfig.language();
        ExtentManager.init();   // einmalig
        log.info("Setting up Test Suite. Used language: {}", language.displayName());
    }

    @BeforeMethod(alwaysRun = true)
    public void setUpTest(Method method) {
        log.info("Thread={} BaseTest instance={}",
                Thread.currentThread().getId(),
                System.identityHashCode(this));

        // DI / Context wie gehabt
        TestComponent di = DaggerTestComponent.factory().create();
        TL_DI.set(di);

        TestContext ctx = di.testContext();
        TL_CTX.set(ctx);
        TL_PAGE.set(ctx.getPage());

        TL_HOME.set(di.startPage());
        TL_NAV.set(di.eFinanceTopNavigationBar());
        TL_TOPBAR.set(di.topbarPanel());
        TL_FRAME.set(di.eFinanceTransactionOverviewFrame());

        // Reporting – pro Thread ein eigener ExtentTest
        ReportManager.startTest(method.getName());
        ExtentTest extentTest = ReportManager.getTest();
        TL_LOGGER.set(new TestLogger(extentTest));

        testLog().step("Test '" + method.getName() + "' started");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownTest(ITestResult result) {
        try {
            TestContext ctx = TL_CTX.get();
            if (ctx != null) ctx.close();
        } finally {
            TL_FRAME.remove();
            TL_TOPBAR.remove();
            TL_NAV.remove();
            TL_HOME.remove();

            TL_PAGE.remove();
            TL_CTX.remove();
            TL_DI.remove();

            TL_LOGGER.remove();      // Logger für diesen Thread freigeben
            ReportManager.unload();  // ThreadLocal<ExtentTest> im ReportManager leeren
        }
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        ReportManager.flushReports();  // schreibt Dateien
    }

    // Helfer
    public String captureScreenshotForListener(String name) {
        return ScreenShotUtil.takeScreenShot(page(), name);
    }

    protected void navigateToHomePage(String url) {
        StartPage start = homePage();
        if (start != null) {
            start.navigateTo(url);
        }
    }
}