# Problema Rucsacului - Knapsack Problem (Java)

## Descriere

Acest proiect implementează și compară trei algoritmi pentru rezolvarea problemei rucsacului (0/1 Knapsack Problem):

1. **Backtracking cu Pruning** - Soluție exactă, explorează toate combinațiile posibile cu tăiere a ramurilor suboptime
2. **Programare Dinamică** - Soluție optimă garantată, folosind o matrice pentru memorare
3. **Greedy (Euristic)** - Soluție aproximativă rapidă, selectează obiectele după raportul valoare/greutate

## Structura Proiectului

```
Proiect_AA/
├── src/
│   ├── Item.java              # Record pentru obiecte (greutate, valoare, index)
│   ├── KnapsackResult.java    # Record pentru rezultate
│   ├── KnapsackSolver.java    # Implementarea celor 3 algoritmi
│   ├── TestGenerator.java     # Generator de teste
│   ├── KnapsackBenchmark.java # Benchmarking cu măsurare timp
│   └── Main.java              # Punct de intrare
├── knapsack_tests/            # Directorul cu testele generate
├── Makefile                   # Automatizare build
└── README.md                  # Acest fișier
```

## Cerințe

- **Java 25** (sau compatibil cu Java 16+ pentru records)
- **Make** (opțional, pentru automatizare)

## Cum se folosește

### Cu Make (recomandat)

```bash
# Compilare
make build

# Generare teste
make generate

# Rulare benchmark
make run

# Tot într-o singură comandă
make benchmark

# Curățare
make clean

# Ajutor
make help
```

### Manual

```bash
# Compilare
mkdir -p out
javac -d out src/*.java

# Generare teste
java -cp out Main generate

# Rulare benchmark
java -cp out Main run
```

## Complexitate Algoritmi

| Algoritm | Timp | Spațiu | Optimalitate |
|----------|------|--------|--------------|
| Backtracking | O(2^n) worst case | O(n) | Exactă ✓ |
| Programare Dinamică | O(n × W) | O(n × W) | Exactă ✓ |
| Greedy | O(n log n) | O(n) | Aproximativă |

Unde:
- `n` = numărul de obiecte
- `W` = capacitatea rucsacului

## Teste Generate

Generatorul creează 20 de teste cu diverse configurații:

### Cazuri Normale
- Test 1: 10 obiecte, capacitate 50
- Test 2: 50 obiecte, capacitate 500
- Test 3: 100 obiecte, capacitate 1000

### Cazuri Extreme
- Test 4: 1 obiect minim
- Test 5: 1 obiect mare
- Test 6: 10000 obiecte ușoare
- Test 7: Capacitate foarte mică
- Test 8: Valori maxime

### Cazuri Aleatorii
- Teste 9-20: Configurații aleatorii

## Export CSV pentru Grafice (Raport AA)

După rularea benchmark-ului, se generează automat fișierul `rezultate.csv`:

```csv
Test,NumarObiecte,Capacitate,Algoritm,Timp_Secunde,Timp_Ms,Valoare,Skipped
test_1.txt,10,50,Backtracking,0.001264,1.264,433,NU
test_1.txt,10,50,DP,0.000081,0.081,433,NU
test_1.txt,10,50,Greedy,0.000038,0.038,428,NU
```

**Acest CSV poate fi folosit pentru:**
- Grafice în **Excel** (Import Data → From CSV)
- Grafice în **Python** cu `pandas` și `matplotlib`
- Grafice în **Matlab**

### Exemplu Python pentru grafice:

```python
import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv('rezultate.csv')
df_valid = df[df['Skipped'] == 'NU']

# Grafic: Timp vs Număr Obiecte
for alg in ['Backtracking', 'DP', 'Greedy']:
    subset = df_valid[df_valid['Algoritm'] == alg]
    plt.plot(subset['NumarObiecte'], subset['Timp_Ms'], 'o-', label=alg)

plt.xlabel('Număr Obiecte')
plt.ylabel('Timp (ms)')
plt.legend()
plt.savefig('grafic_timp.png')
```

## Exemple de Output Console

```
================================================================
Test: test_1.txt
================================================================
Numar obiecte: 10, Capacitate: 50

--- Backtracking ---
Valoare maxima: 433
Timp executie: 0.001264 s (1.264 ms)

--- Programare Dinamica ---
Valoare maxima: 433
Timp executie: 0.000081 s (0.081 ms)

--- Greedy ---
Valoare aproximativa: 428
Timp executie: 0.000038 s (0.038 ms)

================================================================
Rezultatele au fost exportate in: rezultate.csv
```

## Surse și Referințe

1. **Algoritmi și Structuri de Date**
   - [Google OR-Tools - Knapsack](https://developers.google.com/optimization/pack/knapsack)
   - [GeeksforGeeks - 0/1 Knapsack](https://www.geeksforgeeks.org/0-1-knapsack-problem-dp-10/)
   - [Wikipedia - Knapsack Problem](https://en.wikipedia.org/wiki/Knapsack_problem)

2. **Documentație Java**
   - [Oracle Java Documentation](https://docs.oracle.com/en/java/)

## Autor

Proiect pentru cursul de Analiza Algoritmilor (AA) - 2025-2026

---

> **Notă:** Acest proiect este conceput pentru evaluarea și compararea algoritmilor pentru problema rucsacului. Rezultatele benchmark-ului pot fi folosite pentru secțiunea de Evaluare din raportul proiectului.
