package bjad.web.body;

import java.io.File;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;

import bjad.web.MediaType;

/**
 * Bean used for the File, Byte Array, or Input Stream
 * upload options within the Web Component. 
 *
 * @author 
 *    Ben Dougall
 *  
 * @param <T> 
 *    The type of object the bean contains. Should be
 *    File, InputStream, or byte[].
 */
public class FileUploadBody<T> extends AbstractBodyModel
{
   /**
    * The content type being sent. 
    */
   protected MediaType mediaType;
   /**
    * The file, byte[], or the input stream to upload 
    * within the Web operation.
    */
   protected T body;
   
   /**
    * Constructor, setting the body for the request.
    * 
    * @param body
    *    The body for the request.
    */
   public FileUploadBody(T body)
   {
      this(null, body);
   }
   
   /**
    * Constructor, setting both the media type and 
    * the body for the request.
    * 
    * @param mediaType
    *    The type of content to send.
    * @param body
    *    The body for the request.
    */
   public FileUploadBody(MediaType mediaType, T body)
   {
      this.setMediaType(mediaType);
      this.setBody(body);
   }

   /**
    * @return 
    *   The mediaType property within the AbstractFileUploadBody instance
    */
   public MediaType getMediaType()
   {
      return this.mediaType;
   }

   /**
    * @param mediaType 
    *   The mediaType to set within the AbstractFileUploadBody instance
    */
   public void setMediaType(MediaType mediaType)
   {
      this.mediaType = mediaType;
   }

   /**
    * @return 
    *   The body property within the AbstractFileUploadBody instance
    */
   public T getBody()
   {
      return this.body;
   }

   /**
    * @param body 
    *   The body to set within the AbstractFileUploadBody instance
    */
   public void setBody(T body)
   {
      this.body = body;
   }

   /**
    * Returns the HTTP entity for the body bean passed.
    * 
    * @return
    *    The HTTP Entity for the file upload operation
    *    with the body provided.
    * @throws IllegalArgumentException
    *    If the body is not a File, InputStream, or byte array,
    *    this exception will be thrown.
    */
   public HttpEntity getEntity() throws IllegalArgumentException
   {
      ContentType ct = mediaType != null ?
            ContentType.create(mediaType.getContentType(), mediaType.getCharset()) :
            null;
            
      if (body instanceof File)
      {
         File f = (File)body;
         return ct != null ? new FileEntity(f, ct) : new FileEntity(f);
      }
      else if (body instanceof byte[])
      {
         byte[] b = (byte[])body;
         return ct != null ? new ByteArrayEntity(b, ct) : new ByteArrayEntity(b);
      }
      else if (body instanceof InputStream)
      {
         InputStream is = (InputStream)body;
         return ct != null ? new InputStreamEntity(is, ct) : new InputStreamEntity(is);
      }
      else
      {
         throw new IllegalArgumentException("Body must be a java.io.File, java.io.InputStream, or a byte array.");
      }
   }
   
   /**
    * Returns a string representing the bean. 
    * @see java.lang.Object#toString()
    * @return
    *    The string representing the bean.
    */
   @Override
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      sb.append("Body being applied with a ");
      if (body instanceof File)
      {
         sb.append("File (\"").append((((File)body).getAbsolutePath())).append("\")");
      }
      else if (body instanceof InputStream)
      {
         sb.append("InputStream");
      }
      else if (body instanceof byte[])
      {
         sb.append("Byte array (\"").append((((byte[])body).length)).append(" bytes in length\")");
      }
      else if (body == null)
      {
         sb.append("no body data");
      }
      else
      {
         sb.append("unsupported data type (").append(body.getClass().getCanonicalName()).append(")");
         return sb.toString();
      }
      
      // Add the media type string if the media type was set.
      if (getMediaType() != null)
      {
         sb.append(" with media type ").append(mediaType.toString());
      }
      return sb.toString();
   }

   /**
    * @see bjad.web.body.AbstractBodyModel#getLogString()
    */
   @Override
   public String getLogString()
   {
      return this.toString();
   }
}
