package com.aplicacionweb.restaurante.Service;

import com.aplicacionweb.restaurante.Models.Reservas.ReservaDTO;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import weka.classifiers.Classifier;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.InputStream;

@Service
public class PrediccionReservaService {

    private Classifier classifier;
    private Instances estructuraDatos;

    @PostConstruct
    public void init() {
        try {
            // Carga del modelo
            InputStream modelStream = new ClassPathResource("reservaaModelPrueba.model").getInputStream();
            classifier = (Classifier) weka.core.SerializationHelper.read(modelStream);

            // Carga de la estructura ARFF
            InputStream arffStream = new ClassPathResource("reservas.arff").getInputStream();
            DataSource source = new DataSource(arffStream);
            estructuraDatos = source.getDataSet();
            estructuraDatos.setClassIndex(estructuraDatos.numAttributes() - 1); // 'cancelada' es la clase
        } catch (Exception e) {
            throw new RuntimeException("Error cargando modelo o estructura", e);
        }
    }

    public String predecirCancelacion(ReservaDTO reservaDTO) throws Exception {
        Instance instance = new DenseInstance(estructuraDatos.numAttributes());
        instance.setDataset(estructuraDatos);

        // Asignación de valores a la instancia (asegurándose de que los tipos sean correctos)

        // Atributo anticipación (numeric)
        instance.setValue(0, reservaDTO.getAnticipacion());

        // Atributo numeroPersonas (numeric)
        instance.setValue(1, reservaDTO.getNumeroPersonas());

        // Atributo origenReserva (nominal) -> Convertir a mayúsculas
        String origenReserva = reservaDTO.getOrigenReserva().toUpperCase();  // Convertir a mayúsculas
        if (estructuraDatos.attribute(2).indexOfValue(origenReserva) == -1) {
            throw new IllegalArgumentException("Origen de reserva inválido: " + origenReserva);
        }
        instance.setValue(2, origenReserva);  // Nominal

        // Atributo metodoDePago (nominal) -> Convertir a mayúsculas
        String metodoDePago = reservaDTO.getMetodoDePago().toUpperCase();  // Convertir a mayúsculas
        if (estructuraDatos.attribute(3).indexOfValue(metodoDePago) == -1) {
            throw new IllegalArgumentException("Método de pago inválido: " + metodoDePago);
        }
        instance.setValue(3, metodoDePago);  // Nominal

        // Atributo estadoReserva (nominal) -> Convertir a minúsculas
        String estadoReserva = reservaDTO.getEstadoReserva().toLowerCase();  // Convertir a minúsculas
        if (estructuraDatos.attribute(4).indexOfValue(estadoReserva) == -1) {
            throw new IllegalArgumentException("Estado de reserva inválido: " + estadoReserva);
        }
        instance.setValue(4, estadoReserva);  // Nominal

        // Atributo clienteRecurrente (nominal)
        instance.setValue(5, reservaDTO.getClienteRecurrente() ? "true" : "false");  // Nominal

        // Atributo diaSemana (nominal) -> Convertir a mayúsculas
        String diaSemana = reservaDTO.getDiaSemana().toUpperCase();  // Convertir a mayúsculas
        if (estructuraDatos.attribute(6).indexOfValue(diaSemana) == -1) {
            throw new IllegalArgumentException("Día de la semana inválido: " + diaSemana);
        }
        instance.setValue(6, diaSemana);  // Nominal

        // Realización de la predicción
        double prediccion = classifier.classifyInstance(instance);
        return estructuraDatos.classAttribute().value((int) prediccion); // "true" o "false"
    }
}
