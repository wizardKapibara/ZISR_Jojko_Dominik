import math

def funkcja_gaussa(x, srednia, odchylenie_stand):
    if odchylenie_stand == 0:
        return 1.0 if x == srednia else 0.0

    wykladnik = -((x - srednia) ** 2) / (2 * (odchylenie_stand ** 2))
    return math.exp(wykladnik)

parametry_klas = {
    "Iris-setosa": {
        "sepal_length": {"mean": 5.01, "std": 0.35},
        "sepal_width":  {"mean": 3.42, "std": 0.38},
        "petal_length": {"mean": 1.46, "std": 0.17},
        "petal_width":  {"mean": 0.24, "std": 0.11}
    },
    "Iris-versicolor": {
        "sepal_length": {"mean": 5.94, "std": 0.51},
        "sepal_width":  {"mean": 2.77, "std": 0.31},
        "petal_length": {"mean": 4.26, "std": 0.47},
        "petal_width":  {"mean": 1.33, "std": 0.20}
    },
    "Iris-virginica": {
        "sepal_length": {"mean": 6.59, "std": 0.63},
        "sepal_width":  {"mean": 2.97, "std": 0.32},
        "petal_length": {"mean": 5.55, "std": 0.55},
        "petal_width":  {"mean": 2.03, "std": 0.27}
    }
}

def klasyfikuj_irysa(sl, sw, pl, pw):

    wyniki_przynaleznosci = {}

    for klasa, cechy in parametry_klas.items():
        p_sl = funkcja_gaussa(sl, cechy["sepal_length"]["mean"], cechy["sepal_length"]["std"])
        p_sw = funkcja_gaussa(sw, cechy["sepal_width"]["mean"], cechy["sepal_width"]["std"])
        p_pl = funkcja_gaussa(pl, cechy["petal_length"]["mean"], cechy["petal_length"]["std"])
        p_pw = funkcja_gaussa(pw, cechy["petal_width"]["mean"], cechy["petal_width"]["std"])

        calkowita_przynaleznosc = p_sl + p_sw + p_pl + p_pw
        wyniki_przynaleznosci[klasa] = calkowita_przynaleznosc

    return wyniki_przynaleznosci

def pobierz_wartosc(nazwa_cechy):
    while True:
        try:
            wartosc = float(input(f"Podaj {nazwa_cechy} "))
            return wartosc
        except ValueError:
            print("Nieprawidłowe wejście. Proszę podać liczbę.")

def main():

    sl = pobierz_wartosc("Sepal Length")
    sw = pobierz_wartosc("Sepal Width")
    pl = pobierz_wartosc("Petal Length")
    pw = pobierz_wartosc("Petal Width")

    wyniki = klasyfikuj_irysa(sl, sw, pl, pw)

    najbardziej_prawdopodobna = max(wyniki, key=wyniki.get)
    max_wynik = wyniki[najbardziej_prawdopodobna]

    print("\n")
    if max_wynik > 0:
        print(f"Jest to najprawdopodobniej:")
        print(f"{najbardziej_prawdopodobna.upper()}")
    else:
        print("Wyniki odbiegają")
    print("\n")

if __name__ == "__main__":
    main()