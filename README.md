# 💰 AlkeWallet

> **Billetera virtual por consola desarrollada en Java**

---

## 📌 Descripción general

**AlkeWallet** es una aplicación de consola desarrollada en **Java** que permite simular el funcionamiento de una billetera virtual. El sistema posibilita la creación y gestión de usuarios, cuentas y monedas, así como la ejecución de operaciones financieras básicas como transferencias, depósitos, extracciones, simular conversiones de montos entre monedas, consultar el historial de operaciones e incluso convertir la cuenta de una moneda a otra. 

El proyecto está diseñado siguiendo principios de **Programación Orientada a Objetos (POO)**, priorizando la extensibilidad, la claridad del dominio y la separación de responsabilidades.

---

## 🧠 Requerimientos generales

### Administración de fondos 
El programa debe permitir crear una cuenta, ver su saldo disponible, realizar ingreso y retiro de dinero
(debe impactar sobre el saldo actual).
### Conversión de moneda 
El programa debe permitir convertir el saldo de un tipo de moneda a otra.

## 🧠 Requerimientos técnicos/específicos:
### Backend 
Java implementando el paradigma orientado a objetos para desarrollar y gestionar la lógica del negocio. Utilizar interfaces
para reutilización de código.
### Diagramas de clase 
Representar en un Diagrama de Clases el
modelo de clases que da solución a los requerimientos.
### Pruebas unitarias
Se realizarán pruebas unitarias para garantizar la calidad y el correcto funcionamiento de los componentes desarrollados.


---

## 🧠 Objetivos del proyecto

- Aplicar conceptos de POO (herencia, polimorfismo, abstracción, encapsulamiento) así como el principio de diseño IoC (Inversion of Control) y el patrón de diseño ID (Inyección de dependencias)
- Modelar un dominio financiero simple pero extensible
- Centralizar la lógica de negocio
- Practicar validaciones y manejo de errores

---

## 👥 Público objetivo

- Estudiantes de Java
- Desarrolladores junior
- Evaluadores técnicos
- Otros desarrolladores/programadores que desen extender y mantener las funcionalidades de la aplicación

---

## ⚙️ Requisitos del sistema

| Requisito | Versión |
|---------|--------|
| Java JDK | 21 o superior |
| IDE | IntelliJ, Eclipse, Apache NetBeans, VS Code |

---

## 🚀 Ejecución del proyecto

1. Clonar el repositorio
2. Compilar el proyecto
3. Ejecutar la clase principal:

```bash
 java -jar out/artifacts/AlkeWallet_jar/AlkeWallet.jar
```
4. Se pueden agregar comandos de linea para usar el Modo Secreto de introducción de claves (para no hacer visible los caracteres mientras se los introduce) como así tambien especificar
el idioma de partida de la aplicacion en entre inglés (en) y español (es).
Variantes:
```bash
 java -jar out/artifacts/AlkeWallet_jar/AlkeWallet.jar en
 java -jar out/artifacts/AlkeWallet_jar/AlkeWallet.jar console en
 java -jar out/artifacts/AlkeWallet_jar/AlkeWallet.jar -c
 java -jar out/artifacts/AlkeWallet_jar/AlkeWallet.jar -c en
 java -jar out/artifacts/AlkeWallet_jar/AlkeWallet.jar en -c
```
---

## 📝 Memoria del desarollo
<p>
Si bien en una primera instancia estuvo pensada como una aplicación de pocas clases y una función principal que manejaba todas las operaciones, debido a los requerimientos descritos en la consigna, pronto se hizo evidente de que se debia generar una desagregación y desacoplamientos de los diferentes objetos y funcionalidades. Esto llevó a incorporar, en primera instancia un enfoque de "machine states", es decir un cambio de estado por cada tipo distinto de subactividad que el usuario realiza en la aplicación, pero que conservan todos el mismo principio: "mostrar información contextual", la mayoría de las veces un menú; y "procesar opción", una función que determina que efectos produce la entrada del usuario por consola.
</p>
<p>
Luego, al hacerse evidente que muchas de las, no todas, operaciones compartian las mismas caracteristicas, se decidió partir de la definición de una clase abstracta con la posterior implementación de diversas interfaces que iban dotando a las operaciones de cada vez más capacidades. El core de la aplicación estaba representado por un ciclo while sin una condición de salida específica (más que la eventual terminación del programa) y una lectura, en la fase rudimentaria, de un valor del tipo int. Luego debido a las necesidades de manejar más que la simple elección de opciones numeradas, sino además comandos de escape, cifras monetarias, credenciales de usuario, etc., se optó por leer toda entrada como un String, con la posterior conversión a int en los casos que así lo requirieran.
 </p>
 <p>
