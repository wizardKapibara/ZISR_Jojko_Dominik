package com.fss;

import fuzzlib.*;
import fuzzlib.norms.*;
import fuzzlib.reasoning.ReasoningSystem;
import fuzzlib.reasoning.SystemConfig;

import java.io.*;
import java.util.*;

/**
 * Walidacja jednosystemowego rozmytego detektora cukrzycy (architektura STACK)
 * na zbiorze Pima Indians Diabetes Dataset.
 * Wyjscie: linie CSV zapisywane do results_20.csv oraz podsumowanie na stdout.
 */
public class DiabetesTest {

    private ReasoningSystem rs;

    private FuzzySet glNiski, glPodwy, glWysoki;
    private FuzzySet bmiNorm, bmiNadw, bmiOtyl;
    private FuzzySet ciNiskie, ciNorm, ciWys;
    private FuzzySet ciazMalo, ciazUmiar, ciazDuzo;
    private FuzzySet wiekMlody, wiekSredni, wiekStarszy;
    private FuzzySet skoraMale, skoraUmiar, skoraDuze;

    public DiabetesTest() {
        initFuzzySystem();
    }

    private SystemConfig mamdaniConfig(int in, int out, int prem, int cons) {
        SystemConfig cfg = new SystemConfig();
        cfg.setInputWidth(in);
        cfg.setOutputWidth(out);
        cfg.setNumberOfPremiseSets(prem);
        cfg.setNumberOfConclusionSets(cons);
        cfg.setIsOperationType(TNorm.TN_PRODUCT);
        cfg.setAndOperationType(TNorm.TN_MINIMUM);
        cfg.setOrOperationType(SNorm.SN_PROBABSUM);
        cfg.setImplicationType(TNorm.TN_MINIMUM);
        cfg.setConclusionAgregationType(SNorm.SN_PROBABSUM);
        cfg.setTruthCompositionType(TNorm.TN_MINIMUM);
        cfg.setAutoDefuzzyfication(false);
        cfg.setDefuzzyfication(DefuzMethod.DF_COG);
        cfg.setAutoAlpha(true);
        cfg.setTruthPrecision(0.001, 0.0001);
        return cfg;
    }

    private FuzzySet fs(String id, double[][] pts) {
        FuzzySet s = new FuzzySet(id, "");
        for (double[] p : pts) s.addPoint(p[0], p[1]);
        return s;
    }

