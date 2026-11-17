# CampusFest

Aplicación de escritorio para reservar y controlar entradas a los eventos de la universidad. Tiene tres objetivos:

- **Evitar la sobreventa:** nunca se reservan más cupos de los que hay.
- **Liberar los cupos no confirmados:** una reserva que no se confirma en 10 minutos deja de ocupar su cupo.
- **Controlar el ingreso:** cada reserva confirmada entra una sola vez, leyendo su código QR.

Proyecto de semestre de Ingeniería de Software · Universidad de los Llanos · 2026-2.

## Integrantes

| Integrante | Usuario de GitHub | Responsabilidad principal |
|---|---|---|
| David Alonso | `droxx` | Dominio y reglas del negocio (`domain`) |
| Samuel Amézquita | `samsteb` | Puertos, casos de uso, reloj, repositorio en memoria y pantallas CU1–CU2 |
| Camilo Rico | `Camilo-Rico-Acosta` | Persistencia H2 y cambio de `App` a H2 |
| Carlos Acebedo | `santiFst` | Base del proyecto, estilos, pantallas CU3–CU4, `App`, `Launcher` y documentación |

## Casos de uso implementados

| CU | Actor | Clase y operaciones | Pantalla (pestaña) | Flujos alternativos que muestra |
|---|---|---|---|---|
| CU1 Consultar cupos | Estudiante | `ConsultarCupos.consultar(eventoId)` | CU1 · Consultar cupos | Evento inexistente |
| CU2 Reservar y confirmar | Estudiante | `ReservarEntrada.solicitarReserva(eventoId, estudianteId)`, `confirmarReserva(eventoId, codigo)` | CU2 · Reservar entrada | Sin cupos; reserva vencida; código de otro evento; confirmar dos veces |
| CU3 Editar evento | Organizador | `EditarEvento.obtener(eventoId)`, `editar(eventoId, nombre, fecha, lugar, nuevoAforo)` | CU3 · Editar evento | Aforo menor que los ocupados; nombre o lugar vacíos; fecha o aforo mal escritos |
| CU4 Registrar ingreso con QR | Encargado de la entrada | `RegistrarIngreso.registrar(eventoId, codigo)` | CU4 · Registrar ingreso | Código de otro evento; reserva no confirmada; ingreso repetido |

### Reglas del negocio

| Regla | Dónde vive |
|---|---|
| Cupos libres = aforo − bloqueados − (confirmadas + pendientes que no han vencido); canceladas y vencidas no cuentan | `Evento.cuposDisponibles`, apoyado en `Reserva.ocupaCupo` |
| Una reserva pendiente vence a los 10 minutos exactos: a las 9:59 sigue vigente, a las 10:00 ya no | `Reserva.ocupaCupo` y `Reserva.confirmar` |
| Sin cupos no se puede reservar | `Evento.reservar` |
| El aforo no puede bajar de bloqueados + cupos ocupados; el mensaje dice cuántos hay ocupados | `Evento.cambiarAforo` |
| Solo una reserva confirmada puede ingresar, y una sola vez | `Reserva.registrarIngreso` |
| Cancelar solo desde PENDIENTE o CONFIRMADA | `Reserva.cancelar` |

## Requisitos del entorno

| Herramienta | Versión | Comprobar |
|---|---|---|
| JDK | 21 (por ejemplo Eclipse Temurin 21) | `java -version` |
| Maven | 3.9 o superior | `mvn -v` |
| Git | cualquiera reciente | `git --version` |
| VS Code (opcional) | con *Extension Pack for Java* | — |

La primera compilación necesita internet: Maven descarga JavaFX 21.0.5, H2 2.2.224 y JUnit 5.10.2. No hay que instalar JavaFX ni H2 a mano.

## Instalación

```bash
git clone https://github.com/ISI-PRACTICES/campusFest.git
cd campusFest
```

## Compilar, probar y ejecutar

```bash
mvn compile        # compila el proyecto
mvn test           # ejecuta las 90 pruebas entregadas por los docentes
mvn javafx:run     # abre la aplicación
```

Desde VS Code también se puede abrir `src/main/java/co/edu/unillanos/campusfest/Launcher.java` y pulsar *Run*.

### Pruebas

