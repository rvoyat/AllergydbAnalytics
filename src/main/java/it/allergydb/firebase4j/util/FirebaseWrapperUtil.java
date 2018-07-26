package it.allergydb.firebase4j.util;

import it.allergydb.firebase4j.error.FirebaseException;
import it.allergydb.firebase4j.error.JacksonUtilityException;
import it.allergydb.firebase4j.service.Firebase;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 
 * @author rvoyat
 *
 */
public class FirebaseWrapperUtil {

      
    
    public static Map<String,Object> getData(Filter filter) throws FirebaseException, UnsupportedEncodingException{
        Map<String, Object> body = null;
        try {
            String json = getJSON(filter);
            body = JacksonUtility.GET_JSON_STRING_AS_MAP( json );
            
        } catch( JacksonUtilityException jue ) {
            String msg = "unable to convert response-body into map;  "; 
            throw new FirebaseException( msg, jue );
        }
        return body;
    }
    
    
    public static void put(String id,String json) throws FirebaseException, UnsupportedEncodingException{
        Firebase firebase = new Firebase("https://allergy-db.firebaseio.com/"); 
        firebase.put(id,json);
    }

    
    public static String getJSON(Filter filter) throws UnsupportedEncodingException, FirebaseException{
        
     // create the firebase
        // FirebaseResponse response = connect(filter);
        
       // return response.getRawBody();   // the data returned in it's raw-form (ie: JSON)
        Firebase firebase = new Firebase("https://allergy-db.firebaseio.com/"); 
        firebase.addQuery("orderBy", "\"$key\"");
        String start =  filter.getKey_start();
        long limit =  filter.getRange() ;
        firebase.addQuery("startAt", "\""+ start +"\""); 
        //firebase.addQuery("endAt", "\""+ end +"\""); 
        //firebase.addQuery("equalTo", "\"1519652480622\"");
        firebase.addQuery("limitToFirst", ""+limit+"");
        return firebase.getJSON(filter.getPath()); 
    }


    public String getById(String id) throws FirebaseException, UnsupportedEncodingException {
        Firebase firebase = new Firebase("https://allergy-db.firebaseio.com/"); 
        firebase.addQuery("orderBy", "\"$key\""); 
        //firebase.addQuery("startAt", "\""+ start +"\""); 
        //firebase.addQuery("endAt", "\""+ end +"\""); 
         firebase.addQuery("equalTo", "\""+id+"\"");
        //firebase.addQuery("limitToFirst", ""+limit+"");
        return firebase.getJSON(null); 
    }
     
    
     
    
}
