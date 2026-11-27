# Actividad 2 - Entradas de cine

Un cine abre la venta de entradas para una sesión de *Avengers: Doomsday*. La taquilla, la web, la app y el quiosco venden a la vez las mismas entradas. Completa un programa que procese pedidos de grupos sin vender más entradas de las disponibles.

La simulación utiliza salas, horarios y aforos ficticios. Ejecuta `AppCine.main()` en IntelliJ y utiliza **Program arguments** para elegir las pruebas.

## 1. Sesión y pedidos

Hay una sesión de *Avengers: Doomsday* en la Sala 1 a las 18:00, con 30 entradas disponibles por defecto. El objeto `SalaCine` representa el inventario de esa sesión. Se crea **una** instancia y los cuatro objetos `CanalVenta` reciben esa misma referencia. Cada canal se ejecutará en su propio hilo.

| Canal | Entradas solicitadas en cada pedido | Total |
|---|---|---:|
| Taquilla | 2, 4, 1, 3, 2, 4 | 16 |
| Web | 3, 1, 4, 2, 3, 1 | 14 |
| App | 1, 2, 3, 4, 1, 2 | 13 |
| Quiosco | 4, 3, 2, 1, 4, 3 | 17 |

Son 24 pedidos que solicitan 60 entradas en total. Cada canal mantiene el orden de sus seis pedidos, pero los canales pueden intercalarse. Un pedido se acepta **completo** o se rechaza **completo**. Si solicitan 4 y quedan 3, no se vende ninguna de esas 4. Tras un rechazo se continúa con el siguiente pedido, sin reintentar el rechazado.

## 2. Código proporcionado

- `SalaCine.java` representa las entradas de una sesión. Guarda película, horario, aforo inicial y entradas disponibles. El constructor, las validaciones de cantidades y los getters están preparados. Todos los canales de la misma sesión reciben la misma instancia.
- `CanalVenta.java` representa una tarea. Cada objeto guarda sus pedidos y sus contadores de compras.
- `AppCine.java` prepara los datos y coordina la ejecución. `crearCanales()`, los métodos `registrar()`, la lectura de argumentos y `mostrarResumen()` están implementados.

## 3. `SalaCine.java`

### `vender(int cantidad)` — TODO 1

Recibe el tamaño de un grupo (llamo grupo a un conjunto de entradas compradas a la vez, por ejemplo, una persona que compra 4 entradas de una vez) y devuelve `true` si se venden todas sus entradas o `false` si no hay suficientes. La validación de cantidad positiva ya está incluida. Un pedido rechazado no modifica el inventario.

- Comprueba si quedan entradas suficientes y descuenta la cantidad únicamente cuando el grupo cabe completo.
- Coloca `Thread.sleep(50)` entre la comprobación satisfactoria y el descuento. Es una pausa artificial para estudiar la carrera al retirar la sincronización. No representa el pago. En código real se eliminaría.

## 4. `CanalVenta.java`

### `run()` — TODO 2

Ejecuta los pedidos del propio canal, en el orden del array. En la parte principal todos son compras y sus cantidades son positivas. El tratamiento de `InterruptedException` ya está preparado.

- Por cada pedido, llama una sola vez a `vender(cantidad)`. Si se acepta la venta, suma la cantidad a `entradasVendidas` e incrementa `pedidosAceptados`. Si se rechaza, incrementa `pedidosRechazados` y no reintenta ese pedido; sigue con el siguiente. Un grupo de 4 supone un pedido y 4 entradas, no cuatro pedidos.
- A continuación, llama a `registrar(numero, cantidad, aceptado)`, donde `numero` es la numeración de los pedidos desde 1.
- Entre un pedido y el siguiente, espera 40 ms. Esta pausa queda en el canal, fuera del bloqueo de `SalaCine`. No hace falta esperar después del último pedido.

