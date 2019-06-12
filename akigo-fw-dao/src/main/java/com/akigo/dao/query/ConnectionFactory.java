package com.akigo.dao.query;

import java.sql.Connection;

public interface ConnectionFactory {

    Connection create();

}
