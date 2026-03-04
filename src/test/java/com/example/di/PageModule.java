package com.example.di;

import com.example.core.TestContext;
import com.example.pages.demo.home.StartPage;
import dagger.Module;
import dagger.Provides;

@Module
public class PageModule {

    @Provides
    StartPage provideStartPage(TestContext ctx) {
        return new StartPage(ctx.getPage());
    }
}