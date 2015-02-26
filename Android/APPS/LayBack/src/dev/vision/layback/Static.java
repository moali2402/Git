package dev.vision.layback;

import dev.vision.layback.classes.Contacts;
import dev.vision.layback.classes.ContactsMap;
import dev.vision.layback.classes.Number;
import dev.vision.layback.classes.User;

public class Static {

    static Contacts Contacts = new Contacts();
    static User ME = new User("MO");
    static{
    	ME.setNumber(new Number("Default", "00201020966997"));
    }
}