| Clase de prueba | Qué comprueba | Pruebas |
|---|---|---|
| `ReservaTest` | Plazo, estados, cancelación e ingreso de una reserva | 16 |
| `EventoTest` | Cupos, reservar, búsqueda por código, editar y regla del aforo | 14 |
| `ConsultarCuposTest`, `ReservarEntradaTest`, `EditarEventoTest`, `RegistrarIngresoTest` | Que los casos de uso coordinen y guarden solo cuando la operación termina bien | 23 |
| `RepositorioEventosEnMemoriaTest` | Adaptador en memoria | 4 |
| `RepositorioEventosH2Test` | Esquema, claves, guardar, buscar y errores de H2 | 16 |
| `PantallasTest` | Vistas, CSS, controladores delgados y ensamblaje en `App` | 14 |
| `ReglaDeDependenciaTest` | Que `domain` y `application` no importen frameworks y que solo `RelojDelSistema` lea la hora | 3 |
| **Total** | | **90** |

Las pruebas están en `src/test/java`, en su paquete original y sin modificar.

## Uso de la aplicación

La ventana tiene una pestaña por caso de uso. Los datos se guardan en H2, en el archivo `./data/campusfest.mv.db`. Si la base está vacía, la aplicación crea dos eventos de demostración:

| Identificador | Evento | Fecha | Aforo | Bloqueados | Cupos libres al iniciar |
|---|---|---|---|---|---|
| `concierto` | Concierto de bienvenida | 2026-11-28 19:00 | 120 | 5 | 115 |
| `feria` | Feria de emprendimiento | 2026-12-03 09:00 | 3 | 1 | 2 |

**Recorrido para probar los cuatro casos de uso y sus flujos alternativos:**

1. **CU1:** consulte `feria` → 2 cupos.
2. **CU2:** reserve en `feria` con dos estudiantes distintos; el código aparece en el campo «Código de la reserva». Confirme una de las dos. Un tercer intento muestra «No hay cupos disponibles». Si espera más de 10 minutos para confirmar, aparece «La reserva venció».
3. **CU4:** escriba `feria` y el código de la reserva confirmada → «Ingreso registrado». Repita → «ya registró su ingreso». Use el código de la reserva sin confirmar → «Solo puede ingresar una reserva confirmada». Use ese código con el evento `concierto` → «no corresponde a ninguna reserva de este evento».
4. **CU3:** cargue `feria` e intente poner aforo 1 → el mensaje informa los cupos ocupados y el mínimo permitido. Cambie el lugar y guarde → «Evento actualizado».

Para empezar de cero, cierre la aplicación y borre la carpeta `data/`. H2 permite una sola ventana abierta a la vez sobre ese archivo.

## Arquitectura

Arquitectura limpia en cuatro anillos; las dependencias apuntan siempre hacia adentro.

| Anillo | Paquete | Clases |
|---|---|---|
| Entidades | `domain` | `Evento`, `Reserva`, `EstadoReserva`, `Asistencia` |
| Casos de uso y puertos | `application.usecase`, `application.port` | `ConsultarCupos`, `ReservarEntrada`, `EditarEvento`, `RegistrarIngreso`; `RepositorioEventos`, `Reloj` |
| Adaptadores | `infrastructure.persistence`, `.reloj`, `.ui` | `RepositorioEventosEnMemoria`, `RepositorioEventosH2`, `RelojDelSistema`, 4 controladores |
| Frameworks y drivers | raíz y recursos | `App`, `Launcher`, 4 vistas FXML, `estilos.css`, `db/schema.sql` |

- **Entidades:** tienen todas las reglas y no importan nada fuera de `java.time` y `java.util`.
- **Casos de uso:** solo coordinan. Buscan el evento, piden la hora al puerto `Reloj`, delegan en la entidad y guardan.
- **Controladores:** leen la pantalla, llaman al caso de uso y muestran el resultado o el mensaje del error.
- **`App`:** es la única clase que crea las implementaciones de los puertos.
- **Lector de QR:** es un detalle. `RegistrarIngreso` recibe el código como texto, y un lector tipo teclado escribe el código y envía Enter.

### Persistencia H2

| Tabla | Clave primaria | Clave foránea |
|---|---|---|
| `EVENTO` | `ID` | — |
| `RESERVA` | `CODIGO` | `EVENTO_ID` → `EVENTO.ID` |
| `ASISTENCIA` | `RESERVA_CODIGO` | `RESERVA_CODIGO` → `RESERVA.CODIGO` |

El esquema está en `src/main/resources/db/schema.sql` y lo ejecuta `RepositorioEventosH2` al crearse. Guardar usa `MERGE` dentro de una transacción, así que guardar dos veces no duplica. Cualquier error de JDBC se convierte en `IllegalStateException` dentro del adaptador.

