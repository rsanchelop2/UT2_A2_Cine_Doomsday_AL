package es.marianasanz.psep.ut2.cine;

/** Un solo hilo modifica los contadores de cada canal. Main los lee tras los join(). */
public class CanalVenta implements Runnable {
    private final String nombre;
    private final SalaCine sala;
    // Positivo: compra. Negativo: devolución (solo ampliación 2). Nunca hay ceros.
    private final int[] pedidos;
    private int entradasVendidas;
    private int pedidosAceptados;
    private int pedidosRechazados;
    // Datos reservados para la ampliación 2. En el programa principal permanecen a cero.
    private int entradasDevueltas;
    private int devolucionesAceptadas;
    private int devolucionesRechazadas;

    public CanalVenta(String nombre, SalaCine sala, int[] pedidos) {
        this.nombre = nombre;
        this.sala = sala;
        this.pedidos = pedidos.clone();
    }

    @Override
    public void run() {
        try {
            // TODO 2. Procesa cada pedido una vez, en orden.
            // Todos los pedidos son positivos. Llama a vender(cantidad),
            // actualiza los tres contadores de compras y usa registrar(...).
            // Entre pedidos espera 40 ms fuera del bloqueo de la sala.
            // TODO 6 también incluye adaptar este recorrido para los negativos. Hacerlo después.
            Thread.sleep(0); // Modifica este cuerpo provisional.
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Canal interrumpido: " + nombre);
        }
    }

    // TODO 6. Ampliación 2
    // Recibe una cantidad POSITIVA. No se puede devolver más de lo comprado y no devuelto.
    // Una devolución rechazada se cuenta una sola vez y no cambia ningún stock.
    private boolean procesarDevolucion(int cantidad) throws InterruptedException {
        return false; // Pendiente solo en la ampliación 2.
    }

    private void registrar(int numero, int cantidad, boolean aceptado) {
        registrar(numero, "COMPRA", cantidad, aceptado);
    }
    private void registrar(int numero, String tipo, int cantidad, boolean aceptado) {
        System.out.printf("[%s / %s / %s] Operación %d: %s %d -> %s%n",
                sala.getSesion(), nombre, Thread.currentThread().getName(),
                numero, tipo, cantidad, aceptado ? "ACEPTADA" : "RECHAZADA");
    }
    public String getNombre() { return nombre; }
    public int getEntradasVendidas() { return entradasVendidas; }
    public int getPedidosAceptados() { return pedidosAceptados; }
    public int getPedidosRechazados() { return pedidosRechazados; }
    public int getEntradasDevueltas() { return entradasDevueltas; }
    public int getDevolucionesAceptadas() { return devolucionesAceptadas; }
    public int getDevolucionesRechazadas() { return devolucionesRechazadas; }
}
