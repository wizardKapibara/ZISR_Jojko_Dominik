package com.fss;

import neuralnetwork.data.DataPackage;
import neuralnetwork.data.DataVector;

import fuzzlib.norms.TNorm;
import fuzzlib.norms.TNMin;
import fuzzlib.norms.TNProduct;
import fuzzlib.norms.TNLukas;
import fuzzlib.norms.TNDrastic;
import fuzzlib.norms.TNNilpot;
import fuzzlib.norms.TNHamacher;
import fuzzlib.norms.TNEinstein;

/**
 * Zadanie 3 - Klasyfikator win z użyciem T-Norm z biblioteki fuzzlib.
 *
 * Analogia do Zadania 2, ale zamiast bezpośredniego mnożenia przynależności
 * stosowane są T-Normy (operacje przecięcia zbiorów rozmytych) z biblioteki fuzzlib:
 *   - TNMin      : min(a, b)
 *   - TNProduct  : a * b  (jak w ZAD 2)
 *   - TNLukas    : max(0, a + b - 1)   Lukasiewicz
 *   - TNDrastic  : b jeśli a=1, a jeśli b=1, 0 wpp
 *   - TNNilpot   : min(a,b) jeśli a+b > 1, else 0
 *   - TNHamacher : (a*b) / (a + b - a*b)  gdy a+b > 0, else 0
 *   - TNEinstein : (a*b) / (2 - (a + b - a*b))
 */
public class MainClassifierTNorm {

    static final int NUM_FEATURES = 13; // kolumny 1..13 (kolumna 0 = klasa)
    static final int LEARN_SIZE   = 30; // liczba próbek uczących na klasę

    public static void main(String[] args) {

        // 1. Wczytaj dane
        DataPackage all = new DataPackage();
        all.setFieldSeparator(",");
        all.setDecimalSeparator('.');
        all.loadTextFile("wines.txt");

        // Podziel na klasy
        DataPackage class1 = all.removeVectorsByColumnValue(0, 1);
        DataPackage class2 = all.removeVectorsByColumnValue(0, 2);
        DataPackage class3 = all.removeVectorsByColumnValue(0, 3);

        // 2. Zbuduj zbiór uczący (30 próbek z każdej klasy) i testowy (reszta)
        DataPackage learnPkg = class1.removeRandomVectors(LEARN_SIZE);
        learnPkg.add(class2.removeRandomVectors(LEARN_SIZE));
        learnPkg.add(class3.removeRandomVectors(LEARN_SIZE));

        DataPackage testPkg = new DataPackage();
        testPkg.add(class1);
        testPkg.add(class2);
        testPkg.add(class3);

        System.out.println("Liczba próbek uczących : " + learnPkg.size());
        System.out.println("Liczba próbek testowych: " + testPkg.size());
        System.out.println();

        // 3. Testuj dla każdej T-Normy
        TNorm[] tnorms = {
            new TNMin(),
            new TNProduct(),
            new TNLukas(),
            new TNDrastic(),
            new TNNilpot(),
            new TNHamacher(),
            new TNEinstein()
        };

        for (TNorm tnorm : tnorms) {
            int correct = 0;
            int total   = testPkg.size();

            for (DataVector testVector : testPkg.getList()) {
                double maxResult  = -1.0;
                double predClass  = -1.0;

                for (DataVector learnVector : learnPkg.getList()) {
                    // Inicjalizacja: element neutralny T-Normy to 1.0
                    double membership = 1.0;

                    for (int i = 1; i <= NUM_FEATURES; i++) {
                        double gaussVal = getGaussian(
                            testVector.get(i),
                            learnPkg.getColumnRange(i),
                            learnVector.get(i)
                        );
                        // Przecięcie T-Normą z fuzzlib
                        membership = tnorm.calc(membership, gaussVal);
                    }

                    if (membership > maxResult) {
                        maxResult = membership;
                        predClass = learnVector.get(0);
                    }
                }

                if (predClass == testVector.get(0)) {
                    correct++;
                }
            }

            double accuracy = 100.0 * correct / total;
            System.out.printf("T-Norma: %-15s  Poprawne: %3d / %3d   Dokładność: %.1f%%%n",
                tnorm.toString(), correct, total, accuracy);
        }
    }

    /**
     * Wartość przynależności funkcji Gaussa.
     * @param value   testowana wartość cechy
     * @param width   zakres kolumny (max - min) - szerokość funkcji
     * @param center  środek funkcji (wartość z próbki uczącej)
     */
    public static double getGaussian(double value, double width, double center) {
        double w   = 1.0 / width;
        double tmp = value - center;
        return Math.exp((-tmp * tmp) / 2 * w * w);
    }
}
