package it.allergydb.firebase4j.util;

import it.allergydb.firebase4j.error.FirebaseException;
import it.allergydb.firebase4j.error.JacksonUtilityException;

import java.io.UnsupportedEncodingException;

public class Prova {
     

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        try {
            String json = FirebaseWrapperUtil.getJSON(new Filter());
            System.out.println(json);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FirebaseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
