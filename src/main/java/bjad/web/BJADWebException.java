package bjad.web;

import java.net.SocketTimeoutException;

/**
 * General exception for the web component when something
 * wrong occurs while completing web calls. 
 *
 * @author 
 *  Ben Dougall
 */
public class BJADWebException extends Exception
{
   /**
    * The strings to look for in the exception messages to 
    * determine if the exception is a timeout exception or not.
    */
   protected static final String[] TIMEOUT_MESSAGE_PORTIONS = new String[] 
         {
               "timeout",
               "timed out",
               "time out"
         };
   
   /**
    * Serial number needed for the serialization warning. 
    */
   private static final long serialVersionUID = 8431404276248499368L;   
  
   /**
    * Creates the exception with the message passed.
    *
    * @param message
    *    The message to include in the exception.
    */
   public BJADWebException(String message)
   {
      super(message);
   }
   
   /**
    * Creates the exception with the cause passed. The 
    * message for the exception will be:
    * "WebException thrown due to (cause's full class name)" 
    *
    * @param cause
    *    The root cause of the exception.
    */
   public BJADWebException(Throwable cause)
   {
      this("BJADWebException thrown due to " + cause.getClass().getCanonicalName(), cause);
   }
   
   /**
    * Creates the exception with the message and cause passed.
    * If the cause is another BJADWebException, the cause will be 
    * rooted down to the passed BJADWebException cause if available. 
    *
    * @param message
    *    The message to include within the exception.
    * @param cause
    *    The cause of the exception.
    */
   public BJADWebException(String message, Throwable cause)
   {
      super(
            message, 
            (
                  cause != null &&
                  cause instanceof BJADWebException
            ) 
            ? 
                  cause.getCause() : // The cause from the BJADWebException passed
                  cause); // The exception passed.
   }
   
   /**
    * Returns true if any of the root causes of the 
    * exception are a socket timeout exception or 
    * contains the either of the following strings,
    * "timed out" or "timeout"
    * 
    * @return
    *    True if any of the root causes of the 
    *    exception are a socket timeout exception or 
    *    contains the either of the following strings,
    *    "timed out" or "timeout". False otherwise.
    */
   public boolean isTimeoutException()
   {
      Throwable cause = this;
      while (cause != null)
      {
         // If the cause is a SocketTimeoutException, return true 
         // right away.
         if (cause instanceof SocketTimeoutException)
         {
            return true;
         }
         
         // Get the message from the exception.
         String message = cause.getMessage();
         // If the message was null, set to a blank string or
         // if not null, use the lowercase version of the string.
         message = message == null ? "" : message.toLowerCase();
         
         // Check the message against the timeout strings array 
         // to see if the exception represents a timeout exception.
         for (String lookFor : TIMEOUT_MESSAGE_PORTIONS)
         {
            if (message.contains(lookFor))
            {
               return true;
            }
         }
         cause = cause.getCause();
      }
      return false;
   }
}
