import java.math.BigInteger;


public class RSAEncryptor {
    private int p;
    private int q;
    private int n;
    private int e;
    private int d;

    public RSAEncryptor(int p, int q, int e) {
        this.p = p;
        this.q = q;
        this.n = q * p;
        this.e = e;
        this.d = this.calculatePrivateKey();
    }

    private int calculatePrivateKey() {
        int phi = (p - 1) * (q - 1);
        BigInteger eBigInteger = BigInteger.valueOf(e);
        BigInteger phiBigInteger = BigInteger.valueOf(phi);
        BigInteger dBigInteger = eBigInteger.modInverse(phiBigInteger);
        return dBigInteger.intValue();
    }

    public String encrypt(String message) {
        StringBuilder ciphertextBuilder = new StringBuilder();
        for(int i = 0; i < message.length(); ++i) {
            char c = message.charAt(i);
            BigInteger m = BigInteger.valueOf((int) c);
            BigInteger encrypted = m.modPow(BigInteger.valueOf(e), BigInteger.valueOf(n));
            ciphertextBuilder.append(encrypted).append(" ");
        }
        return ciphertextBuilder.toString();
    }

    public String decrypt(String ciphertext) {
        StringBuilder plaintextBuilder = new StringBuilder();
        String[] encryptedChars = ciphertext.split(" ");
        for (String encryptedChar : encryptedChars) {
            BigInteger encrypted = new BigInteger(encryptedChar);
            BigInteger decrypted = encrypted.modPow(BigInteger.valueOf(d), BigInteger.valueOf(n));
            char plaintextChar = (char) decrypted.intValue();
            plaintextBuilder.append(plaintextChar);
        }
        return plaintextBuilder.toString();
    }

    public void setP(int p) {
        this.p = p;
    }

    public void setQ(int q) {
        this.q = q;
    }

    // Метод для преобразования текста в ASCII строку
    public static String toAsciiString(String message) {
        StringBuilder sb = new StringBuilder();
        for (char c : message.toCharArray()) {
            int asciiCode = (int) c;
            sb.append(asciiCode);
        }
        return sb.toString();
    }

    // Метод для преобразования ASCII строки обратно в текст
    public static String asciiToString(String asciiString) {
        StringBuilder sb = new StringBuilder();
        int i = 0;

        // Пока не дойдем до конца строки
        while (i < asciiString.length()) {
            // Попробуем считать 2 или 3 цифры, которые могут быть ASCII-кодами
            int asciiCode;

            // Если оставшиеся 2 символа составляют допустимый ASCII-код (от 32 до 99), берем 2 цифры
            if (i + 2 <= asciiString.length()) {
                asciiCode = Integer.parseInt(asciiString.substring(i, i + 2));
                if (asciiCode >= 32 && asciiCode <= 99) {  // Проверяем, что это допустимый код
                    sb.append((char) asciiCode);
                    i += 2;  // Если это 2-значный код, сдвигаем на 2 позиции
                    continue;
                }
            }

            // Если остались 3 цифры, считаем их как 3-значный ASCII-код
            if (i + 3 <= asciiString.length()) {
                asciiCode = Integer.parseInt(asciiString.substring(i, i + 3));
                if (asciiCode >= 32 && asciiCode <= 255) {  // Проверяем, что это допустимый код
                    sb.append((char) asciiCode);
                    i += 3;  // Если это 3-значный код, сдвигаем на 3 позиции
                    continue;
                }
            }

            // В случае, если код не подходит (ошибка), просто увеличиваем индекс на 1
            i++;
        }

        return sb.toString();
    }

}
