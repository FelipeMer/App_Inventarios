package com.codebyfelipe.appinventarios.ui.scanner;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import java.util.List;

public class BarcodeAnalyzer implements ImageAnalysis.Analyzer { //Analiza cada fotograma de la cámara con ML Kit y detecta patrones de código de barras.

    public interface OnBarcodeDetected {
        void onDetected(String value);
    }

    private final BarcodeScanner scanner = BarcodeScanning.getClient();
    private final OnBarcodeDetected callback;
    private boolean detected = false; // evita disparar el callback múltiples veces por segundo

    public BarcodeAnalyzer(OnBarcodeDetected callback) {
        this.callback = callback;
    }

    @OptIn(markerClass = ExperimentalGetImage.class)
    @Override
    public void analyze(@NonNull ImageProxy imageProxy) {
        if (detected || imageProxy.getImage() == null) {
            imageProxy.close();
            return;
        }

        InputImage image = InputImage.fromMediaImage(
                imageProxy.getImage(), imageProxy.getImageInfo().getRotationDegrees());

        scanner.process(image)
                .addOnSuccessListener(barcodes -> {
                    if (!detected && !barcodes.isEmpty()) {
                        for (Barcode barcode : barcodes) {
                            String rawValue = barcode.getRawValue();
                            if (rawValue != null) {
                                detected = true;
                                callback.onDetected(rawValue);
                                break;
                            }
                        }
                    }
                })
                .addOnCompleteListener(task -> imageProxy.close());
    }
}