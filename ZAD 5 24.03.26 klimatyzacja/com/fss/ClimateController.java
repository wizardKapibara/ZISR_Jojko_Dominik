package com.fss;

import java.util.Scanner;
import fuzzlib.FuzzySet;
import fuzzlib.norms.TNMin;
import fuzzlib.norms.SNMax;

public class ClimateController {

    public static void main(String[] args) {

        // zbiory wejsciowe - temperatura (C)
        FuzzySet tempBardzoZimna = new FuzzySet();
        FuzzySet tempZimna       = new FuzzySet();
        FuzzySet tempKomfortowa  = new FuzzySet();
        FuzzySet tempCiepla      = new FuzzySet();
        FuzzySet tempGoraca      = new FuzzySet();
        FuzzySet tempBardzoGoraca = new FuzzySet();

        tempBardzoZimna.addPoint(0.0,  1.0);
        tempBardzoZimna.addPoint(10.0, 1.0);
        tempBardzoZimna.addPoint(16.0, 0.0);

        tempZimna.addPoint(12.0, 0.0);
        tempZimna.addPoint(17.0, 1.0);
        tempZimna.addPoint(21.0, 0.0);

        tempKomfortowa.addPoint(18.0, 0.0);
        tempKomfortowa.addPoint(22.0, 1.0);
        tempKomfortowa.addPoint(26.0, 0.0);

        tempCiepla.addPoint(23.0, 0.0);
        tempCiepla.addPoint(27.0, 1.0);
        tempCiepla.addPoint(32.0, 0.0);

        tempGoraca.addPoint(29.0, 0.0);
        tempGoraca.addPoint(33.0, 1.0);
        tempGoraca.addPoint(38.0, 0.0);

        tempBardzoGoraca.addPoint(35.0, 0.0);
        tempBardzoGoraca.addPoint(40.0, 1.0);
        tempBardzoGoraca.addPoint(50.0, 1.0);

        // zbiory wejsciowe - wilgotnosc (%)
        FuzzySet wilgBardzoSucha    = new FuzzySet();
        FuzzySet wilgSucha          = new FuzzySet();
        FuzzySet wilgNormalna       = new FuzzySet();
        FuzzySet wilgWilgotna       = new FuzzySet();
        FuzzySet wilgBardzoWilgotna = new FuzzySet();

        wilgBardzoSucha.addPoint(0.0,  1.0);
        wilgBardzoSucha.addPoint(20.0, 1.0);
        wilgBardzoSucha.addPoint(35.0, 0.0);

        wilgSucha.addPoint(20.0, 0.0);
        wilgSucha.addPoint(35.0, 1.0);
        wilgSucha.addPoint(50.0, 0.0);

        wilgNormalna.addPoint(40.0, 0.0);
        wilgNormalna.addPoint(55.0, 1.0);
        wilgNormalna.addPoint(65.0, 0.0);

        wilgWilgotna.addPoint(60.0, 0.0);
        wilgWilgotna.addPoint(72.0, 1.0);
        wilgWilgotna.addPoint(85.0, 0.0);

        wilgBardzoWilgotna.addPoint(78.0,  0.0);
        wilgBardzoWilgotna.addPoint(90.0,  1.0);
        wilgBardzoWilgotna.addPoint(100.0, 1.0);

        // zbiory wyjsciowe - chlodzenie (0-100%)
        FuzzySet chlBrak        = new FuzzySet();
        FuzzySet chlSlabe       = new FuzzySet();
        FuzzySet chlUmiarkowane = new FuzzySet();
        FuzzySet chlMocne       = new FuzzySet();
        FuzzySet chlBardzoMocne = new FuzzySet();
        FuzzySet chlMaksymalne  = new FuzzySet();

        chlBrak.addPoint(0.0,  1.0);
        chlBrak.addPoint(5.0,  1.0);
        chlBrak.addPoint(15.0, 0.0);

        chlSlabe.addPoint(10.0, 0.0);
        chlSlabe.addPoint(20.0, 1.0);
        chlSlabe.addPoint(32.0, 0.0);

        chlUmiarkowane.addPoint(28.0, 0.0);
        chlUmiarkowane.addPoint(40.0, 1.0);
        chlUmiarkowane.addPoint(52.0, 0.0);

        chlMocne.addPoint(48.0, 0.0);
        chlMocne.addPoint(60.0, 1.0);
        chlMocne.addPoint(72.0, 0.0);

        chlBardzoMocne.addPoint(68.0, 0.0);
        chlBardzoMocne.addPoint(80.0, 1.0);
        chlBardzoMocne.addPoint(90.0, 0.0);

        chlMaksymalne.addPoint(85.0,  0.0);
        chlMaksymalne.addPoint(93.0,  1.0);
        chlMaksymalne.addPoint(100.0, 1.0);

        // zbiory wyjsciowe - nawiew (0-100%)
        FuzzySet nawBrak        = new FuzzySet();
        FuzzySet nawSlaby       = new FuzzySet();
        FuzzySet nawUmiarkowany = new FuzzySet();
        FuzzySet nawMocny       = new FuzzySet();
        FuzzySet nawBardzoMocny = new FuzzySet();
        FuzzySet nawMaksymalny  = new FuzzySet();

        nawBrak.addPoint(0.0,  1.0);
        nawBrak.addPoint(5.0,  1.0);
        nawBrak.addPoint(15.0, 0.0);

        nawSlaby.addPoint(10.0, 0.0);
        nawSlaby.addPoint(20.0, 1.0);
        nawSlaby.addPoint(32.0, 0.0);

        nawUmiarkowany.addPoint(28.0, 0.0);
        nawUmiarkowany.addPoint(40.0, 1.0);
        nawUmiarkowany.addPoint(52.0, 0.0);

        nawMocny.addPoint(48.0, 0.0);
        nawMocny.addPoint(60.0, 1.0);
        nawMocny.addPoint(72.0, 0.0);

        nawBardzoMocny.addPoint(68.0, 0.0);
        nawBardzoMocny.addPoint(80.0, 1.0);
        nawBardzoMocny.addPoint(90.0, 0.0);

        nawMaksymalny.addPoint(85.0,  0.0);
        nawMaksymalny.addPoint(93.0,  1.0);
        nawMaksymalny.addPoint(100.0, 1.0);

        // pobranie danych od uzytkownika
        Scanner scanner = new Scanner(System.in);
        System.out.print("Podaj temperature (C): ");
        double temperatura = scanner.nextDouble();
        System.out.print("Podaj wilgotnosc (%):  ");
        double wilgotnosc = scanner.nextDouble();
        scanner.close();

        // fuzzyfikacja temperatury
        double muBardzoZimna  = tempBardzoZimna.getMembership(temperatura);
        double muZimna        = tempZimna.getMembership(temperatura);
        double muKomfortowa   = tempKomfortowa.getMembership(temperatura);
        double muCiepla       = tempCiepla.getMembership(temperatura);
        double muGoraca       = tempGoraca.getMembership(temperatura);
        double muBardzoGoraca = tempBardzoGoraca.getMembership(temperatura);

        // fuzzyfikacja wilgotnosci
        double muBardzoSucha    = wilgBardzoSucha.getMembership(wilgotnosc);
        double muSucha          = wilgSucha.getMembership(wilgotnosc);
        double muNormalna       = wilgNormalna.getMembership(wilgotnosc);
        double muWilgotna       = wilgWilgotna.getMembership(wilgotnosc);
        double muBardzoWilgotna = wilgBardzoWilgotna.getMembership(wilgotnosc);

        TNMin tnmin = new TNMin();
        SNMax snmax = new SNMax();

        // reguly dla chlodzenia:
        // temperatura -> chlodzenie
        FuzzySet c1 = clipSet(chlBrak,        muBardzoZimna,  tnmin); // bardzo zimna -> brak
        FuzzySet c2 = clipSet(chlBrak,        muZimna,        tnmin); // zimna -> brak
        FuzzySet c3 = clipSet(chlSlabe,       muKomfortowa,   tnmin); // komfortowa -> slabe
        FuzzySet c4 = clipSet(chlUmiarkowane, muCiepla,       tnmin); // ciepla -> umiarkowane
        FuzzySet c5 = clipSet(chlMocne,       muGoraca,       tnmin); // goraca -> mocne
        FuzzySet c6 = clipSet(chlMaksymalne,  muBardzoGoraca, tnmin); // bardzo goraca -> maksymalne
        // wilgotnosc -> chlodzenie
        FuzzySet c7  = clipSet(chlBrak,        muBardzoSucha,    tnmin); // bardzo sucha -> brak
        FuzzySet c8  = clipSet(chlBrak,        muSucha,          tnmin); // sucha -> brak
        FuzzySet c9  = clipSet(chlSlabe,       muNormalna,       tnmin); // normalna -> slabe
        FuzzySet c10 = clipSet(chlUmiarkowane, muWilgotna,       tnmin); // wilgotna -> umiarkowane
        FuzzySet c11 = clipSet(chlBardzoMocne, muBardzoWilgotna, tnmin); // bardzo wilgotna -> bardzo mocne

        // agregacja chlodzenia
        FuzzySet a1  = new FuzzySet(); FuzzySet.processSetsWithNorm(a1,  c1,  c2,  snmax);
        FuzzySet a2  = new FuzzySet(); FuzzySet.processSetsWithNorm(a2,  a1,  c3,  snmax);
        FuzzySet a3  = new FuzzySet(); FuzzySet.processSetsWithNorm(a3,  a2,  c4,  snmax);
        FuzzySet a4  = new FuzzySet(); FuzzySet.processSetsWithNorm(a4,  a3,  c5,  snmax);
        FuzzySet a5  = new FuzzySet(); FuzzySet.processSetsWithNorm(a5,  a4,  c6,  snmax);
        FuzzySet a6  = new FuzzySet(); FuzzySet.processSetsWithNorm(a6,  a5,  c7,  snmax);
        FuzzySet a7  = new FuzzySet(); FuzzySet.processSetsWithNorm(a7,  a6,  c8,  snmax);
        FuzzySet a8  = new FuzzySet(); FuzzySet.processSetsWithNorm(a8,  a7,  c9,  snmax);
        FuzzySet a9  = new FuzzySet(); FuzzySet.processSetsWithNorm(a9,  a8,  c10, snmax);
        FuzzySet a10 = new FuzzySet(); FuzzySet.processSetsWithNorm(a10, a9,  c11, snmax);
        a10.PackFlatSections();
        double wynikChlodzenia = a10.DeFuzzyfy();

        // reguly dla nawiewu:
        // temperatura -> nawiew
        FuzzySet n1 = clipSet(nawBrak,        muBardzoZimna,  tnmin); // bardzo zimna -> brak
        FuzzySet n2 = clipSet(nawSlaby,       muZimna,        tnmin); // zimna -> slaby
        FuzzySet n3 = clipSet(nawSlaby,       muKomfortowa,   tnmin); // komfortowa -> slaby
        FuzzySet n4 = clipSet(nawUmiarkowany, muCiepla,       tnmin); // ciepla -> umiarkowany
        FuzzySet n5 = clipSet(nawMocny,       muGoraca,       tnmin); // goraca -> mocny
        FuzzySet n6 = clipSet(nawMaksymalny,  muBardzoGoraca, tnmin); // bardzo goraca -> maksymalny
        // wilgotnosc -> nawiew
        FuzzySet n7  = clipSet(nawSlaby,       muBardzoSucha,    tnmin); // bardzo sucha -> slaby
        FuzzySet n8  = clipSet(nawSlaby,       muSucha,          tnmin); // sucha -> slaby
        FuzzySet n9  = clipSet(nawUmiarkowany, muNormalna,       tnmin); // normalna -> umiarkowany
        FuzzySet n10 = clipSet(nawMocny,       muWilgotna,       tnmin); // wilgotna -> mocny
        FuzzySet n11 = clipSet(nawMaksymalny,  muBardzoWilgotna, tnmin); // bardzo wilgotna -> maksymalny

        // agregacja nawiewu
        FuzzySet b1  = new FuzzySet(); FuzzySet.processSetsWithNorm(b1,  n1,  n2,  snmax);
        FuzzySet b2  = new FuzzySet(); FuzzySet.processSetsWithNorm(b2,  b1,  n3,  snmax);
        FuzzySet b3  = new FuzzySet(); FuzzySet.processSetsWithNorm(b3,  b2,  n4,  snmax);
        FuzzySet b4  = new FuzzySet(); FuzzySet.processSetsWithNorm(b4,  b3,  n5,  snmax);
        FuzzySet b5  = new FuzzySet(); FuzzySet.processSetsWithNorm(b5,  b4,  n6,  snmax);
        FuzzySet b6  = new FuzzySet(); FuzzySet.processSetsWithNorm(b6,  b5,  n7,  snmax);
        FuzzySet b7  = new FuzzySet(); FuzzySet.processSetsWithNorm(b7,  b6,  n8,  snmax);
        FuzzySet b8  = new FuzzySet(); FuzzySet.processSetsWithNorm(b8,  b7,  n9,  snmax);
        FuzzySet b9  = new FuzzySet(); FuzzySet.processSetsWithNorm(b9,  b8,  n10, snmax);
        FuzzySet b10 = new FuzzySet(); FuzzySet.processSetsWithNorm(b10, b9,  n11, snmax);
        b10.PackFlatSections();
        double wynikNawiewu = b10.DeFuzzyfy();

        System.out.printf("Chlodzenie: %.1f%%%n", wynikChlodzenia);
        System.out.printf("Nawiew:     %.1f%%%n", wynikNawiewu);
    }

    static FuzzySet clipSet(FuzzySet set, double alpha, TNMin tnmin) {
        FuzzySet alphaSet = new FuzzySet();
        alphaSet.addPoint(0.0,   alpha);
        alphaSet.addPoint(100.0, alpha);
        FuzzySet result = new FuzzySet();
        FuzzySet.processSetsWithNorm(result, alphaSet, set, tnmin);
        return result;
    }
}