    private void initFuzzySystem() {
        glNiski  = fs("gl_niski",  new double[][]{{0,1},{90,1},{130,0}});
        glPodwy  = fs("gl_podwy",  new double[][]{{90,0},{135,1},{168,0}});
        glWysoki = fs("gl_wysoki", new double[][]{{140,0},{175,1},{200,1}});

        bmiNorm = fs("bmi_norm", new double[][]{{0,1},{22,1},{30,0}});
        bmiNadw = fs("bmi_nadw", new double[][]{{20,0},{28,1},{38,0}});
        bmiOtyl = fs("bmi_otyl", new double[][]{{28,0},{40,1},{67,1}});

        ciNiskie = fs("ci_niskie", new double[][]{{0,1},{65,1},{82,0}});
        ciNorm   = fs("ci_norm",   new double[][]{{58,0},{76,1},{92,0}});
        ciWys    = fs("ci_wys",    new double[][]{{78,0},{100,1},{122,1}});

        ciazMalo  = fs("ciaz_malo",  new double[][]{{0,1},{2,1},{7,0}});
        ciazUmiar = fs("ciaz_umiar", new double[][]{{2,0},{6,1},{12,0}});
        ciazDuzo  = fs("ciaz_duzo",  new double[][]{{7,0},{13,1},{17,1}});

        wiekMlody   = fs("wiek_mlody",  new double[][]{{21,1},{33,1},{55,0}});
        wiekSredni  = fs("wiek_sredni", new double[][]{{30,0},{45,1},{65,0}});
        wiekStarszy = fs("wiek_stary",  new double[][]{{48,0},{65,1},{81,1}});

        skoraMale  = fs("skora_male",  new double[][]{{0,1},{15,1},{38,0}});
        skoraUmiar = fs("skora_umiar", new double[][]{{18,0},{35,1},{58,0}});
        skoraDuze  = fs("skora_duze",  new double[][]{{38,0},{60,1},{99,1}});

        FuzzySet ryzNiskie = fs("ryz_niskie", new double[][]{{0,1},{15,1},{35,0}});
        FuzzySet ryzUmiar  = fs("ryz_umiar",  new double[][]{{25,0},{45,1},{60,0}});
        FuzzySet ryzWyso   = fs("ryz_wyso",   new double[][]{{50,0},{70,1},{85,0}});
        FuzzySet ryzBardz  = fs("ryz_bardz",  new double[][]{{75,0},{90,1},{100,1}});

        rs = new ReasoningSystem(mamdaniConfig(6, 1, 18, 4));
        rs.getInputVar(0).id = "gl";
        rs.getInputVar(1).id = "bmi";
        rs.getInputVar(2).id = "ci";
        rs.getInputVar(3).id = "ciaz";
        rs.getInputVar(4).id = "wiek";
        rs.getInputVar(5).id = "skora";
        rs.getOutputVar(0).id = "ryz";

        for (FuzzySet f : new FuzzySet[]{
                glNiski, glPodwy, glWysoki,
                bmiNorm, bmiNadw, bmiOtyl,
                ciNiskie, ciNorm, ciWys,
                ciazMalo, ciazUmiar, ciazDuzo,
                wiekMlody, wiekSredni, wiekStarszy,
                skoraMale, skoraUmiar, skoraDuze})
            rs.addPremiseSet(f);

        for (FuzzySet f : new FuzzySet[]{ryzNiskie, ryzUmiar, ryzWyso, ryzBardz})
            rs.addConclusionSet(f);

        try {
            // R1: N+N+N
            rs.addRule(1,1);
            rs.addRuleItem("gl","gl_niski","AND","bmi","bmi_norm");
            rs.addRuleItem("ci","ci_niskie","AND","skora","skora_male");
            rs.addRuleItem("wiek","wiek_mlody","AND","ciaz","ciaz_malo");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleConclusion("ryz","ryz_niskie");
            // R2: N+N+U
            rs.addRule(1,1);
            rs.addRuleItem("gl","gl_niski","AND","bmi","bmi_norm");
            rs.addRuleItem("ci","ci_niskie","AND","skora","skora_male");
            rs.addRuleItem("wiek","wiek_sredni","AND","ciaz","ciaz_umiar");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleConclusion("ryz","ryz_niskie");
            // R3: N+N+W
            rs.addRule(1,1);
            rs.addRuleItem("gl","gl_niski","AND","bmi","bmi_norm");
            rs.addRuleItem("ci","ci_niskie","AND","skora","skora_male");
            rs.addRuleItem("wiek","wiek_stary","AND","ciaz","ciaz_duzo");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleConclusion("ryz","ryz_umiar");
            // R4: N+U+N
            rs.addRule(1,1);
            rs.addRuleItem("gl","gl_niski","AND","bmi","bmi_norm");
            rs.addRuleItem("ci","ci_norm","AND","skora","skora_umiar");
            rs.addRuleItem("wiek","wiek_mlody","AND","ciaz","ciaz_malo");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleConclusion("ryz","ryz_niskie");
            // R5: N+U+U
            rs.addRule(1,1);
            rs.addRuleItem("gl","gl_niski","AND","bmi","bmi_norm");
            rs.addRuleItem("ci","ci_norm","AND","skora","skora_umiar");
            rs.addRuleItem("wiek","wiek_sredni","AND","ciaz","ciaz_umiar");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleConclusion("ryz","ryz_umiar");
            // R6: N+U+W
            rs.addRule(1,1);
            rs.addRuleItem("gl","gl_niski","AND","bmi","bmi_norm");
            rs.addRuleItem("ci","ci_norm","AND","skora","skora_umiar");
            rs.addRuleItem("wiek","wiek_stary","AND","ciaz","ciaz_duzo");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleConclusion("ryz","ryz_umiar");
            // R7: N+W+N
            rs.addRule(1,1);
            rs.addRuleItem("gl","gl_niski","AND","bmi","bmi_norm");
            rs.addRuleItem("ci","ci_wys","AND","skora","skora_duze");
            rs.addRuleItem("wiek","wiek_mlody","AND","ciaz","ciaz_malo");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleConclusion("ryz","ryz_umiar");
            // R8: N+W+U
            rs.addRule(1,1);
            rs.addRuleItem("gl","gl_niski","AND","bmi","bmi_norm");
            rs.addRuleItem("ci","ci_wys","AND","skora","skora_duze");
            rs.addRuleItem("wiek","wiek_sredni","AND","ciaz","ciaz_umiar");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleConclusion("ryz","ryz_wyso");
            // R9: N+W+W
            rs.addRule(1,1);
            rs.addRuleItem("gl","gl_niski","AND","bmi","bmi_norm");
            rs.addRuleItem("ci","ci_wys","AND","skora","skora_duze");
            rs.addRuleItem("wiek","wiek_stary","AND","ciaz","ciaz_duzo");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleConclusion("ryz","ryz_wyso");
            // R10: U+N+N
            rs.addRule(1,1);
            rs.addRuleItem("gl","gl_podwy","AND","bmi","bmi_nadw");
            rs.addRuleItem("ci","ci_niskie","AND","skora","skora_male");
            rs.addRuleItem("wiek","wiek_mlody","AND","ciaz","ciaz_malo");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleConclusion("ryz","ryz_niskie");
            // R11: U+N+U
            rs.addRule(1,1);
            rs.addRuleItem("gl","gl_podwy","AND","bmi","bmi_nadw");
            rs.addRuleItem("ci","ci_niskie","AND","skora","skora_male");
            rs.addRuleItem("wiek","wiek_sredni","AND","ciaz","ciaz_umiar");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleConclusion("ryz","ryz_umiar");
            // R12: U+N+W
            rs.addRule(1,1);
            rs.addRuleItem("gl","gl_podwy","AND","bmi","bmi_nadw");
            rs.addRuleItem("ci","ci_niskie","AND","skora","skora_male");
            rs.addRuleItem("wiek","wiek_stary","AND","ciaz","ciaz_duzo");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleConclusion("ryz","ryz_umiar");
            // R13: U+U+N
            rs.addRule(1,1);
            rs.addRuleItem("gl","gl_podwy","AND","bmi","bmi_nadw");
            rs.addRuleItem("ci","ci_norm","AND","skora","skora_umiar");
            rs.addRuleItem("wiek","wiek_mlody","AND","ciaz","ciaz_malo");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleConclusion("ryz","ryz_umiar");
            // R14: U+U+U
            rs.addRule(1,1);
            rs.addRuleItem("gl","gl_podwy","AND","bmi","bmi_nadw");
            rs.addRuleItem("ci","ci_norm","AND","skora","skora_umiar");
            rs.addRuleItem("wiek","wiek_sredni","AND","ciaz","ciaz_umiar");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleConclusion("ryz","ryz_umiar");
            // R15: U+U+W
            rs.addRule(1,1);
            rs.addRuleItem("gl","gl_podwy","AND","bmi","bmi_nadw");
            rs.addRuleItem("ci","ci_norm","AND","skora","skora_umiar");
            rs.addRuleItem("wiek","wiek_stary","AND","ciaz","ciaz_duzo");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleConclusion("ryz","ryz_wyso");
            // R16: U+W+N
            rs.addRule(1,1);
            rs.addRuleItem("gl","gl_podwy","AND","bmi","bmi_nadw");
            rs.addRuleItem("ci","ci_wys","AND","skora","skora_duze");
            rs.addRuleItem("wiek","wiek_mlody","AND","ciaz","ciaz_malo");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleConclusion("ryz","ryz_wyso");
            // R17: U+W+U
            rs.addRule(1,1);
            rs.addRuleItem("gl","gl_podwy","AND","bmi","bmi_nadw");
            rs.addRuleItem("ci","ci_wys","AND","skora","skora_duze");
            rs.addRuleItem("wiek","wiek_sredni","AND","ciaz","ciaz_umiar");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleConclusion("ryz","ryz_wyso");
            // R18: U+W+W
            rs.addRule(1,1);
            rs.addRuleItem("gl","gl_podwy","AND","bmi","bmi_nadw");
            rs.addRuleItem("ci","ci_wys","AND","skora","skora_duze");
            rs.addRuleItem("wiek","wiek_stary","AND","ciaz","ciaz_duzo");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleConclusion("ryz","ryz_bardz");
            // R19: W+N+N
            rs.addRule(1,1);
            rs.addRuleItem("gl","gl_wysoki","AND","bmi","bmi_otyl");
            rs.addRuleItem("ci","ci_niskie","AND","skora","skora_male");
            rs.addRuleItem("wiek","wiek_mlody","AND","ciaz","ciaz_malo");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleConclusion("ryz","ryz_umiar");
            // R20: W+N+U
            rs.addRule(1,1);
            rs.addRuleItem("gl","gl_wysoki","AND","bmi","bmi_otyl");
            rs.addRuleItem("ci","ci_niskie","AND","skora","skora_male");
            rs.addRuleItem("wiek","wiek_sredni","AND","ciaz","ciaz_umiar");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleConclusion("ryz","ryz_wyso");
            // R21: W+N+W
            rs.addRule(1,1);
            rs.addRuleItem("gl","gl_wysoki","AND","bmi","bmi_otyl");
            rs.addRuleItem("ci","ci_niskie","AND","skora","skora_male");
            rs.addRuleItem("wiek","wiek_stary","AND","ciaz","ciaz_duzo");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleConclusion("ryz","ryz_wyso");
            // R22: W+U+N
            rs.addRule(1,1);
            rs.addRuleItem("gl","gl_wysoki","AND","bmi","bmi_otyl");
            rs.addRuleItem("ci","ci_norm","AND","skora","skora_umiar");
            rs.addRuleItem("wiek","wiek_mlody","AND","ciaz","ciaz_malo");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleConclusion("ryz","ryz_wyso");
            // R23: W+U+U
            rs.addRule(1,1);
            rs.addRuleItem("gl","gl_wysoki","AND","bmi","bmi_otyl");
            rs.addRuleItem("ci","ci_norm","AND","skora","skora_umiar");
            rs.addRuleItem("wiek","wiek_sredni","AND","ciaz","ciaz_umiar");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleConclusion("ryz","ryz_wyso");
            // R24: W+U+W
            rs.addRule(1,1);
            rs.addRuleItem("gl","gl_wysoki","AND","bmi","bmi_otyl");
            rs.addRuleItem("ci","ci_norm","AND","skora","skora_umiar");
            rs.addRuleItem("wiek","wiek_stary","AND","ciaz","ciaz_duzo");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleConclusion("ryz","ryz_bardz");
            // R25: W+W+N
            rs.addRule(1,1);
            rs.addRuleItem("gl","gl_wysoki","AND","bmi","bmi_otyl");
            rs.addRuleItem("ci","ci_wys","AND","skora","skora_duze");
            rs.addRuleItem("wiek","wiek_mlody","AND","ciaz","ciaz_malo");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleConclusion("ryz","ryz_bardz");
            // R26: W+W+U
            rs.addRule(1,1);
            rs.addRuleItem("gl","gl_wysoki","AND","bmi","bmi_otyl");
            rs.addRuleItem("ci","ci_wys","AND","skora","skora_duze");
            rs.addRuleItem("wiek","wiek_sredni","AND","ciaz","ciaz_umiar");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleConclusion("ryz","ryz_bardz");
            // R27: W+W+W
            rs.addRule(1,1);
            rs.addRuleItem("gl","gl_wysoki","AND","bmi","bmi_otyl");
            rs.addRuleItem("ci","ci_wys","AND","skora","skora_duze");
            rs.addRuleItem("wiek","wiek_stary","AND","ciaz","ciaz_duzo");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleItem("STACK","","AND","STACK","");
            rs.addRuleConclusion("ryz","ryz_bardz");
        } catch (Exception e) { e.printStackTrace(); }
    }

