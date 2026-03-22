# 🧠 Modular CPU Cache Simulator

## 📌 Project Overview
A Java-based desktop application that simulates the internal memory architecture of a CPU. It provides a highly visual, interactive environment to explore how different cache configurations, mapping techniques, and replacement algorithms impact memory performance and hit/miss ratios.

## 🛠️ Technical Stack & Concepts
* **Language:** Java
* **UI Framework:** Java Swing (featuring custom `AbstractTableModel` implementations for real-time memory grid binding).
* **Architecture Concepts Simulated:**
    * **Mapping Types:** Direct Mapped, Fully Associative, N-Way Set Associative.
    * **Replacement Policies:** LRU (Least Recently Used), LFU (Least Frequently Used), FIFO, Random.
    * **Write Policies:** Write-Through, Write-Back (utilizing Dirty Bits).

## ⚙️ Features
* **Visual Memory Debugger:** A live `JTable` that tracks the exact state of every cache line, including Valid bits, Dirty bits, Tags, Data payloads, and policy metadata (timestamps/reference counts).
* **Dynamic Configuration:** Users can hot-swap the cache size, associativity (Ways), and policies, instantly resetting the simulation environment.
* **Live Statistics Dashboard:** Real-time tracking of memory Accesses, Cache Hits, Cache Misses, and overall Hit Rate percentages to compare algorithm efficiency.
* **Granular Logging:** A scrolling event log that tracks memory fetching, evictions, and write-allocations step-by-step.

## 🚀 How to Run
1. Clone the repository to your local machine.
2. Open the project in any standard Java IDE (IntelliJ, Eclipse, etc.).
3. Run the `SimulatorApp` class to launch the GUI.
4. Configure your desired cache geometry in the left panel and click "Reset & Apply Configuration".
5. Input memory addresses and data to perform simulated READ and WRITE operations.