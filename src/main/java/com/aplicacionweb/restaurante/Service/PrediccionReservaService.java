package com.aplicacionweb.restaurante.Service;

import com.aplicacionweb.restaurante.Models.Prediccion;
import com.aplicacionweb.restaurante.Models.Reservas.ReservaDTO;
import com.aplicacionweb.restaurante.Repository.PrediccionRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import weka.classifiers.Classifier;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.InputStream;
import java.time.LocalDateTime;

@Service
public class PrediccionReservaService {

    private Classifier classifier;
    private Instances estructuraDatos;

    @Autowired
    private PrediccionRepository prediccionRepository;  // Repositorio para guardar la predicción

    @PostConstruct
    public void init() {
        try {
            // Carga del modelo
            InputStream modelStream = new ClassPathResource("reservaModelPrueba.model").getInputStream();
            classifier = (Classifier) weka.core.SerializationHelper.read(modelStream);

            // Carga de la estructura ARFF (sin 'estadoReserva' en los atributos, solo como clase)
            InputStream arffStream = new ClassPathResource("reservas.arff").getInputStream();
            DataSource source = new DataSource(arffStream);
            estructuraDatos = source.getDataSet();
            estructuraDatos.setClassIndex(estructuraDatos.numAttributes() - 1); // estado_reserva como clase
        } catch (Exception e) {
            throw new RuntimeException("Error cargando modelo o estructura", e);
        }
    }

    public String predecirEstadoReserva(ReservaDTO reservaDTO) throws Exception {
        Instance instance = new DenseInstance(estructuraDatos.numAttributes());
        instance.setDataset(estructuraDatos);

        // Asignación de valores a la instancia (respetando el orden de atributos)
        instance.setValue(0, reservaDTO.getAnticipacion()); // anticipacion (numeric)
        instance.setValue(1, reservaDTO.getNumeroPersonas()); // numero_personas (numeric)

        String origen = reservaDTO.getOrigenReserva().toUpperCase();
        if (estructuraDatos.attribute(2).indexOfValue(origen) == -1)
            throw new IllegalArgumentException("Origen inválido: " + origen);
        instance.setValue(2, origen);

        String metodo = reservaDTO.getMetodoDePago().toUpperCase();
        if (estructuraDatos.attribute(3).indexOfValue(metodo) == -1)
            throw new IllegalArgumentException("Método de pago inválido: " + metodo);
        instance.setValue(3, metodo);

        instance.setValue(4, reservaDTO.getClienteRecurrente() ? "true" : "false"); // cliente_recurrente

        String dia = reservaDTO.getDiaSemana().toUpperCase();
        if (estructuraDatos.attribute(5).indexOfValue(dia) == -1)
            throw new IllegalArgumentException("Día inválido: " + dia);
        instance.setValue(5, dia);

        // Predicción del estado de la reserva
        double prediccion = classifier.classifyInstance(instance);
        String estadoPrediccion = estructuraDatos.classAttribute().value((int) prediccion); // "pagada" o "no_pagada"

        // Guardar la predicción en la base de datos
        Prediccion prediccionReserva = new Prediccion();
        prediccionReserva.setNumeroPersonas(reservaDTO.getNumeroPersonas());
        prediccionReserva.setOrigenReserva(reservaDTO.getOrigenReserva());
        prediccionReserva.setMetodoPago(reservaDTO.getMetodoDePago());
        prediccionReserva.setClienteRecurrente(reservaDTO.getClienteRecurrente());
        prediccionReserva.setAnticipacion(reservaDTO.getAnticipacion());
        prediccionReserva.setDiaSemana(reservaDTO.getDiaSemana());
        prediccionReserva.setEstadoReserva(estadoPrediccion);
        prediccionReserva.setFecha(LocalDateTime.now()); // Fecha y hora de la predicción

        // Guardar la predicción en la base de datos
        prediccionRepository.save(prediccionReserva);

        return estadoPrediccion; // Devuelve el estado de la predicción (pagada o no_pagada)
    }
}
