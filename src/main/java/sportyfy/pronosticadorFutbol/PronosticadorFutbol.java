package sportyfy.pronosticadorFutbol;

import sportyfy.core.Pronosticador;
import sportyfy.core.Pronostico;
import sportyfy.core.PronosticoNull;
import sportyfy.core.entidades.equipo.Equipo;
import sportyfy.core.entidades.partido.PartidoFuturo;
import sportyfy.core.entidades.partido.PartidoJugado;


import java.util.List;
import java.util.stream.Collectors;

public class PronosticadorFutbol implements Pronosticador {

    private final String deporte;

    public PronosticadorFutbol() {
        this.deporte = "Futbol";
    }

    @Override
    public Pronostico pronosticar(PartidoFuturo partidoFuturo, List<PartidoJugado> partidosAnteriores) {
        validarDatos(partidoFuturo.getEquipoLocal(), partidoFuturo.getEquipoVisitante(), partidosAnteriores);

        double golesEquipoLocal = calcularPromedioGolesEquipo(partidoFuturo.getEquipoLocal(), partidosAnteriores);
        double golesEquipoVisitante = calcularPromedioGolesEquipo(partidoFuturo.getEquipoVisitante(), partidosAnteriores);

        if (golesEquipoLocal > golesEquipoVisitante) {
            return new Pronostico(partidoFuturo.getEquipoLocal(), partidoFuturo);
        } else if (golesEquipoLocal < golesEquipoVisitante) {
            return new Pronostico(partidoFuturo.getEquipoVisitante(), partidoFuturo);
        } else {
            // Empate
            return new PronosticoNull(partidoFuturo);
        }
    }

    @Override
    public String obtenerDeporte() {
        return deporte;
    }

    private void validarDatos(Equipo equipoLocal, Equipo equipoVisitante, List<PartidoJugado> partidosAnteriores) {
        if (partidosAnteriores.isEmpty()) {
            throw new IllegalArgumentException("No hay informacion de partidos para realizar el pronostico");
        }

        if (equipoLocal == null || equipoVisitante == null) {
            throw new IllegalArgumentException("No se puede realizar el pron�stico con equipos nulos");
        }
    }

    private double calcularPromedioGolesEquipo(Equipo equipo, List<PartidoJugado> partidos) {
        List<PartidoJugado> partidosDelEquipo = obtenerPartidosDeEquipo(equipo, partidos);

        if (partidosDelEquipo.isEmpty()) {
            return 0.0; // Si no hay partidos del equipo, el promedio es 0.
        }

        double totalGoles = calcularTotalGolesEquipo(equipo, partidosDelEquipo);

        return totalGoles / partidosDelEquipo.size();
    }

    private double calcularTotalGolesEquipo(Equipo equipo, List<PartidoJugado> partidos) {
        return partidos.stream()
                .mapToDouble(partido -> partido.esLocal(equipo) ? partido.getGolesLocal() : partido.getGolesVisitante())
                .sum();
    }

    private List<PartidoJugado> obtenerPartidosDeEquipo(Equipo equipo, List<PartidoJugado> partidos) {
        return partidos.stream()
                .filter(partido -> partido.participa(equipo))
                .collect(Collectors.toList());
    }
}
