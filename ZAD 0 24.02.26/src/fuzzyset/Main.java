package fuzzyset;

public class Main {
	
    public static void main(String[] args) {
        FuzzySet gauss = new GaussianFuzzySet(30, 5);
        double[] punkty = {12, 25, 32, 40, 43};
        
        System.out.print("Poziomy przynależności: ");
        for (double x : punkty) {
            System.out.print(gauss.getMembership(x) + " ");
        }
        System.out.println("");
        
        FuzzySet gauss1 = new GaussianFuzzySet(30, 6);
        FuzzySet gauss2 = new GaussianFuzzySet(40, 8);
        
        double p1 = gauss1.getMembership(23);
        double p2 = gauss2.getMembership(28);
        double p1_i_p2 = p1 * p2;
        
        System.out.println("Podobieństwo do określenia 'ciepło': " + p1);
        System.out.println("Podobieństwo do określenia 'wilgotno': " + p2);
        System.out.println("Podobieństwo do określenia 'ciepło i wilgotno': " + p1_i_p2);
    }
}