    private String labelGl(double v) {
        double n=glNiski.getMembership(v), p=glPodwy.getMembership(v), w=glWysoki.getMembership(v);
        if (w>=p && w>=n) return "wysoki";
        if (p>=n) return "podwyzszony";
        return "niski";
    }
    private String labelBmi(double v) {
        double n=bmiNorm.getMembership(v), d=bmiNadw.getMembership(v), o=bmiOtyl.getMembership(v);
        if (o>=d && o>=n) return "otylosc";
        if (d>=n) return "nadwaga";
        return "norma";
    }
    private String labelCi(double v) {
        if (v == 0) return "brak danych";
        double n=ciNiskie.getMembership(v), m=ciNorm.getMembership(v), w=ciWys.getMembership(v);
        if (w>=m && w>=n) return "wysokie";
        if (m>=n) return "normalne";
        return "niskie";
    }
    private String labelCiaz(double v) {
        double m=ciazMalo.getMembership(v), u=ciazUmiar.getMembership(v), d=ciazDuzo.getMembership(v);
        if (d>=u && d>=m) return "duzo";
        if (u>=m) return "umiarkowana";
        return "malo";
    }
    private String labelWiek(double v) {
        double m=wiekMlody.getMembership(v), s=wiekSredni.getMembership(v), st=wiekStarszy.getMembership(v);
        if (st>=s && st>=m) return "starszy";
        if (s>=m) return "sredni";
        return "mlody";
    }
    private String labelSkora(double v) {
        if (v == 0) return "brak danych";
        double m=skoraMale.getMembership(v), u=skoraUmiar.getMembership(v), d=skoraDuze.getMembership(v);
        if (d>=u && d>=m) return "duza";
        if (u>=m) return "umiarkowana";
        return "mala";
    }
    private String riskLevel(double r) {
        if (r < 30) return "NISKIE";
        if (r < 55) return "UMIARKOWANE";
        if (r < 75) return "WYSOKIE";
        return "BARDZO WYSOKIE";
    }

