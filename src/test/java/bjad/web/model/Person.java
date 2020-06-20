package bjad.web.model;

import java.util.UUID;

/**
 * Bean for the basic object type used by the 
 * fake web server for unit tests. 
 *
 * @author 
 *  Ben Dougall
 */
public class Person
{
   /** 
    * The id for the person bean.
    */
   protected String id;
   /**
    * The person's first name.
    */
   protected String firstName;
   /**
    * The person's last name.
    */
   protected String lastName;
   /**
    * Default Constructor
    */
   public Person()
   {
      this(UUID.randomUUID().toString(), "", "");
   }
   /**
    * The constructor for the name fields
    * @param firstName
    *   The first name
    * @param lastName
    *   The last name
    */
   public Person(String firstName, String lastName)
   {
      this(UUID.randomUUID().toString(), firstName, lastName);
   }
   /**
    * Custom constructor for all fields in the bean.
    * @param id
    *    The id
    * @param firstName
    *    The first name
    * @param lastName
    *    The last name.
    */
   public Person(String id, String firstName, String lastName)
   {
      this.id = id;
      this.firstName = firstName;
      this.lastName = lastName;
   }
   /**
    * @return 
    *   The id property within the Person instance
    */
   public String getId()
   {
      return this.id;
   }
   /**
    * @param id 
    *   The id to set within the Person instance
    */
   public void setId(String id)
   {
      this.id = id;
   }
   /**
    * @return 
    *   The firstName property within the Person instance
    */
   public String getFirstName()
   {
      return this.firstName;
   }
   /**
    * @param firstName 
    *   The firstName to set within the Person instance
    */
   public void setFirstName(String firstName)
   {
      this.firstName = firstName;
   }
   /**
    * @return 
    *   The lastName property within the Person instance
    */
   public String getLastName()
   {
      return this.lastName;
   }
   /**
    * @param lastName 
    *   The lastName to set within the Person instance
    */
   public void setLastName(String lastName)
   {
      this.lastName = lastName;
   }
}
