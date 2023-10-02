package sportyfy.pronosticadorFutbol;

import sportyfy.core.Pronosticador;
import sportyfy.core.Pronostico;
import sportyfy.core.entidades.Equipo;
import sportyfy.core.entidades.Partido;

import java.util.List;
import java.util.stream.Collectors;

public class PronosticadorFutbol implements Pronosticador {

    private final String deporte;

    public PronosticadorFutbol() {
        this.deporte = "Futbol";
    }

    @Override
    public Pronostico pronosticar(Equipo equipoLocal, Equipo equipoVisitante, List<Partido> partidosAnteriores) {
        validarDatos(equipoLocal, equipoVisitante, partidosAnteriores);

        double golesEquipoLocal = calcularPromedioGolesEquipo(equipoLocal, partidosAnteriores);
        double golesEquipoVisitante = calcularPromedioGolesEquipo(equipoVisitante, partidosAnteriores);

        if (golesEquipoLocal > golesEquipoVisitante) {
            return new Pronostico(equipoLocal);
        } else if (golesEquipoLocal < golesEquipoVisitante) {
            return new Pronostico(equipoVisitante);
        } else {
            return new Pronostico(null);
        }
    }

    @Override
    public String obtenerDeporte() {
        return deporte;
    }

    private void validarDatos(Equipo equipoLocal, Equipo equipoVisitante, List<Partido> partidosAnteriores) {
        if (partidosAnteriores.isEmpty()) {
            throw new IllegalArgumentException("No hay información de partidos para realizar el pronóstico");
        }

        if (equipoLocal == null || equipoVisitante == null) {
            throw new IllegalArgumentException("No se puede realizar el pronóstico con equipos nulos");
        }
    }

    private double calcularPromedioGolesEquipo(Equipo equipo, List<Partido> partidos) {
        List<Partido> partidosDelEquipo = obtenerPartidosDeEquipo(equipo, partidos);

        if (partidosDelEquipo.isEmpty()) {
            return 0.0; // Si no hay partidos del equipo, el promedio es 0.
        }

        double totalGoles = calcularTotalGolesEquipo(equipo, partidosDelEquipo);

        return totalGoles / partidosDelEquipo.size();
    }

    private double calcularTotalGolesEquipo(Equipo equipo, List<Partido> partidos) {
        return partidos.stream()
                .mapToDouble(partido -> partido.esLocal(equipo) ? partido.getGolesLocal() : partido.getGolesVisitante())
                .sum();
    }

    private List<Partido> obtenerPartidosDeEquipo(Equipo equipo, List<Partido> partidos) {
        return partidos.stream()
                .filter(partido -> partido.participa(equipo))
                .collect(Collectors.toList());
    }
}
