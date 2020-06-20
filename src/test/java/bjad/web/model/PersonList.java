package bjad.web.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean of a list of Person beans.
 *
 * @author 
 *  Ben Dougall
 */
public class PersonList
{
   private List<Person> persons = new ArrayList<Person>();

   /**
    * @return 
    *   The persons property within the PersonList instance
    */
   public List<Person> getPersons()
   {
      return this.persons;
   }

   /**
    * @param persons 
    *   The persons to set within the PersonList instance
    */
   public void setPersons(List<Person> persons)
   {
      this.persons = persons;
   }
   
}
