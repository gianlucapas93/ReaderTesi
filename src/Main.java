import java.awt.geom.GeneralPath;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        //CAMPIONE
        String csvFileCamp = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\itarea_compl2016_telematics_sent_016.csv";
        String csvFileCampCLR = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\itarea_compl2016_telematics_sent_016_clr.csv";
        String csvFileCampDisc = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\itarea_compl2016_telematics_sent_016_clr_disc.csv";
        String csvFileCampDiscAfterACCSX = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\itarea_compl2016_telematics_sent_016_clr_disc_acc_sx.csv";

        String csvFileCampWeek = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\itarea_compl2016_telematics_sent_016_clr_disc_week.csv";

        String smallfile = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\0150298089-2016.csv";
        String smalloutput = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\0150298089-2016-DISC.csv";

        String csvCampsplitOld = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\itarea_compl2016_telematics_sent_016_clr_disc_ACCSX_old.csv";
        String csvCampsplitNew = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\itarea_compl2016_telematics_sent_016_clr_disc_ACCSX_new.csv";
        String pathGeneral = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\";
        //FILE COMPLETO
        String csvFile = "C:\\Users\\Gianluca\\Desktop\\Tesi\\itarea_compl2016_telematics_sent.csv";
        String csvFileCLR = "C:\\Users\\Gianluca\\Desktop\\Tesi\\itarea_compl2016_telematics_sent_clr.csv";
        String csvFileDisc = "C:\\Users\\Gianluca\\Desktop\\Tesi\\itarea_compl2016_telematics_sent_clr_disc.csv";
        String csvFileDiscY = "C:\\Users\\Gianluca\\Desktop\\Tesi\\itarea_compl2016_telematics_sent_clr_disc_y.csv";
        String csvFileAfterAccsx = "C:\\Users\\Gianluca\\Desktop\\Tesi\\itarea_compl2016_telematics_sent_clr_accsx.csv";
        String csvFileWeek = "C:\\Users\\Gianluca\\Desktop\\Tesi\\itarea_compl2016_telematics_sent_clr_accsx_week.csv";
        String csvFileYear = "C:\\Users\\Gianluca\\Desktop\\Tesi\\itarea_compl2016_telematics_sent_clr_accsx_year.csv";
        String csvFilePlusMonth = "C:\\Users\\Gianluca\\Desktop\\Tesi\\itarea_compl2016_telematics_sent_clr_accsx_week_m.csv";
        String csvFileACCSXsample = "C:\\Users\\Gianluca\\Desktop\\Tesi\\itarea_compl2016_telematics_sent_clr_accsx_SAMPLE.csv";


        String chooseFields = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\chooseFields.txt";
        String chooseFieldsCLR = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\chooseFieldsCLR.txt";
        String discretizationFile = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\discretization.txt";
        String discretizationFileCLR = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\discretizationCLR.txt";
        String discretizationFileYear = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\discretizationY.txt";


        long start = System.currentTimeMillis();
        ArrayList<Integer> fieldsOnOff, fieldsOnOffRAW;
        Map<Integer, Discretization> map = new HashMap<>();


        //Serve per creare i file txt con gli attributi, si fa solo la prima volta
//        OpenFileAndCreate op=new OpenFileAndCreate();
//        op.initialize();
//        op.createNewOnOff();
//        op.initializeDiscretization();


        ReadSettings rs = new ReadSettings();
        fieldsOnOffRAW = rs.readChooseFile();

        //CREO i due file chooseFieldsCLR e discretizationFileCLR con solo gli attributi scelti
        //per mantenere il mapping tra choose e disc
//        rs.tookOutZero(chooseFields,chooseFieldsCLR,fieldsOnOffRAW);
//        rs.tookOutZero(discretizationFile,discretizationFileCLR,fieldsOnOffRAW);


        CleanFile cf = new CleanFile(csvFile, fieldsOnOffRAW, csvFileCLR);
        //cf.clean();

        fieldsOnOff = rs.readOnOff(discretizationFileYear);
        map = rs.getMap();


        Transformation tr = new Transformation(csvFileCLR, csvFileDisc, fieldsOnOff, map);


        //tr.propagateAccSx(csvFileCLR, csvFileAfterAccsx);

        //tr.groupByWeek(csvFileAfterAccsx,csvFileWeek);
//        tr.addMonth(csvFileWeek,csvFilePlusMonth);
        tr.doDiscretization(csvFileYear,csvFileDiscY);

        tr.sampleFile(csvFileDiscY, csvFileACCSXsample, 100);


        //CARTELLA DOVE RACCOGLIERE TUTTI I FILE DELLE POLIZZE SINGOLE


        File va = new File(pathGeneral + "VA");
//        va.mkdir();
        File vs = new File(pathGeneral + "VS");
//        vs.mkdir();
        File na = new File(pathGeneral + "NA");
//        na.mkdir();
        File ns = new File(pathGeneral + "NS");
//        ns.mkdir();

//        tr.splitOldNewAnnSem(csvFileDisc,va.toString(),na.toString(),vs.toString(),ns.toString());
//


        ArrayList<String> paths = new ArrayList<>();
        String VA = pathGeneral + "VA\\" + "2016-VA.csv";
        paths.add(VA);   //sono i file
        String VS = pathGeneral + "VS\\" + "2016-VS.csv";
        paths.add(VS);
        String NA = pathGeneral + "NA\\" + "2016-NA.csv";
        paths.add(NA);
        String NS = pathGeneral + "NS\\" + "2016-NS.csv";
        paths.add(NS);
//
//        System.out.println("INIZIO");
//        tr.splitForNplza(VA,pathGeneral+"\\VA\\");
//        System.out.println("VA completato");
//        tr.splitForNplza(VS,pathGeneral+"\\VS\\");
//        System.out.println("VS completato");
//        tr.splitForNplza(NA,pathGeneral+"\\NA\\");
//        System.out.println("NA completato");
//        tr.splitForNplza(NS,pathGeneral+"\\NS\\");
//        System.out.println("NS completato");
////        }


//
//
//        System.out.println(fieldsOnOff);
//        System.out.println(map);

        long end = System.currentTimeMillis();
        System.out.println("PROCESSO COMPLETO: " + (end - start) / 1000 + " seconds");


    }
}

