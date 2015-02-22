package com.eaw1805.www.controllers.remote;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Implementation of a GWT RPC controller.
 */
public class GwtRpcController
        extends RemoteServiceServlet
        implements Controller, ServletContextAware {

    private static final long serialVersionUID = 4705796962390612185L;

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(GwtRpcController.class);

    private ServletContext servletContext;

    private RemoteService remoteService;

    @SuppressWarnings({"rawtypes", "restriction"})
    private Class remoteServiceClass;

    public ModelAndView handleRequest(final HttpServletRequest request,
                                      final HttpServletResponse response) throws Exception {
        LOGGER.debug("Invoked ----------------");
        super.doPost(request, response);
        return null;
    }


    public String processCall(final String payload) throws SerializationException {
        try {
            LOGGER.debug("Invoked ----------------[" + payload + "]");
            RPCRequest rpcRequest = RPC.decodeRequest(payload, this.remoteServiceClass);

            // delegate work to the spring injected service
            return RPC.invokeAndEncodeResponse(this.remoteService, rpcRequest.getMethod(), rpcRequest.getParameters());
        } catch (IncompatibleRemoteServiceException ex) {
            LOGGER.fatal("An IncompatibleRemoteServiceException was thrown while processing this call.", ex);
            getServletContext()
                    .log("An IncompatibleRemoteServiceException was thrown while processing this call.", ex);
            return RPC.encodeResponseForFailure(null, ex);
        }
    }


    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(final ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void setRemoteService(final RemoteService remoteService) {
        this.remoteService = remoteService;
        this.remoteServiceClass = this.remoteService.getClass();
    }

}