    public double diagnose(double ciaze, double glukoza, double cisnienie, double grubosc, double bmi, double wiek) {
        rs.setInput(0, glukoza);
        rs.setInput(1, bmi);
        rs.setInput(2, cisnienie);
        rs.setInput(3, ciaze);
        rs.setInput(4, wiek);
        rs.setInput(5, grubosc);
        rs.Process();
        return rs.getOutputVar(0).outset.DeFuzzyfy();
    }

    public static void main(String[] args) throws Exception {
        DiabetesTest dt = new DiabetesTest();

        String csvPath = "diabetes.csv";
        if (args.length > 0) csvPath = args[0];

        List<double[]> rows = new ArrayList<>();
        List<Integer> labels = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            br.readLine(); // naglowek
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                rows.add(new double[]{
                    Double.parseDouble(p[0]), // ciaze
                    Double.parseDouble(p[1]), // glukoza
                    Double.parseDouble(p[2]), // cisnienie
                    Double.parseDouble(p[3]), // grubosc skory
                    Double.parseDouble(p[5]), // bmi
                    Double.parseDouble(p[7])  // wiek
                });
                labels.add(Integer.parseInt(p[8].trim()));
            }
        }

        // Zapis wynikow CSV (uzywany przez generate_report.py)
        String outCsv = "results_20.csv";
        PrintWriter pw = new PrintWriter(new FileWriter(outCsv));
        pw.println("Nr,Ciaze,Glukoza,Cisnienie,Skora,BMI,Wiek,Outcome,Ryzyko,PoziomRyzyka,Predykcja,Poprawny," +
                   "EtykietaGl,EtykietaBMI,EtykietaCi,EtykietaCiaz,EtykietaWiek,EtykietaSkora");

        double threshold = 45.0;
        int correct20 = 0;

        System.out.println("Nr  | Gl    | BMI   | Ci  | Skora | Wiek | Ciaz | Out | Ryzyko% | Poziom         | OK?");
        System.out.println("-".repeat(95));

        for (int i = 0; i < 20; i++) {
            double[] r = rows.get(i);
            double ciaze=r[0], glukoza=r[1], cisnienie=r[2], grubosc=r[3], bmi=r[4], wiek=r[5];
            double risk = dt.diagnose(ciaze, glukoza, cisnienie, grubosc, bmi, wiek);
            int pred = risk >= threshold ? 1 : 0;
            int actual = labels.get(i);
            boolean ok = pred == actual;
            if (ok) correct20++;

            String poziom = dt.riskLevel(risk);
            String etGl   = dt.labelGl(glukoza);
            String etBmi  = dt.labelBmi(bmi);
            String etCi   = dt.labelCi(cisnienie);
            String etCz   = dt.labelCiaz(ciaze);
            String etWi   = dt.labelWiek(wiek);
            String etSk   = dt.labelSkora(grubosc);

            System.out.printf(Locale.ROOT, "%-3d | %-5.0f | %-5.1f | %-3.0f | %-5.0f | %-4.0f | %-4.0f | %-3d | %6.1f%% | %-14s | %s%n",
                i+1, glukoza, bmi, cisnienie, grubosc, wiek, ciaze, actual,
                risk, poziom, ok ? "OK" : "BLAD");

            pw.printf(Locale.ROOT, "%d,%.0f,%.0f,%.0f,%.0f,%.1f,%.0f,%d,%.2f,%s,%d,%s,%s,%s,%s,%s,%s,%s%n",
                i+1, ciaze, glukoza, cisnienie, grubosc, bmi, wiek,
                actual, risk, poziom, pred, ok ? "TAK" : "NIE",
                etGl, etBmi, etCi, etCz, etWi, etSk);
        }
        pw.close();

        System.out.println("-".repeat(95));
        System.out.printf("Poprawne (prog=%.0f%%): %d/20 (%.0f%%)%n%n", threshold, correct20, correct20 * 5.0);
        System.out.println("Zapisano: " + outCsv);
    }
}
