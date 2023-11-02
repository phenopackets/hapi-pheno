package org.monarchinitiative.phenoex.model;

import java.util.List;

public interface _Template {


    /*
    // Same for primitives

    Object getTemplateProperty();  // Usual getter
    void setTemplateProperty(Object tire);  // Usual setter
    Object cgetTemplateProperty();  // This is create or get
    Object csetTemplateProperty(Object tire); // this is a chaining setter

     */

    Object getTemplateProperty();
    void setTemplateProperty(Object object);
    Object cgetTemplateProperty();
    _Template csetTemplateProperty(Object object);



    /*

    // For lists as long as it follows this template

    List<Object> getTemplateList();  // getter
    void setTemplateList(List<Object> list); // setter
    List<Object> cgetTemplateList(); // create or get the List itself
    _Template csetTemplateList(List<Object> list); // set the list and return self
    Object addTemplateList();  // Add an instance of the list type  and return it
    _Template addTemplateList(Object object);  // add the specified instance and return self.

     */

    List<Object> getTemplateList();
    void setTemplateList(List<Object> list);
    List<Object> cgetTemplateList();
    _Template csetTemplateList(List<Object> list);
    Object addTemplateList();
    _Template addTemplateList(Object object);

}
