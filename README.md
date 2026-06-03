# Distributed Game Leaderboard Simulation System

[cite_start]A Java-based simulation of a **Geographically Distributed Game Leaderboard Infrastructure** developed to fulfill the project requirements for the **CSW20103 Distributed Computing System** course module[cite: 1, 2, 3]. 

---

## 📌 Project Overview
This repository provides a modular, object-oriented Java prototype that simulates a multi-region gaming backend infrastructure across three distinct zones: **Asia (Node-A)**, **Europe (Node-B)**, and the **United States (Node-C)**[cite: 304, 311, 427]. The primary goal is to evaluate the system's operational trade-offs across three fundamental distributed computing dimensions:

* Process & Thread Management:** Leverages concurrent player execution threads decoupled from asynchronous worker pools to simulate global peak gaming traffic without blocking issues[cite: 304, 318, 319].
* Naming & Resource Discovery:** Implements a dual-mode service registry capable of resolving node coordinates through **Flat Naming** (direct Hash Table maps) or **Structured Naming** (hierarchical DNS-like tree lookups)[cite: 305, 371].
* *Data Replication & Consistency Strategy:** Operates a configurable replication engine enabling runtime toggling between **Sequential Consistency** (strict synchronous syncing) and **Eventual Consistency** (asynchronous background updates with simulated global network latency)[cite: 306, 416].
* Fault Tolerance:** Analyzes system behavior, cluster divergence, and data availability under an injected, abrupt regional server crash scenario[cite: 307, 429].

---

## 🛠️ System Requirements
Before setting up, ensure you have the following installed locally:
* **Java Development Kit (JDK):** Version 8 or higher (JDK 11 / 17 or later recommended).
* **Git Client:** To clone and manage your repository branches.
* **Terminal / CLI Tool:** (e.g., Bash, Zsh, Command Prompt, or PowerShell).

---

## 📂 Repository Directory Layout
The project maintains strict folder isolation between source configurations and compiled bytecodes:

```text
JAVAPROJECT/
│
├── .vscode/                    # IDE specific environmental settings
│   └── settings.json
│
├── bin/                        # Target folder for compiled bytecode classes (.class)
│   ├── ConsistencyModel.class
│   ├── LeaderboardNode.class
│   ├── NamingService.class
│   ├── PlayerClient.class
│   ├── ReplicationManager.class
│   └── Main.class
│
├── src/                        # Root directory for raw Java source code (.java)
│   ├── ConsistencyModel.java   # Configurable enumeration for replication strategies
│   ├── LeaderboardNode.java    # Distributed regional server node component
│   ├── NamingService.java      # Dual-mode logical naming map directory 
│   ├── PlayerClient.java       # Asynchronous player action client thread
│   ├── ReplicationManager.java # Core cross-region database syncing engine
│   └── Main.java               # Simulation orchestrator & driver script
│
└── README.md                   # Repository setup and execution guide
