package org.fostash.atomic.oracle;

import org.fostash.atomic.dsl.IFrom;
import org.fostash.atomic.dsl.IInsert;
import org.fostash.atomic.dsl.ISelect.IColumn;
import org.fostash.atomic.dsl.exception.SqlBuilderException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * Created by Fausto on 25/04/16.
 */
public class Insert implements IInsert {

    /**  table field. */
    private IFrom.ITable table;
    /** column list field. */
    private final List<IColumn> columns = new ArrayList<>();
    /** value to insert list field. */
    private final List<Object> values = new LinkedList<>();

    @Override
    public IInsert into(final IFrom.ITable iTable, final IColumn... iColumns) {
        this.table = iTable;
        if (Objects.nonNull(iColumns) && iColumns.length > 0) {
            columns.addAll(Arrays.asList(iColumns));
        }
        return this;
    }

    @Override
    public final IInsert values(final Object...objects) {
        if (objects.length > 0) {
            values.addAll(Arrays.asList(objects));
        } else {
            throw new SqlBuilderException("no values defined for insert");
        }
        return this;
    }

    /**
     * @return sql insert string
     */
    @Override
    public final String toString() {
        return "INSERT INTO " + table
                + " ("
                + columns.stream().map(IColumn::getName).collect(Collectors.joining(", "))
                + ") VALUES ("
                + values.stream().map(SQLUtils.SQLValue::toSQL).collect(Collectors.joining(", "))
                + ");";
    }
}
