package it.allergydb.hdfs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
/**
 * 
 * @author rvoyat
 *
 */
public class HDFSUtil {
    
    private static FileSystem hdfs;
    
    public static boolean createWorkingDirectory(String path) throws IOException{ 
        Path workingDir = getHdfs().getWorkingDirectory();
        Path newFolderPath= new Path(path);

        newFolderPath=Path.mergePaths(workingDir, newFolderPath);

        if(getHdfs().exists(newFolderPath)) {
            getHdfs().delete(newFolderPath, true); //Delete existing Directory

        }
        return getHdfs().mkdirs(newFolderPath);     //Create new Directory 
    }
    
    public Path createFile(String path,String fileName) throws IOException{
        
        Path newFilePath= null;
        if(createWorkingDirectory(path)){
            newFilePath = new Path(path+"/"+fileName);
            getHdfs().delete(newFilePath,true);
            getHdfs().createNewFile(newFilePath);
        }
        return newFilePath;
    }
    
    public String readData(String path, String fileName,String outFile) throws IOException{

        Path newFilePath= new Path(path+"/"+fileName);
        BufferedReader bfr=new BufferedReader(new InputStreamReader(getHdfs().open(newFilePath)));String str = null;

        FileOutputStream out = new FileOutputStream(new File(outFile));
        StringBuilder sb = new StringBuilder("");
        while ((str = bfr.readLine())!= null)

        {
            out.write(str.getBytes());
            out.flush();
        }
        out.close();
        return "OK";
    }
    
    public FSDataOutputStream openStream(String path,String fileName) throws IOException{
        return getHdfs().create(createFile(path, fileName)); 
    }
    
    public void writeFile(FSDataOutputStream fsOutStream, byte[] byt) throws IOException{
        fsOutStream.write( byt );
    }
    
    public void closeStream(FSDataOutputStream fsOutStream ) throws IOException{
        fsOutStream.close();
    }
    
    public static FileSystem getHdfs() throws IOException{
        if(hdfs==null){
            Configuration conf = new Configuration(); 

            conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
            conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
            
            hdfs = FileSystem.get(conf);
        }
        return hdfs;
    }

}
