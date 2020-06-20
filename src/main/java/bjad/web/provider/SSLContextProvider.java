package bjad.web.provider;

import javax.net.ssl.SSLContext;

import bjad.web.BJADWebException;

/**
 * Provider interface for SSL Context to
 * apply to the WebOperationRequest's in
 * order to set the SSL Context prior
 * to the web call. 
 *
 * @author 
 *  Ben Dougall
 */
public interface SSLContextProvider
{
   /**
    * Provides the SSL Context to apply to the 
    * WebOperationRequest prior to the web
    * operation occurring.
    * 
    * @return
    *    The SSL Context for the WebOperationRequest
    * @throws BJADWebException
    *    Any exceptions in generating the SSLContext
    *    will be thrown.
    */
   public SSLContext getSSLContext() throws BJADWebException;
}
