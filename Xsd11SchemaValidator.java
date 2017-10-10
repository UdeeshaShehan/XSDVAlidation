/*
 * Xsd11SchemaValidator.java
 * Copyright (c) 2013 by Dr. Herong Yang, herongyang.com
 */
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;
import javax.xml.XMLConstants;
import javax.xml.transform.sax.SAXSource;
import org.xml.sax.InputSource;
import javax.xml.validation.Validator;
import java.io.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;
class Xsd11SchemaValidator {
  private static int errorCount = 0;
  public static void main(String[] a) {
    if (a.length<2) {
      System.out.println("Usage:");
      System.out.println("java Xsd11SchemaValidator schema_file_name "
        + "xml_file_name");
    } else {
      String schemaName = a[0];
      String xmlName = a[1];
      Schema schema = loadSchema(schemaName);
      validateXml(schema, xmlName);
    }
  }
  public static void validateXml(Schema schema, String xmlName) {
    try {
      // creating a Validator instance
      Validator validator = schema.newValidator();

      // setting my own error handler
      validator.setErrorHandler(new MyErrorHandler());

      // preparing the XML file as a SAX source
      SAXSource source = new SAXSource(
        new InputSource(new java.io.FileInputStream(xmlName)));

      // validating the SAX source against the schema
      validator.validate(source);
      System.out.println();
      if (errorCount>0) {
        System.out.println("Failed with errors: "+errorCount);
      } else {
        System.out.println("Passed.");
      } 

    } catch (Exception e) {
      // catching all validation exceptions
      System.out.println();
      System.out.println(e.toString());
    }
  }
  public static Schema loadSchema(String name) {
    Schema schema = null;
    try {
//      String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
      String language = "http://www.w3.org/XML/XMLSchema/v1.1";
      SchemaFactory factory = SchemaFactory.newInstance(language);
      schema = factory.newSchema(new File(name));
    } catch (Exception e) {
      System.out.println(e.toString());
    }
    return schema;
  }
  private static class MyErrorHandler implements ErrorHandler {
    public void warning(SAXParseException e) throws SAXException {
       System.out.println("Warning: "); 
       printException(e);
    }
    public void error(SAXParseException e) throws SAXException {
       System.out.println("Error: "); 
       printException(e);
    }
    public void fatalError(SAXParseException e) throws SAXException {
       System.out.println("Fattal error: "); 
       printException(e);
    }
    private void printException(SAXParseException e) {
      errorCount++;
      System.out.println("   Line number: "+e.getLineNumber());
      System.out.println("   Column number: "+e.getColumnNumber());
      System.out.println("   Message: "+e.getMessage());
      System.out.println();
    }
  }
}