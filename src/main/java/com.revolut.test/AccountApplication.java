package com.revolut.test;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import ru.vyarus.dropwizard.guice.GuiceBundle;


public class AccountApplication extends Application<AccountConfiguration> {

    public static void main(String[] args) throws Exception {
        new AccountApplication().run("server");
    }

    @Override
    public void initialize(Bootstrap<AccountConfiguration> bootstrap) {
        bootstrap.addBundle(GuiceBundle.builder()
                .enableAutoConfig("com.revolut.test")
                .build());
    }

    @Override
    public void run(AccountConfiguration accountConfiguration, Environment environment) throws Exception {}

}
