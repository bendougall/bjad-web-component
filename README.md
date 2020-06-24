# bjad-web-component
BJAD component for completing operations against web servers, web services, and APIs.
**Note**: This guide's code examples assumes a *Person* class exists with members for Id, firstname, and lastname (as well as their getters and setters)

## Executing web operations
The following examples show how the component can be used to call web operations.
Essentially, the steps to take are:
1. Build the request object by:
   - Using properties/property files (see "Requests using properties and property file(s)" section for more information) 
   - Manually building the request object
2. Instantiate the component with the request object by 
   - Constructor arg
   - Calling component.setRequest(BJADWebRequest)
3. Call performWebCall passing the return data type (and body parameters if necessary)
   - If the return type is byte[] or String, no transformation is done on the returned data
       - Any other data type will use the GSON implementation from the request to transform the data returned into the java object type requested.
   - See "Executing operations with a body section" for more details about applying a body to the web operation's request.
4. Get the result of the web operation from the BJADWebResponse bean returned from performWebCall
   - BJADWebResponse.isGoodResponse() will return true if the status code is between 200 and 299
   - BJADWebResponse.getData() will return the data from the web operation (if type is not a byte[] or string, GSON would be used to transform the data into the object requested)
    
### Requests using properties and property file(s)
Using the bjad.web.properties.EnhancedPropertyHelper class, you can create BJADWebRequest beans from properties and/or property files.
The following properties are looked for when this option is used:
- URL = The url to call
- HTTPMethod = The HTTP method to call against the remote web server. Must match one of the options in bjad.web.HTTPMethodType
- ObjectToJSONProvider = the implementation of bjad.web.provider.ObjectToJSONProvider to use that will provide the request with a customized GSON implementation
- SSLContextProvider = the implementation of bjad.web.provider.SSLContextProvider to use that will provide the request with a customized SSL Context to use to call secured web resources
- CharacterSet = The character set to use when building the String from the web operation's response
- ConnectionTimeout = The number of milliseconds that need to elapse when attempting to connect to the web operation before causing a timeout exception
- ReadTimeout = The number of milliseconds that need to elapse after the connection has been made before causing a timeout exception.

Within these properties, the property files call refer to additional property files within the file system or the classpath. Add the following properties to load additional resources (keep the numbering unqiue)
AdditionalPropertyFile.1 = classpath:(class path file path to load)
AdditionalPropertyFile.2 = file:(relative file path to the current working directory, or full file path to somewhere on the file system)

Properties in these files can also refer to other properties within the loaded resources. 

#### Example of using multiple property files to build a request to get a specific Person bean from an API
Parent.properties
```
BaseURL = http://localhost:52526/person
CharacterSet = UTF-8
ConnectionTimeout = 5000
ReadTimeout = 5000
```
GetPerson.properties
```
AdditionalPropertyFile.1 = classpath:Parent.properties

URL = ${BaseURL}/${person.id}
HTTPMethod = GET
```

And the following code could be used to build the request with the URL set to http://localhost:52526/person/1234
```java
Properties p = new Properties();
p.put("person.id", "1234");
props = new EnhancedPropertyHelper();
props.loadProperties(p);
props.loadPropertiesFromClasspathFile("WebProps/GetPerson.properties");
 
BJADWebRequest req = BJADWebRequestFactory.createRequest(props);
```

### Calling the Web Operations
Now that we can create Request objects, the next step is to actually call the Web Operation to do the desired CRUD or other action your application requires. 

#### Example of operations that do not include a body
This is used for the following HTTP Methods: GET (shown below), DELETE, OPTIONS, TRACE, HEAD and TRACE
```java
BJADWebRequest req = new BJADWebRequest();
req.setUrl("https://google.ca");
req.setMethod(HTTPMethodType.GET);

BJADWebComponent component = new BJADWebComponent();
component.setRequest(req);

BJADWebResponse<String> response = component.performWebCall(String.class);
String htmlContent = response.getData();
```

#### Executing operations with a body section
The additional parameter to the performWebCall function is for body options to send to the web server when performing operations. 
The paramter is for an instance of one of the AbstractBodyModel implementations, which include:
- ObjectJSONStringBody, which sets the object the GSON implementation will use to make into a JSON string, and optionally, both 
   - the character set the string will be set to
   - the GSON implementation to use to convert the object to a JSON string 
- StringBody, which sets the string to send, and optionally, the character set for the string being sent
- FormEncodedBody, which sets the body to a URL form encoded string, optionally seting the character to use for the string
- FileUploadBody, which sets the body to the byte[] to send, via the byte[] itself, the file to convert, or the input stream to parse and send. Optionally, the content type of the byte[] can be set. 

##### Example of operations that DO include a body

This is used for the following HTTP Methods: POST (shown below), PUT, PATCH.
```java
BJADWebRequest req = new BJADWebRequest();
req.setUrl("http://localhost:52525/person");
req.setMethod(HTTPMethodType.POST);

Person p = new Person("FirstName", "LastName");

BJADWebComponent component = new BJADWebComponent();
component.setRequest(req);

BJADWebResponse<Person> response = component.performWebCall(Person.class, new ObjectJSONStringBody(p));
Person addedPerson = response.getData(); // This person object would have the ID property set
System.out.println("Person Added, ID generated = " + addedPerson.getId());
```
