package com.eaw1805.www.controllers;

import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Simple central controllers.
 */
public class SimpleController implements Controller {

    public org.springframework.web.servlet.ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final org.springframework.web.servlet.ModelAndView mav = new org.springframework.web.servlet.ModelAndView("index");
        return mav;
    }
}
