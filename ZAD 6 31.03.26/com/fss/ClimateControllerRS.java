package com.fss;

import java.util.Scanner;
import fuzzlib.*;
import fuzzlib.norms.*;
import fuzzlib.reasoning.ReasoningSystem;
import fuzzlib.reasoning.SystemConfig;

public class ClimateControllerRS {

    public static void main(String[] args) {

        // ==================== ZBIORY WEJSCIOWE - TEMPERATURA (0-40 C) ====================

        // bardzo zimna: zakres 0-15, szczyt na 0-8
        FuzzySet tempBardzoZimna = new FuzzySet("b_zimna", "");
        tempBardzoZimna.addPoint(0.0,  1.0);
        tempBardzoZimna.addPoint(8.0,  1.0);
        tempBardzoZimna.addPoint(16.0, 0.0);

        // zimna: trojkat z centrum na 17
        FuzzySet tempZimna = new FuzzySet("zimna", "");
        tempZimna.addPoint(12.0, 0.0);
        tempZimna.addPoint(17.0, 1.0);
        tempZimna.addPoint(22.0, 0.0);

        // ciepla: trojkat z centrum na 26
        FuzzySet tempCiepla = new FuzzySet("ciepla", "");
        tempCiepla.addPoint(20.0, 0.0);
        tempCiepla.addPoint(26.0, 1.0);
        tempCiepla.addPoint(32.0, 0.0);

        // goraca: zakres 30-40, szczyt na 35-40
        FuzzySet tempGoraca = new FuzzySet("goraca", "");
        tempGoraca.addPoint(29.0, 0.0);
        tempGoraca.addPoint(36.0, 1.0);
        tempGoraca.addPoint(40.0, 1.0);

        // ==================== ZBIORY WEJSCIOWE - WILGOTNOSC (0-100 %) ====================

        // sucha: zakres 0-35, szczyt na 0-20
        FuzzySet wilgSucha = new FuzzySet("sucha", "");
        wilgSucha.addPoint(0.0,  1.0);
        wilgSucha.addPoint(20.0, 1.0);
        wilgSucha.addPoint(38.0, 0.0);

        // normalna: trojkat z centrum na 50
        FuzzySet wilgNormalna = new FuzzySet("normalna", "");
        wilgNormalna.addPoint(35.0, 0.0);
        wilgNormalna.addPoint(50.0, 1.0);
        wilgNormalna.addPoint(65.0, 0.0);

        // wilgotna: trojkat z centrum na 72
        FuzzySet wilgWilgotna = new FuzzySet("wilgotna", "");
        wilgWilgotna.addPoint(60.0, 0.0);
        wilgWilgotna.addPoint(72.0, 1.0);
        wilgWilgotna.addPoint(85.0, 0.0);

        // bardzo wilgotna: zakres 80-100, szczyt na 90-100
        FuzzySet wilgBardzoWilgotna = new FuzzySet("b_wilgotna", "");
        wilgBardzoWilgotna.addPoint(78.0,  0.0);
        wilgBardzoWilgotna.addPoint(90.0,  1.0);
        wilgBardzoWilgotna.addPoint(100.0, 1.0);

        // ==================== ZBIORY WYJSCIOWE - CHLODZENIE (0-100 %) ====================

        // c_ - prefix dla zbiorow chlodzenia (aby uniknac kolizji nazw z nawiewem)
        FuzzySet chlBrak = new FuzzySet("c_brak", "");
        chlBrak.addPoint(0.0,  1.0);
        chlBrak.addPoint(5.0,  1.0);
        chlBrak.addPoint(15.0, 0.0);

        FuzzySet chlSlabe = new FuzzySet("c_slabe", "");
        chlSlabe.addPoint(10.0, 0.0);
        chlSlabe.addPoint(25.0, 1.0);
        chlSlabe.addPoint(38.0, 0.0);

        FuzzySet chlUmiarkowane = new FuzzySet("c_umiark", "");
        chlUmiarkowane.addPoint(28.0, 0.0);
        chlUmiarkowane.addPoint(50.0, 1.0);
        chlUmiarkowane.addPoint(65.0, 0.0);

        FuzzySet chlMocne = new FuzzySet("c_mocne", "");
        chlMocne.addPoint(55.0, 0.0);
        chlMocne.addPoint(72.0, 1.0);
        chlMocne.addPoint(87.0, 0.0);

        FuzzySet chlMaksymalne = new FuzzySet("c_maks", "");
        chlMaksymalne.addPoint(82.0,  0.0);
        chlMaksymalne.addPoint(93.0,  1.0);
        chlMaksymalne.addPoint(100.0, 1.0);

        // ==================== ZBIORY WYJSCIOWE - NAWIEW (0-100 %) ====================

        // n_ - prefix dla zbiorow nawiewu
        FuzzySet nawBrak = new FuzzySet("n_brak", "");
        nawBrak.addPoint(0.0,  1.0);
        nawBrak.addPoint(5.0,  1.0);
        nawBrak.addPoint(15.0, 0.0);

        FuzzySet nawSlaby = new FuzzySet("n_slaby", "");
        nawSlaby.addPoint(10.0, 0.0);
        nawSlaby.addPoint(25.0, 1.0);
        nawSlaby.addPoint(38.0, 0.0);

        FuzzySet nawUmiarkowany = new FuzzySet("n_umiark", "");
        nawUmiarkowany.addPoint(28.0, 0.0);
        nawUmiarkowany.addPoint(50.0, 1.0);
        nawUmiarkowany.addPoint(65.0, 0.0);

        FuzzySet nawMocny = new FuzzySet("n_mocny", "");
        nawMocny.addPoint(55.0, 0.0);
        nawMocny.addPoint(72.0, 1.0);
        nawMocny.addPoint(87.0, 0.0);

        FuzzySet nawMaksymalny = new FuzzySet("n_maks", "");
        nawMaksymalny.addPoint(82.0,  0.0);
        nawMaksymalny.addPoint(93.0,  1.0);
        nawMaksymalny.addPoint(100.0, 1.0);

        // ==================== KONFIGURACJA SYSTEMU WNIOSKOWANIA ====================

        SystemConfig config = new SystemConfig();
        config.setInputWidth(2);           // 2 wejscia: temperatura, wilgotnosc
        config.setOutputWidth(2);          // 2 wyjscia: chlodzenie, nawiew
        config.setNumberOfPremiseSets(8);  // 4 zbiory temp + 4 zbiory wilg
        config.setNumberOfConclusionSets(10); // 5 zbiorow chlodzenia + 5 zbiorow nawiewu

        config.setIsOperationType(TNorm.TN_PRODUCT);       // metoda obliczania przynaleznosci "IS"
        config.setAndOperationType(TNorm.TN_MINIMUM);      // AND: minimum Zadeh
        config.setOrOperationType(SNorm.SN_PROBABSUM);     // OR: suma probabilistyczna
        config.setImplicationType(TNorm.TN_MINIMUM);       // implikacja: minimum (Mamdani)
        config.setConclusionAgregationType(SNorm.SN_PROBABSUM); // agregacja konkluzji
        config.setTruthCompositionType(TNorm.TN_MINIMUM);
        config.setAutoDefuzzyfication(false);
        config.setDefuzzyfication(DefuzMethod.DF_COG);     // defuzyfikacja: srodek ciezkosci
        config.setAutoAlpha(true);
        config.setTruthPrecision(0.001, 0.0001);

        // ==================== FAZYFIKATOR WEJSCIA ====================

        // Fazyfikator rozmywa dokladna wartosc wejsciowa na malym obszarze
        // Dzieki temu zamiast punktu crisp dostajemy maly zbior rozmyty
        FuzzySet fazyfikator = new FuzzySet();
        fazyfikator.newTriangle(0, 0.5);   // trojkat o polszerokosci 0.5 (±0.5 od wartosci wejsciowej)

        // ==================== TWORZENIE SYSTEMU ====================

        ReasoningSystem rs = new ReasoningSystem(config);
        rs.getInputVar(0).id = "temp";
        rs.getInputVar(1).id = "wilg";
        rs.getOutputVar(0).id = "chlod";
        rs.getOutputVar(1).id = "nawiew";

        // wlaczenie fazyfikatora na wejsciach
        rs.getInputVar(0).fuzz = fazyfikator;
        rs.getInputVar(1).fuzz = fazyfikator;

        // dodanie zbiorow przeslanek (temperatura)
        rs.addPremiseSet(tempBardzoZimna);
        rs.addPremiseSet(tempZimna);
        rs.addPremiseSet(tempCiepla);
        rs.addPremiseSet(tempGoraca);

        // dodanie zbiorow przeslanek (wilgotnosc)
        rs.addPremiseSet(wilgSucha);
        rs.addPremiseSet(wilgNormalna);
        rs.addPremiseSet(wilgWilgotna);
        rs.addPremiseSet(wilgBardzoWilgotna);

        // dodanie zbiorow konkluzji (chlodzenie)
        rs.addConclusionSet(chlBrak);
        rs.addConclusionSet(chlSlabe);
        rs.addConclusionSet(chlUmiarkowane);
        rs.addConclusionSet(chlMocne);
        rs.addConclusionSet(chlMaksymalne);

        // dodanie zbiorow konkluzji (nawiew)
        rs.addConclusionSet(nawBrak);
        rs.addConclusionSet(nawSlaby);
        rs.addConclusionSet(nawUmiarkowany);
        rs.addConclusionSet(nawMocny);
        rs.addConclusionSet(nawMaksymalny);

        // ==================== REGULY WNIOSKOWANIA ====================
        //
        // Tabela regul:
        //
        //  temp \ wilg  | sucha      | normalna   | wilgotna   | b_wilgotna
        //  -------------|------------|------------|------------|------------
        //  b_zimna      | brak/brak  | brak/slaby | slabe/um.  | umiark/mocny
        //  zimna        | brak/slaby | slabe/um.  | umiark/m.  | mocne/maks
        //  ciepla       | slabe/um.  | umiark/m.  | mocne/maks | maks/maks
        //  goraca       | umiark/m.  | mocne/maks | maks/maks  | maks/maks

        try {
            // --- b_zimna ---
            rs.addRule(1, 2);
            rs.addRuleItem("temp", "b_zimna", "AND", "wilg", "sucha");
            rs.addRuleConclusion("chlod", "c_brak");
            rs.addRuleConclusion("nawiew", "n_brak");

            rs.addRule(1, 2);
            rs.addRuleItem("temp", "b_zimna", "AND", "wilg", "normalna");
            rs.addRuleConclusion("chlod", "c_brak");
            rs.addRuleConclusion("nawiew", "n_slaby");

            rs.addRule(1, 2);
            rs.addRuleItem("temp", "b_zimna", "AND", "wilg", "wilgotna");
            rs.addRuleConclusion("chlod", "c_slabe");
            rs.addRuleConclusion("nawiew", "n_umiark");

            rs.addRule(1, 2);
            rs.addRuleItem("temp", "b_zimna", "AND", "wilg", "b_wilgotna");
            rs.addRuleConclusion("chlod", "c_umiark");
            rs.addRuleConclusion("nawiew", "n_mocny");

            // --- zimna ---
            rs.addRule(1, 2);
            rs.addRuleItem("temp", "zimna", "AND", "wilg", "sucha");
            rs.addRuleConclusion("chlod", "c_brak");
            rs.addRuleConclusion("nawiew", "n_slaby");

            rs.addRule(1, 2);
            rs.addRuleItem("temp", "zimna", "AND", "wilg", "normalna");
            rs.addRuleConclusion("chlod", "c_slabe");
            rs.addRuleConclusion("nawiew", "n_umiark");

            rs.addRule(1, 2);
            rs.addRuleItem("temp", "zimna", "AND", "wilg", "wilgotna");
            rs.addRuleConclusion("chlod", "c_umiark");
            rs.addRuleConclusion("nawiew", "n_mocny");

            rs.addRule(1, 2);
            rs.addRuleItem("temp", "zimna", "AND", "wilg", "b_wilgotna");
            rs.addRuleConclusion("chlod", "c_mocne");
            rs.addRuleConclusion("nawiew", "n_maks");

            // --- ciepla ---
            rs.addRule(1, 2);
            rs.addRuleItem("temp", "ciepla", "AND", "wilg", "sucha");
            rs.addRuleConclusion("chlod", "c_slabe");
            rs.addRuleConclusion("nawiew", "n_umiark");

            rs.addRule(1, 2);
            rs.addRuleItem("temp", "ciepla", "AND", "wilg", "normalna");
            rs.addRuleConclusion("chlod", "c_umiark");
            rs.addRuleConclusion("nawiew", "n_mocny");

            rs.addRule(1, 2);
            rs.addRuleItem("temp", "ciepla", "AND", "wilg", "wilgotna");
            rs.addRuleConclusion("chlod", "c_mocne");
            rs.addRuleConclusion("nawiew", "n_maks");

            rs.addRule(1, 2);
            rs.addRuleItem("temp", "ciepla", "AND", "wilg", "b_wilgotna");
            rs.addRuleConclusion("chlod", "c_maks");
            rs.addRuleConclusion("nawiew", "n_maks");

            // --- goraca ---
            rs.addRule(1, 2);
            rs.addRuleItem("temp", "goraca", "AND", "wilg", "sucha");
            rs.addRuleConclusion("chlod", "c_umiark");
            rs.addRuleConclusion("nawiew", "n_mocny");

            rs.addRule(1, 2);
            rs.addRuleItem("temp", "goraca", "AND", "wilg", "normalna");
            rs.addRuleConclusion("chlod", "c_mocne");
            rs.addRuleConclusion("nawiew", "n_maks");

            rs.addRule(1, 2);
            rs.addRuleItem("temp", "goraca", "AND", "wilg", "wilgotna");
            rs.addRuleConclusion("chlod", "c_maks");
            rs.addRuleConclusion("nawiew", "n_maks");

            rs.addRule(1, 2);
            rs.addRuleItem("temp", "goraca", "AND", "wilg", "b_wilgotna");
            rs.addRuleConclusion("chlod", "c_maks");
            rs.addRuleConclusion("nawiew", "n_maks");

        } catch (Exception e) {
            System.out.println("Blad reguly: " + e.getMessage());
        }

        // ==================== POBRANIE DANYCH I WNIOSKOWANIE ====================

        Scanner scanner = new Scanner(System.in);
        System.out.print("Podaj temperature (0-40 C): ");
        double temperatura = scanner.nextDouble();
        System.out.print("Podaj wilgotnosc (0-100 %): ");
        double wilgotnosc = scanner.nextDouble();
        scanner.close();

        rs.setInput(0, temperatura);
        rs.setInput(1, wilgotnosc);
        rs.Process();

        double chlodzenie = rs.getOutputVar(0).outset.DeFuzzyfy();
        double nawiew     = rs.getOutputVar(1).outset.DeFuzzyfy();

        System.out.println();
        System.out.printf("Temperatura: %.1f C,  Wilgotnosc: %.1f %%%n", temperatura, wilgotnosc);
        System.out.printf("Chlodzenie:  %.1f %%%n", chlodzenie);
        System.out.printf("Nawiew:      %.1f %%%n", nawiew);
    }
}
