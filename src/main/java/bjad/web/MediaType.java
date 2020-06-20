package bjad.web;

/**
 * Media type bean for HTTP calls for the 
 * Content-Type and Accept header values. 
 *
 * @author 
 *  Ben Dougall
 */
public class MediaType
{
   /**
    * Media Type for application/json. 
    */
   public static final MediaType JSON = new MediaType("application/json");
   
   /**
    * Media type for binary data.
    */
   public static final MediaType BINARY = new MediaType("application/octet-stream");
   
   /**
    * Media type for HTML content.
    */
   public static final MediaType HTML = new MediaType("text/html");
   
   /**
    * Media type for XML data that as per 
    * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types">
    * online sources</a>, is <i>NOT</i> readable from casual users 
    * (<a href="https://tools.ietf.org/html/rfc3023#section-3">RFC 3023</a>, section 3). 
    */
   public static final MediaType XML_APPLICATION = new MediaType("application/xml");
   
   /**
    * Media type for XML data that as per 
    * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types">
    * online sources</a>, is readable from casual users 
    * (<a href="https://tools.ietf.org/html/rfc3023#section-3">RFC 3023</a>, section 3). 
    */
   public static final MediaType XML_TEXT = new MediaType("text/xml");
   
   /**
    * Media type for ZIP content.
    */
   public static final MediaType ZIP = new MediaType("application/zip");
   
   /**
    * Media type for PDF content
    */
   public static final MediaType PDF = new MediaType("application/pdf");
   
   /** The content type for the media type bean. */
   protected String mediaType;
   /** The character set to apply in the media type string. */
   protected String charset;
   
   /**
    * Constructor setting the media type for 
    * the object.
    *
    * @param mediaType
    *    The media type to set (i.e. application/json)
    */
   public MediaType(String mediaType)
   {
      this.mediaType = mediaType.toLowerCase();
      this.charset = null;
   }
   
   /**
    * Constructor setting the media type and 
    * character set for the object. 
    *
    * @param mediaType
    *    The media type to set (i.e. text/html)
    * @param charset
    *    The character set to use (i.e. UTF-8)
    */
   public MediaType(String mediaType, String charset)
   {
      this.mediaType = mediaType.toLowerCase();
      this.charset = charset;
   }
   
   /**
    * Creates a new media type bean that will include
    * the character set within the object.
    * 
    * @param charset
    *    The character set to apply to the new media type.
    * @return
    *    The new media type with the character set applied to it.
    */
   public MediaType addCharset(String charset)
   {
      return new MediaType(this.mediaType, charset);
   }
   
   /**
    * Gets the content type string for the object
    * 
    * @return
    *    The object's content type.
    */
   public String getContentType()
   {
      return this.mediaType;
   }
   
   /**
    * Gets the charset type (if set) for the object
    * 
    * @return
    *    The object's character set, or null if not set.
    */
   public String getCharset()
   {
      return this.charset;
   }
   
   /**
    * Generates and returns the string for the content type  
    * or accept http header, including the character set if set.
    * 
    * @return
    *    The string for the Content-Type or Accept HTTP header,
    *    example being application/json or text/xml; charset=UTF-8
    */
   @Override
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      sb.append(this.mediaType);
      if (this.charset != null && !this.charset.trim().isEmpty())
      {
         sb.append("; charset=").append(this.charset.toUpperCase());
      }
      return sb.toString();
   }
   
   /**
    * Creates a media type for an image whose file type is 
    * specified by the string passed.
    * 
    * @param format
    *    The type of image (JPEG, GIF, etc...)
    * @return
    *    The constructed media type with the format made into
    *    lower case to follow standards. 
    * @throws IllegalArgumentException
    *    The format of the image cannot be null.
    */
   public static MediaType imageType(String format) throws IllegalArgumentException
   {
      if (format == null || format.trim().isEmpty())
      {
         throw new IllegalArgumentException("Format argument cannot be null.");
      }
      return new MediaType("image/" + format.toLowerCase());
   }   
}
