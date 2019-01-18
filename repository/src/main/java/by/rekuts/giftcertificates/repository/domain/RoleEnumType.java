package by.rekuts.giftcertificates.repository.domain;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.EnumType;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class RoleEnumType extends EnumType {

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement,
                            Object value,
                            int index,
                            SharedSessionContractImplementor session)
            throws SQLException {
        if(value == null) {
            preparedStatement.setNull( index, Types.OTHER );
        }
        else {
            preparedStatement.setObject(
                    index,
                    value.toString(),
                    Types.OTHER
            );
        }
    }
}