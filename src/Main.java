import java.awt.geom.GeneralPath;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        String path2016 = "C:\\Users\\Gianluca\\Desktop\\Tesi\\2016\\";
        String path2015 = "C:\\Users\\Gianluca\\Desktop\\Tesi\\2015\\";

        //FILE COMPLETO 2016
        String csvFile = "C:\\Users\\Gianluca\\Desktop\\Tesi\\2016\\itarea_compl2016_telematics_sent.csv";
        String csvFileCLR = "C:\\Users\\Gianluca\\Desktop\\Tesi\\2016\\itarea_compl2016_telematics_sent_clr.csv";
        String csvFileDiscW = "C:\\Users\\Gianluca\\Desktop\\Tesi\\2016\\itarea_compl2016_telematics_sent_clr_disc_w.csv";
        String csvFileDiscY = "C:\\Users\\Gianluca\\Desktop\\Tesi\\2016\\itarea_compl2016_telematics_sent_clr_disc_y.csv";
        String csvFileDiscM = "C:\\Users\\Gianluca\\Desktop\\Tesi\\2016\\itarea_compl2016_telematics_sent_clr_disc_m.csv";
        String csvFileAfterAccsx = "C:\\Users\\Gianluca\\Desktop\\Tesi\\2016\\itarea_compl2016_telematics_sent_clr_accsx.csv";
        String csvFileWeekTmp = "C:\\Users\\Gianluca\\Desktop\\Tesi\\2016\\itarea_compl2016_telematics_sent_clr_accsx_week_tmp.csv";
        String csvFileMonth = "C:\\Users\\Gianluca\\Desktop\\Tesi\\2016\\itarea_compl2016_telematics_sent_clr_accsx_month.csv";
        String csvFileYear = "C:\\Users\\Gianluca\\Desktop\\Tesi\\2016\\itarea_compl2016_telematics_sent_clr_accsx_year.csv";
        String csvFileWeek = "C:\\Users\\Gianluca\\Desktop\\Tesi\\2016\\itarea_compl2016_telematics_sent_clr_accsx_week.csv";
        String csvFileACCSXsample = "C:\\Users\\Gianluca\\Desktop\\Tesi\\2016\\itarea_compl2016_telematics_sent_clr_accsx_SAMPLE.csv";

        //FILE COMPLETO 2015
        String csvFile2015 = "C:\\Users\\Gianluca\\Desktop\\Tesi\\2015\\itarea_compl2015_telematics_sent.csv";
        String csvFileCLR2015 = "C:\\Users\\Gianluca\\Desktop\\Tesi\\2015\\itarea_compl2015_telematics_sent_clr.csv";
        String csvFileDiscW2015 = "C:\\Users\\Gianluca\\Desktop\\Tesi\\2015\\itarea_compl2015_telematics_sent_clr_disc_w.csv";
        String csvFileDiscY2015 = "C:\\Users\\Gianluca\\Desktop\\Tesi\\2015\\itarea_compl2015_telematics_sent_clr_disc_y.csv";
        String csvFileDiscM2015 = "C:\\Users\\Gianluca\\Desktop\\Tesi\\2015\\itarea_compl2015_telematics_sent_clr_disc_m.csv";
        String csvFileAfterAccsx2015 = "C:\\Users\\Gianluca\\Desktop\\Tesi\\2015\\itarea_compl2015_telematics_sent_clr_accsx.csv";
        String csvFileWeekTmp2015 = "C:\\Users\\Gianluca\\Desktop\\Tesi\\2015\\itarea_compl2015_telematics_sent_clr_accsx_week_tmp.csv";
        String csvFileMonth2015 = "C:\\Users\\Gianluca\\Desktop\\Tesi\\2015\\itarea_compl2015_telematics_sent_clr_accsx_month.csv";
        String csvFileYear2015 = "C:\\Users\\Gianluca\\Desktop\\Tesi\\2015\\itarea_compl2015_telematics_sent_clr_accsx_year.csv";
        String csvFileWeek2015 = "C:\\Users\\Gianluca\\Desktop\\Tesi\\2015\\itarea_compl2015_telematics_sent_clr_accsx_week.csv";
        String csvFileACCSXsample2015 = "C:\\Users\\Gianluca\\Desktop\\Tesi\\2015\\itarea_compl2015_telematics_sent_clr_accsx_SAMPLE.csv";
        String discretizationFileCLR2015 = "C:\\Users\\Gianluca\\Desktop\\Tesi\\2015\\ReaderData\\discretizationCLR.txt";
        String discretizationFileY2015 = "C:\\Users\\Gianluca\\Desktop\\Tesi\\2015\\ReaderData\\discretizationY.txt";

        String chooseFields = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\chooseFields.txt";
        String chooseFieldsCLR = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\chooseFieldsCLR.txt";
        String discretizationFile = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\discretization.txt";
        String discretizationFileCLR = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\discretizationCLR.txt";
        String discretizationFileYear = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\discretizationY.txt";
        String discretizationFileMonth = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\discretizationM.txt";

        String test1 = "C:\\Users\\Gianluca\\Desktop\\Tesi\\test1.csv";
        String test2 = "C:\\Users\\Gianluca\\Desktop\\Tesi\\test2.csv";
        String test3 = "C:\\Users\\Gianluca\\Desktop\\Tesi\\test3.csv";

        long start = System.currentTimeMillis();
        ArrayList<Integer> fieldsOnOff, fieldsOnOffRAW;
        Map<Integer, Discretization> map = new HashMap<>();

        Integer anno;

        //Serve per creare i file txt con gli attributi, si fa solo la prima volta

