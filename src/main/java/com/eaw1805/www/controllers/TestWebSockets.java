package com.eaw1805.www.controllers;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Testing web sockets.
 */
public class TestWebSockets extends AbstractController {
    @Override
    protected ModelAndView handleRequestInternal(final HttpServletRequest servletRequest,
                                                 final HttpServletResponse servletResponse) throws Exception {
        return new ModelAndView("testWebSockets");
    }
}
