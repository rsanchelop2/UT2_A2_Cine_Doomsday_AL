package es.marianasanz.psep.ut2.cine;

public class AppCine {
    // Configuración y selección de modos ya proporcionadas.
    public static void main(String[] args) throws InterruptedException {
        boolean doble = false;
        boolean devoluciones = false;
        int entradas = 30;
        boolean aforoIndicado = false;
        for (String arg : args) {
            if (arg.equals("--doble")) doble = true;
            else if (arg.equals("--devoluciones")) devoluciones = true;
            else if (!aforoIndicado && !arg.startsWith("--")) {
                entradas = Integer.parseInt(arg);
                aforoIndicado = true;
            } else throw new IllegalArgumentException("Argumento no reconocido: " + arg);
        }
        if (doble) {
            if (aforoIndicado) throw new IllegalArgumentException(
                    "--doble usa aforos fijos de 30 y 20. No añadas un aforo numérico.");
            ejecutarDosSesiones(devoluciones);
        } else {
            SalaCine sala = new SalaCine("Avengers: Doomsday", "Sala 1 / 18:00", entradas);
            CanalVenta[] canales = devoluciones ? crearCanalesConDevoluciones(sala) : crearCanales(sala);
            System.out.printf("%s | %s | Entradas iniciales: %d%n",
                    sala.getPelicula(), sala.getSesion(), sala.getEntradasIniciales());
            ejecutarCanales(canales);
            mostrarResumen(sala, canales);
        }
    }

    public static CanalVenta[] crearCanales(SalaCine sala) {
        return new CanalVenta[] {
            new CanalVenta("TAQUILLA", sala, new int[] {2, 4, 1, 3, 2, 4}),
            new CanalVenta("WEB", sala, new int[] {3, 1, 4, 2, 3, 1}),
            new CanalVenta("APP", sala, new int[] {1, 2, 3, 4, 1, 2}),
            new CanalVenta("QUIOSCO", sala, new int[] {4, 3, 2, 1, 4, 3})
        };
    }

    public static CanalVenta[] crearCanalesConDevoluciones(SalaCine sala) {
        // Datos de la ampliación 2 ya preparados. Los negativos son devoluciones.
        return new CanalVenta[] {
            new CanalVenta("TAQUILLA", sala, new int[] {2, -1, 4, -1, 1, -9, 3, -1}),
            new CanalVenta("WEB", sala, new int[] {3, -2, 1, -1, 4, -1, 2, -1}),
            new CanalVenta("APP", sala, new int[] {1, -1, 2, -1, 3, -2, 4, -1}),
            new CanalVenta("QUIOSCO", sala, new int[] {4, -1, 3, -2, 2, -1, 1, -1})
        };
    }

    public static void ejecutarCanales(CanalVenta[] canales) throws InterruptedException {
        // TODO 3. Un Thread por canal.
        Thread[] listaCanales = new Thread[canales.length];

        for (int i = 0; i < canales.length; i++) {
            listaCanales[i] = new Thread(canales[i]);
            listaCanales[i].start();
        }

        for (Thread hilo : listaCanales) {
            hilo.join();
        }
    }

    private static void ejecutarDosSesiones(boolean devoluciones) throws InterruptedException {
        // TODO 4. Ampliación 1. Dos salas y ocho trabajadores.
        // Usa crearCanales() en cada sala. Si devoluciones es true, usa en su lugar
        // crearCanalesConDevoluciones(). La ampliación 1 se prueba SIN devoluciones.
        System.out.println("AMPLIACIÓN 1 PENDIENTE");
    }

    private static void mostrarResumen(SalaCine sala, CanalVenta[] canales) {
        System.out.printf("%nCIERRE DE VENTAS: %s / %s%n", sala.getPelicula(), sala.getSesion());
        int vendidas = 0, devueltas = 0, comprasOk = 0, comprasNo = 0, devolOk = 0, devolNo = 0;
        for (CanalVenta canal : canales) {
            System.out.printf("%s: vendidas=%d, devueltas=%d, comprasOk=%d, comprasNo=%d, devolOk=%d, devolNo=%d%n",
                    canal.getNombre(), canal.getEntradasVendidas(), canal.getEntradasDevueltas(),
                    canal.getPedidosAceptados(), canal.getPedidosRechazados(),
                    canal.getDevolucionesAceptadas(), canal.getDevolucionesRechazadas());
            vendidas += canal.getEntradasVendidas();
            devueltas += canal.getEntradasDevueltas();
            comprasOk += canal.getPedidosAceptados(); comprasNo += canal.getPedidosRechazados();
            devolOk += canal.getDevolucionesAceptadas(); devolNo += canal.getDevolucionesRechazadas();
        }
        int disponibles = sala.getEntradasDisponibles();
        System.out.printf("Iniciales: %d | Vendidas: %d | Devueltas: %d | Disponibles: %d%n",
                sala.getEntradasIniciales(), vendidas, devueltas, disponibles);
        System.out.printf("Compras aceptadas: %d | rechazadas: %d%n", comprasOk, comprasNo);
        System.out.printf("Devoluciones aceptadas: %d | rechazadas: %d%n", devolOk, devolNo);
        System.out.println("Operaciones procesadas: " + (comprasOk + comprasNo + devolOk + devolNo));
        boolean correcto = disponibles == sala.getEntradasIniciales() - vendidas + devueltas
                && disponibles >= 0 && disponibles <= sala.getEntradasIniciales();
        System.out.println("Control del inventario: " + (correcto ? "CORRECTO" : "ERROR"));
    }
}
