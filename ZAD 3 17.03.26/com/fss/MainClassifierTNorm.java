package com.fss;

import neuralnetwork.data.DataPackage;
import neuralnetwork.data.DataVector;
import fuzzlib.norms.*;

public class MainClassifierTNorm {

    public static void main(String[] args) {

        DataPackage all = new DataPackage();
        all.setFieldSeparator(",");
        all.setDecimalSeparator('.');
        all.loadTextFile("wines.txt");

        DataPackage class1 = all.removeVectorsByColumnValue(0, 1);
        DataPackage class2 = all.removeVectorsByColumnValue(0, 2);
        DataPackage class3 = all.removeVectorsByColumnValue(0, 3);

        DataPackage learn = class1.removeRandomVectors(30);
        learn.add(class2.removeRandomVectors(30));
        learn.add(class3.removeRandomVectors(30));

        DataPackage test = new DataPackage();
        test.add(class1);
        test.add(class2);
        test.add(class3);

        TNorm[] tnorms = { new TNMin(), new TNProduct(), new TNLukas(),
                           new TNDrastic(), new TNNilpot(), new TNHamacher(), new TNEinstein() };

        for (TNorm tnorm : tnorms) {
            int correct = 0;
            for (DataVector tv : test.getList()) {
                double maxVal = -1;
                double predClass = -1;
                for (DataVector lv : learn.getList()) {
                    double m = 1.0;
                    for (int i = 1; i <= 13; i++)
                        m = tnorm.calc(m, gaussian(tv.get(i), learn.getColumnRange(i), lv.get(i)));
                    if (m > maxVal) { maxVal = m; predClass = lv.get(0); }
                }
                if (predClass == tv.get(0)) correct++;
            }
            System.out.printf("%-15s %d/%d  %.1f%%%n", tnorm, correct, test.size(), 100.0 * correct / test.size());
        }
    }

    static double gaussian(double value, double width, double center) {
        double w = 1.0 / width, d = value - center;
        return Math.exp(-d * d / 2 * w * w);
    }
}
