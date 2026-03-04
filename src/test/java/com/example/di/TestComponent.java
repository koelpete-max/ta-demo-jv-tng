package com.example.di;

import com.example.core.TestContext;
import com.example.pages.demo.efinance.EFinanceTopNavigationBar;
import com.example.pages.demo.efinance.transactionoverview.EFinanceTransactionOverviewFrame;
import com.example.pages.demo.home.StartPage;
import com.example.pages.demo.home.TopbarPanel;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
        PlaywrightModule.class,
        ConfigModule.class,
        TestContextModule.class,
        PageModule.class
})
public interface TestComponent {

    TestContext testContext();

    StartPage startPage();
    EFinanceTopNavigationBar eFinanceTopNavigationBar();
    TopbarPanel topbarPanel();
    EFinanceTransactionOverviewFrame eFinanceTransactionOverviewFrame();

    @Component.Factory
    interface Factory {
        TestComponent create();
    }
}