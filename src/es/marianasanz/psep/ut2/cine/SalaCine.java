package es.marianasanz.psep.ut2.cine;

/** Una instancia representa las entradas de UNA sesión de cine. */
public class SalaCine {
    private final String pelicula;
    private final String sesion;
    private final int entradasIniciales;
    private int entradasDisponibles;

    public SalaCine(String pelicula, String sesion, int entradas) {
        if (entradas < 0) throw new IllegalArgumentException("El aforo no puede ser negativo");
        this.pelicula = pelicula;
        this.sesion = sesion;
        this.entradasIniciales = entradas;
        this.entradasDisponibles = entradas;
    }

    // TODO 1. Devuelve true solo si se vende TODO el grupo. Si no cabe, devuelve false.
    // La pausa de 50 ms es artificial y debe quedar ENTRE comprobar y descontar.
    // No representa el pago ni el tiempo que tarda el espectador en ver la película.
    public boolean vender(int cantidad) throws InterruptedException {
        if (cantidad <= 0) throw new IllegalArgumentException("El pedido debe ser positivo");
        Thread.sleep(50);
        if (entradasDisponibles >= cantidad) {
            System.out.println("Hay entradas disponibles");
            Thread.sleep(50);
            entradasDisponibles -= cantidad;
            return true;
        }
        System.out.println("No hay entradas disponibles");
        return false;
    }


    // TODO 5. Ampliación 2. Solo acepta cantidades que no hagan superar el aforo inicial.
    // El canal comprueba antes que devuelve entradas de sus propias compras.
    public boolean devolver(int cantidad) throws InterruptedException {
        if (cantidad <= 0) throw new IllegalArgumentException("La devolución debe ser positiva");
        Thread.sleep(0); // Sustituye este cuerpo provisional.
        return false;
    }

    public String getPelicula() { return pelicula; }
    public String getSesion() { return sesion; }
    public int getEntradasIniciales() { return entradasIniciales; }
    // Consultar estos resultados desde main después de TODOS los join().
    public int getEntradasDisponibles() { return entradasDisponibles; }
}
