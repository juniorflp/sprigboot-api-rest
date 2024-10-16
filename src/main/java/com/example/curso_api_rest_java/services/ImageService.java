package com.example.curso_api_rest_java.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${image.upload.directory}")
    private String imageDirectory;

    public String saveImage(MultipartFile file) throws IOException {
        final long MAX_FILE_SIZE = 200 * 1024 * 1024; //200mb

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IOException("O arquivo excede o tamanho máximo permitido de 200MB.");
        }

        File directory = new File(imageDirectory);
        if (!directory.exists()) {
            directory.mkdir();
        }

        String originalFileName = file.getOriginalFilename();
        if(originalFileName == null){
            throw new IOException("O arquivo não possui um nome válido.");
        }

        String sanitizeFileName = sanitizeFileName(originalFileName);

        String fileName = UUID.randomUUID().toString() + "_" + sanitizeFileName;

        Path filePath = Paths.get(imageDirectory, fileName);

        //save the file
        try {
            Files.copy(file.getInputStream(), filePath);
        } catch (IOException e) {
            throw new IOException("Erro ao salvar o arquivo: " + filePath.toString(), e);
        }
        if (Files.exists(filePath) && Files.size(filePath) > 0) {
            return filePath.toString();
        } else {
            throw new IOException("Arquivo não foi salvo corretamente: " + filePath.toString());
        }


    }

    // Método para sanitizar o nome do arquivo
    private String sanitizeFileName(String fileName) {
        // Remove qualquer ocorrência de "../" ou "./" para evitar navegação para fora do diretório base
        fileName = fileName.replaceAll("\\.\\.\\/|\\.\\/", "");

        // Remove caracteres não permitidos, como caracteres que podem ser usados em ataques de path traversal
        fileName = fileName.replaceAll("[^a-zA-Z0-9\\.\\-\\_]", "_");

        // Substitui espaços em branco por "_"
        fileName = fileName.replaceAll("\\s+", "_");

        return fileName;
    }
}
