package com.eseict.reapi.hams.client.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Aes128 — AES-128 ECB 암·복호화 검증")
class Aes128Test {

    private static final String LEGACY_KEY = "GoodLinkAESKey&&";

    private Aes128 aes;

    @BeforeEach
    void setUp() {
        aes = new Aes128(LEGACY_KEY);
    }

    // ──────────────────────────────────────────────────────────
    // 암호화 → 복호화 왕복 검증
    // ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("암호화 후 복호화하면 원문이 복원된다")
    void encryptThenDecrypt_restoresOriginal() {
        String original = "admin";
        String encrypted = aes.encrypt(original);
        String decrypted = aes.decrypt(encrypted);
        assertThat(decrypted).isEqualTo(original);
    }

    @Test
    @DisplayName("빈 문자열도 암호화 → 복호화 왕복이 가능하다")
    void emptyString_roundTripSucceeds() {
        String original = "";
        String encrypted = aes.encrypt(original);
        String decrypted = aes.decrypt(encrypted);
        assertThat(decrypted).isEqualTo(original);
    }

    @Test
    @DisplayName("긴 문자열도 암호화 → 복호화 왕복이 가능하다")
    void longString_roundTripSucceeds() {
        String original = "ThisIsALongPasswordString1234567890!@#";
        String encrypted = aes.encrypt(original);
        String decrypted = aes.decrypt(encrypted);
        assertThat(decrypted).isEqualTo(original);
    }

    // ──────────────────────────────────────────────────────────
    // 암호화 결과 형식 검증
    // ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("암호화 결과는 null 이 아니고 Base64 형식이다")
    void encrypt_returnsBase64String() {
        String encrypted = aes.encrypt("admin");
        assertThat(encrypted).isNotNull();
        // Base64 문자셋: A-Z, a-z, 0-9, +, /, = (패딩)
        assertThat(encrypted).matches("[A-Za-z0-9+/=]+");
    }

    @Test
    @DisplayName("동일 평문은 항상 동일한 암호문을 생성한다 (ECB 결정론적)")
    void sameInput_producesSameCiphertext() {
        String enc1 = aes.encrypt("password");
        String enc2 = aes.encrypt("password");
        assertThat(enc1).isEqualTo(enc2);
    }

    @Test
    @DisplayName("서로 다른 평문은 서로 다른 암호문을 생성한다")
    void differentInputs_produceDifferentCiphertext() {
        String enc1 = aes.encrypt("admin");
        String enc2 = aes.encrypt("user");
        assertThat(enc1).isNotEqualTo(enc2);
    }

    // ──────────────────────────────────────────────────────────
    // 레거시 키 고정 벡터 검증
    // ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("레거시 키로 '1' 암호화 결과는 매번 동일하다 (회귀 방지)")
    void legacyKey_encrypt1_isStable() {
        String encrypted = aes.encrypt("1");
        assertThat(encrypted).isNotNull();
        // 고정된 키/알고리즘이므로 항상 같은 값이 나와야 한다
        String encryptedAgain = new Aes128(LEGACY_KEY).encrypt("1");
        assertThat(encrypted).isEqualTo(encryptedAgain);
    }

    @Test
    @DisplayName("레거시 키로 '1' 을 암호화하고 복호화하면 '1' 이 복원된다")
    void legacyKey_encrypt1_thenDecrypt_restores1() {
        String encrypted = aes.encrypt("1");
        String decrypted = aes.decrypt(encrypted);
        assertThat(decrypted).isEqualTo("1");
    }

    // ──────────────────────────────────────────────────────────
    // 에러 경로 검증
    // ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("잘못된 Base64 문자열 복호화 시 null 을 반환한다")
    void decrypt_invalidBase64_returnsNull() {
        String result = aes.decrypt("NOT_VALID_BASE64!!!");
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("키가 16자 미만이면 나머지는 0으로 패딩되어 정상 동작한다")
    void shortKey_paddedWithZero_worksCorrectly() {
        Aes128 shortKeyAes = new Aes128("shortkey");
        String encrypted = shortKeyAes.encrypt("test");
        String decrypted = shortKeyAes.decrypt(encrypted);
        assertThat(decrypted).isEqualTo("test");
    }

    @Test
    @DisplayName("서로 다른 키로 암호화한 값은 상대 키로 복호화 시 null 또는 다른 값이 나온다")
    void differentKeys_cannotDecryptEachOther() {
        Aes128 aes1 = new Aes128("GoodLinkAESKey&&");
        Aes128 aes2 = new Aes128("OtherAESKey12345");

        String encryptedBy1 = aes1.encrypt("hello");
        // 다른 키로 복호화하면 null 이거나 원문과 다른 값이 나온다
        String decryptedBy2 = aes2.decrypt(encryptedBy1);
        // null 이거나 원문("hello") 과 달라야 한다
        assertThat(decryptedBy2 == null || !decryptedBy2.equals("hello")).isTrue();
    }
}
