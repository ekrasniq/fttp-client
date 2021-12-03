package de.pseudoymisierung.mainzelliste.client.bloomfilter;

import de.pseudonymisierung.mainzelliste.client.fttp.bloomfilter.RandomRecordBloomFilterGenerator;
import de.pseudonymisierung.mainzelliste.client.fttp.normalization.FieldsNormalization;
import de.ukhd.medic.mpi.MpiRunner;

import java.text.SimpleDateFormat;
import java.util.*;

public class GenerateBloomFilter {
    private static final String HOST = "icwmpi99";
    private static final int ICW_PDQ_PORT = 3750;



        protected static String generateBloomFilter(String pidAssigningAuthorityUniversalId) throws Exception {
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

            // get idat from MPI
            HashMap<String, String> idat = MpiRunner.getIdatFromMpi(pidAssigningAuthorityUniversalId);

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

