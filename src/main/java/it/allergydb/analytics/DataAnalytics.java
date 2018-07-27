/**
 * 
 */
package it.allergydb.analytics;

import it.allergydb.analytics.json.Analytics;
import it.allergydb.firebase4j.error.FirebaseException;
import it.allergydb.firebase4j.util.Filter;
import it.allergydb.svm.LSVMResult;
import it.allergydb.svm.LinearSupportVectorMachines;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Classe che implementa la Knowledge Discovery from Data per i
 * dati clinici allergologici presenti sul database allergy-db su Firebase
 * I dati raw interrogati sono in formato documentale json
 * Procedura KDD:
 * 
 * FASE 1 : Nella fase di pre-processing oltre a recuperare i dati e salavare
 * in memoria dati analitici viene creato uno stream di dati in formato SVM
 * per la fase di machine learning successiva e salvati su Hdfs hadoop.
 * 
 * FASE 2: Algortimo di Machine Learning supervisionato di classificazione binaria
 * (Vedi Classe it.allergydb.svm.LinearSupportVectorMachines)
 * 
 * FASE 3: Pos-processing dei dati raccolti, scrittura su Firebase
 * di un documento 'Analytics' in formato documentale json
 * per la visualizzazione successiva sul site di Allergy-db
 * 
 * 
 * @author rvoyat
 *
 */
public class DataAnalytics {

    protected static final Logger LOGGER = Logger.getRootLogger();
    
    public static String ALLERGENE_DEFAULT = "Pollen_Graminacee";
    
    public static String HADOOP_USER = "hduser";
    
    public static String HADOOP_HOME_DIR = "/opt/hadoop";
    
    
    /**
     * Args[0] -> Allergene su cui calcolare il modello composto da: Categoria_allergene, ad esempio: Pollen_Graminacee, 
     * Args[1] -> Nome utente hadoop
     * Args[2] -> hadoop home directory
     * 
     * @param args 
     */
    public static void main(String[] args)   {  
        
        LOGGER.info("Start Data Analytics - Allergy db");
        
        long start = System.currentTimeMillis();
        String allergenLabel = StringUtils.isNotBlank(args[0])?args[0]:ALLERGENE_DEFAULT;
        
        System.setProperty("HADOOP_USER_NAME", StringUtils.isNotBlank(args[1])?args[1]:HADOOP_USER);
        System.setProperty("hadoop.home.dir", StringUtils.isNotBlank(args[2])?args[2]:HADOOP_HOME_DIR);
        
        String path = "regressionAllergyDB";
        String nameFileDataset ="dataset.txt";
        String modelName = "SVMWithSGDModel_"+allergenLabel;
        String modelsDir ="target/tmp/"; 
        boolean saveModel = false;
        
        try {
            
            //phase 1 Pre-processing 
            LOGGER.info("Start phase 1- Preprocessing ...");
            FeaturesInput input = new FeaturesInput(path, nameFileDataset, new Filter(), allergenLabel);
            PreProcessing preProcessing = new PreProcessing();
            Analytics analytics =  preProcessing.createDatasetFeatures(input); 
            LOGGER.info("End phase 1- Time elapsed: "+(System.currentTimeMillis()-start));
            
            //phase 2 MLib 
            LOGGER.info("Start phase 2- Machine learning algorithm ...");
            LinearSupportVectorMachines lSVM = new LinearSupportVectorMachines(path, nameFileDataset, allergenLabel, 
                    modelName, modelsDir, saveModel);
            LSVMResult resultSVM  = lSVM.run();
            LOGGER.info("End phase 2- Time elapsed: "+(System.currentTimeMillis()-start));
            
            //phase 3 Post-processing
            LOGGER.info("Start phase 3- Post processing ...");
            PostProcessing postProcessing = new PostProcessing();
            postProcessing.aggiornaAnalyticsToFirebase(analytics,resultSVM);
            LOGGER.info("End phase 3- Time elapsed: "+(System.currentTimeMillis()-start));
            
        } catch (IOException | FirebaseException e) { 
            LOGGER.error(e.getStackTrace());
        } finally{
            LOGGER.info("End Data Analytics - Time elapsed: "+(System.currentTimeMillis()-start));
        }
        
    }

}
