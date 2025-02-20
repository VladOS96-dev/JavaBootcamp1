package ex00;

import java.io.*;
import java.util.*;

public class FileSignatures {
    private final Map<String, byte[]> signatures = new HashMap<>();

    public void loadSignatures(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length < 2) continue;

                String type = parts[0];
                byte[] signature = hexStringToByteArray(parts[1]);
                signatures.put(type, signature);
            }
        }
    }

    public String identifyFile(String filepath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filepath)) {
            byte[] fileBytes = new byte[8];
            int bytesRead = fis.read(fileBytes);

            if (bytesRead == -1) return "UNDEFINED";


            System.out.println();
            for (Map.Entry<String, byte[]> entry : signatures.entrySet()) {
                if (startsWith(fileBytes, entry.getValue())) {
                    return entry.getKey();
                }
            }
        }
        return "UNDEFINED";
    }

    private boolean startsWith(byte[] fileBytes, byte[] signature) {
        if (fileBytes.length < signature.length) return false;
        for (int i = 0; i < signature.length; i++) {
            if (fileBytes[i] != signature[i]) {
                return false;
            }
        }
        return true;
    }

    private byte[] hexStringToByteArray(String hex) {
        String[] hexBytes = hex.split(" ");
        byte[] bytes = new byte[hexBytes.length];
        for (int i = 0; i < hexBytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hexBytes[i], 16);
        }
        return bytes;
    }
}
