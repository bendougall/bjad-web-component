package bjad.web;

/**
 * Enum of the various HTTP methods we can use 
 * against the web resources via this component.  
 *
 * @author 
 *  Ben Dougall
 */
public enum HTTPMethodType
{
   /**
    * The GET method requests a representation of the specified resource. Requests using GET should only retrieve data.
    */
   GET,
   /**
    * The POST method is used to submit an entity to the specified resource, 
    * often causing a change in state or side effects on the server.
    */
   POST,
   /**
    * The PUT method replaces all current representations of the target resource with the request payload.
    */
   PUT,
   /**
    * The PATCH method is used to apply partial modifications to a resource.
    */
   PATCH,
   /**
    * The DELETE method deletes the specified resource.
    */
   DELETE,
   /**
    * The OPTIONS method is used to describe the communication options for the target resource.
    */
   OPTIONS,
   /**
    * The HEAD method asks for a response identical to that of a GET request, but without the response body.
    */
   HEAD,
   /**
    * The TRACE method performs a message loop-back test along the path to the target resource.
    */
   TRACE;
}
