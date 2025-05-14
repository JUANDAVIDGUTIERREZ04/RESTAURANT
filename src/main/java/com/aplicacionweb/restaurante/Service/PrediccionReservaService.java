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
import java.util.Locale;

@Service
public class PrediccionReservaService {

    private Classifier classifier;
    private Instances estructuraDatos;

    @Autowired
    private PrediccionRepository prediccionRepository;

    @PostConstruct
    public void init() {
        try {
            InputStream modelStream = new ClassPathResource("nuevoModel.model").getInputStream();
            classifier = (Classifier) weka.core.SerializationHelper.read(modelStream);

            InputStream arffStream = new ClassPathResource("reservaNuevoModelo.arff").getInputStream();
            DataSource source = new DataSource(arffStream);
            estructuraDatos = source.getDataSet();
            estructuraDatos.setClassIndex(estructuraDatos.numAttributes() - 1); // Atributo "cancelada"
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar modelo o estructura ARFF", e);
        }
    }

    public String predecirEstadoReserva(ReservaDTO dto) throws Exception {
        Instance instancia = new DenseInstance(estructuraDatos.numAttributes());
        instancia.setDataset(estructuraDatos);

        // Conversión robusta para números
        instancia.setValue(0, convertirADouble(dto.getAnticipacion()));
        instancia.setValue(1, convertirADouble(dto.getNumeroPersonas()));

        // Validación y asignación de atributos categóricos
        String metodo = dto.getMetodoDePago().toUpperCase(Locale.ROOT);
        if (estructuraDatos.attribute(2).indexOfValue(metodo) == -1)
            throw new IllegalArgumentException("Método de pago inválido: " + metodo);
        instancia.setValue(2, metodo);

        instancia.setValue(3, dto.getClienteRecurrente() ? "TRUE" : "FALSE");

        String dia = dto.getDiaSemana().toUpperCase(Locale.ROOT);
        if (estructuraDatos.attribute(4).indexOfValue(dia) == -1)
            throw new IllegalArgumentException("Día inválido: " + dia);
        instancia.setValue(4, dia);

        // Clasificación
        double predIndex = classifier.classifyInstance(instancia);
        String estado = estructuraDatos.classAttribute().value((int) predIndex);

        // Probabilidad de cada clase
        double[] distribucion = classifier.distributionForInstance(instancia);
        double probCancelada = distribucion[estructuraDatos.classAttribute().indexOfValue("cancelada")];

        // Guardar en base de datos
        Prediccion pred = new Prediccion();
        pred.setNumeroPersonas(dto.getNumeroPersonas());
        pred.setMetodoPago(dto.getMetodoDePago());
        pred.setClienteRecurrente(dto.getClienteRecurrente());
        pred.setAnticipacion(dto.getAnticipacion());
        pred.setDiaSemana(dto.getDiaSemana());
        pred.setEstadoReserva(estado);
        pred.setProbabilidadCancelacion(probCancelada);
        pred.setFecha(LocalDateTime.now());

        prediccionRepository.save(pred);

        return String.format("Estado predicho: %s | Probabilidad de cancelación: %.2f%%",
                estado, probCancelada * 100);
    }

    private double convertirADouble(Number valor) {
        if (valor == null) return 0.0;

        try {
            String valorStr = valor.toString().replace(",", ".").trim();
            return Double.parseDouble(valorStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valor inválido para número: " + valor, e);
        }
    }
}