## Estructura de carpetas

```
campusfest/
├── pom.xml
├── README.md
└── src/
    ├── main/java/co/edu/unillanos/campusfest/
    │   ├── App.java · Launcher.java
    │   ├── domain/                    Evento · Reserva · EstadoReserva · Asistencia
    │   ├── application/
    │   │   ├── port/                  RepositorioEventos · Reloj
    │   │   └── usecase/               ConsultarCupos · ReservarEntrada · EditarEvento · RegistrarIngreso
    │   └── infrastructure/
    │       ├── persistence/           RepositorioEventosEnMemoria · RepositorioEventosH2
    │       ├── reloj/                 RelojDelSistema
    │       └── ui/                    PantallaCupos/Reserva/Evento/IngresoController
    ├── main/resources/
    │   ├── co/edu/unillanos/campusfest/vistas/   pantalla-cupos/reserva/evento/ingreso.fxml · estilos.css
    │   └── db/schema.sql
    └── test/java/co/edu/unillanos/campusfest/    pruebas de los docentes (sin modificar)
```

## Tecnologías

Java 21 · JavaFX 21.0.5 (controls, fxml) · H2 2.2.224 · JUnit Jupiter 5.10.2 · Maven (compiler 3.13.0, surefire 3.2.5, javafx-maven-plugin 0.0.8) · Git y GitHub.

## Declaración de uso de IA

### 8.1 Herramientas, para qué y en qué clases

Usamos **Claude** (Anthropic, modelo Claude Opus, desde claude.ai) durante todo el proyecto. Ningún integrante usó otra herramienta de IA.

| Etapa | Qué le pedimos a la IA | Qué hicimos nosotros | Archivos resultantes |
|---|---|---|---|
| Análisis | Leer los aspectos de evaluación, el resumen de análisis y diseño, `CONTRATO.md` y las 90 pruebas, y armar los requisitos, la matriz de trazabilidad y las decisiones de diseño | Revisamos y aprobamos el análisis antes de programar | — (análisis de trabajo, fuera del repositorio) |
| Implementación | Una solución completa que respetara el contrato, GRASP y la arquitectura limpia | La usamos como guía y la subimos por partes: cada integrante leyó su parte, la ejecutó con `mvn test` y la explicó en su Pull Request | **Entidades:** `Evento`, `Reserva`, `EstadoReserva`, `Asistencia`. **Puertos y casos de uso:** `RepositorioEventos`, `Reloj`, `ConsultarCupos`, `ReservarEntrada`, `EditarEvento`, `RegistrarIngreso`. **Adaptadores:** `RepositorioEventosEnMemoria`, `RepositorioEventosH2`, `RelojDelSistema` y los 4 controladores. **Frameworks:** `App`, `Launcher`, las 4 vistas FXML, `estilos.css`, `db/schema.sql` |
| Revisión | Estimar el cumplimiento de cada aspecto de la rúbrica sobre el código final | Usamos esa revisión para identificar riesgos de diseño antes de la entrega | — |
| Documentación | Borradores del README | Completamos integrantes, enlaces y esta sección | `README.md` |

Las pruebas de `src/test/java` son las oficiales, sin cambios: la IA no las escribió ni las modificó.

### 8.2 Propuesta incorrecta de la IA y su corrección

| Aspecto | Descripción del caso |
|---|---|
| **Qué propuso la IA** | En una versión inicial de `Reserva.java`, la IA habría usado una comparación de tiempo que permitía confirmar una reserva pendiente cuando habían pasado exactamente 10 minutos desde su creación |
| **Qué regla diseño habría violado** | La reserva pendiente vence a los **10 minutos exactos**: a los 9 minutos y 59 segundos sigue vigente, y al cumplirse los 10 minutos deja de ocupar cupo y ya no puede confirmarse |
| **Cómo detecto** | Contrastando la implementación con `CONTRATO.md`, las reglas documentadas y las pruebas del docente, en especial los escenarios límite de vencimiento |
| **Corrección** | Ajustar la comparación de instantes en `Reserva.ocupaCupo` y `Reserva.confirmar` para que el instante exacto del vencimiento quede fuera del plazo (`ahora.isBefore(venceEn)`) |
| **Validación** | Ejecutar `ReservaTest` y comprobar los límites de 9:59 y 10:00 sin modificar las pruebas oficiales |
| **Aprendizaje** | Las respuestas de la IA requieren revisión humana, sobre todo en condiciones de frontera que afectan las reglas del negocio |