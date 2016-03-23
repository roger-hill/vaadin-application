package com.roger;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
@Widgetset("com.roger.MyAppWidgetset")
@Title("Rogers App")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        
        final TextField name = new TextField();
        name.setCaption("Type your date of birth here:");
        name.setInputPrompt("dd/mm/yyyy");
        name.setConverter(new MyDateConverter());

        Button button = new Button("Click Me");
        button.setCaption("My caption");
        button.setDescription("My description");
        button.addClickListener( e -> {
            Date date = null;
            boolean conversionError = false;
            try {
                date = (Date) name.getConvertedValue();
            } catch (Converter.ConversionException ce) {
                conversionError = true;
            }
            if (!conversionError) {
                layout.addComponent(new Label("Thanks " + name.getValue() + ", it works! " + date.toString()));
            } else {
                layout.addComponent(new Label("Date format must be: " + new MyDateConverter().getDateFormatString()));
            }
            Window window = new Window("My Window Title");
            window.center();
            addWindow(window);

        });
        
        layout.addComponents(name, button);
        layout.setMargin(true);
        layout.setSpacing(true);
        
        setContent(layout);
    }

    @WebServlet(urlPatterns = {"/app/*", "/VAADIN/*"}, name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

    private class MyDateConverter extends StringToDateConverter {

        private static final String format = "dd/MM/yyyy";

        @Override
        protected DateFormat getFormat(Locale locale) {

            DateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat;
        }

        public String getDateFormatString() {

            return format;
        }
    }
}
