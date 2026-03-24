package fuzzyset;

public class TriangularFuzzySet extends FuzzySet {
    private double a, b, c;

    public TriangularFuzzySet(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public double getMembership(double x) {
        if (x <= a || x >= c) return 0.0;
        if (x == b) return 1.0;
        if (x > a && x < b) return (x - a) / (b - a);
        if (x > b && x < c) return (c - x) / (c - b);
        return 0.0;
    }
}