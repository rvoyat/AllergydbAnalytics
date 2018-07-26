/**
 * 
 */
package it.allergydb.svm;

import java.io.IOException;
import java.io.Serializable;

import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.classification.SVMModel;
import org.apache.spark.mllib.classification.SVMWithSGD;
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.util.MLUtils;
import org.apache.spark.sql.SparkSession;

import scala.Tuple2;

/** 
 * Binary Classification: Linear Support Vector Machines (SVMs) 
 * Generazione di un modello SVM per la classificazione binaria di un allergene
 * in base ai sintomi
 * 
 * @author rvoyat
 *
 */
public class LinearSupportVectorMachines implements Serializable{


    protected static final Logger LOGGER = Logger.getRootLogger();
    
    private String path = "regressionAllergyDB";
    private String nameFileDataset ="dataset.txt";
    private String allergenLabel = "Pollen_Birch";
    private String modelName = "SVMWithSGDModel_"+allergenLabel;
    private String modelsDir ="target/tmp/"; 
    private boolean saveModel = false;



    public LinearSupportVectorMachines(String path, String nameFileDataset, String allergenLabel, String modelName, String modelsDir,
            boolean saveModel) {
        this.path = path;
        this.nameFileDataset = nameFileDataset;
        this.allergenLabel = allergenLabel;
        this.modelName = modelName;
        this.modelsDir = modelsDir;
        this.saveModel = saveModel;
    } 
    
 
    public LSVMResult run() { 

        //Machine Learning phase: 
        SparkSession spark = SparkSession
                .builder() 
                .appName("LinearSupportVectorMachines").master("local")
          .getOrCreate(); 
        //Carico il file da HDFS
        JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(spark.sparkContext(), path+"/"+nameFileDataset).toJavaRDD();
                 
       // Split iniziale [60% training data, 40% testing data].
        JavaRDD<LabeledPoint> training = data.sample(false, 0.6, 11L);
        training.cache();
        JavaRDD<LabeledPoint> test = data.subtract(training);

        // Run training algorithm to build the model.
        int numIterations = 100;
        SVMModel model = SVMWithSGD.train(training.rdd(), numIterations);

        // Clear the default threshold.
        model.clearThreshold();

        // Compute raw scores on the test set.
        JavaRDD<Tuple2<Object, Object>> scoreAndLabels = test.map(p ->
          new Tuple2<>(model.predict(p.features()), p.label()));

        // Get evaluation metrics.areaUnderROC and Mean Squared Error
        BinaryClassificationMetrics metrics =
          new BinaryClassificationMetrics(JavaRDD.toRDD(scoreAndLabels));
        double auROC = metrics.areaUnderROC();
        double MSE = new JavaDoubleRDD(scoreAndLabels.map(
                new Function<Tuple2<Object, Object>, Object>() {
                  public Object call(Tuple2<Object, Object> pair) {
                    return Math.pow(Double.valueOf(pair._1().toString()) - Double.valueOf(pair._2().toString()), 2.0);
                  }
                }
            ).rdd()).mean(); 
           

        // Save model
        if(saveModel)
            model.save(spark.sparkContext(), modelsDir+modelName);
        //SVMModel sameModel = SVMModel.load(spark.sparkContext(), "target/tmp/"+modelName);
        
        //Stop Spark context
        spark.sparkContext().stop(); 
        
        
        // Post-process phase : TODO salvataggio del modello e delle metriche su cloud per la visualizzazione nel sito web
        System.out.println("Area under ROC = " + auROC);
        System.out.println("Test Data Mean Squared Error = " + MSE);
            
        return new LSVMResult(auROC, MSE, "Model SVM Apache Spark format version:"+model.formatVersion());

    }

}
