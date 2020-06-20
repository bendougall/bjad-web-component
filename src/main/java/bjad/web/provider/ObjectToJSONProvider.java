package bjad.web.provider;

import com.google.gson.Gson;

import bjad.web.BJADWebException;

/**
 * Provider interface for GSON implementation to
 * apply to the WebOperationRequest's in
 * order to set the GSON implementation prior
 * to the web call. 
 *
 * @author 
 *  Ben Dougall
 */
public interface ObjectToJSONProvider
{
   /**
    * Provides the GSON implementation to apply to the 
    * WebOperationRequest prior to the web
    * operation occurring.
    * 
    * @return
    *    The GSON implementation for the WebOperationRequest
    * @throws BJADWebException
    *    Any exceptions in generating the GSON implementation
    *    will be thrown.
    */
   public Gson getObjectToJSONMapper() throws BJADWebException;
}
