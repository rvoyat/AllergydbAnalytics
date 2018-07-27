package it.allergydb.analytics;

import it.allergydb.analytics.json.Analytics;
import it.allergydb.firebase4j.error.FirebaseException;
import it.allergydb.firebase4j.error.JacksonUtilityException;
import it.allergydb.firebase4j.util.FirebaseWrapperUtil;
import it.allergydb.firebase4j.util.JacksonUtility;
import it.allergydb.hdfs.HDFSUtil;
import it.allergydb.svm.MapToSVMMaker;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.log4j.Logger;

/**
 * Classe per il pre-processing  della KDD 
 * 
 * @author rvoyat
 *
 */
public class PreProcessing {

    protected static final Logger LOGGER = Logger.getRootLogger();
    
    private HDFSUtil hdfsUtil;
    private FirebaseWrapperUtil firebaseWrapperUtil;
    private MapToSVMMaker svmMaker;
    private ResultUtil analyticsUtil;
    
    /**
     * Metodo che si occupa di scaricare i dati da Firebase, creare dati aggregati e crear il dataset per le fasi successive
     * 
     * @param input
     * @return
     * @throws IOException
     */
    public Analytics createDatasetFeatures(FeaturesInput input) throws IOException {
         

        Analytics result = getAnalyticsUtil().generateVoidAnalytics(input);
        
        //APRO STREAM FILE SU HDFS
        FSDataOutputStream fsOutStream = getHdfsUtil().openStream(input.getPath(), input.getNomeFile());
          
        boolean continueWhile = true; 
        int processati = 0;  
        try { 
            do{
                try {
                    // Leggo dati da firebase
                    String json =getFirebaseWrapperUtil().getJSON(input.getFilterFirebase());
                    Map<String,Object> data  = JacksonUtility.GET_JSON_STRING_AS_MAP( json );
                    
                    //Aggiorno Analytics
                    getAnalyticsUtil().updateAnalytics(result,json,data);
                    
                    // Trasformo nel formato desiderato
                    String svm = getSvmMaker().convert(data,input.getAllergenLabel());
                    
                    if(svm!= null){
                        // Scrivo su hdfs
                        getHdfsUtil().writeFile(fsOutStream, svm.getBytes()); 
                        //Aggiorno start
                        String start = String.valueOf((getSvmMaker().getLastKey().longValue()+1));
                        input.getFilterFirebase().setKey_start(start);
                        processati += input.getFilterFirebase().getRange();
                    } else {
                        continueWhile = false;
                        break;
                    }
                    
                } catch (Exception | FirebaseException |JacksonUtilityException e) {
                	LOGGER.error("Error pre-processing",e);
                    continueWhile = false;
                    break;
                } 
                
                
            }while(continueWhile && processati< input.getFilterFirebase().getSoglia());
            
        } catch (Exception   e ) { 
        	LOGGER.error("Error pre-processing",e);
        } 
        //CHIUDO STREAM FILE SU HDFS
        getHdfsUtil().closeStream(fsOutStream);
        
        //Rifinisco analytics
        getAnalyticsUtil().rifinisciResult(result); 
        
        return result;
    }

   

    /**
     * @return the hdfsUtil
     */
    public HDFSUtil getHdfsUtil() {
        if(hdfsUtil==null)
            hdfsUtil = new HDFSUtil();
        return hdfsUtil;
    }

    /**
     * @param hdfsUtil the hdfsUtil to set
     */
    public void setHdfsUtil(HDFSUtil hdfsUtil) {
        this.hdfsUtil = hdfsUtil;
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

    /**
     * @return the csvMaker
     */
    public MapToSVMMaker getSvmMaker() {
        if(svmMaker==null)
            svmMaker = new MapToSVMMaker();
        return svmMaker;
    }

    /**
     * @param csvMaker the csvMaker to set
     */
    public void setSvmMaker(MapToSVMMaker svmMaker) {
        this.svmMaker = svmMaker;
    }



    /**
     * @return the analyticsUtil
     */
    public ResultUtil getAnalyticsUtil() {
        if(analyticsUtil==null)
            analyticsUtil = new ResultUtil();
        return analyticsUtil;
    }



    /**
     * @param analyticsUtil the analyticsUtil to set
     */
    public void setAnalyticsUtil(ResultUtil analyticsUtil) {
        this.analyticsUtil = analyticsUtil;
    }

        
    
}
