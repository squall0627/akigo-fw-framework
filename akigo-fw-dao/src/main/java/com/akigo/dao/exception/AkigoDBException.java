package com.akigo.dao.exception;

import com.akigo.core.exception.SystemException;

import java.sql.SQLException;
import java.util.Optional;

public class AkigoDBException extends SystemException {

    private Optional<SQLException> sqlException_;

    public AkigoDBException(String message) {
        super(message);
    }

    public AkigoDBException(String message, SQLException cause) {
        super(message, cause);
        this.sqlException_ = Optional.ofNullable(cause);
    }

    public AkigoDBException(SQLException cause) {
        super(cause);
        this.sqlException_ = Optional.ofNullable(cause);
    }

    public final String getSQLState() {
        if (sqlException_ == null) return null;
        return sqlException_.map(SQLException::getSQLState).orElse(null);
    }

    public final SQLException getSQLException() {
        if (sqlException_ == null) return null;
        return sqlException_.orElse(null);
    }
}
