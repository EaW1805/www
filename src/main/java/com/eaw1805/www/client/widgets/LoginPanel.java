package com.eaw1805.www.client.widgets;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoginPanel extends FormPanel {

    public LoginPanel() {

//        setEncoding(FormPanel.ENCODING_MULTIPART);
        setMethod(FormPanel.METHOD_POST);


        VerticalPanel holder = new VerticalPanel();

        holder.add(new Label("Username"));
        TextBox username = new TextBox();
        username.setName("j_username");
        holder.add(username);


        holder.add(new Label("Password"));
        PasswordTextBox passwd = new PasswordTextBox();
        passwd.setName("j_password");
        holder.add(passwd);
        holder.add(new SubmitButton("Submit", new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                submit();
            }
        }));
        add(holder);
        setAction(GWT.getHostPageBaseURL() + "/j_spring_security_check");
        addSubmitCompleteHandler(new SubmitCompleteHandler() {
            @Override
            public void onSubmitComplete(SubmitCompleteEvent event) {
                Window.alert(event.getResults());
            }
        });
    }
}
