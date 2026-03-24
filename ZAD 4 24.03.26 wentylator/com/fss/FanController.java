package com.fss;

import java.util.Scanner;
import fuzzlib.FuzzySet;
import fuzzlib.norms.TNMin;
import fuzzlib.norms.TNProduct;
import fuzzlib.norms.SNMax;

public class FanController {

    public static void main(String[] args) {

        // zbiory wejsciowe - temperatura
        FuzzySet tempNiska = new FuzzySet();
        FuzzySet tempSrednia = new FuzzySet();
        FuzzySet tempWysoka = new FuzzySet();

        tempNiska.addPoint(0.0, 1.0);
        tempNiska.addPoint(10.0, 1.0);
        tempNiska.addPoint(20.0, 0.0);

        tempSrednia.addPoint(15.0, 0.0);
        tempSrednia.addPoint(25.0, 1.0);
        tempSrednia.addPoint(35.0, 0.0);

        tempWysoka.addPoint(30.0, 0.0);
        tempWysoka.addPoint(40.0, 1.0);
        tempWysoka.addPoint(50.0, 1.0);

        // zbiory wyjsciowe - moc wentylatora
        FuzzySet wenOff = new FuzzySet();
        FuzzySet wenSredni = new FuzzySet();
        FuzzySet wenPelny = new FuzzySet();

        wenOff.addPoint(0.0, 1.0);
        wenOff.addPoint(10.0, 1.0);
        wenOff.addPoint(20.0, 0.0);

        wenSredni.addPoint(30.0, 0.0);
        wenSredni.addPoint(50.0, 1.0);
        wenSredni.addPoint(70.0, 0.0);

        wenPelny.addPoint(80.0, 0.0);
        wenPelny.addPoint(90.0, 1.0);
        wenPelny.addPoint(100.0, 1.0);

        // pobranie temperatury od uzytkownika
        Scanner scanner = new Scanner(System.in);
        System.out.print("Podaj temperature (C): ");
        double temperatura = scanner.nextDouble();
        scanner.close();

        // fuzzyfikacja - obliczenie przynaleznosci
        double muNiska   = tempNiska.getMembership(temperatura);
        double muSrednia = tempSrednia.getMembership(temperatura);
        double muWysoka  = tempWysoka.getMembership(temperatura);

        // odcinanie zbiorow wyjsciowych (TNMin - odcina zbior pozioma linia na poziomie mu)
        TNMin tnmin = new TNMin();

        FuzzySet alphaMin1 = new FuzzySet();
        alphaMin1.addPoint(0.0, muNiska);
        alphaMin1.addPoint(100.0, muNiska);
        FuzzySet odcietyOffMin = new FuzzySet();
        FuzzySet.processSetsWithNorm(odcietyOffMin, alphaMin1, wenOff, tnmin);

        FuzzySet alphaMin2 = new FuzzySet();
        alphaMin2.addPoint(0.0, muSrednia);
        alphaMin2.addPoint(100.0, muSrednia);
        FuzzySet odcietySredniMin = new FuzzySet();
        FuzzySet.processSetsWithNorm(odcietySredniMin, alphaMin2, wenSredni, tnmin);

        FuzzySet alphaMin3 = new FuzzySet();
        alphaMin3.addPoint(0.0, muWysoka);
        alphaMin3.addPoint(100.0, muWysoka);
        FuzzySet odcietyPelnyMin = new FuzzySet();
        FuzzySet.processSetsWithNorm(odcietyPelnyMin, alphaMin3, wenPelny, tnmin);

        // agregacja przez SNorm (max) i defuzzyfikacja - TNMin
        SNMax snmax = new SNMax();
        FuzzySet agr1 = new FuzzySet();
        FuzzySet agr2 = new FuzzySet();
        FuzzySet.processSetsWithNorm(agr1, odcietyOffMin, odcietySredniMin, snmax);
        FuzzySet.processSetsWithNorm(agr2, agr1, odcietyPelnyMin, snmax);
        agr2.PackFlatSections();
        double wynikMin = agr2.DeFuzzyfy();

        // odcinanie zbiorow wyjsciowych (TNProduct - skaluje zbior w dol przez mnozenie przez mu, zachowuje ksztalt)
        TNProduct tnproduct = new TNProduct();

        FuzzySet alphaPr1 = new FuzzySet();
        alphaPr1.addPoint(0.0, muNiska);
        alphaPr1.addPoint(100.0, muNiska);
        FuzzySet odcietyOffPr = new FuzzySet();
        FuzzySet.processSetsWithNorm(odcietyOffPr, alphaPr1, wenOff, tnproduct);

        FuzzySet alphaPr2 = new FuzzySet();
        alphaPr2.addPoint(0.0, muSrednia);
        alphaPr2.addPoint(100.0, muSrednia);
        FuzzySet odcietySredniPr = new FuzzySet();
        FuzzySet.processSetsWithNorm(odcietySredniPr, alphaPr2, wenSredni, tnproduct);

        FuzzySet alphaPr3 = new FuzzySet();
        alphaPr3.addPoint(0.0, muWysoka);
        alphaPr3.addPoint(100.0, muWysoka);
        FuzzySet odcietyPelnyPr = new FuzzySet();
        FuzzySet.processSetsWithNorm(odcietyPelnyPr, alphaPr3, wenPelny, tnproduct);

        // agregacja przez SNorm (max) i defuzzyfikacja - TNProduct
        FuzzySet agr3 = new FuzzySet();
        FuzzySet agr4 = new FuzzySet();
        FuzzySet.processSetsWithNorm(agr3, odcietyOffPr, odcietySredniPr, snmax);
        FuzzySet.processSetsWithNorm(agr4, agr3, odcietyPelnyPr, snmax);
        agr4.PackFlatSections();
        double wynikProduct = agr4.DeFuzzyfy();

        System.out.printf("TNMin:     %.1f%%%n", wynikMin);
        System.out.printf("TNProduct: %.1f%%%n", wynikProduct);
    }
}
