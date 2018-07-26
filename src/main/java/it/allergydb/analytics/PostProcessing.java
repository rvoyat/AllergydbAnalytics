package it.allergydb.analytics;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.allergydb.analytics.json.Analytics;
import it.allergydb.firebase4j.error.FirebaseException;
import it.allergydb.firebase4j.util.FirebaseWrapperUtil;
import it.allergydb.firebase4j.util.IAllergyConstants;
import it.allergydb.svm.LSVMResult;

/**
 * 
 * @author rvoyat
 *
 */
public class PostProcessing {

    protected static final Logger LOGGER = Logger.getRootLogger();
     
    private FirebaseWrapperUtil firebaseWrapperUtil;
    
    public void aggiornaAnalyticsToFirebase(Analytics analytics,LSVMResult resultSVM) throws UnsupportedEncodingException, JsonProcessingException, FirebaseException{
        
        aggiornaDatiMachineLearnig(analytics,resultSVM);
        
        String json = new ObjectMapper().writeValueAsString(analytics);
        
        LOGGER.info(json);
        
        getFirebaseWrapperUtil().put(IAllergyConstants.KEY_ANALYTICS,json);
        
    }

    
    
    private void aggiornaDatiMachineLearnig(Analytics analytics, LSVMResult resultSVM) {
        // TODO Auto-generated method stub
        
    }



    /**
     * @return the firebaseWrapperUtil
     */
    public FirebaseWrapperUtil getFirebaseWrapperUtil() {
        if(firebaseWrapperUtil == null)
            firebaseWrapperUtil = new FirebaseWrapperUtil();
        return firebaseWrapperUtil;
    }

    /**
     * @param firebaseWrapperUtil the firebaseWrapperUtil to set
     */
    public void setFirebaseWrapperUtil(FirebaseWrapperUtil firebaseWrapperUtil) {
        this.firebaseWrapperUtil = firebaseWrapperUtil;
    }
}
