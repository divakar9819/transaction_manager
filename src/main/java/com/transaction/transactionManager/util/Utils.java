package com.transaction.transactionManager.util;

import java.util.UUID;

/**
 * @author Divakar Verma
 * @created_at : 12/02/2024 - 5:01 pm
 * @mail_to: vermadivakar2022@gmail.com
 */
public class Utils {

    public static String getRandomId(){
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString().replace("-","");
        return id.substring(0,10);
    }
}
