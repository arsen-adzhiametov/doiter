package com.doiter.service;

/**
 * Localised goals. Use toString() to get localized message. toString() will
 * get current language from TransactionInfo If no language information in
 * TransactionInfo, Russian will be used by default
 * 
 * If you want to specify language, use toStringWithLng("en")
 * 
 */
public enum Message {

    UPGRADE_MESSAGE(
            "Application was moved to a new account. Please download application from new account.",
            "Приложение было перемещено. Для возможности получать обновления, скачайте новую версию и затем удалите старую. История поездок сохранится.",
            "Аплікація була переміщена. Будь ласка, завантажте аплікацію повторно.");

    
    private String ru;
    private String en;
    private String ua;

    private Message(String en, String ru, String ua) {
        this.ru = ru;
        this.en = en;
        this.ua = ua;
    }

    public String toStringWithLng(String language) {
        if ("ru".equals(language)) {
            return ru;
        } else if ("uk".equals(language)) {
            return ua;
        } else if ("en".equals(language)) {
            return en;
        }
        return ru;
    }
}
