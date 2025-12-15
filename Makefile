# Knapsack Project - Java Build
# Proiect AA - Analiza Algoritmilor

# Configurare
JAVA := java
JAVAC := javac
SRC_DIR := src
OUT_DIR := out
TEST_DIR := knapsack_tests

# Găsim toate fișierele sursă
SOURCES := $(wildcard $(SRC_DIR)/*.java)
CLASSES := $(patsubst $(SRC_DIR)/%.java,$(OUT_DIR)/%.class,$(SOURCES))

# Regula implicită
.PHONY: default
default: build

# Compilare
.PHONY: build
build: $(OUT_DIR)
	$(JAVAC) -d $(OUT_DIR) $(SOURCES)
	@echo "Compilare reușită! Clasele sunt în directorul '$(OUT_DIR)'"

$(OUT_DIR):
	mkdir -p $(OUT_DIR)

# Generare teste
.PHONY: generate
generate: build
	$(JAVA) -cp $(OUT_DIR) Main generate

# Rulare benchmark
.PHONY: run
run: build
	$(JAVA) -cp $(OUT_DIR) Main run

# Benchmark complet (generare + rulare)
.PHONY: benchmark
benchmark: build generate run

# Curățare
.PHONY: clean
clean:
	rm -rf $(OUT_DIR)
	rm -rf $(TEST_DIR)
	@echo "Curățare completă!"

# Ajutor
.PHONY: help
help:
	@echo "╔════════════════════════════════════════════════════════════╗"
	@echo "║         Proiect Knapsack - Makefile Help                   ║"
	@echo "╚════════════════════════════════════════════════════════════╝"
	@echo ""
	@echo "Comenzi disponibile:"
	@echo "  make build     - Compilează toate fișierele Java"
	@echo "  make generate  - Generează testele în knapsack_tests/"
	@echo "  make run       - Rulează benchmark-ul pe teste"
	@echo "  make benchmark - Compilează, generează și rulează"
	@echo "  make clean     - Șterge fișierele generate"
	@echo "  make help      - Afișează acest mesaj"
	@echo ""
	@echo "Exemplu de utilizare:"
	@echo "  make benchmark"
