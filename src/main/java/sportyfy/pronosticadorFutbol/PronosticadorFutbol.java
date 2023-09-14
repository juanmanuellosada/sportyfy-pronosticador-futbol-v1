package sportyfy.pronosticadorFutbol;

import sportyfy.core.Pronosticador;
import sportyfy.core.Pronostico;
import sportyfy.core.futbol.Equipo;
import sportyfy.core.futbol.Partido;

import java.util.List;
import java.util.stream.Collectors;

public class PronosticadorFutbol implements Pronosticador {

    String deporte;

    public PronosticadorFutbol() {
        this.deporte = "Futbol";
    }

    @Override
    public Pronostico pronosticar(Equipo equipoLocal, Equipo equipoVisitante, List<Partido> partidosAnteriores) {

        double golesEquipoLocal = calcularPromedioGolesEquipo(equipoLocal, partidosAnteriores);
        double golesEquipoVisitante = calcularPromedioGolesEquipo(equipoVisitante, partidosAnteriores);

        if (golesEquipoLocal > golesEquipoVisitante) {
            return new Pronostico(equipoLocal);
        } else if (golesEquipoLocal < golesEquipoVisitante) {
            return new Pronostico(equipoVisitante);
        } else {
            // Empate
            return new Pronostico(null);
        }
    }

    @Override
    public String obtenerDeporte() {
        return deporte;
    }


    // Método para calcular el promedio de goles de un equipo en partidos anteriores
// Método para calcular el promedio de goles de un equipo en todos los partidos
    private double calcularPromedioGolesEquipo(Equipo equipo, List<Partido> partidos) {
        List<Partido> partidosDelEquipo = obtenerPartidosDeEquipo(equipo, partidos);

        if (partidosDelEquipo.isEmpty()) {
            return 0.0; // Si no hay partidos del equipo, el promedio es 0.
        }

        double totalGoles = partidosDelEquipo.stream()
                .mapToDouble(partido -> {
                    if (partido.esLocal(equipo)) {
                        return partido.getGolesLocal();
                    } else {
                        return partido.getGolesVisitante();
                    }
                })
                .sum();

        return totalGoles / partidosDelEquipo.size();
    }

    // Método para obtener la lista de partidos de un equipo
    private List<Partido> obtenerPartidosDeEquipo(Equipo equipo, List<Partido> partidos) {
        return partidos.stream()
                .filter(partido -> partido.participa(equipo))
                .collect(Collectors.toList());
    }
}
