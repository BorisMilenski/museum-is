package com.borismilenski.museumis.util;

import com.borismilenski.museumis.model.Position;
import org.springframework.jdbc.core.RowMapper;

public interface DBToClassMapper<T> {
    RowMapper<T> map();
}
