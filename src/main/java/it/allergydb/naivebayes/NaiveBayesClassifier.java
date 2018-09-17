/**
 * 
 */
package it.allergydb.naivebayes;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.classification.NaiveBayes;
import org.apache.spark.mllib.classification.NaiveBayesModel;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.util.MLUtils;
import org.apache.spark.sql.SparkSession;

import scala.Tuple2;

/** 
 * Binary/Multi Classification: Naive-Bayes
 * Generazione di un modello basato sul teorema di Bayes(metodo Bayesiano) per la classificazione binaria di un allergene
 * in base ai sintomi
 * 
 * @author rvoyat
 *
 */
public class NaiveBayesClassifier implements Serializable{


    /**
	 * 
	 */
	private static final long serialVersionUID = -1169652599382406379L;

	protected static final Logger LOGGER = Logger.getRootLogger();
    
    private String path = "regressionAllergyDB";
    private String nameFileDataset ="dataset.txt";
    private String allergenLabel = "Pollen_Birch";
    private String modelName = "NaiveBayes_"+allergenLabel;
    private String modelsDir ="target/tmp/"; 
    private boolean saveModel = false;



    public NaiveBayesClassifier(String path, String nameFileDataset, String allergenLabel, String modelName, String modelsDir,
            boolean saveModel) {
        this.path = path;
        this.nameFileDataset = nameFileDataset;
        this.allergenLabel = allergenLabel;
        this.modelName = modelName;
        this.modelsDir = modelsDir;
        this.saveModel = saveModel;
    } 
    
 
    /**
     * 
     * @return NaiveBayesResult
     */
    public NaiveBayesResult run() { 

        //Creo la sessione di Spark: 
        SparkSession spark = SparkSession.builder().appName("NaiveBayesClassifier").master("local").getOrCreate(); 
        
        //Carico il file da HDFS
        JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(spark.sparkContext(), path+"/"+nameFileDataset).toJavaRDD();
                 
       // Split iniziale [60% training data, 40% testing data].
        JavaRDD<LabeledPoint> training = data.sample(false, 0.6, 11L);
        training.cache();
        JavaRDD<LabeledPoint> test = data.subtract(training);

        // Lancio il training algorithm per buildare il modello.
        int numIterations = 100;
        NaiveBayesModel model = NaiveBayes.train(training.rdd(), numIterations);
        
        JavaPairRDD<Double, Double> predictionAndLabel =
        		  test.mapToPair(p -> new Tuple2<>(model.predict(p.features()), p.label()));
        
        double accuracy = predictionAndLabel.filter(pl -> pl._1().equals(pl._2())).count() / (double) test.count();


        // Salvo il  modello se previsto
        if(saveModel)
            model.save(spark.sparkContext(), modelsDir+modelName);
         
        
        //Stop Spark context
        spark.sparkContext().stop(); 
        
         
        LOGGER.info("Acuracy = " + accuracy); 
            
        return new NaiveBayesResult(accuracy, "Model Naive Bayes Apache Spark format version:"+model.formatVersion());

    }

}
