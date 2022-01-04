package de.pseudoymisierung.mainzelliste.client.bloomfilter;

import de.pseudonymisierung.mainzelliste.client.fttp.bloomfilter.RandomRecordBloomFilterGenerator;
import de.pseudonymisierung.mainzelliste.client.fttp.normalization.FieldsNormalization;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class BloomFilterGenerator {
    private static final String HOST = "icwmpi99";
    private static final int ICW_PDQ_PORT = 3750;

    public static String generateBloomFilter(HashMap<String, String> idat) {
        // define how idat fields will be normalize
        Properties normalizationConfig = new Properties();
        normalizationConfig.put("field.firstName.transformer.0.type", "StringFieldTransformer");
        normalizationConfig.put("field.firstName.transformer.0.replacement", "{\"Dr\\\\.|Dipl\\\\.\":\"\",\"é\":\"e\",\"ä\":\"ae\",\"Ä\":\"AE\",\"ö\":\"oe\",\"Ö\":\"OE\",\"ü\":\"ue\",\"Ü\":\"UE\"}");
        normalizationConfig.put("field.firstName.transformer.0.upperCase", "true");
        normalizationConfig.put("field.firstName.transformer.0.trim", "true");
        normalizationConfig.put("field.firstName.transformer.1.allowedChars", "[A-Z\\s]");
        normalizationConfig.put("field.lastName.transformer.0.type", "StringFieldTransformer");
        normalizationConfig.put("field.lastName.transformer.0.replacement", "{\"Dr\\\\.|Dipl\\\\.\":\"\",\"é\":\"e\",\"ä\":\"ae\",\"Ä\":\"AE\",\"ö\":\"oe\",\"Ö\":\"OE\",\"ü\":\"ue\",\"Ü\":\"UE\"}");
        normalizationConfig.put("field.lastName.transformer.0.upperCase", "true");
        normalizationConfig.put("field.lastName.transformer.0.trim", "true");
        normalizationConfig.put("field.lastName.transformer.1.allowedChars", "[A-Z\\s]");

        // init field normalization
        FieldsNormalization fieldsNormalization = new FieldsNormalization(normalizationConfig);

        //define bloom filter configuration
        Properties bloomFilterConfig = new Properties();
        bloomFilterConfig.put("length", "800");
        bloomFilterConfig.put("nGramLength", "2");
        bloomFilterConfig.put("randomPositionNumber", "30");
        bloomFilterConfig.put("vocabulary", "abcdefghijkalmnoprstuvmxyzABCDEFGHIJKLMNOPQRSTUVWXYZ 0123456789");
        bloomFilterConfig.put("balanced.seed", "154866848");
        bloomFilterConfig.put("field.firstName.seed", "487484864");
        bloomFilterConfig.put("field.lastName.seed", "311296856");
        bloomFilterConfig.put("field.birthDate.seed", "897689196");
        bloomFilterConfig.put("field.gender.seed", "298767897");

        //init bloom filter generator
        RandomRecordBloomFilterGenerator bloomFilterGenerator = new RandomRecordBloomFilterGenerator(bloomFilterConfig);

        // prepare idat fields
        String datePattern = "yyyyMMdd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);

        Map<String, String> fields = new HashMap<>();
        fields.put("firstName", idat.get("firstName"));
        fields.put("lastName", idat.get("lastName"));
        fields.put("birthDate", dateFormat.format(idat.get("birthDate")));
        fields.put("gender", idat.get("sex").toLowerCase());

        // normalize fields
        Map<String, String> normalizedFields = fieldsNormalization.process(fields);


        // generate bloom filter
        return bloomFilterGenerator.generateBalancedBloomFilter(normalizedFields).getBase64String();
    }

}