Cada “trabajador” modifica solo los contadores de su `CanalVenta`. No añadas un contador global concurrente. Las trazas de canales distintos pueden intercalarse y no se exige que su orden en consola coincida exactamente con el orden de las ventas. Además, luego querremos saber cuántas entradas se han vendido o se han rechazado en cada canal de venta.

## 5. `AppCine.java`

### `ejecutarCanales(CanalVenta[] canales)` — TODO 3

Recibe las tareas y crea un `Thread` por canal. Guarda sus referencias en un array y asigna nombres que permitan identificar a los trabajadores.

- Arranca todos los canales con `start()` antes de comenzar las esperas. El método solo debe regresar cuando todos hayan terminado.

`main()` y `mostrarResumen()` ya están preparados. El cierre y la consulta del stock se realizan después de ejecutar `ejecutarCanales()`. No debes programar otro informe. Los getters de resultados se utilizan cuando todos los trabajadores han terminado.

## 6. Acceso concurrente a los datos

Varios canales trabajan simultáneamente sobre las entradas de una misma sesión. Tu programa debe garantizar que:

- Nunca se vendan más entradas de las disponibles.
- Un pedido se acepte o rechace completo.
- Las devoluciones no hagan superar el aforo inicial.
- Las compras (y en la ampliación 2 las devoluciones) mantengan un inventario coherente.

Identifica qué datos comparten los trabajadores y qué operaciones deben ejecutarse sin que otro hilo interfiera. Aplica la sincronización necesaria utilizando lo trabajado en clase y procura limitar la protección a las operaciones que la necesitan.

## 7. Pruebas obligatorias

Ejecuta `AppCine.main()`. Indica los argumentos en **Program arguments**. Sin argumentos utiliza 30 entradas. Compara las salidas con los archivos de `ejemplos_salida`.

| Prueba | Argumentos | Resultado esperado |
|---|---|---|
| Aforo limitado | `30` | 24 compras procesadas. Stock no negativo. Vendidas + disponibles = 30. Repite tres veces y compara el reparto. |
| Entradas suficientes | `80` | 60 entradas vendidas, 20 disponibles. 24 compras aceptadas y ninguna rechazada. |
| Sin entradas | `0` | 0 vendidas y 0 disponibles. 24 compras rechazadas. |
| Última entrada | `1` | 1 vendida y 0 disponibles. Una compra aceptada y 23 rechazadas. |

## Ampliación 1 - Dos sesiones de cine (2,5 puntos)

Añade una segunda sesión con sus propias entradas. Esta ampliación funciona sin implementar las devoluciones. Actívala con `--doble` en los argumentos; no añadas un aforo numérico, porque este modo utiliza aforos fijos.

### `ejecutarDosSesiones(boolean devoluciones)` — TODO 4

- Crea dos objetos `SalaCine` para *Avengers: Doomsday*: Sala 1 a las 18:00 con 30 entradas y Sala 2 a las 21:00 con 20. Cada uno conserva su propio inventario y monitor.
- Utiliza `crearCanales(sala)` para preparar cuatro canales por sesión. Reúne los ocho en un único array y pásalo a `ejecutarCanales()`, para que todos arranquen antes de esperar a su finalización.
- Al terminar, llama a `mostrarResumen()` una vez por sala, con los cuatro canales de esa sala. Conserva el funcionamiento de una sola sesión. No sumes las existencias de ambas ni uses `static synchronized`.

El parámetro `devoluciones` permite combinar esta ampliación con la ampliación 2. Si vale `true`, crea los canales con `crearCanalesConDevoluciones(sala)`, que ya se proporciona. Para hacer y evaluar solo la ampliación 1, se utiliza `--doble` sin esa opción. Cuando hagamos la ampliación 2, habrá que volver a modificar este método.

### Pruebas de la ampliación 1

