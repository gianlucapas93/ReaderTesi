import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReadSettings {

    String chooseFields = "C:\\Users\\Gianluca\\Desktop\\Politecnico\\chooseFields.txt";
    //BufferedReader br= null;
    String line="";
    String csvsplitby=",";
    int i=0;
    ArrayList<Integer> arrayList=new ArrayList<>();
    BufferedReader br;

    public ArrayList<Integer> readOnOff(){

        try{
            br= new BufferedReader(new FileReader(chooseFields));
            while((line=br.readLine())!=null){

            String[] splitline=line.split(" ");
            arrayList.add(i,Integer.parseInt(splitline[1]));
            i++;

        }}
        catch(IOException e){
            e.printStackTrace();
        }
        return arrayList;
    }

}
