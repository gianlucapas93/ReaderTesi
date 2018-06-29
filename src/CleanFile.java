import java.io.*;
import java.util.ArrayList;

public class CleanFile {
    String csvFile;
    ArrayList<Integer> fieldsOnOff;
    String csvFileCleaned;

    public CleanFile(String csvFile, ArrayList<Integer> fieldsOnOff,String csvFileCleaned) {
        this.csvFile = csvFile;
        this.fieldsOnOff = fieldsOnOff;
        this.csvFileCleaned=csvFileCleaned;
    }

    public void clean(){
        String line="";

        try{

            BufferedReader br= new BufferedReader(new FileReader(csvFile));

            BufferedWriter bw = new BufferedWriter(new FileWriter(csvFileCleaned));

            int y=0;
            long start = System.currentTimeMillis();

            while((line=br.readLine())!=null){
                String[] field=line.split(",",-1);

                for(Integer i=0; i<fieldsOnOff.size() && i<field.length; i++){
                    if(fieldsOnOff.get(i)!=0) bw.write(field[i]+",");
                }
                //y++;
                bw.newLine();
                //if(y==1000)break;
            }
            bw.close();
            long end = System.currentTimeMillis();
            System.out.println("Clean file: "+(end - start) / 1000+ " seconds");
        }catch(IOException e){
            e.printStackTrace();
        }
    }


}

