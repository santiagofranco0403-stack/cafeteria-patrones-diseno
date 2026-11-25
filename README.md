Cafetería – Sistema de pedidos 

 cafetería o restaurante
Sistema de pedidos para un local con productos personalizables, hecho en Java, con
versión con interfaz gráfica  

## Integrantes

Nombre 

| JUAN SEBASTIAN ROJAS VARGAS  |  JOHAN SANTIAGO FRANCO CORREA| | 


 Funcionalidades implementadas

- Menú configurable leído desde `src/main/resources/menu.txt` (bebidas, platos y postres).
- Adiciones opcionales que cambian precio y descripción (shot extra, leche de almendras, crema,
  queso extra, tocineta extra), apilables.
- Combos armados paso a paso (personalizado) y combos fijos de la carta (desayuno y almuerzo).
- Pedidos por mesa con estados y transiciones controladas:
  `RECIBIDO → EN_PREPARACION → LISTO → ENTREGADO → PAGADO` (o `CANCELADO`).
- Aviso automático a **cocina**, **caja** y **mesero** cuando cambia el estado de un pedido.
- Promociones por horario o por día (happy hour de bebidas, martes de postres, fin de semana);
  al cobrar se aplica la mejor promoción vigente.
- Cierre de caja con reporte de ventas (totales, descuentos y ventas por categoría).

### Fuera del alcance

- Persistencia en base de datos (los datos viven en memoria durante la ejecución).
- Inventario de ingredientes y pagos reales.
- Versión web (el enunciado indica que no da puntos adicionales).

## Patrones de diseño aplicados

| Problema | Patrón | Categoría | Clases principales |
|---|---|---|---|
| Adiciones que modifican precio y descripción sin crear una subclase por combinación | **Decorator** | Estructural | `AdicionDecorator`, `ShotExtra`, `LecheAlmendras`, `Crema`, `QuesoExtra`, `TocinetaExtra` |
| Combos con muchas partes opcionales y reglas de armado | **Builder** | Creacional | `ComboBuilder`, `Combo`, `DirectorCombos` |
| Crear distintos tipos de producto a partir de un menú configurable | **Factory Method** | Creacional | `CreadorProducto`, `CreadorBebida`, `CreadorPlato`, `CreadorPostre`, `MenuConfigurable` |
| Avisar a cocina, caja y mesero cuando cambia el estado de un pedido | **Observer** | Comportamiento | `Pedido`, `ObservadorPedido`, `Cocina`, `Caja`, `Mesero` |
| Promociones que cambian según la hora o el día | **Strategy** | Comportamiento | `Promocion`, `HappyHourBebidas`, `MartesDePostres`, `FinDeSemana`, `SinPromocion`, `SelectorPromociones` |

## Arquitectura

```
com.cafeteria
├── modelo        Producto, ProductoBase, Bebida, Plato, Postre, Categoria
├── decorador     Adiciones (Decorator)
├── fabrica       Menú configurable y creadores (Factory Method)
├── builder       Combos (Builder)
├── pedido        Pedido, EstadoPedido, ObservadorPedido (Observer - sujeto)
├── observador    Cocina, Caja, Mesero (Observer - observadores)
├── promocion     Promociones (Strategy)
├── reporte       ReporteVentas (cierre de caja)
├── servicio      CafeteriaService  ← núcleo que usan las dos interfaces
└── ui
    ├── consola   AppConsola
    └── gui       AppGUI (Swing)
```

La consola y la GUI **no contienen lógica de negocio**: solo leen datos del usuario y llaman a
`CafeteriaService`. Por eso cualquier cambio de reglas (una promoción nueva, una adición nueva)
aparece en las dos interfaces sin tocarlas.

## Requisitos

- Java 17 o superior (JDK).
- Maven 3.8+ (opcional, para compilar y correr las pruebas). IntelliJ, NetBeans o VS Code
  pueden abrir el proyecto directamente como proyecto Maven.

## Cómo compilar y ejecutar

### Con Maven

```bash
mvn clean package          # compila, ejecuta las pruebas y genera target/cafeteria.jar
java -jar target/cafeteria.jar            # interfaz gráfica
java -jar target/cafeteria.jar --consola  # versión por consola
mvn test                   # solo pruebas unitarias
```

### Sin Maven (solo JDK)

```bash
# Linux / macOS
mkdir -p out && javac -encoding UTF-8 -d out $(find src/main/java -name "*.java")
cp src/main/resources/menu.txt out/
java -cp out com.cafeteria.Main            # GUI
java -cp out com.cafeteria.Main --consola  # consola
```

```powershell
# Windows (PowerShell)
mkdir out
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src/main/java -Filter *.java).FullName
copy src/main/resources/menu.txt out/
java -cp out com.cafeteria.Main --consola
```

> En la consola de Windows, si las tildes se ven mal, ejecute antes `chcp 65001`.

## Pruebas unitarias

Ubicadas en `src/test/java`, una clase por patrón y una prueba de integración del flujo principal:

| Prueba | Patrón que verifica |
|---|---|
| `AdicionDecoratorTest` | Decorator |
| `ComboBuilderTest` | Builder |
| `MenuConfigurableTest` | Factory Method |
| `PedidoObserverTest` | Observer |
| `PromocionStrategyTest` | Strategy |
| `CafeteriaServiceTest` | Flujo completo pedido → cocina → mesero → caja → cierre |

## Diagramas UML

En la carpeta [`docs/diagramas`](docs/diagramas) están los diagramas en PlantUML (`.puml`):
casos de uso, clases general, un diagrama de clases por patrón y el diagrama de secuencia del
flujo principal. Se pueden ver en <https://www.plantuml.com/plantuml> o con la extensión
PlantUML de VS Code / IntelliJ.

## Cómo extender el sistema (cambio de requisito en vivo)

- **Nueva adición** (ej. caramelo): crear `Caramelo extends AdicionDecorator` y agregarla a `TipoAdicion`.
- **Nueva promoción** (ej. cumpleaños): crear una clase que implemente `Promocion` y registrarla
  en `CafeteriaService` con `promociones.registrar(...)`.
- **Nuevo tipo de producto** (ej. snack): crear la clase del producto y un `CreadorSnack`, y
  registrarlo con `menu.registrarCreador("SNACK", new CreadorSnack())`.
- **Nueva área que debe enterarse de los pedidos** (ej. domicilios): implementar
  `ObservadorPedido` y suscribirla en `nuevoPedido`.
- **Nuevo combo fijo**: agregar un método en `DirectorCombos`.

## Declaración de uso de inteligencia artificial

De acuerdo con las reglas de integridad académica del parcial: en este proyecto se usó
**Claude (Anthropic)** como apoyo para generar la estructura inicial del código, las pruebas
unitarias, este README y los diagramas PlantUML. El grupo revisó el resultado y es responsable
de poder explicar cada parte del diseño y del código en la sustentación.
