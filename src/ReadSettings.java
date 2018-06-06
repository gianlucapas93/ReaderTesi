import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadSettings {

    String chooseFields = "C:\\Users\\Gianluca\\Desktop\\Politecnico\\chooseFields.txt";
    String discretizationFile = "C:\\Users\\Gianluca\\Desktop\\Politecnico\\discretization.txt";
    //BufferedReader br= null;
    String line="",line2="";
    String csvsplitby=",";
    int i=0;
    ArrayList<Integer> arrayList=new ArrayList<>();
    Map<String,Discretization> map =new HashMap<>();
    BufferedReader br,br2;

    public ArrayList<Integer> readOnOff(){

        try{
            br= new BufferedReader(new FileReader(chooseFields));
            br2= new BufferedReader(new FileReader(discretizationFile));
            while((line=br.readLine())!=null && (line2=br2.readLine())!=null){

            String[] splitline=line.split(" ");
            String[] splitline2= line2.split(";");
            int j=0;
            if(splitline2[1].equals("1")){
                Integer n=Integer.parseInt(splitline2[2]);

                String[] intervals =splitline2[3].split(",");
                Integer[] valueOfIntervals=new Integer[n];
                Integer i =0;
                for(String s: intervals){

                    valueOfIntervals[i++]=Integer.parseInt(s);
                }
                String[] nameIntervals=splitline2[4].split(",");
                Discretization d=new Discretization(true,n,valueOfIntervals,nameIntervals);
                map.put(splitline[0],d);
            }
            arrayList.add(j,Integer.parseInt(splitline[1]));
            j++;

        }}
        catch(IOException e){
            e.printStackTrace();
        }
        return arrayList;
    }

}