En estapas posteriores se ageragon Enums para distinguier entre los distintos tipos de transferencias, validaciones previas y posteriores a la ejecución de las operaciones, repositorios a través de métodos staticos, la introducción de contraseñas en modo secreto, incorporaciones de registros para cada operacion, formateo de los datos entregados por estos para la generación de un historial, un simulador de conversiones de monedas y la posibilidad de convertir la cuenta a otra moneda.
En un etapa más avanzada, ya con más de una semana de desarrollo, se decidió añadir una feature de cambio de idioma, tanto desde consola como a traves del menú inicial, lo que implico generar las .properties y externalizar todas la cadenas de texto que se mostraban por pantalla.
</p>
 <p>
 En las últimas fases del desarrollo, para darle un poco más de características cercanas a la realidad de un sistema de gestión de activos, se incorporaron algunas restricciones "fiscales" y "bancarias" para los retiros, depósitos y transferencias, tanto en sus montos, como en la cantiadad de operaciones disponibles por sesión y por cuenta de usuario.
 </p>
 <p>
Cabe mencionar la adición de otras funcionalidades auxiliares que resultaron de las necesidades de testeo, como la posibilidad de controlar los mensajes de alerta e información entregados por la aplicación, para controlar el correcto funcionamiento.
 [VER Memoria del Testing](#🔎-memoria-del-testing)
 </p>


## 🏗️ Arquitectura general

El sistema se organiza en capas lógicas:

### 📦 Dominio
- `cuenta`
- `estado`
- `moneda`
- `operacion`
- `usuario`

### 🔧 Servicio
- `formatters`
- `passwords`
- `providers`
- `repositorios`

### 🎛️ Control y flujo
- `Main` (entry point)
- `AlkeWallet` Ciclos y entrada de opciones por consola
- `ContextoUsuario` Manejo de sesión y gestion de input y output

---

## 🔁 Flujo de ejecución

```text
Inicio
  ↓
Inicialización del sistema (se procesan comandos de linea si los hubiera)
  ↓
Se crean entidades básicas para la interacción
  ↓
Se comienza el ciclo de interacciones (while)
  ↓
Se muestra información contextual
  ↓
Lectura de opción
  ↓
Ejecución de funcionalidad
  ↓
Retorno al menú / salida
```

---

## ✨ Funcionalidades principales

### 🔣​ Lenguaje de la app
- Español
- Inglés

### 👤 Gestión de usuarios
- Creación de usuarios
- Registro de usuario en la DB interna
- Asociación de cuenta

### 💰​ Gestión de monedas
- Creación de monedas
- Registro de monedas en la DB interna
- Definición de su ratio de convertibilidad respecto al USD

### 🗃️ Gestión de cuentas
- Creación de cuentas
- Asociación de monedas

### 💸 Operaciones financieras
- Depósitos
- Extracciones
- Transferencias entre cuentas
- Conversión de cuenta a otra moneda

### ​🧾 Operaciones de consulta
- Datos de la cuenta
- Saldo actual
- Conversión de montos entre monedas
- Historial de transacciones


> Todas las operaciones heredan de la clase abstracta `Operacion`, lo que permite agregar nuevos tipos sin modificar código existente.

---

## 🧪 Manejo de errores y validaciones

El sistema contempla:

- Validación de opciones de menú
- Control de entradas inválidas por consola
  - Valores negativos
  - Formato erróneo de cantidad monetaria
  - Entrada alfabéticas
  - Ausencia de valor
- Excepciones de negocio:
  - Saldo insuficiente
  - Moneda inexistente
  - Cuenta inválida
  - Usuario inexistente
  - Límites de montos por operación
  - Limites de operaciones por sesión y por cuenta
- Uso de bloques `try-catch` para mantener la ejecución estable

---

## 📚 Casos de uso técnicos

### Caso de uso: Transferencia entre cuentas

📍 **Actor:** Usuario Origen, Usuario Destino

📍 **Precondiciones:**
- Ambas cuentas existen y tienen monedas asignadas
- La cuenta origen posee saldo suficiente

📍 **Flujo principal:**
1. El usuario origen selecciona la opción *Transferir*
2. Ingresa cuenta destino
3. Selecciona tipo de transferencia
 - Ambas cuentas tienen las misma moneda?
 - Ambas cuentas tienen diferente moneda?
   * Seleccionar monto en moneda destino
   * Seleccioanr monto en moneda cuenta propia
5. Ingresa monto
6. Se prevalida la operación
7. Se ejecuta la transferencia
8. Se postvalida la operación
7. Se registra la operación

---

## 🔧 Extensibilidad y mantenimiento

El diseño del sistema permite:

- ➕ Agregar nuevas operaciones sin modificar código existente (Principio Open/Closed)
- 🌍 Incorporar nuevas monedas
- 🖥️ Sustituir la interfaz de consola por:
  - Interfaz gráfica (InputProvider/OutputProvider)
  - API REST (Repositorios)
  - Persistencia en base de datos (Entidades que pueden serializarse)

---

## 🛠️ Tecnologías utilizadas

- Java 21+
- Programación Orientada a Objetos
- Java Collections
- BigDecimal para manejo de montos

---

## 🔎 Memoria del Testing

sdfsfds


--- 

## 📄 Notas finales

Este proyecto está orientado al **aprendizaje y demostración de conceptos**, por lo que prioriza la claridad del diseño por sobre la optimización extrema.

---

✨ *Documentación técnica – AlkeWallet*