//        OpenFileAndCreate op=new OpenFileAndCreate(anno);
//        op.initializeOnOff();
//        op.createNewOnOff();
//        op.initializeDiscretization();

        anno = 2015;
        ReadSettings rs = new ReadSettings(anno);
        fieldsOnOffRAW = rs.readChooseFile();

        //CREO i due file chooseFieldsCLR e discretizationFileCLR con solo gli attributi scelti
        //per mantenere il mapping tra choose e disc
//        rs.tookOutZero(chooseFields,chooseFieldsCLR,fieldsOnOffRAW);
//        rs.tookOutZero(discretizationFile,discretizationFileCLR,fieldsOnOffRAW);


        CleanFile cf = new CleanFile(fieldsOnOffRAW);
//        cf.clean();
//        cf.clean2015(csvFile2015,csvFileCLR2015);


        if (anno == 2016) {
            fieldsOnOff = rs.readOnOff(discretizationFileMonth);
        } else {
            fieldsOnOff = rs.readOnOff(discretizationFileY2015);
        }

        map = rs.getMap();


        Transformation tr = new Transformation(fieldsOnOff, map);

//        tr.propagateAccSx(csvFileCLR2015, csvFileAfterAccsx2015);


//        tr.sampleFile(csvFileAfterAccsx,test1,0,200);
//        tr.groupByWeek(csvFileAfterAccsx,csvFileWeekTmp,csvFilePlusMonth);
//        tr.addMonth(csvFileWeek2015, csvFilePlusMonth2015);
//        tr.sampleFile(csvFilePlusMonth2015,csvFileACCSXsample2015,200);

//        int rows=tr.countRows(csvFileMonth2015);

//        tr.groupByMonth(csvFilePlusMonth, csvFileMonth);
//        tr.groupByYear(csvFilePlusMonth2015,csvFileYear2015);
//        tr.sampleFile(csvFileMonth, test1, 1,100);

//        tr.doDiscretization(csvFileMonth2015,csvFileDiscM2015);

        //CARTELLA DOVE RACCOGLIERE TUTTI I FILE DELLE POLIZZE SINGOLE

        String pathGeneral;
        if (anno == 2016) {
            pathGeneral = path2016;
        } else {
            pathGeneral = path2015;
        }
        File va = new File(pathGeneral + "VA");
//        va.mkdir();
        File vs = new File(pathGeneral + "VS");
//        vs.mkdir();
        File na = new File(pathGeneral + "NA");
//        na.mkdir();
        File ns = new File(pathGeneral + "NS");
//        ns.mkdir();
//
//        tr.splitOldNewAnnSem(csvFileDiscW2015, va.toString(), na.toString(), vs.toString(), ns.toString(),anno);
//



        String VA = pathGeneral + "VA\\" + anno + "-VA.csv";
        String VS = pathGeneral + "VS\\" + anno + "-VS.csv";
        String NA = pathGeneral + "NA\\" + anno + "-NA.csv";
        String NS = pathGeneral + "NS\\" + anno + "-NS.csv";


//        System.out.println("INIZIO Split for Nplza");
//        tr.splitForNplza(VA, pathGeneral + "\\VA\\",anno);
//        System.out.println("VA completato");
//        tr.splitForNplza(VS, pathGeneral + "\\VS\\",anno);
//        System.out.println("VS completato");
//        tr.splitForNplza(NA, pathGeneral + "\\NA\\",anno);
//        System.out.println("NA completato");
//        tr.splitForNplza(NS, pathGeneral + "\\NS\\",anno);
//        System.out.println("NS completato");
////        }



        //CONTROLLI
//        Check ck = new Check();
//        ck.checkAccsxAndCost(csvFileCLR2015,",");
//        tr.sampleFile(csvFileCLR2015,test1,50000);

        long end = System.currentTimeMillis();
        System.out.println("PROCESSO COMPLETO: " + (end - start) / 1000 + " seconds");


    }
}

