import fuzzlib.*;
import fuzzlib.creators.OperationCreator;
import fuzzlib.norms.*;
import fuzzlib.reasoning.ReasoningSystem;
import fuzzlib.reasoning.SystemConfig;

public class ReasoningSystemTest {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        FuzzySet f = new FuzzySet();
        FuzzySet g = new FuzzySet();
        FuzzySet h = new FuzzySet();

//		f.AddPoint(0,0);
//		f.AddPoint(2,1);
//		f.AddPoint(2.001,0);
//		f.AddPoint(3,0);
        f.newGaussian(4, 0.5);
        g.newGaussian(6, 0.5);

        TNorm t = OperationCreator.newTNorm(TNorm.TN_PRODUCT);

        FuzzySet.processSetsWithNorm(h, f, g, t);

        System.out.println("Zbiór f:" + f);
        System.out.println("Zbiór g:" + g);
        System.out.println("f *T g:" + h);
        System.out.println("Wyostrzenie h:" + h.DeFuzzyfy());
        h.PackFlatSections();
        System.out.println("f *T g:" + h);
        System.out.println("Wyostrzenie h:" + h.DeFuzzyfy());

        System.out.println();
        System.out.println("system wnioskujący:");
        System.out.println();

        ReasoningSystem rs1;
        FuzzySet puj, pzer, pdod;
        FuzzySet kduj, kuj, kzer, kdod, kddod;

        //przypisanie identyfikatorów
        puj = new FuzzySet("ujem", "");
        pzer = new FuzzySet("zer", "");
        pdod = new FuzzySet("dod", "");

        kduj = new FuzzySet("duz_ujem", "");
        kuj = new FuzzySet("ujem", "");
        kzer = new FuzzySet("zer", "");
        kdod = new FuzzySet("dod", "");
        kddod = new FuzzySet("duz_dod", "");

        //okreslenie zawartości zbiorów
        pzer.addPoint(-1, 0);
        pzer.addPoint(0, 1);
        pzer.addPoint(1, 0);
        //pzer.NewTriangle(0.0, 1);
        puj.assign(pdod.assign(pzer));
        puj.fuzzyfy(-1);
        pdod.fuzzyfy(1);

        kzer.addPoint(-0.5, 0);
        kzer.addPoint(0, 1);
        kzer.addPoint(0.5, 0);

        //kzer.IncreasePrecision(10);
        kzer.IncreaseYPrecision(0.05, 0.005);

        kduj.assign(kuj.assign(kdod.assign(kddod.assign(kzer))));
        kduj.fuzzyfy(-1);
        kuj.fuzzyfy(-0.5);
        kdod.fuzzyfy(0.5);
        kddod.fuzzyfy(1);

        //stworzenie konfiguracji systemu
        SystemConfig config = new SystemConfig();
        config.setInputWidth(2); ///
        config.setOutputWidth(1);///
        config.setNumberOfPremiseSets(3); ///
        config.setNumberOfConclusionSets(5); ///

        config.setIsOperationType(TNorm.TN_PRODUCT);
        config.setAndOperationType(TNorm.TN_MINIMUM);
        config.setOrOperationType(SNorm.SN_PROBABSUM);
        config.setImplicationType(TNorm.TN_MINIMUM);
        config.setConclusionAgregationType(SNorm.SN_PROBABSUM);
        config.setTruthCompositionType(TNorm.TN_MINIMUM);
        config.setAutoDefuzzyfication(false);
        config.setDefuzzyfication(DefuzMethod.DF_COG);
        config.setAutoAlpha(true);
        config.setTruthPrecision(0.001, 0.0001);

        // zbiór rozmywający
        FuzzySet fuzzyfier = new FuzzySet();
        fuzzyfier.newTriangle(0, 0.01);
        //fuzzyfier.IncreaseYPrecision(0.1, 0.001);
//	    fuzzyfier.NewGaussianFast(0, 0.015);

        //zresetowanie systemu i przypisanie określonej konfiguracji
        //rs1 = new ReasoningSystem();
        rs1 = new ReasoningSystem(config);
        rs1.getInputVar(0).id = "wejx";
        rs1.getInputVar(1).id = "wejy";
//	    rs1.getInputVar(0).fuzz = fuzzyfier;
//	    rs1.getInputVar(1).fuzz = fuzzyfier;
        rs1.getOutputVar(0).id = "wyj";

        //dodanie nowych przesłanek
        rs1.addPremiseSet(puj);
        rs1.addPremiseSet(pzer);
        rs1.addPremiseSet(pdod);
        rs1.addConclusionSet(kduj);
        rs1.addConclusionSet(kuj);
        rs1.addConclusionSet(kzer);
        rs1.addConclusionSet(kdod);
        rs1.addConclusionSet(kddod);

        try {
            rs1.addRule(1, 1);
            rs1.addRuleItem("wejx", "ujem", "AND", "wejy", "dod");
            rs1.addRuleConclusion("wyj", "zer");
            rs1.addRule(1, 1);
            rs1.addRuleItem("wejx", "ujem", "AND", "wejy", "zer");
            rs1.addRuleConclusion("wyj", "dod");
            rs1.addRule(1, 1);
            rs1.addRuleItem("wejx", "ujem", "AND", "wejy", "ujem");
            rs1.addRuleConclusion("wyj", "duz_dod");
            rs1.addRule(1, 1);
            rs1.addRuleItem("wejx", "zer", "AND", "wejy", "dod");
            rs1.addRuleConclusion("wyj", "ujem");
            rs1.addRule(1, 1);
            rs1.addRuleItem("wejx", "zer", "AND", "wejy", "zer");
            rs1.addRuleConclusion("wyj", "zer");
            rs1.addRule(1, 1);
            rs1.addRuleItem("wejx", "zer", "AND", "wejy", "ujem");
            rs1.addRuleConclusion("wyj", "dod");
            rs1.addRule(1, 1);
            rs1.addRuleItem("wejx", "dod", "AND", "wejy", "dod");
            rs1.addRuleConclusion("wyj", "duz_ujem");
            rs1.addRule(1, 1);
            rs1.addRuleItem("wejx", "dod", "AND", "wejy", "zer");
            rs1.addRuleConclusion("wyj", "ujem");
            rs1.addRule(1, 1);
            rs1.addRuleItem("wejx", "dod", "AND", "wejy", "ujem");
            rs1.addRuleConclusion("wyj", "zer");
        } catch (Exception e)  {
            
        }
            rs1.setInput(0, 0.45); //rs1.setInput("wejx",0.45)
            rs1.setInput(1, 0.9);
            rs1.Process();

            System.out.println("Wynik: " + rs1.getOutputVar(0).outset.DeFuzzyfy());

        }

    }
