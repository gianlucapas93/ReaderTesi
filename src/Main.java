import java.io.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        //CAMPIONE
        //String csvFile = "C:\\Users\\Gianluca\\Desktop\\Politecnico\\itarea_compl2016_telematics_sent_016.csv";
        //FILE COMPLETO
        String csvFile = "C:\\Users\\Gianluca\\Desktop\\Tesi\\itarea_compl2016_telematics_sent.csv";
        String csvFileCleaned = "C:\\Users\\Gianluca\\Desktop\\Tesi\\itarea_compl2016_telematics_sent_clr2.csv";
        ArrayList<Integer> fieldsOnOff;


        //OpenFileAndCreate op=new OpenFileAndCreate();
        //op.initialize();
        //op.initializeDiscretization();

        ReadSettings rs=new ReadSettings();
        fieldsOnOff=rs.readOnOff();
        System.out.println(fieldsOnOff);

//        CleanFile cf=new CleanFile(csvFile,fieldsOnOff,csvFileCleaned);
//        cf.clean();




    }
}
