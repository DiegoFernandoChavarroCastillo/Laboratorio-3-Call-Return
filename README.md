# 🌐 Laboratorio de Programación de Redes en Java

Este repositorio contiene el desarrollo y la documentación de los ejercicios propuestos en el taller de redes. El objetivo principal es demostrar el uso de Java para comunicación distribuida, abarcando desde el análisis de protocolos básicos hasta la implementación de arquitecturas P2P y servidores web.

---

## 📂 Estructura del Proyecto

```text
networking-lab/
├── ejercicio1/       (URLInfo) -> Análisis de componentes de una URL.
├── ejercicio2/       (SimpleBrowser) -> Cliente HTTP para descarga de contenido.
├── ejercicio3_4/     (Sockets) -> Comunicación Cliente-Servidor mediante TCP Sockets.
├── ejercicio7_web/   (HttpServer) -> Servidor Web con soporte para archivos estáticos.
├── ejercicio8_udp/   (UDP Time) -> Servicio de tiempo basado en datagramas (UDP).
├── rmi/rmichat/      (RMI Chat) -> Chat distribuido mediante Invocación de Métodos Remotos.
├── rpc/rpc/          (RPC) -> Implementación de llamadas a procedimientos remotos.
└── p2p/p2p/          (P2P) -> Red descentralizada con soporte de Tracker.
```

---

## 🛠️ Compilación General

Para compilar todos los módulos del proyecto, ejecute el siguiente comando desde la raíz del directorio `networking-lab`:

```bash
javac ejercicio1/*.java ejercicio2/*.java ejercicio3_4/*.java ejercicio7_web/*.java ejercicio8_udp/*.java rmi/rmichat/*.java rpc/rpc/*.java p2p/p2p/*.java
```

---

## 🚀 Guía de Ejecución

### 1. Análisis de URLs (`URLInfo`)
Este componente permite descomponer una URL para identificar sus partes fundamentales: protocolo, host, puerto, ruta y parámetros de consulta.
*   **Ejecución:**
    ```bash
    java -cp . ejercicio1.URLInfo
    ```

### 2. Cliente de Descarga (`SimpleBrowser`)
Un cliente HTTP básico que solicita el contenido HTML de una dirección web y lo almacena localmente en un archivo llamado `resultado.html`.
*   **Ejecución:**
    ```bash
    java -cp . ejercicio2.SimpleBrowser
    ```

### 3. Servidores de Procesamiento Matemático (`Sockets`)
Implementación de una arquitectura Cliente-Servidor clásica utilizando el protocolo TCP. El servidor procesa datos numéricos (cuadrados y funciones trigonométricas) y retorna los resultados al cliente.
*   **Servidor de Cuadrados (Puerto 35001):**
    1. Iniciar servidor: `java -cp . ejercicio3_4.SquareServer`
    2. Iniciar cliente: `java -cp . ejercicio3_4.SquareClient`
*   **Servidor Trigonométrico (Puerto 35002):**
    1. Iniciar servidor: `java -cp . ejercicio3_4.TrigServer`
    2. Iniciar cliente: `java -cp . ejercicio3_4.TrigClient` (Permite cambiar funciones con `fun:sin` o `fun:tan`).

### 4. Servidor Web Estático (`HttpServer`)
Servidor HTTP capaz de gestionar múltiples solicitudes y servir recursos estáticos (HTML e imágenes) desde el directorio `www/`.
*   **Ejecución (Puerto 35010):**
    1. Iniciar servidor: `java -cp . rmi.pruebas.HttpServer`
    2. Acceso vía navegador: `http://localhost:35010/index.html`

### 5. Servicio de Tiempo por Datagramas (`UDP`)
Demostración de comunicación no orientada a conexión. El servidor emite la hora del sistema y el cliente la consume periódicamente, manteniendo tolerancia a fallas de conexión.
*   **Ejecución:**
    1. Servidor: `java -cp . ejercicio8_udp.DatagramTimeServerLoop`
    2. Cliente: `java -cp . ejercicio8_udp.DatagramTimeClientLoop`

### 6. Comunicación Distribuida mediante RMI (`Chat`)
Uso de Java RMI para permitir que dos aplicaciones invoquen métodos entre sí a través de la red, simulando un entorno de mensajería instantánea.
*   **Ejecución (Dos Terminales):**
    1. Terminal A: `java -cp rmi rmichat.ChatNode Alice 24001 127.0.0.1 24002 Bob`
    2. Terminal B: `java -cp rmi rmichat.ChatNode Bob 24002 127.0.0.1 24001 Alice`

### 7. Red P2P con Servidor de Descubrimiento (`Tracker`)
Arquitectura descentralizada donde los nodos se registran en un servidor central (Tracker) para luego establecer comunicaciones directas entre pares.
*   **Ejecución:**
    1. Tracker: `java -cp p2p p2p.TrackerServer`
    2. Nodo A: `java -cp p2p p2p.PeerMain peerA 7001 127.0.0.1`
    3. Nodo B: `java -cp p2p p2p.PeerMain peerB 7002 127.0.0.1`

---

## 📊 Especificación de Red

| Servicio | Puerto Predeterminado | Descripción |
| :--- | :--- | :--- |
| **HttpServer** | 35010 | Servicio de archivos web. |
| **SquareServer** | 35001 | Cálculo de potencias. |
| **TrigServer** | 35002 | Cálculo trigonométrico. |
| **UDP Time** | 4445 | Sincronización de tiempo. |
| **Tracker (P2P)** | 6000 | Gestión de nodos P2P. |
| **RPC Server** | 5000 | Punto final para llamadas RPC. |
