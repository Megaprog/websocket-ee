package org.jmmo.util;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.util.ResourceBundle;

@Singleton
public class Localization {

    private ResourceBundle resourceBundle;

    public String localize(String code) {
        return resourceBundle.getString(code);
    }

    @PostConstruct
    public void init() {
        resourceBundle = ResourceBundle.getBundle("org.jmmo.i18n.messages");
    }
}