Ejecuta `--doble`. Cada sesión debe procesar 24 compras, mantener stock no negativo y cumplir vendidas + disponibles = iniciales. Explica qué objeto se bloquea y por qué salas distintas pueden vender simultáneamente, mientras los canales de una misma sesión deben turnarse. No se exige un reparto fijo de las ventas.

## Ampliación 2 - Devolución de entradas (2,5 puntos)

Añade devoluciones al mismo inventario. Actívala con `--devoluciones`. La selección de los datos ya está programada.

El método `crearCanalesConDevoluciones(sala)` proporciona ocho operaciones por canal. Los positivos son compras y los negativos, devoluciones: `3` significa comprar 3 y `-2` significa devolver 2. Al llamar a `vender()` o `devolver()`, la cantidad siempre es positiva. No hay operaciones de cero.

| Canal | Operaciones en su orden original |
|---|---|
| Taquilla | 2, -1, 4, -1, 1, -9, 3, -1 |
| Web | 3, -2, 1, -1, 4, -1, 2, -1 |
| App | 1, -1, 2, -1, 3, -2, 4, -1 |
| Quiosco | 4, -1, 3, -2, 2, -1, 1, -1 |

### `devolver(int cantidad)` en `SalaCine.java` — TODO 5

Recibe una cantidad positiva y devuelve `true` si puede reincorporarla al inventario. Si la devolución hiciera superar el aforo inicial, devuelve `false` sin modificarlo. La validación de cantidad positiva ya está proporcionada.

- Coloca `Thread.sleep(50)` entre la comprobación satisfactoria y el aumento. Es la misma pausa artificial que en `vender()`.
- Ten en cuenta que mientras un hilo vende, otro no puede ejecutar la devolución sobre esa misma sesión.

### `procesarDevolucion(int cantidad)` y adaptación de `run()` — TODO 6

Un canal solo puede devolver entradas que haya comprado correctamente y todavía conserve. Para ello dispone de `entradasVendidas` y `entradasDevueltas`. No puede devolver las compras de otro canal. Las devoluciones también se aceptan o rechazan completas.

- Antes de llamar a `devolver(cantidad)`, comprueba que la cantidad no supera lo comprado y no devuelto. No tiene sentido devolver 4 entradas si compraste 5 y ya devolviste 2, porque solo te quedan 3. Si supera esa cantidad, incrementa `devolucionesRechazadas` y devuelve `false`, sin llamar a la sala.
- Si la validación anterior no supera lo comprado y no devuelto, solicita la devolución a la sala. Si se acepta, suma la cantidad a `entradasDevueltas` e incrementa `devolucionesAceptadas`. Si la sala la rechaza, incrementa `devolucionesRechazadas`. Un rechazo se cuenta una sola vez.
- Adapta `run()` para distinguir compras y devoluciones. Los números negativos deben pasar, convertidos a positivos, a `procesarDevolucion()`. Registra su resultado con `registrar(numero, "DEVOLUCION", cantidad, aceptado)`. Las compras mantienen el comportamiento anterior y ambas operaciones respetan los 40 ms de espera entre ellas.

No restes las devoluciones de `entradasVendidas`: ese atributo conserva el total de compras aceptadas. `entradasDevueltas` acumula las devoluciones aceptadas. Los contadores pertenecen a un único trabajador por canal y `main` los lee tras terminar las operaciones de cada hilo. `mostrarResumen()` ya contempla ambos tipos de operación.

### Pruebas de la ampliación 2

| Prueba | Argumentos | Resultado esperado |
|---|---|---|
| Compras y devoluciones | `--devoluciones` | 32 operaciones. Stock entre 0 y 30. Disponibles = iniciales - vendidas + devueltas. |
| Aforo suficiente | `80 --devoluciones` | 40 entradas compradas y 18 devueltas. Quedan 58. 16 compras aceptadas, 15 devoluciones aceptadas y una rechazada. |
| Sin compras posibles | `0 --devoluciones` | 16 compras y 16 devoluciones rechazadas. Vendidas, devueltas y disponibles a cero. |
