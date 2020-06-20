package bjad.web;

import javax.net.ssl.SSLContext;

import bjad.web.provider.SSLContextProvider;

/**
 * Provides the SSL Context.
 *
 * @author 
 *  Ben Dougall
 */
public class TestSSLContextProvider implements SSLContextProvider
{
   /**
    * @see bjad.web.provider.SSLContextProvider#getSSLContext()
    */
   @Override
   public SSLContext getSSLContext() throws BJADWebException
   {
      try
      {
         return SSLContext.getDefault();
      }
      catch (Exception ex)
      {
         throw new BJADWebException("Failed to create the default SSL Context", ex);
      }
   }
}
